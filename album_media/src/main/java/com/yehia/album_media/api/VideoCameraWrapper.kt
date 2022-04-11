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
import android.content.Intent
import androidx.annotation.IntRange
import com.yehia.album_media.Album
import com.yehia.album_media.app.camera.CameraActivity

/**
 *
 * Camera wrapper.
 * Created by Yan Zhenjie on 2017/4/18.
 */
class VideoCameraWrapper(context: Context) : BasicCameraWrapper<VideoCameraWrapper?>(context) {
    private var mQuality = 1
    private var mLimitDuration = Int.MAX_VALUE.toLong()
    private var mLimitBytes = Int.MAX_VALUE.toLong()

    /**
     * Currently value 0 means low quality, suitable for MMS messages, and  value 1 means high quality.
     *
     * @param quality should be 0 or 1.
     */
    fun quality(@IntRange(from = 0, to = 1) quality: Int): VideoCameraWrapper {
        mQuality = quality
        return this
    }

    /**
     * Specify the maximum allowed recording duration in seconds.
     *
     * @param duration the maximum number of seconds.
     */
    fun limitDuration(@IntRange(from = 1) duration: Long): VideoCameraWrapper {
        mLimitDuration = duration
        return this
    }

    /**
     * Specify the maximum allowed size.
     *
     * @param bytes the size of the byte.
     */
    fun limitBytes(@IntRange(from = 1) bytes: Long): VideoCameraWrapper {
        mLimitBytes = bytes
        return this
    }

    override fun start() {
        CameraActivity.sResult = mResult
        CameraActivity.sCancel = mCancel
        val intent = Intent(mContext, CameraActivity::class.java)
        intent.putExtra(Album.KEY_INPUT_FUNCTION, Album.FUNCTION_CAMERA_VIDEO)
        intent.putExtra(Album.KEY_INPUT_FILE_PATH, mFilePath)
        intent.putExtra(Album.KEY_INPUT_CAMERA_QUALITY, mQuality)
        intent.putExtra(Album.KEY_INPUT_CAMERA_DURATION, mLimitDuration)
        intent.putExtra(Album.KEY_INPUT_CAMERA_BYTES, mLimitBytes)
        mContext.startActivity(intent)
    }
}