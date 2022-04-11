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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 *
 * The implementation of divider does not add dividers around the list.
 * Created by YanZhenjie on 2017/8/14.
 */
class Api20ItemDivider @JvmOverloads constructor(
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
        val layoutManager = parent.layoutManager
        if (layoutManager is LinearLayoutManager) {
            val orientation = getOrientation(layoutManager)
            val position = parent.getChildLayoutPosition(view)
            val spanCount = getSpanCount(layoutManager)
            val childCount = layoutManager.getItemCount()
            if (orientation == RecyclerView.VERTICAL) {
                offsetVertical(outRect, position, spanCount, childCount)
            } else {
                offsetHorizontal(outRect, position, spanCount, childCount)
            }
        } else if (layoutManager is StaggeredGridLayoutManager) {
            outRect[width, height, width] = height // |-|-
        }
    }

    private fun offsetHorizontal(outRect: Rect, position: Int, spanCount: Int, childCount: Int) {
        val firstRaw = isFirstRaw(RecyclerView.HORIZONTAL, position, spanCount, childCount)
        val lastRaw = isLastRaw(RecyclerView.HORIZONTAL, position, spanCount, childCount)
        val firstColumn = isFirstColumn(RecyclerView.HORIZONTAL, position, spanCount, childCount)
        val lastColumn = isLastColumn(RecyclerView.HORIZONTAL, position, spanCount, childCount)
        if (spanCount == 1) {
            if (firstColumn && lastColumn) { // xxxx
                outRect[0, 0, 0] = 0
            } else if (firstColumn) { // xx|x
                outRect[0, 0, width] = 0
            } else if (lastColumn) { // |xxx
                outRect[width, 0, 0] = 0
            } else { // |x|x
                outRect[width, 0, width] = 0
            }
        } else {
            if (firstColumn && firstRaw) { // xx|-
                outRect[0, 0, width] = height
            } else if (firstColumn && lastRaw) { // x-|x
                outRect[0, height, width] = 0
            } else if (lastColumn && firstRaw) { // |xx-
                outRect[width, 0, 0] = height
            } else if (lastColumn && lastRaw) { // |-xx
                outRect[width, height, 0] = 0
            } else if (firstColumn) { // x-|-
                outRect[0, height, width] = height
            } else if (lastColumn) { // |-x-
                outRect[width, height, 0] = height
            } else if (firstRaw) { // |x|-
                outRect[width, 0, width] = height
            } else if (lastRaw) { // |-|x
                outRect[width, height, width] = 0
            } else { // |-|-
                outRect[width, height, width] = height
            }
        }
    }

    private fun offsetVertical(outRect: Rect, position: Int, spanCount: Int, childCount: Int) {
        val firstRaw = isFirstRaw(RecyclerView.VERTICAL, position, spanCount, childCount)
        val lastRaw = isLastRaw(RecyclerView.VERTICAL, position, spanCount, childCount)
        val firstColumn = isFirstColumn(RecyclerView.VERTICAL, position, spanCount, childCount)
        val lastColumn = isLastColumn(RecyclerView.VERTICAL, position, spanCount, childCount)
        if (spanCount == 1) {
            if (firstRaw && lastRaw) { // xxxx
                outRect[0, 0, 0] = 0
            } else if (firstRaw) { // xxx-
                outRect[0, 0, 0] = height
            } else if (lastRaw) { // x-xx
                outRect[0, height, 0] = 0
            } else { // x-x-
                outRect[0, height, 0] = height
            }
        } else {
            if (firstRaw && firstColumn) { // xx|-
                outRect[0, 0, width] = height
            } else if (firstRaw && lastColumn) { // |xx-
                outRect[width, 0, 0] = height
            } else if (lastRaw && firstColumn) { // x-|x
                outRect[0, height, width] = 0
            } else if (lastRaw && lastColumn) { // |-xx
                outRect[width, height, 0] = 0
            } else if (firstRaw) { // |x|-
                outRect[width, 0, width] = height
            } else if (lastRaw) { // |-|x
                outRect[width, height, width] = 0
            } else if (firstColumn) { // x-|-
                outRect[0, height, width] = height
            } else if (lastColumn) { // |-x-
                outRect[width, height, 0] = height
            } else { // |-|-
                outRect[width, height, width] = height
            }
        }
    }

    private fun getOrientation(layoutManager: RecyclerView.LayoutManager?): Int {
        if (layoutManager is LinearLayoutManager) {
            return layoutManager.orientation
        } else if (layoutManager is StaggeredGridLayoutManager) {
            return layoutManager.orientation
        }
        return RecyclerView.VERTICAL
    }

    private fun getSpanCount(layoutManager: RecyclerView.LayoutManager?): Int {
        if (layoutManager is GridLayoutManager) {
            return layoutManager.spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            return layoutManager.spanCount
        }
        return 1
    }

    private fun isFirstRaw(
        orientation: Int,
        position: Int,
        columnCount: Int,
        childCount: Int
    ): Boolean {
        return if (orientation == RecyclerView.VERTICAL) {
            position < columnCount
        } else {
            if (columnCount == 1) true else position % columnCount == 0
        }
    }

    private fun isLastRaw(
        orientation: Int,
        position: Int,
        columnCount: Int,
        childCount: Int
    ): Boolean {
        return if (orientation == RecyclerView.VERTICAL) {
            if (columnCount == 1) {
                position + 1 == childCount
            } else {
                val lastRawItemCount = childCount % columnCount
                val rawCount =
                    (childCount - lastRawItemCount) / columnCount + if (lastRawItemCount > 0) 1 else 0
                val rawPositionJudge = (position + 1) % columnCount
                if (rawPositionJudge == 0) {
                    val positionRaw = (position + 1) / columnCount
                    rawCount == positionRaw
                } else {
                    val rawPosition = (position + 1 - rawPositionJudge) / columnCount + 1
                    rawCount == rawPosition
                }
            }
        } else {
            if (columnCount == 1) true else (position + 1) % columnCount == 0
        }
    }

    private fun isFirstColumn(
        orientation: Int,
        position: Int,
        columnCount: Int,
        childCount: Int
    ): Boolean {
        return if (orientation == RecyclerView.VERTICAL) {
            if (columnCount == 1) true else position % columnCount == 0
        } else {
            position < columnCount
        }
    }

    private fun isLastColumn(
        orientation: Int,
        position: Int,
        columnCount: Int,
        childCount: Int
    ): Boolean {
        return if (orientation == RecyclerView.VERTICAL) {
            if (columnCount == 1) true else (position + 1) % columnCount == 0
        } else {
            if (columnCount == 1) {
                position + 1 == childCount
            } else {
                val lastRawItemCount = childCount % columnCount
                val rawCount =
                    (childCount - lastRawItemCount) / columnCount + if (lastRawItemCount > 0) 1 else 0
                val rawPositionJudge = (position + 1) % columnCount
                if (rawPositionJudge == 0) {
                    val positionRaw = (position + 1) / columnCount
                    rawCount == positionRaw
                } else {
                    val rawPosition = (position + 1 - rawPositionJudge) / columnCount + 1
                    rawCount == rawPosition
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager
        val orientation = getOrientation(layoutManager)
        val spanCount = getSpanCount(layoutManager)
        val childCount = layoutManager!!.childCount
        if (layoutManager is LinearLayoutManager) {
            canvas.save()
            for (i in 0 until childCount) {
                val view = layoutManager.getChildAt(i)
                val position = parent.getChildLayoutPosition(view!!)
                if (orientation == RecyclerView.VERTICAL) {
                    drawVertical(canvas, view, position, spanCount, childCount)
                } else {
                    drawHorizontal(canvas, view, position, spanCount, childCount)
                }
            }
            canvas.restore()
        } else if (layoutManager is StaggeredGridLayoutManager) {
            canvas.save()
            for (i in 0 until childCount) {
                val view = layoutManager.getChildAt(i)
                mDrawer.drawLeft(view, canvas)
                mDrawer.drawTop(view, canvas)
                mDrawer.drawRight(view, canvas)
                mDrawer.drawBottom(view, canvas)
            }
            canvas.restore()
        }
    }

    private fun drawHorizontal(
        canvas: Canvas,
        view: View?,
        position: Int,
        spanCount: Int,
        childCount: Int
    ) {
        val firstRaw = isFirstRaw(RecyclerView.HORIZONTAL, position, spanCount, childCount)
        val lastRaw = isLastRaw(RecyclerView.HORIZONTAL, position, spanCount, childCount)
        val firstColumn = isFirstColumn(RecyclerView.HORIZONTAL, position, spanCount, childCount)
        val lastColumn = isLastColumn(RecyclerView.HORIZONTAL, position, spanCount, childCount)
        if (spanCount == 1) {
            if (firstRaw && lastColumn) { // xxxx
                // Nothing.
            } else if (firstColumn) { // xx|x
                mDrawer.drawRight(view, canvas)
            } else if (lastColumn) { // |xxx
                mDrawer.drawLeft(view, canvas)
            } else { // |x|x
                mDrawer.drawLeft(view, canvas)
                mDrawer.drawRight(view, canvas)
            }
        } else {
            if (firstColumn && firstRaw) { // xx|-
                mDrawer.drawRight(view, canvas)
                mDrawer.drawBottom(view, canvas)
            } else if (firstColumn && lastRaw) { // x-|x
                mDrawer.drawTop(view, canvas)
                mDrawer.drawRight(view, canvas)
            } else if (lastColumn && firstRaw) { // |xx-
                mDrawer.drawLeft(view, canvas)
                mDrawer.drawBottom(view, canvas)
            } else if (lastColumn && lastRaw) { // |-xx
                mDrawer.drawLeft(view, canvas)
                mDrawer.drawTop(view, canvas)
            } else if (firstColumn) { // x-|-
                mDrawer.drawTop(view, canvas)
                mDrawer.drawRight(view, canvas)
                mDrawer.drawBottom(view, canvas)
            } else if (lastColumn) { // |-x-
                mDrawer.drawLeft(view, canvas)
                mDrawer.drawTop(view, canvas)
                mDrawer.drawBottom(view, canvas)
            } else if (firstRaw) { // |x|-
                mDrawer.drawLeft(view, canvas)
                mDrawer.drawRight(view, canvas)
                mDrawer.drawBottom(view, canvas)
            } else if (lastRaw) { // |-|x
                mDrawer.drawLeft(view, canvas)
                mDrawer.drawTop(view, canvas)
                mDrawer.drawRight(view, canvas)
            } else { // |-|-
                mDrawer.drawLeft(view, canvas)
                mDrawer.drawTop(view, canvas)
                mDrawer.drawRight(view, canvas)
                mDrawer.drawBottom(view, canvas)
            }
        }
    }

    private fun drawVertical(
        canvas: Canvas,
        view: View?,
        position: Int,
        spanCount: Int,
        childCount: Int
    ) {
        val firstRaw = isFirstRaw(RecyclerView.VERTICAL, position, spanCount, childCount)
        val lastRaw = isLastRaw(RecyclerView.VERTICAL, position, spanCount, childCount)
        val firstColumn = isFirstColumn(RecyclerView.VERTICAL, position, spanCount, childCount)
        val lastColumn = isLastColumn(RecyclerView.VERTICAL, position, spanCount, childCount)
        if (spanCount == 1) {
            if (firstRaw && lastRaw) { // xxxx
                // Nothing.
            } else if (firstRaw) { // xxx-
                mDrawer.drawBottom(view, canvas)
            } else if (lastRaw) { // x-xx
                mDrawer.drawTop(view, canvas)
            } else { // x-x-
                mDrawer.drawTop(view, canvas)
                mDrawer.drawBottom(view, canvas)
            }
        } else {
            if (firstRaw && firstColumn) { // xx|-
                mDrawer.drawRight(view, canvas)
                mDrawer.drawBottom(view, canvas)
            } else if (firstRaw && lastColumn) { // |xx-
                mDrawer.drawLeft(view, canvas)
                mDrawer.drawBottom(view, canvas)
            } else if (lastRaw && firstColumn) { // x-|x
                mDrawer.drawTop(view, canvas)
                mDrawer.drawRight(view, canvas)
            } else if (lastRaw && lastColumn) { // |-xx
                mDrawer.drawLeft(view, canvas)
                mDrawer.drawTop(view, canvas)
            } else if (firstRaw) { // |x|-
                mDrawer.drawLeft(view, canvas)
                mDrawer.drawRight(view, canvas)
                mDrawer.drawBottom(view, canvas)
            } else if (lastRaw) { // |-|x
                mDrawer.drawLeft(view, canvas)
                mDrawer.drawTop(view, canvas)
                mDrawer.drawRight(view, canvas)
            } else if (firstColumn) { // x-|-
                mDrawer.drawTop(view, canvas)
                mDrawer.drawRight(view, canvas)
                mDrawer.drawBottom(view, canvas)
            } else if (lastColumn) { // |-x-
                mDrawer.drawLeft(view, canvas)
                mDrawer.drawTop(view, canvas)
                mDrawer.drawBottom(view, canvas)
            } else { // |-|-
                mDrawer.drawLeft(view, canvas)
                mDrawer.drawTop(view, canvas)
                mDrawer.drawRight(view, canvas)
                mDrawer.drawBottom(view, canvas)
            }
        }
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