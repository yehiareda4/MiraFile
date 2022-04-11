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
package com.yehia.album_media.app.gallery

import android.os.Bundle
import com.yehia.album_media.*
import com.yehia.album_media.api.widget.Widget
import com.yehia.album_media.app.Contract
import com.yehia.album_media.app.Contract.GalleryPresenter
import com.yehia.album_media.mvp.BaseActivity
import com.yehia.album_media.util.AlbumUtils

/**
 * Created by YanZhenjie on 2017/8/16.
 */
class GalleryAlbumActivity : BaseActivity(), GalleryPresenter {
    private var mWidget: Widget? = null
    private var mAlbumFiles: ArrayList<AlbumFile>? = null
    private var mCurrentPosition = 0
    private var mCheckable = false
    private var mView: Contract.GalleryView<AlbumFile>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.album_activity_gallery)
        mView = GalleryView(this, this)
        val argument = intent.extras!!
        mWidget = argument.getParcelable(Album.KEY_INPUT_WIDGET)
        mAlbumFiles = argument.getParcelableArrayList(Album.KEY_INPUT_CHECKED_LIST)
        mCurrentPosition = argument.getInt(Album.KEY_INPUT_CURRENT_POSITION)
        mCheckable = argument.getBoolean(Album.KEY_INPUT_GALLERY_CHECKABLE)
        mView!!.setTitle(mWidget!!.title)
        mView!!.setupViews(mWidget!!, mCheckable)
        mView!!.bindData(mAlbumFiles)
        if (mCurrentPosition == 0) {
            onCurrentChanged(mCurrentPosition)
        } else {
            mView!!.setCurrentItem(mCurrentPosition)
        }
        setCheckedCount()
    }

    private fun setCheckedCount() {
        var checkedCount = 0
        for (albumFile in mAlbumFiles!!) {
            if (albumFile!!.isChecked) checkedCount += 1
        }
        var completeText = getString(R.string.album_menu_finish)
        completeText += "(" + checkedCount + " / " + mAlbumFiles!!.size + ")"
        mView!!.setCompleteText(completeText)
    }

    override fun clickItem(position: Int) {
        if (sClick != null) {
            sClick!!.onAction(this@GalleryAlbumActivity, mAlbumFiles!![mCurrentPosition]!!)
        }
    }

    override fun longClickItem(position: Int) {
        if (sLongClick != null) {
            sLongClick!!.onAction(this@GalleryAlbumActivity, mAlbumFiles!![mCurrentPosition]!!)
        }
    }

    override fun onCurrentChanged(position: Int) {
        mCurrentPosition = position
        mView!!.setSubTitle("${position + 1}  / ${mAlbumFiles!!.size}")
        val albumFile = mAlbumFiles!![position]
        if (mCheckable) mView!!.setChecked(albumFile!!.isChecked)
        mView!!.setLayerDisplay(albumFile!!.isDisable)
        if (albumFile.mediaType == AlbumFile.Companion.TYPE_VIDEO) {
            if (!mCheckable) mView!!.setBottomDisplay(true)
            mView!!.setDuration(AlbumUtils.convertDuration(albumFile.duration))
            mView!!.setDurationDisplay(true)
        } else {
            if (!mCheckable) mView!!.setBottomDisplay(false)
            mView!!.setDurationDisplay(false)
        }
    }

    override fun onCheckedChanged() {
        val albumFile = mAlbumFiles!![mCurrentPosition]
        albumFile.isChecked = (!albumFile.isChecked)
        setCheckedCount()
    }

    override fun complete() {
        if (sResult != null) {
            val checkedList = ArrayList<AlbumFile>()
            for (albumFile in mAlbumFiles!!) {
                if (albumFile!!.isChecked) checkedList.add(albumFile)
            }
            sResult!!.onAction(checkedList)
        }
        finish()
    }

    override fun onBackPressed() {
        if (sCancel != null) sCancel!!.onAction("User canceled.")
        finish()
    }

    override fun finish() {
        sResult = null
        sCancel = null
        sClick = null
        sLongClick = null
        super.finish()
    }

    companion object {
        var sResult: Action<ArrayList<AlbumFile>>? = null
        var sCancel: Action<String>? = null
        var sClick: ItemAction<AlbumFile>? = null
        var sLongClick: ItemAction<AlbumFile>? = null
    }
}