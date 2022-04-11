/*
 * Copyright © Yan Zhenjie. All Rights Reserved
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
package com.yehia.album_media.widget.photoview.scrollerproxy

import android.content.Context
import android.os.Build

abstract class ScrollerProxy {
    abstract fun computeScrollOffset(): Boolean
    abstract fun fling(
        startX: Int, startY: Int, velocityX: Int, velocityY: Int, minX: Int, maxX: Int, minY: Int,
        maxY: Int, overX: Int, overY: Int
    )

    abstract fun forceFinished(finished: Boolean)
    abstract val isFinished: Boolean
    abstract val currX: Int
    abstract val currY: Int

    companion object {
        fun getScroller(context: Context?): ScrollerProxy {
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                PreGingerScroller(context)
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                GingerScroller(context)
            } else {
                IcsScroller(context)
            }
        }
    }
}