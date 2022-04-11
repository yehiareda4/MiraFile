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
package com.yehia.album_media.widget.divider

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.annotation.ColorInt

/**
 * Created by YanZhenjie on 2018/4/20.
 */
internal class ColorDrawer(color: Int, width: Int, height: Int) :
    Drawer(ColorDrawable(opaqueColor(color)), width, height) {
    companion object {
        /**
         * The target color is packaged in an opaque color.
         *
         * @param color color.
         * @return color.
         */
        @ColorInt
        fun opaqueColor(@ColorInt color: Int): Int {
            val alpha = Color.alpha(color)
            if (alpha == 0) return color
            val red = Color.red(color)
            val green = Color.green(color)
            val blue = Color.blue(color)
            return Color.argb(255, red, green, blue)
        }
    }
}