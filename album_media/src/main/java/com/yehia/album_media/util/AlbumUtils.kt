/*
 * Copyright 2016 Yan Zhenjie.
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
package com.yehia.album_media.util

import android.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.webkit.MimeTypeMap
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.DrawableCompat
import com.yehia.album_media.provider.CameraFileProvider
import com.yehia.album_media.widget.divider.Api20ItemDivider
import com.yehia.album_media.widget.divider.Api21ItemDivider
import com.yehia.album_media.widget.divider.Divider
import java.io.File
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * Helper for album.
 * Created by Yan Zhenjie on 2016/10/30.
 */
object AlbumUtils {
    private const val CACHE_DIRECTORY = "AlbumCache"

    /**
     * Get a writable root directory.
     *
     * @param context context.
     * @return [File].
     */
    fun getAlbumRootPath(context: Context): File {
        return if (sdCardIsAvailable()) {
            File(
                Environment.getExternalStorageDirectory(),
                CACHE_DIRECTORY
            )
        } else {
            File(context.filesDir, CACHE_DIRECTORY)
        }
    }

    /**
     * SD card is available.
     *
     * @return true when available, other wise is false.
     */
    fun sdCardIsAvailable(): Boolean {
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            Environment.getExternalStorageDirectory().canWrite()
        } else false
    }

    /**
     * Setting [Locale] for [Context].
     *
     * @param context to set the specified locale context.
     * @param locale  locale.
     */
    fun applyLanguageForContext(context: Context, locale: Locale): Context {
        val resources = context.resources
        val config = resources.configuration
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            context.createConfigurationContext(config)
        } else {
            config.locale = locale
            resources.updateConfiguration(config, resources.displayMetrics)
            context
        }
    }

    /**
     * Take picture.
     *
     * @param activity    activity.
     * @param requestCode code, see .
     * @param outPath     file path.
     */
    fun takeImage(activity: Activity, requestCode: Int, outPath: File) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val uri = getUri(activity, outPath)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        activity.startActivityForResult(intent, requestCode)
    }

    /**
     * Take video.
     *
     * @param activity    activity.
     * @param requestCode code, see .
     * @param outPath     file path.
     * @param quality     currently value 0 means low quality, suitable for MMS messages, and  value 1 means high quality.
     * @param duration    specify the maximum allowed recording duration in seconds.
     * @param limitBytes  specify the maximum allowed size.
     */
    fun takeVideo(
        activity: Activity, requestCode: Int, outPath: File,
        @IntRange(from = 0, to = 1) quality: Int,
        @IntRange(from = 1) duration: Long,
        @IntRange(from = 1) limitBytes: Long
    ) {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        val uri = getUri(activity, outPath)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, quality)
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, duration)
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, limitBytes)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        activity.startActivityForResult(intent, requestCode)
    }

    /**
     * Generates an externally accessed URI based on path.
     *
     * @param context context.
     * @param outPath file path.
     * @return the uri address of the file.
     */
    private fun getUri(context: Context, outPath: File): Uri {
        val uri: Uri = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Uri.fromFile(outPath)
        } else {
            FileProvider.getUriForFile(
                context,
                CameraFileProvider.getProviderName(context),
                outPath
            )
        }
        return uri
    }

    /**
     * Generate a random jpg file path.
     *
     * @return file path.
     */
    @Deprecated("use {@link #randomJPGPath(Context)} instead.")
    fun randomJPGPath(): String {
        val bucket = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        return randomJPGPath(bucket)
    }

    /**
     * Generate a random jpg file path.
     *
     * @param context context.
     * @return file path.
     */
    fun randomJPGPath(context: Context): String {
        return if (Environment.MEDIA_MOUNTED != Environment.getExternalStorageState()) {
            randomJPGPath(context.cacheDir)
        } else randomJPGPath()
    }

    /**
     * Generates a random jpg file path in the specified directory.
     *
     * @param bucket specify the directory.
     * @return file path.
     */
    fun randomJPGPath(bucket: File): String {
        return randomMediaPath(bucket, ".jpg")
    }

    /**
     * Generate a random mp4 file path.
     *
     * @return file path.
     */
    @Deprecated("use {@link #randomMP4Path(Context)} instead.")
    fun randomMP4Path(): String {
        val bucket = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
        return randomMP4Path(bucket)
    }

    /**
     * Generate a random mp4 file path.
     *
     * @param context context.
     * @return file path.
     */
    fun randomMP4Path(context: Context): String {
        return if (Environment.MEDIA_MOUNTED != Environment.getExternalStorageState()) {
            randomMP4Path(context.cacheDir)
        } else randomMP4Path()
    }

    /**
     * Generates a random mp4 file path in the specified directory.
     *
     * @return file path.
     */
    fun randomMP4Path(bucket: File): String {
        return randomMediaPath(bucket, ".mp4")
    }

    /**
     * Generates a random file path using the specified suffix name in the specified directory.
     *
     * @param bucket    specify the directory.
     * @param extension extension.
     * @return file path.
     */
    private fun randomMediaPath(bucket: File, extension: String): String {
        if (bucket.exists() && bucket.isFile) bucket.delete()
        if (!bucket.exists()) bucket.mkdirs()
        val outFilePath = getNowDateTime("yyyyMMdd_HHmmssSSS") + "_" + getMD5ForString(
            UUID.randomUUID().toString()
        ) + extension
        val file = File(bucket, outFilePath)
        return file.absolutePath
    }

    /**
     * Format the current time in the specified format.
     *
     * @return the time string.
     */
    fun getNowDateTime(format: String): String {
        val formatter = SimpleDateFormat(format, Locale.ENGLISH)
        val curDate = Date(System.currentTimeMillis())
        return formatter.format(curDate)
    }

    /**
     * Get the mime type of the file in the url.
     *
     * @param url file url.
     * @return mime type.
     */
    fun getMimeType(url: String): String {
        val extension = getExtension(url)
        if (!MimeTypeMap.getSingleton().hasExtension(extension)) return ""
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        return if (TextUtils.isEmpty(mimeType)) "" else mimeType!!
    }

    /**
     * Get the file extension in url.
     *
     * @param url file url.
     * @return extension.
     */
    fun getExtension(url: String): String {
        var url = url
        url = if (TextUtils.isEmpty(url)) "" else url!!.lowercase(Locale.getDefault())
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        return if (TextUtils.isEmpty(extension)) "" else extension
    }

    /**
     * Specifies a tint for `drawable`.
     *
     * @param drawable drawable target, mutate.
     * @param color    color.
     */
    fun setDrawableTint(drawable: Drawable, @ColorInt color: Int) {
        DrawableCompat.setTint(DrawableCompat.wrap(drawable.mutate()), color)
    }

    /**
     * Specifies a tint for `drawable`.
     *
     * @param drawable drawable target, mutate.
     * @param color    color.
     * @return convert drawable.
     */
    fun getTintDrawable(drawable: Drawable, @ColorInt color: Int): Drawable {
        var drawable = drawable
        drawable = DrawableCompat.wrap(drawable.mutate())
        DrawableCompat.setTint(drawable, color)
        return drawable
    }

    /**
     * [ColorStateList].
     *
     * @param normal    normal color.
     * @param highLight highLight color.
     * @return [ColorStateList].
     */
    fun getColorStateList(@ColorInt normal: Int, @ColorInt highLight: Int): ColorStateList {
        val states = arrayOfNulls<IntArray>(6)
        states[0] = intArrayOf(R.attr.state_checked)
        states[1] = intArrayOf(R.attr.state_pressed)
        states[2] = intArrayOf(R.attr.state_selected)
        states[3] = intArrayOf()
        states[4] = intArrayOf()
        states[5] = intArrayOf()
        val colors = intArrayOf(highLight, highLight, highLight, normal, normal, normal)
        return ColorStateList(states, colors)
    }

    /**
     * Change part of the color of CharSequence.
     *
     * @param content content text.
     * @param start   start index.
     * @param end     end index.
     * @param color   color.
     * @return `SpannableString`.
     */
    fun getColorText(
        content: CharSequence,
        start: Int,
        end: Int,
        @ColorInt color: Int
    ): SpannableString {
        val stringSpan = SpannableString(content)
        stringSpan.setSpan(
            ForegroundColorSpan(color),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return stringSpan
    }

    /**
     * Return a color-int from alpha, red, green, blue components.
     *
     * @param color color.
     * @param alpha alpha, alpha component [0..255] of the color.
     */
    @ColorInt
    fun getAlphaColor(@ColorInt color: Int, @IntRange(from = 0, to = 255) alpha: Int): Int {
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

    /**
     * Generate divider.
     *
     * @param color color.
     * @return [Divider].
     */
    fun getDivider(@ColorInt color: Int): Divider {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Api21ItemDivider(color)
        } else Api20ItemDivider(color)
    }

    /**
     * Time conversion.
     *
     * @param duration ms.
     * @return such as: `00:00:00`, `00:00`.
     */
    fun convertDuration(@IntRange(from = 1) duration: Long): String {
        var duration = duration
        duration /= 1000
        val hour = (duration / 3600).toInt()
        val minute = ((duration - hour * 3600) / 60).toInt()
        val second = (duration - hour * 3600 - minute * 60).toInt()
        var hourValue = ""
        var minuteValue: String
        val secondValue: String
        if (hour > 0) {
            hourValue = if (hour >= 10) {
                Integer.toString(hour)
            } else {
                "0$hour"
            }
            hourValue += ":"
        }
        minuteValue = if (minute > 0) {
            if (minute >= 10) {
                Integer.toString(minute)
            } else {
                "0$minute"
            }
        } else {
            "00"
        }
        minuteValue += ":"
        secondValue = if (second > 0) {
            if (second >= 10) {
                Integer.toString(second)
            } else {
                "0$second"
            }
        } else {
            "00"
        }
        return hourValue + minuteValue + secondValue
    }

    /**
     * Get the MD5 value of string.
     *
     * @param content the target string.
     * @return the MD5 value.
     */
    fun getMD5ForString(content: String): String {
        val md5Buffer = StringBuilder()
        try {
            val digest = MessageDigest.getInstance("MD5")
            val tempBytes = digest.digest(content!!.toByteArray())
            var digital: Int
            for (i in tempBytes.indices) {
                digital = tempBytes[i].toInt()
                if (digital < 0) {
                    digital += 256
                }
                if (digital < 16) {
                    md5Buffer.append("0")
                }
                md5Buffer.append(Integer.toHexString(digital))
            }
        } catch (ignored: Exception) {
            return Integer.toString(content.hashCode())
        }
        return md5Buffer.toString()
    }
}