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

import android.os.AsyncTask
import com.yehia.album_media.Album
import com.yehia.album_media.AlbumFile
import com.yehia.album_media.AlbumFolder
import com.yehia.album_media.app.album.data.MediaReadTask.ResultWrapper

/**
 *
 * Image scan task.
 * Created by Yan Zhenjie on 2017/3/28.
 */
class MediaReadTask(
    private val mFunction: Int,
    private val mCheckedFiles: List<AlbumFile>?,
    private val mMediaReader: MediaReader,
    private val mCallback: Callback
) : AsyncTask<Void?, Void?, ResultWrapper>() {
    interface Callback {
        /**
         * Callback the results.
         *
         * @param albumFolders album folder list.
         */
        fun onScanCallback(
            albumFolders: ArrayList<AlbumFolder>?,
            checkedFiles: ArrayList<AlbumFile>?
        )
    }

    class ResultWrapper {
        var mAlbumFolders: ArrayList<AlbumFolder>? = null
        var mAlbumFiles: ArrayList<AlbumFile>? = null
    }


    override fun onPostExecute(wrapper: ResultWrapper) {
        mCallback.onScanCallback(wrapper.mAlbumFolders, wrapper.mAlbumFiles)
    }

    override fun doInBackground(vararg p0: Void?): ResultWrapper {
        val albumFolders: ArrayList<AlbumFolder> = when (mFunction) {
            Album.FUNCTION_CHOICE_IMAGE -> {
                mMediaReader.allImage
            }
            Album.FUNCTION_CHOICE_VIDEO -> {
                mMediaReader.allVideo
            }
            Album.FUNCTION_CHOICE_ALBUM -> {
                mMediaReader.allMedia
            }
            else -> {
                throw AssertionError("This should not be the case.")
            }
        }
        val checkedFiles = ArrayList<AlbumFile>()
        if (mCheckedFiles != null && mCheckedFiles.isNotEmpty()) {
            val albumFiles: List<AlbumFile>? = albumFolders[0].albumFiles
            for (checkAlbumFile in mCheckedFiles) {
                for (i in albumFiles!!.indices) {
                    val albumFile = albumFiles[i]
                    if (checkAlbumFile == albumFile) {
                        albumFile.isChecked = true
                        checkedFiles.add(albumFile)
                    }
                }
            }
        }
        val wrapper = ResultWrapper()
        wrapper.mAlbumFolders = albumFolders
        wrapper.mAlbumFiles = checkedFiles
        return wrapper
    }
}