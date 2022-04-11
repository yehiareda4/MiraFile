/*
 * Copyright 2017 Yan Zhenjie.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yehia.album_media.app.album.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.MediaMetadataRetriever
import android.text.TextUtils
import android.webkit.URLUtil
import androidx.annotation.WorkerThread
import com.yehia.album_media.util.AlbumUtils
import java.io.*
import java.util.*

/**
 * Created by YanZhenjie on 2017/10/15.
 */
class ThumbnailBuilder(context: Context) {
    private val mCacheDir: File

    /**
     * Create a thumbnail for the image.
     *
     * @param imagePath image path.
     * @return thumbnail path.
     */
    @WorkerThread
    fun createThumbnailForImage(imagePath: String): String {
        if (TextUtils.isEmpty(imagePath)) return ""
        val inFile = File(imagePath)
        if (!inFile.exists()) return ""
        val thumbnailFile = randomPath(imagePath)
        if (thumbnailFile.exists()) return thumbnailFile.absolutePath
        val inBitmap = readImageFromPath(imagePath, THUMBNAIL_SIZE, THUMBNAIL_SIZE)
            ?: return ""
        val compressStream = ByteArrayOutputStream()
        inBitmap.compress(Bitmap.CompressFormat.JPEG, THUMBNAIL_QUALITY, compressStream)
        return try {
            compressStream.close()
            thumbnailFile.createNewFile()
            val writeStream = FileOutputStream(thumbnailFile)
            writeStream.write(compressStream.toByteArray())
            writeStream.flush()
            writeStream.close()
            thumbnailFile.absolutePath
        } catch (ignored: Exception) {
            ""
        }
    }

    /**
     * Create a thumbnail for the video.
     *
     * @param videoPath video path.
     * @return thumbnail path.
     */
    @WorkerThread
    fun createThumbnailForVideo(videoPath: String): String {
        if (TextUtils.isEmpty(videoPath)) return ""
        val thumbnailFile = randomPath(videoPath)
        return if (thumbnailFile.exists()) thumbnailFile.absolutePath else try {
            val retriever = MediaMetadataRetriever()
            if (URLUtil.isNetworkUrl(videoPath)) {
                retriever.setDataSource(videoPath, HashMap())
            } else {
                retriever.setDataSource(videoPath)
            }
            val bitmap = retriever.frameAtTime
            thumbnailFile.createNewFile()
            bitmap!!.compress(
                Bitmap.CompressFormat.JPEG,
                THUMBNAIL_QUALITY,
                FileOutputStream(thumbnailFile)
            )
            thumbnailFile.absolutePath
        } catch (ignored: Exception) {
            null
        }!!
    }

    private fun randomPath(filePath: String): File {
        val outFilePath = AlbumUtils.getMD5ForString(filePath) + ".album"
        return File(mCacheDir, outFilePath)
    }

    companion object {
        private const val THUMBNAIL_SIZE = 360
        private const val THUMBNAIL_QUALITY = 80

        /**
         * Deposit in the province read images, mViewWidth is high, the greater the picture clearer, but also the memory.
         *
         * @param imagePath pictures in the path of the memory card.
         * @return bitmap.
         */
        fun readImageFromPath(imagePath: String, width: Int, height: Int): Bitmap? {
            val imageFile = File(imagePath)
            if (imageFile.exists()) {
                try {
                    var inputStream = BufferedInputStream(FileInputStream(imageFile))
                    val options = BitmapFactory.Options()
                    options.inJustDecodeBounds = true
                    BitmapFactory.decodeStream(inputStream, null, options)
                    inputStream.close()
                    options.inJustDecodeBounds = false
                    options.inSampleSize = computeSampleSize(options, width, height)
                    var sampledBitmap: Bitmap? = null
                    var attemptSuccess = false
                    while (!attemptSuccess) {
                        inputStream = BufferedInputStream(FileInputStream(imageFile))
                        try {
                            sampledBitmap = BitmapFactory.decodeStream(inputStream, null, options)
                            attemptSuccess = true
                        } catch (e: Exception) {
                            options.inSampleSize *= 2
                        }
                        inputStream.close()
                    }
                    val lowerPath = imagePath!!.lowercase(Locale.getDefault())
                    if (lowerPath.endsWith(".jpg") || lowerPath.endsWith(".jpeg")) {
                        val degrees = computeDegree(imagePath)
                        if (degrees > 0) {
                            val matrix = Matrix()
                            matrix.setRotate(degrees.toFloat())
                            val newBitmap = Bitmap.createBitmap(
                                sampledBitmap!!,
                                0,
                                0,
                                sampledBitmap.width,
                                sampledBitmap.height,
                                matrix,
                                true
                            )
                            if (newBitmap != sampledBitmap) {
                                sampledBitmap.recycle()
                                sampledBitmap = newBitmap
                            }
                        }
                    }
                    return sampledBitmap
                } catch (ignored: Exception) {
                }
            }
            return null
        }

        private fun computeSampleSize(
            options: BitmapFactory.Options,
            width: Int,
            height: Int
        ): Int {
            var inSampleSize = 1
            if (options.outWidth > width || options.outHeight > height) {
                val widthRatio = Math.round(options.outWidth.toFloat() / width.toFloat())
                val heightRatio = Math.round(options.outHeight.toFloat() / height.toFloat())
                inSampleSize = Math.min(widthRatio, heightRatio)
            }
            return inSampleSize
        }

        private fun computeDegree(path: String): Int {
            return try {
                val exifInterface = ExifInterface(path!!)
                val orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> {
                        90
                    }
                    ExifInterface.ORIENTATION_ROTATE_180 -> {
                        180
                    }
                    ExifInterface.ORIENTATION_ROTATE_270 -> {
                        270
                    }
                    else -> {
                        0
                    }
                }
            } catch (e: Exception) {
                0
            }
        }
    }

    init {
        mCacheDir = AlbumUtils.getAlbumRootPath(context)
        if (mCacheDir.exists() && mCacheDir.isFile) mCacheDir.delete()
        if (!mCacheDir.exists()) mCacheDir.mkdirs()
    }
}