/*
 * Copyright 2018 Yan Zhenjie
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
package com.yehia.album_media.app.album

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import com.yehia.album_media.R
import com.yehia.album_media.api.widget.Widget
import com.yehia.album_media.app.Contract
import com.yehia.album_media.app.Contract.NullPresenter
import com.yehia.album_media.util.AlbumUtils
import com.yehia.album_media.util.SystemBar

/**
 * Created by YanZhenjie on 2018/4/7.
 */
internal class NullView(private val mActivity: Activity, presenter: NullPresenter?) :
    Contract.NullView(
        mActivity, presenter
    ), View.OnClickListener {
    private val mToolbar: Toolbar
    private val mTvMessage: TextView
    private val mBtnTakeImage: AppCompatButton
    private val mBtnTakeVideo: AppCompatButton

    @SuppressLint("RestrictedApi")
    override fun setupViews(widget: Widget?) {
        mToolbar.setBackgroundColor(widget!!.toolBarColor)
        val statusBarColor = widget.statusBarColor
        val navigationIcon = getDrawable(R.drawable.album_ic_back_white)
        if (widget.uiStyle == Widget.STYLE_LIGHT) {
            if (SystemBar.setStatusBarDarkFont(mActivity, true)) {
                SystemBar.setStatusBarColor(mActivity, statusBarColor)
            } else {
                SystemBar.setStatusBarColor(mActivity, getColor(R.color.albumColorPrimaryBlack))
            }
            AlbumUtils.setDrawableTint(navigationIcon!!, getColor(R.color.albumIconDark))
            setHomeAsUpIndicator(navigationIcon)
        } else {
            SystemBar.setStatusBarColor(mActivity, statusBarColor)
            setHomeAsUpIndicator(navigationIcon)
        }
        SystemBar.setNavigationBarColor(mActivity, widget.getNavigationBarColor())
        val buttonStyle = widget.getButtonStyle()
        val buttonSelector = buttonStyle.buttonSelector
        mBtnTakeImage.supportBackgroundTintList = buttonSelector
        mBtnTakeVideo.supportBackgroundTintList = buttonSelector
        if (buttonStyle.uiStyle == Widget.STYLE_LIGHT) {
            var drawable = mBtnTakeImage.compoundDrawables[0]
            AlbumUtils.setDrawableTint(drawable, getColor(R.color.albumIconDark))
            mBtnTakeImage.setCompoundDrawables(drawable, null, null, null)
            drawable = mBtnTakeVideo.compoundDrawables[0]
            AlbumUtils.setDrawableTint(drawable, getColor(R.color.albumIconDark))
            mBtnTakeVideo.setCompoundDrawables(drawable, null, null, null)
            mBtnTakeImage.setTextColor(getColor(R.color.albumFontDark))
            mBtnTakeVideo.setTextColor(getColor(R.color.albumFontDark))
        }
    }

    override fun setMessage(message: Int) {
        mTvMessage.setText(message)
    }

    override fun setMakeImageDisplay(display: Boolean) {
        mBtnTakeImage.visibility = if (display) View.VISIBLE else View.GONE
    }

    override fun setMakeVideoDisplay(display: Boolean) {
        mBtnTakeVideo.visibility = if (display) View.VISIBLE else View.GONE
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.btn_camera_image) {
            presenter!!.takePicture()
        } else if (id == R.id.btn_camera_video) {
            presenter!!.takeVideo()
        }
    }

    init {
        mToolbar = mActivity.findViewById(R.id.toolbar)
        mTvMessage = mActivity.findViewById(R.id.tv_message)
        mBtnTakeImage = mActivity.findViewById(R.id.btn_camera_image)
        mBtnTakeVideo = mActivity.findViewById(R.id.btn_camera_video)
        mBtnTakeImage.setOnClickListener(this)
        mBtnTakeVideo.setOnClickListener(this)
    }
}