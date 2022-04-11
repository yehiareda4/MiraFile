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
import com.yehia.album_media.AlbumFile
import com.yehia.album_media.Filter
import com.yehia.album_media.app.album.AlbumActivity
import com.yehia.album_media.app.album.AlbumActivity.Companion.sDurationFilter
import com.yehia.album_media.app.album.AlbumActivity.Companion.sMimeFilter
import com.yehia.album_media.app.album.AlbumActivity.Companion.sSizeFilter

/**
 * Created by YanZhenjie on 2017/8/16.
 */
class VideoMultipleWrapper(context: Context) :
    BasicChoiceVideoWrapper<VideoMultipleWrapper?, ArrayList<AlbumFile>?, String, ArrayList<AlbumFile>?>(
        context
    ) {
    private var mLimitCount = Int.MAX_VALUE
    private var mDurationFilter: Filter<Long>? = null

    /**
     * Set the list has been selected.
     *
     * @param checked the data list.
     */
    fun checkedList(checked: ArrayList<AlbumFile>?): VideoMultipleWrapper {
        mChecked = checked
        return this
    }

    /**
     * Set the maximum number to be selected.
     *
     * @param count the maximum number.
     */
    fun selectCount(
        @IntRange(
            from = 1,
            to = Int.MAX_VALUE.toLong()
        ) count: Int
    ): VideoMultipleWrapper {
        mLimitCount = count
        return this
    }

    /**
     * Filter video duration.
     *
     * @param filter filter.
     */
    fun filterDuration(filter: Filter<Long>?): VideoMultipleWrapper {
        mDurationFilter = filter
        return this
    }

    override fun start() {
        sSizeFilter = mSizeFilter
        sMimeFilter = mMimeTypeFilter
        sDurationFilter = mDurationFilter
        AlbumActivity.sResult = mResult
        AlbumActivity.sCancel = mCancel
        val intent = Intent(mContext, AlbumActivity::class.java)
        intent.putExtra(Album.KEY_INPUT_WIDGET, mWidget)
        intent.putParcelableArrayListExtra(Album.KEY_INPUT_CHECKED_LIST, mChecked)
        intent.putExtra(Album.KEY_INPUT_FUNCTION, Album.FUNCTION_CHOICE_VIDEO)
        intent.putExtra(Album.KEY_INPUT_CHOICE_MODE, Album.MODE_MULTIPLE)
        intent.putExtra(Album.KEY_INPUT_COLUMN_COUNT, mColumnCount)
        intent.putExtra(Album.KEY_INPUT_ALLOW_CAMERA, mHasCamera)
        intent.putExtra(Album.KEY_INPUT_LIMIT_COUNT, mLimitCount)
        intent.putExtra(Album.KEY_INPUT_FILTER_VISIBILITY, mFilterVisibility)
        intent.putExtra(Album.KEY_INPUT_CAMERA_QUALITY, mQuality)
        intent.putExtra(Album.KEY_INPUT_CAMERA_DURATION, mLimitDuration)
        intent.putExtra(Album.KEY_INPUT_CAMERA_BYTES, mLimitBytes)
        mContext.startActivity(intent)
    }
}