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
import com.yehia.album_media.ItemAction

/**
 * Created by YanZhenjie on 2017/8/19.
 */
abstract class BasicGalleryWrapper<Returner : BasicGalleryWrapper<Returner, *, *, *>?, Result, Cancel, Checked>(
    context: Context
) : BasicAlbumWrapper<Returner, ArrayList<Result>, Cancel, ArrayList<Checked>?>(context) {
    var mItemClick: ItemAction<Checked>? = null
    var mItemLongClick: ItemAction<Checked>? = null
    var mCurrentPosition = 0
    var mCheckable = false

    /**
     * Set the list has been selected.
     *
     * @param checked the data list.
     */
    fun checkedList(checked: ArrayList<Checked>?): Returner {
        mChecked = checked
        return this as Returner
    }

    /**
     * When the preview item is clicked.
     *
     * @param click action.
     */
    fun itemClick(click: ItemAction<Checked>?): Returner {
        mItemClick = click
        return this as Returner
    }

    /**
     * When the preview item is clicked long.
     *
     * @param longClick action.
     */
    fun itemLongClick(longClick: ItemAction<Checked>?): Returner {
        mItemLongClick = longClick
        return this as Returner
    }

    /**
     * Set the show position of List.
     *
     * @param currentPosition the current position.
     */
    fun currentPosition(
        @IntRange(
            from = 0,
            to = Int.MAX_VALUE.toLong()
        ) currentPosition: Int
    ): Returner {
        mCurrentPosition = currentPosition
        return this as Returner
    }

    /**
     * The ability to select pictures.
     *
     * @param checkable checkBox is provided.
     */
    fun checkable(checkable: Boolean): Returner {
        mCheckable = checkable
        return this as Returner
    }
}