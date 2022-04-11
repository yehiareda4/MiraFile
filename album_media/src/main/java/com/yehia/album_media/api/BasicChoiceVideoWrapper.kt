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
import androidx.annotation.IntRange

/**
 * Created by YanZhenjie on 2017/11/8.
 */
abstract class BasicChoiceVideoWrapper<Returner : BasicChoiceVideoWrapper<Returner, *, *, *>?, Result, Cancel, Checked> internal constructor(
    context: Context
) : BasicChoiceWrapper<Returner, Result, Cancel, Checked>(context) {
    var mQuality = 1
    var mLimitDuration = Int.MAX_VALUE.toLong()
    var mLimitBytes = Int.MAX_VALUE.toLong()

    /**
     * Set the quality when taking video, should be 0 or 1. Currently value 0 means low quality, and value 1 means high quality.
     *
     * @param quality should be 0 or 1.
     */
    fun quality(@IntRange(from = 0, to = 1) quality: Int): Returner {
        mQuality = quality
        return this as Returner
    }

    /**
     * Specify the maximum allowed recording duration in seconds.
     *
     * @param duration the maximum number of seconds.
     */
    fun limitDuration(@IntRange(from = 1) duration: Long): Returner {
        mLimitDuration = duration
        return this as Returner
    }

    /**
     * Specify the maximum allowed size.
     *
     * @param bytes the size of the byte.
     */
    fun limitBytes(@IntRange(from = 1) bytes: Long): Returner {
        mLimitBytes = bytes
        return this as Returner
    }
}