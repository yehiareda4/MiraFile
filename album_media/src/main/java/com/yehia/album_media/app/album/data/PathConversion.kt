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

import android.media.MediaPlayer
import android.text.TextUtils
import androidx.annotation.WorkerThread
import com.yehia.album_media.AlbumFile
import com.yehia.album_media.Filter
import com.yehia.album_media.util.AlbumUtils
import java.io.File

/**
 * Created by YanZhenjie on 2017/10/18.
 */
class PathConversion(
    private val mSizeFilter: Filter<Long>?,
    private val mMimeFilter: Filter<String>?,
    private val mDurationFilter: Filter<Long>?
) {
    @WorkerThread
    fun convert(filePath: String): AlbumFile {
        val file = File(filePath)
        val albumFile = AlbumFile()
        albumFile.path = filePath
        val parentFile = file.parentFile
        albumFile.bucketName = parentFile.name
        val mimeType = AlbumUtils.getMimeType(filePath)
        albumFile.mimeType = mimeType
        val nowTime = System.currentTimeMillis()
        albumFile.addDate = nowTime
        albumFile.size = file.length()
        var mediaType = 0
        if (!TextUtils.isEmpty(mimeType)) {
            if (mimeType.contains("video")) mediaType = AlbumFile.Companion.TYPE_VIDEO
            if (mimeType.contains("image")) mediaType = AlbumFile.Companion.TYPE_IMAGE
        }
        albumFile.mediaType = mediaType
        if (mSizeFilter != null && mSizeFilter.filter(file.length())) {
            albumFile.isDisable = true
        }
        if (mMimeFilter != null && mMimeFilter.filter(mimeType)) {
            albumFile.isDisable = true
        }
        if (mediaType == AlbumFile.Companion.TYPE_VIDEO) {
            val player = MediaPlayer()
            try {
                player.setDataSource(filePath)
                player.prepare()
                albumFile.duration = player.duration.toLong()
            } catch (ignored: Exception) {
            } finally {
                player.release()
            }
            if (mDurationFilter != null && mDurationFilter.filter(albumFile.duration)) {
                albumFile.isDisable = true
            }
        }
        return albumFile
    }
}