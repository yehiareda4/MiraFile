/*
 * Copyright 2016 Yan Zhenjie.
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

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.yehia.album_media.widget.photoview.AttacherImageView
import com.yehia.album_media.widget.photoview.PhotoViewAttacher
import com.yehia.album_media.widget.photoview.PhotoViewAttacher.OnViewTapListener

/**
 *
 * Adapter of preview the big picture.
 * Created by Yan Zhenjie on 2016/10/19.
 */
abstract class PreviewAdapter<T>(
    private val mContext: Context?,
    private val mPreviewList: List<T>?
) : PagerAdapter(), OnViewTapListener, View.OnLongClickListener {
    private var mItemClickListener: View.OnClickListener? = null
    private var mItemLongClickListener: View.OnClickListener? = null

    /**
     * Set item click listener.
     *
     * @param onClickListener listener.
     */
    fun setItemClickListener(onClickListener: View.OnClickListener?) {
        mItemClickListener = onClickListener
    }

    /**
     * Set item long click listener.
     *
     * @param longClickListener listener.
     */
    fun setItemLongClickListener(longClickListener: View.OnClickListener?) {
        mItemLongClickListener = longClickListener
    }

    override fun getCount(): Int {
        return mPreviewList?.size ?: 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = AttacherImageView(mContext)
        imageView.layoutParams = ViewGroup.LayoutParams(-1, -1)
        loadPreview(imageView, mPreviewList!![position], position)
        container.addView(imageView)
        val attacher = PhotoViewAttacher(imageView)
        if (mItemClickListener != null) {
            attacher.setOnViewTapListener(this)
        }
        if (mItemLongClickListener != null) {
            attacher.setOnLongClickListener(this)
        }
        imageView.setAttacher(attacher)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun onViewTap(v: View?, x: Float, y: Float) {
        mItemClickListener!!.onClick(v)
    }

    override fun onLongClick(v: View): Boolean {
        mItemLongClickListener!!.onClick(v)
        return true
    }

    protected abstract fun loadPreview(imageView: ImageView?, item: T, position: Int)
}