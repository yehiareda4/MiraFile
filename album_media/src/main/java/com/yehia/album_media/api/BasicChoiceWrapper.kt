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
import com.yehia.album_media.Filter

/**
 * Created by YanZhenjie on 2017/8/16.
 */
abstract class BasicChoiceWrapper<Returner : BasicChoiceWrapper<Returner, *, *, *>?, Result, Cancel, Checked> internal constructor(
    context: Context
) : BasicAlbumWrapper<Returner, Result, Cancel, Checked>(context) {
    var mHasCamera = true
    var mColumnCount = 2
    var mSizeFilter: Filter<Long>? = null
    var mMimeTypeFilter: Filter<String>? = null
    var mFilterVisibility = true

    /**
     * Turn on the camera function.
     */
    fun camera(hasCamera: Boolean): Returner {
        mHasCamera = hasCamera
        return this as Returner
    }

    /**
     * Sets the number of columns for the page.
     *
     * @param count the number of columns.
     */
    fun columnCount(@IntRange(from = 2, to = 4) count: Int): Returner {
        mColumnCount = count
        return this as Returner
    }

    /**
     * Filter the file size.
     *
     * @param filter filter.
     */
    fun filterSize(filter: Filter<Long>?): Returner {
        mSizeFilter = filter
        return this as Returner
    }

    /**
     * Filter the file extension.
     *
     * @param filter filter.
     */
    fun filterMimeType(filter: Filter<String>?): Returner {
        mMimeTypeFilter = filter
        return this as Returner
    }

    /**
     * The visibility of the filtered file.
     *
     * @param visibility true is displayed, false is not displayed.
     */
    fun afterFilterVisibility(visibility: Boolean): Returner {
        mFilterVisibility = visibility
        return this as Returner
    }
}