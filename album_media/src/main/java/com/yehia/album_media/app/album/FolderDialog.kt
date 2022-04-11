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
package com.yehia.album_media.app.album

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yehia.album_media.AlbumFolder
import com.yehia.album_media.R
import com.yehia.album_media.api.widget.Widget
import com.yehia.album_media.impl.OnItemClickListener

/**
 *
 * Folder preview.
 * Created by Yan Zhenjie on 2016/10/18.
 */
class FolderDialog(
    context: Context?,
    widget: Widget?,
    albumFolders: List<AlbumFolder?>?,
    itemClickListener: OnItemClickListener
) : BottomSheetDialog(
    context!!, R.style.Album_Dialog_Folder
) {
    private val mWidget: Widget?
    private val mFolderAdapter: FolderAdapter
    private val mAlbumFolders: List<AlbumFolder?>?
    private var mCurrentPosition = 0
    private val mItemClickListener: OnItemClickListener?
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        val window = window
        if (window != null) {
            val display = window.windowManager.defaultDisplay
            val metrics = DisplayMetrics()
            display.getRealMetrics(metrics)
            val minSize = Math.min(metrics.widthPixels, metrics.heightPixels)
            window.setLayout(minSize, -1)
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = mWidget?.navigationBarColor!!
        }
    }

    init {
        setContentView(R.layout.album_dialog_floder)
        mWidget = widget
        mAlbumFolders = albumFolders
        mItemClickListener = itemClickListener
        val recyclerView = delegate.findViewById<RecyclerView>(R.id.rv_content_list)!!
        recyclerView.layoutManager = LinearLayoutManager(getContext())
        mFolderAdapter = FolderAdapter(context, mAlbumFolders, widget?.bucketItemCheckSelector)
        mFolderAdapter.setItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                if (mCurrentPosition != position) {
                    mAlbumFolders!![mCurrentPosition]!!.isChecked = (false)
                    mFolderAdapter.notifyItemChanged(mCurrentPosition)
                    mCurrentPosition = position
                    mAlbumFolders[mCurrentPosition]!!.isChecked = (true)
                    mFolderAdapter.notifyItemChanged(mCurrentPosition)
                    mItemClickListener.onItemClick(view, position)
                }
                dismiss()
            }
        })
        recyclerView.adapter = mFolderAdapter
    }
}