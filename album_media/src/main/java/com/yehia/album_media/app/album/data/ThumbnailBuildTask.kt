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
import android.os.AsyncTask
import com.yehia.album_media.AlbumFile

/**
 * Created by YanZhenjie on 2017/10/15.
 */
class ThumbnailBuildTask(
    context: Context,
    private val mAlbumFiles: ArrayList<AlbumFile>?,
    private val mCallback: Callback
) : AsyncTask<Void?, Void?, ArrayList<AlbumFile>?>() {
    interface Callback {
        /**
         * The task begins.
         */
        fun onThumbnailStart()

        /**
         * Callback results.
         *
         * @param albumFiles result.
         */
        fun onThumbnailCallback(albumFiles: ArrayList<AlbumFile>?)
    }

    private val mThumbnailBuilder: ThumbnailBuilder = ThumbnailBuilder(context)
    override fun onPreExecute() {
        mCallback.onThumbnailStart()
    }

    override fun onPostExecute(albumFiles: ArrayList<AlbumFile>?) {
        mCallback.onThumbnailCallback(albumFiles)
    }

    override fun doInBackground(vararg p0: Void?): ArrayList<AlbumFile>? {
        for (albumFile in mAlbumFiles!!) {
            val mediaType = albumFile.mediaType
            if (mediaType == AlbumFile.TYPE_IMAGE) {
                albumFile.thumbPath =
                    (mThumbnailBuilder.createThumbnailForImage(albumFile.path!!))
            } else if (mediaType == AlbumFile.TYPE_VIDEO) {
                albumFile.thumbPath = (mThumbnailBuilder.createThumbnailForVideo(albumFile.path!!))
            }
        }
        return mAlbumFiles
    }
}