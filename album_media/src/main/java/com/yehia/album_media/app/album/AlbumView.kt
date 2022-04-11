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
import android.content.res.Configuration
import android.graphics.Color
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yehia.album_media.AlbumFolder
import com.yehia.album_media.R
import com.yehia.album_media.api.widget.Widget
import com.yehia.album_media.app.Contract
import com.yehia.album_media.app.Contract.AlbumPresenter
import com.yehia.album_media.impl.DoubleClickWrapper
import com.yehia.album_media.impl.OnCheckedClickListener
import com.yehia.album_media.impl.OnItemClickListener
import com.yehia.album_media.util.AlbumUtils
import com.yehia.album_media.util.SystemBar
import com.yehia.album_media.widget.ColorProgressBar
import com.yehia.album_media.widget.divider.Api21ItemDivider

/**
 * Created by YanZhenjie on 2018/4/7.
 */
internal class AlbumView(private val mActivity: Activity, presenter: AlbumPresenter?) :
    Contract.AlbumView(
        mActivity, presenter
    ), View.OnClickListener {
    private val mToolbar: Toolbar
    private var mCompleteMenu: MenuItem? = null
    private val mRecyclerView: RecyclerView
    private var mLayoutManager: GridLayoutManager? = null
    private var mAdapter: AlbumAdapter? = null
    private val mBtnPreview: Button
    private val mBtnSwitchFolder: Button
    private val mLayoutLoading: LinearLayout
    private val mProgressBar: ColorProgressBar
    override fun onCreateOptionsMenu(menu: Menu) {
        menuInflater?.inflate(R.menu.album_menu_album, menu)
        mCompleteMenu = menu.findItem(R.id.album_menu_finish)
    }

    override fun onOptionsItemSelected(item: MenuItem) {
        val itemId = item.itemId
        if (itemId == R.id.album_menu_finish) {
            presenter?.complete()
        }
    }

    override fun setupViews(widget: Widget?, column: Int, hasCamera: Boolean, choiceMode: Int) {
        SystemBar.setNavigationBarColor(mActivity, widget!!.navigationBarColor)
        val statusBarColor = widget.statusBarColor
        if (widget.uiStyle == Widget.STYLE_LIGHT) {
            if (SystemBar.setStatusBarDarkFont(mActivity, true)) {
                SystemBar.setStatusBarColor(mActivity, statusBarColor)
            } else {
                SystemBar.setStatusBarColor(mActivity, getColor(R.color.albumColorPrimaryBlack))
            }
            mProgressBar.setColorFilter(getColor(R.color.albumLoadingDark))
            val navigationIcon = getDrawable(R.drawable.album_ic_back_white)
            AlbumUtils.setDrawableTint(navigationIcon!!, getColor(R.color.albumIconDark))
            setHomeAsUpIndicator(navigationIcon)
            val completeIcon = mCompleteMenu!!.icon
            AlbumUtils.setDrawableTint(completeIcon, getColor(R.color.albumIconDark))
            mCompleteMenu!!.icon = completeIcon
        } else {
            mProgressBar.setColorFilter(widget.toolBarColor)
            SystemBar.setStatusBarColor(mActivity, statusBarColor)
            setHomeAsUpIndicator(R.drawable.album_ic_back_white)
        }
        mToolbar.setBackgroundColor(widget.toolBarColor)
        val config = mActivity.resources.configuration
        mLayoutManager = GridLayoutManager(context, column, getOrientation(config), false)
        mRecyclerView.layoutManager = mLayoutManager
        val dividerSize = resources.getDimensionPixelSize(R.dimen.album_dp_4)
        mRecyclerView.addItemDecoration(
            Api21ItemDivider(
                Color.TRANSPARENT,
                dividerSize,
                dividerSize
            )
        )
        mAdapter = AlbumAdapter(context, hasCamera, choiceMode, widget.mediaItemCheckSelector)
        mAdapter!!.setAddClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                presenter?.clickCamera(view)
            }

        })
        mAdapter!!.setCheckedClickListener(object : OnCheckedClickListener {
            override fun onCheckedClick(button: CompoundButton, position: Int) {
                presenter?.tryCheckItem(
                    button,
                    position
                )
            }
        })
        mAdapter!!.setItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                presenter?.tryPreviewItem(position)
            }
        })

        mRecyclerView.adapter = mAdapter
    }

    override fun setLoadingDisplay(display: Boolean) {
        mLayoutLoading.visibility = if (display) View.VISIBLE else View.GONE
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        val position = mLayoutManager!!.findFirstVisibleItemPosition()
        mLayoutManager!!.orientation = getOrientation(newConfig)
        mRecyclerView.adapter = mAdapter
        mLayoutManager!!.scrollToPosition(position)
    }

    @RecyclerView.Orientation
    private fun getOrientation(config: Configuration): Int {
        return when (config.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                LinearLayoutManager.VERTICAL
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                LinearLayoutManager.HORIZONTAL
            }
            else -> {
                throw AssertionError("This should not be the case.")
            }
        }
    }

    override fun setCompleteDisplay(display: Boolean) {
        mCompleteMenu!!.isVisible = display
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun bindAlbumFolder(albumFolder: AlbumFolder?) {
        mBtnSwitchFolder.text = albumFolder!!.name
        mAdapter!!.setAlbumFiles(albumFolder.albumFiles)
        mAdapter!!.notifyDataSetChanged()
        mRecyclerView.scrollToPosition(0)
    }

    override fun notifyInsertItem(position: Int) {
        mAdapter!!.notifyItemInserted(position)
    }

    override fun notifyItem(position: Int) {
        mAdapter!!.notifyItemChanged(position)
    }

    override fun setCheckedCount(count: Int) {
        mBtnPreview.text = " ($count)"
    }

    override fun onClick(v: View) {
        when {
            v === mToolbar -> {
                mRecyclerView.smoothScrollToPosition(0)
            }
            v === mBtnSwitchFolder -> {
                presenter!!.clickFolderSwitch()
            }
            v === mBtnPreview -> {
                presenter!!.tryPreviewChecked()
            }
        }
    }

    init {
        mToolbar = mActivity.findViewById(R.id.toolbar)
        mRecyclerView = mActivity.findViewById(R.id.recycler_view)
        mBtnSwitchFolder = mActivity.findViewById(R.id.btn_switch_dir)
        mBtnPreview = mActivity.findViewById(R.id.btn_preview)
        mLayoutLoading = mActivity.findViewById(R.id.layout_loading)
        mProgressBar = mActivity.findViewById(R.id.progress_bar)
        mToolbar.setOnClickListener(DoubleClickWrapper(this))
        mBtnSwitchFolder.setOnClickListener(this)
        mBtnPreview.setOnClickListener(this)
    }
}