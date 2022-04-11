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
package com.yehia.album_media.app.gallery

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener
import com.yehia.album_media.Album
import com.yehia.album_media.AlbumFile
import com.yehia.album_media.R
import com.yehia.album_media.api.widget.Widget
import com.yehia.album_media.app.Contract
import com.yehia.album_media.app.Contract.GalleryPresenter
import com.yehia.album_media.util.SystemBar

/**
 * Created by YanZhenjie on 2018/4/9.
 */
class GalleryView<Data>(private val mActivity: Activity, presenter: GalleryPresenter?) :
    Contract.GalleryView<Data>(
        mActivity, presenter
    ), View.OnClickListener {
    private var mCompleteMenu: MenuItem? = null
    private val mViewPager: ViewPager
    private val mLayoutBottom: RelativeLayout
    private val mTvDuration: TextView
    private val mCheckBox: AppCompatCheckBox
    private val mLayoutLayer: FrameLayout
    override fun onCreateOptionsMenu(menu: Menu) {
        menuInflater!!.inflate(R.menu.album_menu_gallery, menu)
        mCompleteMenu = menu.findItem(R.id.album_menu_finish)
    }

    override fun onOptionsItemSelected(item: MenuItem) {
        val id = item.itemId
        if (id == R.id.album_menu_finish) {
            presenter!!.complete()
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setupViews(widget: Widget, checkable: Boolean) {
        SystemBar.invasionStatusBar(mActivity)
        SystemBar.invasionNavigationBar(mActivity)
        SystemBar.setStatusBarColor(mActivity, Color.TRANSPARENT)
        SystemBar.setNavigationBarColor(mActivity, getColor(R.color.albumSheetBottom))
        setHomeAsUpIndicator(R.drawable.album_ic_back_white)
        if (!checkable) {
            mCompleteMenu!!.isVisible = false
            mCheckBox.visibility = View.GONE
        } else {
            val itemSelector = widget.mediaItemCheckSelector
            mCheckBox.supportButtonTintList = itemSelector
            mCheckBox.setTextColor(itemSelector)
        }
        mViewPager.addOnPageChangeListener(object : SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                presenter!!.onCurrentChanged(position)
            }
        })
    }

    override fun bindData(dataList: List<Data>?) {
        val adapter: PreviewAdapter<Data> = object : PreviewAdapter<Data>(context, dataList) {
            override fun loadPreview(imageView: ImageView?, item: Data, position: Int) {
                if (item is String) {
                    Album.albumConfig!!.albumLoader.load(imageView, item as String)
                } else if (item is AlbumFile) {
                    Album.albumConfig!!.albumLoader.load(imageView, item as AlbumFile)
                }
            }
        }
        adapter.setItemClickListener { presenter!!.clickItem(mViewPager.currentItem) }
        adapter.setItemLongClickListener { presenter!!.longClickItem(mViewPager.currentItem) }
        if (adapter.count > 3) {
            mViewPager.offscreenPageLimit = 3
        } else if (adapter.count > 2) {
            mViewPager.offscreenPageLimit = 2
        }
        mViewPager.adapter = adapter
    }

    override fun setCurrentItem(position: Int) {
        mViewPager.currentItem = position
    }

    override fun setDurationDisplay(display: Boolean) {
        mTvDuration.visibility = if (display) View.VISIBLE else View.GONE
    }

    override fun setDuration(duration: String) {
        mTvDuration.text = duration
    }

    override fun setChecked(checked: Boolean) {
        mCheckBox.isChecked = checked
    }

    override fun setBottomDisplay(display: Boolean) {
        mLayoutBottom.visibility = if (display) View.VISIBLE else View.GONE
    }

    override fun setLayerDisplay(display: Boolean) {
        mLayoutLayer.visibility = if (display) View.VISIBLE else View.GONE
    }

    override fun setCompleteText(text: String) {
        mCompleteMenu!!.title = text
    }

    override fun onClick(v: View) {
        if (v === mCheckBox) {
            presenter!!.onCheckedChanged()
        } else if (v === mLayoutLayer) {
            // Intercept click events.
        }
    }

    init {
        mViewPager = mActivity.findViewById(R.id.view_pager)
        mLayoutBottom = mActivity.findViewById(R.id.layout_bottom)
        mTvDuration = mActivity.findViewById(R.id.tv_duration)
        mCheckBox = mActivity.findViewById(R.id.check_box)
        mLayoutLayer = mActivity.findViewById(R.id.layout_layer)
        mCheckBox.setOnClickListener(this)
        mLayoutLayer.setOnClickListener(this)
    }
}