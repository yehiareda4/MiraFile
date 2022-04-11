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
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import com.yehia.album_media.AlbumFile
import com.yehia.album_media.AlbumFolder
import com.yehia.album_media.Filter
import com.yehia.album_media.R
import java.util.*

/**
 * Created by YanZhenjie on 2017/8/15.
 */
class MediaReader(
    private val mContext: Context,
    private val mSizeFilter: Filter<Long>?,
    private val mMimeFilter: Filter<String>?,
    private val mDurationFilter: Filter<Long>?,
    private val mFilterVisibility: Boolean
) {
    /**
     * Scan for image files.
     */
    @WorkerThread
    private fun scanImageFile(
        albumFolderMap: MutableMap<String, AlbumFolder>,
        allFileFolder: AlbumFolder
    ) {
        val contentResolver = mContext.contentResolver
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            IMAGES,
            null,
            null,
            null
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val path = cursor.getString(0)
                val bucketName = cursor.getString(1)
                val mimeType = cursor.getString(2)
                val addDate = cursor.getLong(3)
                val latitude = cursor.getFloat(4)
                val longitude = cursor.getFloat(5)
                val size = cursor.getLong(6)
                val imageFile = AlbumFile()
                imageFile.mediaType = AlbumFile.Companion.TYPE_IMAGE
                imageFile.path = path
                imageFile.bucketName = bucketName
                imageFile.mimeType = mimeType
                imageFile.addDate = addDate
                imageFile.latitude = latitude
                imageFile.longitude = longitude
                imageFile.size = size
                if (mSizeFilter != null && mSizeFilter.filter(size)) {
                    if (!mFilterVisibility) continue
                    imageFile.isDisable = true
                }
                if (mMimeFilter != null && mMimeFilter.filter(mimeType)) {
                    if (!mFilterVisibility) continue
                    imageFile.isDisable = true
                }
                allFileFolder.addAlbumFile(imageFile)
                var albumFolder = albumFolderMap[bucketName]
                if (albumFolder != null) albumFolder.addAlbumFile(imageFile) else {
                    albumFolder = AlbumFolder()
                    albumFolder.name = bucketName
                    albumFolder.addAlbumFile(imageFile)
                    albumFolderMap[bucketName] = albumFolder
                }
            }
            cursor.close()
        }
    }

    /**
     * Scan for image files.
     */
    @WorkerThread
    private fun scanVideoFile(
        albumFolderMap: MutableMap<String, AlbumFolder>,
        allFileFolder: AlbumFolder
    ) {
        val contentResolver = mContext.contentResolver
        val cursor = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            VIDEOS,
            null,
            null,
            null
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val path = cursor.getString(0)
                val bucketName = cursor.getString(1)
                val mimeType = cursor.getString(2)
                val addDate = cursor.getLong(3)
                val latitude = cursor.getFloat(4)
                val longitude = cursor.getFloat(5)
                val size = cursor.getLong(6)
                val duration = cursor.getLong(7)
                val videoFile = AlbumFile()
                videoFile.mediaType = AlbumFile.Companion.TYPE_VIDEO
                videoFile.path = path
                videoFile.bucketName = bucketName
                videoFile.mimeType = mimeType
                videoFile.addDate = addDate
                videoFile.latitude = latitude
                videoFile.longitude = longitude
                videoFile.size = size
                videoFile.duration = duration
                if (mSizeFilter != null && mSizeFilter.filter(size)) {
                    if (!mFilterVisibility) continue
                    videoFile.isDisable = true
                }
                if (mMimeFilter != null && mMimeFilter.filter(mimeType)) {
                    if (!mFilterVisibility) continue
                    videoFile.isDisable = true
                }
                if (mDurationFilter != null && mDurationFilter.filter(duration)) {
                    if (!mFilterVisibility) continue
                    videoFile.isDisable = true
                }
                allFileFolder.addAlbumFile(videoFile)
                var albumFolder = albumFolderMap[bucketName]
                if (albumFolder != null) albumFolder.addAlbumFile(videoFile) else {
                    albumFolder = AlbumFolder()
                    albumFolder.name = bucketName
                    albumFolder.addAlbumFile(videoFile)
                    albumFolderMap[bucketName] = albumFolder
                }
            }
            cursor.close()
        }
    }

    /**
     * Scan the list of pictures in the library.
     */
    @get:WorkerThread
    val allImage: ArrayList<AlbumFolder>
        get() {
            val albumFolderMap: MutableMap<String, AlbumFolder> = HashMap()
            val allFileFolder = AlbumFolder()
            allFileFolder.isChecked = true
            allFileFolder.name = mContext.getString(R.string.album_all_images)
            scanImageFile(albumFolderMap, allFileFolder)
            val albumFolders = ArrayList<AlbumFolder>()
            Collections.sort(allFileFolder.albumFiles)
            albumFolders.add(allFileFolder)
            for ((_, albumFolder) in albumFolderMap) {
                Collections.sort(albumFolder.albumFiles)
                albumFolders.add(albumFolder)
            }
            return albumFolders
        }

    /**
     * Scan the list of videos in the library.
     */
    @get:WorkerThread
    val allVideo: ArrayList<AlbumFolder>
        get() {
            val albumFolderMap: MutableMap<String, AlbumFolder> = HashMap()
            val allFileFolder = AlbumFolder()
            allFileFolder.isChecked = true
            allFileFolder.name = mContext.getString(R.string.album_all_videos)
            scanVideoFile(albumFolderMap, allFileFolder)
            val albumFolders = ArrayList<AlbumFolder>()
            Collections.sort(allFileFolder.albumFiles)
            albumFolders.add(allFileFolder)
            for ((_, albumFolder) in albumFolderMap) {
                Collections.sort(albumFolder.albumFiles)
                albumFolders.add(albumFolder)
            }
            return albumFolders
        }

    /**
     * Get all the multimedia files, including videos and pictures.
     */
    @get:WorkerThread
    val allMedia: ArrayList<AlbumFolder>
        get() {
            val albumFolderMap: MutableMap<String, AlbumFolder> = HashMap()
            val allFileFolder = AlbumFolder()
            allFileFolder.isChecked = true
            allFileFolder.name = mContext.getString(R.string.album_all_images_videos)
            scanImageFile(albumFolderMap, allFileFolder)
            scanVideoFile(albumFolderMap, allFileFolder)
            val albumFolders = ArrayList<AlbumFolder>()
            Collections.sort(allFileFolder.albumFiles)
            albumFolders.add(allFileFolder)
            for ((_, albumFolder) in albumFolderMap) {
                Collections.sort(albumFolder.albumFiles)
                albumFolders.add(albumFolder)
            }
            return albumFolders
        }

    companion object {
        /**
         * Image attribute.
         */
        private val IMAGES = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.LATITUDE,
            MediaStore.Images.Media.LONGITUDE,
            MediaStore.Images.Media.SIZE
        )

        /**
         * Video attribute.
         */
        private val VIDEOS = arrayOf(
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.LATITUDE,
            MediaStore.Video.Media.LONGITUDE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DURATION
        )
    }
}