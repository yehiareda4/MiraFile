/*
 * Copyright 2018 Yan Zhenjie.
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
package com.yehia.album_media.widget

import android.app.Dialog
import android.content.Context
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.yehia.album_media.*
import com.yehia.album_media.api.widget.Widget

/**
 * Created by YanZhenjie on 2018/4/10.
 */
class LoadingDialog(context: Context) : Dialog(context, R.style.Album_Dialog) {
    private val mProgressBar: ColorProgressBar
    private val mTvMessage: TextView

    /**
     * Set some properties of the view.
     *
     * @param widget widget.
     */
    fun setupViews(widget: Widget?) {
        if (widget!!.uiStyle == Widget.STYLE_LIGHT) {
            val color = ContextCompat.getColor(context, R.color.albumLoadingDark)
            mProgressBar.setColorFilter(color)
        } else {
            mProgressBar.setColorFilter(widget.toolBarColor)
        }
    }

    /**
     * Set the message.
     *
     * @param message message resource id.
     */
    fun setMessage(@StringRes message: Int) {
        mTvMessage.setText(message)
    }

    /**
     * Set the message.
     *
     * @param message message.
     */
    fun setMessage(message: String) {
        mTvMessage.text = message
    }

    init {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setContentView(R.layout.album_dialog_loading)
        mProgressBar = findViewById(R.id.progress_bar)
        mTvMessage = findViewById(R.id.tv_message)
    }
}