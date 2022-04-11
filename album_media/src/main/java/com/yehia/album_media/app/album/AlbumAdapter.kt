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
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import com.yehia.album_media.Album
import com.yehia.album_media.AlbumFile
import com.yehia.album_media.R
import com.yehia.album_media.impl.OnCheckedClickListener
import com.yehia.album_media.impl.OnItemClickListener
import com.yehia.album_media.util.AlbumUtils

/**
 *
 * Picture list display adapter.
 * Created by Yan Zhenjie on 2016/10/18.
 */
class AlbumAdapter(
    context: Context?,
    hasCamera: Boolean,
    choiceMode: Int,
    selector: ColorStateList?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mInflater: LayoutInflater
    private val hasCamera: Boolean
    private val mChoiceMode: Int
    private val mSelector: ColorStateList?
    private var mAlbumFiles: List<AlbumFile>? = null
    private var mAddPhotoClickListener: OnItemClickListener? = null
    private var mItemClickListener: OnItemClickListener? = null
    private var mCheckedClickListener: OnCheckedClickListener? = null
    fun setAlbumFiles(albumFiles: List<AlbumFile>?) {
        mAlbumFiles = albumFiles
    }

    fun setAddClickListener(addPhotoClickListener: OnItemClickListener?) {
        mAddPhotoClickListener = addPhotoClickListener
    }

    fun setItemClickListener(itemClickListener: OnItemClickListener?) {
        mItemClickListener = itemClickListener
    }

    fun setCheckedClickListener(checkedClickListener: OnCheckedClickListener?) {
        mCheckedClickListener = checkedClickListener
    }

    override fun getItemCount(): Int {
        val camera = if (hasCamera) 1 else 0
        return if (mAlbumFiles == null) camera else mAlbumFiles!!.size + camera
    }

    override fun getItemViewType(position: Int): Int {
        var position = position
        return when (position) {
            0 -> {
                if (hasCamera) TYPE_BUTTON else TYPE_IMAGE
            }
            else -> {
                position = if (hasCamera) position - 1 else position
                val albumFile = mAlbumFiles!![position]
                if (albumFile.mediaType == AlbumFile.Companion.TYPE_VIDEO) TYPE_VIDEO else TYPE_IMAGE
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_BUTTON -> {
                ButtonViewHolder(
                    mInflater.inflate(
                        R.layout.album_item_content_button,
                        parent,
                        false
                    ), mAddPhotoClickListener
                )
            }
            TYPE_IMAGE -> {
                val imageViewHolder = ImageHolder(
                    mInflater.inflate(
                        R.layout.album_item_content_image,
                        parent,
                        false
                    ),
                    hasCamera,
                    mItemClickListener,
                    mCheckedClickListener
                )
                if (mChoiceMode == Album.MODE_MULTIPLE) {
                    imageViewHolder.mCheckBox.visibility = View.VISIBLE
                    imageViewHolder.mCheckBox.supportButtonTintList = mSelector
                    imageViewHolder.mCheckBox.setTextColor(mSelector)
                } else {
                    imageViewHolder.mCheckBox.visibility = View.GONE
                }
                imageViewHolder
            }
            TYPE_VIDEO -> {
                val videoViewHolder = VideoHolder(
                    mInflater.inflate(
                        R.layout.album_item_content_video,
                        parent,
                        false
                    ),
                    hasCamera,
                    mItemClickListener,
                    mCheckedClickListener
                )
                if (mChoiceMode == Album.MODE_MULTIPLE) {
                    videoViewHolder.mCheckBox.visibility = View.VISIBLE
                    videoViewHolder.mCheckBox.supportButtonTintList = mSelector
                    videoViewHolder.mCheckBox.setTextColor(mSelector)
                } else {
                    videoViewHolder.mCheckBox.visibility = View.GONE
                }
                videoViewHolder
            }
            else -> {
                throw AssertionError("This should not be the case.")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var position = position
        when (getItemViewType(position)) {
            TYPE_BUTTON -> {}
            TYPE_IMAGE, TYPE_VIDEO -> {
                val mediaHolder = holder as MediaViewHolder
                val camera = if (hasCamera) 1 else 0
                position = holder.getAdapterPosition() - camera
                val albumFile = mAlbumFiles!![position]
                mediaHolder.setData(albumFile)
            }
            else -> {
                throw AssertionError("This should not be the case.")
            }
        }
    }

    private class ButtonViewHolder(
        itemView: View,
        private val mItemClickListener: OnItemClickListener?
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View) {
            if (mItemClickListener != null && v === itemView) {
                mItemClickListener.onItemClick(v, 0)
            }
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    private class ImageHolder(
        itemView: View,
        private val hasCamera: Boolean,
        private val mItemClickListener: OnItemClickListener?,
        private val mCheckedClickListener: OnCheckedClickListener?
    ) : MediaViewHolder(itemView), View.OnClickListener {
        private val mIvImage: ImageView
        val mCheckBox: AppCompatCheckBox
        private val mLayoutLayer: FrameLayout
        override fun setData(albumFile: AlbumFile) {
            mCheckBox.isChecked = albumFile.isChecked
            Album.albumConfig!!.albumLoader
                .load(mIvImage, albumFile)
            mLayoutLayer.visibility =
                if (albumFile.isDisable) View.VISIBLE else View.GONE
        }

        override fun onClick(v: View) {
            if (v === itemView) {
                val camera = if (hasCamera) 1 else 0
                mItemClickListener!!.onItemClick(v, adapterPosition - camera)
            } else if (v === mCheckBox) {
                val camera = if (hasCamera) 1 else 0
                mCheckedClickListener!!.onCheckedClick(mCheckBox, adapterPosition - camera)
            } else if (v === mLayoutLayer) {
                val camera = if (hasCamera) 1 else 0
                mItemClickListener!!.onItemClick(v, adapterPosition - camera)
            }
        }

        init {
            mIvImage = itemView.findViewById(R.id.iv_album_content_image)
            mCheckBox = itemView.findViewById(R.id.check_box)
            mLayoutLayer = itemView.findViewById(R.id.layout_layer)
            itemView.setOnClickListener(this)
            mCheckBox.setOnClickListener(this)
            mLayoutLayer.setOnClickListener(this)
        }
    }

    private class VideoHolder(
        itemView: View,
        private val hasCamera: Boolean,
        private val mItemClickListener: OnItemClickListener?,
        private val mCheckedClickListener: OnCheckedClickListener?
    ) : MediaViewHolder(itemView), View.OnClickListener {
        private val mIvImage: ImageView
        val mCheckBox: AppCompatCheckBox
        private val mTvDuration: TextView
        private val mLayoutLayer: FrameLayout
        override fun setData(albumFile: AlbumFile) {
            Album.albumConfig!!.albumLoader.load(mIvImage, albumFile)
            mCheckBox.isChecked = albumFile.isChecked
            mTvDuration.text = AlbumUtils.convertDuration(albumFile.duration)
            mLayoutLayer.visibility =
                if (albumFile.isDisable) View.VISIBLE else View.GONE
        }

        override fun onClick(v: View) {
            if (v === itemView) {
                val camera = if (hasCamera) 1 else 0
                mItemClickListener!!.onItemClick(v, adapterPosition - camera)
            } else if (v === mCheckBox) {
                val camera = if (hasCamera) 1 else 0
                mCheckedClickListener!!.onCheckedClick(mCheckBox, adapterPosition - camera)
            } else if (v === mLayoutLayer) {
                if (mItemClickListener != null) {
                    val camera = if (hasCamera) 1 else 0
                    mItemClickListener.onItemClick(v, adapterPosition - camera)
                }
            }
        }

        init {
            mIvImage = itemView.findViewById(R.id.iv_album_content_image)
            mCheckBox = itemView.findViewById(R.id.check_box)
            mTvDuration = itemView.findViewById(R.id.tv_duration)
            mLayoutLayer = itemView.findViewById(R.id.layout_layer)
            itemView.setOnClickListener(this)
            mCheckBox.setOnClickListener(this)
            mLayoutLayer.setOnClickListener(this)
        }
    }

    private abstract class MediaViewHolder(itemView: View?) : RecyclerView.ViewHolder(
        itemView!!
    ) {
        /**
         * Bind Item data.
         */
        abstract fun setData(albumFile: AlbumFile)
    }

    companion object {
        private const val TYPE_BUTTON = 1
        private const val TYPE_IMAGE = 2
        private const val TYPE_VIDEO = 3
    }

    init {
        mInflater = LayoutInflater.from(context)
        this.hasCamera = hasCamera
        mChoiceMode = choiceMode
        mSelector = selector
    }
}