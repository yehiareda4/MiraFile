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
import com.yehia.album_media.AlbumFile

/**
 * Created by YanZhenjie on 2017/10/18.
 */
class PathConvertTask(private val mConversion: PathConversion, private val mCallback: Callback) :
    AsyncTask<String, Void?, AlbumFile>() {
    interface Callback {
        /**
         * The task begins.
         */
        fun onConvertStart()

        /**
         * Callback results.
         *
         * @param albumFile result.
         */
        fun onConvertCallback(albumFile: AlbumFile)
    }

    override fun onPreExecute() {
        mCallback.onConvertStart()
    }

    protected override fun doInBackground(vararg params: String): AlbumFile {
        return mConversion.convert(params[0])
    }

    override fun onPostExecute(file: AlbumFile) {
        mCallback.onConvertCallback(file)
    }
}