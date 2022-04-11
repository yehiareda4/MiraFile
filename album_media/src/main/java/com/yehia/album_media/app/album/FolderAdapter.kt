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

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.recyclerview.widget.RecyclerView
import com.yehia.album_media.Album
import com.yehia.album_media.AlbumFile
import com.yehia.album_media.AlbumFolder
import com.yehia.album_media.R
import com.yehia.album_media.app.album.FolderAdapter.FolderViewHolder
import com.yehia.album_media.impl.OnItemClickListener

/**
 *
 * BottomSheet dialog adapter, show all folder.
 * Created by Yan Zhenjie on 2016/10/18.
 */
internal class FolderAdapter(
    context: Context?,
    mAlbumFolders: List<AlbumFolder?>?,
    buttonTint: ColorStateList?
) : RecyclerView.Adapter<FolderViewHolder>() {
    private val mInflater: LayoutInflater
    private val mAlbumFolders: List<AlbumFolder?>?
    private val mSelector: ColorStateList?
    private var mItemClickListener: OnItemClickListener? = null
    fun setItemClickListener(itemClickListener: OnItemClickListener?) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        return FolderViewHolder(mInflater.inflate(R.layout.album_item_dialog_folder, parent, false),
            mSelector,
            object : OnItemClickListener {
                private var oldPosition = 0
                override fun onItemClick(view: View?, position: Int) {
                    if (mItemClickListener != null) mItemClickListener!!.onItemClick(view, position)
                    val albumFolder = mAlbumFolders!![position]
                    if (!albumFolder!!.isChecked) {
                        albumFolder.isChecked = true
                        mAlbumFolders[oldPosition]!!.isChecked = (false)
                        notifyItemChanged(oldPosition)
                        notifyItemChanged(position)
                        oldPosition = position
                    }
                }
            })
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val newPosition = holder.adapterPosition
        holder.setData(mAlbumFolders!![newPosition])
    }

    override fun getItemCount(): Int {
        return mAlbumFolders?.size ?: 0
    }

    internal class FolderViewHolder @SuppressLint("RestrictedApi") constructor(
        itemView: View,
        selector: ColorStateList?,
        itemClickListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val mItemClickListener: OnItemClickListener?
        private val mIvImage: ImageView
        private val mTvTitle: TextView
        private val mCheckBox: AppCompatRadioButton

        @SuppressLint("SetTextI18n")
        fun setData(albumFolder: AlbumFolder?) {
            val albumFiles: List<AlbumFile>? = albumFolder!!.albumFiles
            mTvTitle.text = "(" + albumFiles!!.size + ") " + albumFolder.name
            mCheckBox.isChecked = albumFolder.isChecked
            Album.albumConfig!!.albumLoader.load(mIvImage, albumFiles[0])
        }

        override fun onClick(v: View) {
            mItemClickListener?.onItemClick(v, adapterPosition)
        }

        init {
            mItemClickListener = itemClickListener
            mIvImage = itemView.findViewById(R.id.iv_gallery_preview_image)
            mTvTitle = itemView.findViewById(R.id.tv_gallery_preview_title)
            mCheckBox = itemView.findViewById(R.id.rb_gallery_preview_check)
            itemView.setOnClickListener(this)
            mCheckBox.supportButtonTintList = selector
        }
    }

    init {
        mInflater = LayoutInflater.from(context)
        mSelector = buttonTint
        this.mAlbumFolders = mAlbumFolders
    }
}