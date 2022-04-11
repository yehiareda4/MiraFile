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
import com.yehia.album_media.api.widget.Widget

/**
 *
 * Album basic wrapper.
 * Created by yanzhenjie on 17-3-29.
 */
abstract class BasicAlbumWrapper<Returner : BasicAlbumWrapper<Returner, *, *, *>?, Result, Cancel, Checked> internal constructor(
    val mContext: Context
) {
    var mResult: Action<Result>? = null
    var mCancel: Action<Cancel>? = null
    var mWidget: Widget?
    var mChecked: Checked? = null

    /**
     * Set the action when result.
     *
     * @param result action when producing result.
     */
    fun onResult(result: Action<Result>?): Returner {
        mResult = result
        return this as Returner
    }

    /**
     * Set the action when canceling.
     *
     * @param cancel action when canceled.
     */
    fun onCancel(cancel: Action<Cancel>?): Returner {
        mCancel = cancel
        return this as Returner
    }

    /**
     * Set the widget property.
     *
     * @param widget the widget.
     */
    fun widget(widget: Widget?): Returner {
        mWidget = widget
        return this as Returner
    }

    /**
     * Start up.
     */
    abstract fun start()

    init {
        mWidget = Widget.Companion.getDefaultWidget(mContext)
    }
}