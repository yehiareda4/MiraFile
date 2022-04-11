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

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Created by YanZhenjie on 2018/4/11.
 */
class TransferLayout : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun performClick(): Boolean {
        return if (childCount == 1) {
            getChildAt(0).performClick()
        } else super.performClick()
    }

    override fun callOnClick(): Boolean {
        return if (childCount == 1) {
            getChildAt(0).performClick()
        } else super.performClick()
    }
}