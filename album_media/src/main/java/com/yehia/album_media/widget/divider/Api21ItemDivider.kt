/*
 * Copyright 2017 Yan Zhenjie
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

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * The implementation of divider adds dividers around the list.
 * Created by YanZhenjie on 2017/8/14.
 */
class Api21ItemDivider @JvmOverloads constructor(
    @ColorInt color: Int,
    width: Int = 4,
    height: Int = 4
) : Divider() {
    override val width: Int
    override val height: Int
    private val mDrawer: Drawer
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect[width, height, width] = height
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        canvas.save()
        val layoutManager = parent.layoutManager
        val childCount = layoutManager!!.childCount
        for (i in 0 until childCount) {
            val view = layoutManager.getChildAt(i)
            mDrawer.drawLeft(view, canvas)
            mDrawer.drawTop(view, canvas)
            mDrawer.drawRight(view, canvas)
            mDrawer.drawBottom(view, canvas)
        }
        canvas.restore()
    }
    /**
     * @param color  line color.
     * @param width  line width.
     * @param height line height.
     */
    /**
     * @param color divider line color.
     */
    init {
        this.width = Math.round(width / 2f)
        this.height = Math.round(height / 2f)
        mDrawer = ColorDrawer(color, this.width, this.height)
    }
}