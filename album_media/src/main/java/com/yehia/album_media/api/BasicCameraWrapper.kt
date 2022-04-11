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
package com.yehia.album_media.api

import android.content.Context
import com.yehia.album_media.Action

/**
 * Created by YanZhenjie on 2017/8/18.
 */
abstract class BasicCameraWrapper<Returner : BasicCameraWrapper<Returner>?>(var mContext: Context) {
    var mResult: Action<String>? = null
    var mCancel: Action<String>? = null
    var mFilePath: String? = null

    /**
     * Set the action when result.
     *
     * @param result action when producing result.
     */
    fun onResult(result: Action<String>?): Returner {
        mResult = result
        return this as Returner
    }

    /**
     * Set the action when canceling.
     *
     * @param cancel action when canceled.
     */
    fun onCancel(cancel: Action<String>?): Returner {
        mCancel = cancel
        return this as Returner
    }

    /**
     * Set the image storage path.
     *
     * @param filePath storage path.
     */
    fun filePath(filePath: String?): Returner {
        mFilePath = filePath
        return this as Returner
    }

    /**
     * Start up.
     */
    abstract fun start()
}