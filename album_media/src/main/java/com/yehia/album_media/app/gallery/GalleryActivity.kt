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
import com.yehia.album_media.Action
import com.yehia.album_media.Album
import com.yehia.album_media.ItemAction
import com.yehia.album_media.R
import com.yehia.album_media.api.widget.Widget
import com.yehia.album_media.app.Contract
import com.yehia.album_media.app.Contract.GalleryPresenter
import com.yehia.album_media.mvp.BaseActivity

/**
 * Created by YanZhenjie on 2017/8/16.
 */
class GalleryActivity : BaseActivity(), GalleryPresenter {
    private var mWidget: Widget? = null
    private var mPathList: ArrayList<String>? = null
    private var mCurrentPosition = 0
    private var mCheckable = false
    private var mCheckedMap: MutableMap<String, Boolean>? = null
    private var mView: Contract.GalleryView<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.album_activity_gallery)
        mView = GalleryView(this, this)
        val argument = intent.extras!!
        mWidget = argument.getParcelable(Album.KEY_INPUT_WIDGET)
        mPathList = argument.getStringArrayList(Album.KEY_INPUT_CHECKED_LIST)
        mCurrentPosition = argument.getInt(Album.KEY_INPUT_CURRENT_POSITION)
        mCheckable = argument.getBoolean(Album.KEY_INPUT_GALLERY_CHECKABLE)
        mCheckedMap = HashMap()
        for (path in mPathList!!) (mCheckedMap as HashMap<String, Boolean>)[path] = true
        mView!!.setTitle(mWidget!!.title)
        mView!!.setupViews(mWidget!!, mCheckable)
        if (!mCheckable) mView!!.setBottomDisplay(false)
        mView!!.setLayerDisplay(false)
        mView!!.setDurationDisplay(false)
        mView!!.bindData(mPathList)
        if (mCurrentPosition == 0) {
            onCurrentChanged(mCurrentPosition)
        } else {
            mView!!.setCurrentItem(mCurrentPosition)
        }
        setCheckedCount()
    }

    private fun setCheckedCount() {
        var checkedCount = 0
        for ((_, value) in mCheckedMap!!) {
            if (value) checkedCount += 1
        }
        var completeText = getString(R.string.album_menu_finish)
        completeText += "(" + checkedCount + " / " + mPathList!!.size + ")"
        mView!!.setCompleteText(completeText)
    }

    override fun clickItem(position: Int) {
        if (sClick != null) {
            sClick!!.onAction(this@GalleryActivity, mPathList!![mCurrentPosition]!!)
        }
    }

    override fun longClickItem(position: Int) {
        if (sLongClick != null) {
            sLongClick!!.onAction(this@GalleryActivity, mPathList!![mCurrentPosition])
        }
    }

    override fun onCurrentChanged(position: Int) {
        mCurrentPosition = position
        mView!!.setSubTitle("${position + 1}  /  ${mPathList!!.size}")
        if (mCheckable) mView!!.setChecked(mCheckedMap!![mPathList!![position]]!!)
    }

    override fun onCheckedChanged() {
        val path = mPathList!![mCurrentPosition]
        mCheckedMap!![path] = !mCheckedMap!![path]!!
        setCheckedCount()
    }

    override fun complete() {
        if (sResult != null) {
            val checkedList = ArrayList<String>()
            for ((key, value) in mCheckedMap!!) {
                if (value) checkedList.add(key)
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
        var sResult: Action<ArrayList<String>>? = null
        var sCancel: Action<String>? = null
        var sClick: ItemAction<String>? = null
        var sLongClick: ItemAction<String>? = null
    }
}