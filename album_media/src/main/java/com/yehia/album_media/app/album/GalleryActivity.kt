/*
 * Copyright 2017 Yan Zhenjie.
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

import android.os.Bundle
import com.yehia.album_media.Album
import com.yehia.album_media.AlbumFile
import com.yehia.album_media.R
import com.yehia.album_media.api.widget.Widget
import com.yehia.album_media.app.Contract
import com.yehia.album_media.app.Contract.GalleryPresenter
import com.yehia.album_media.app.gallery.GalleryView
import com.yehia.album_media.mvp.BaseActivity
import com.yehia.album_media.util.AlbumUtils

/**
 *
 * Preview the pictures in the folder in enlarged form.
 * Created by Yan Zhenjie on 2017/3/25.
 */
class GalleryActivity : BaseActivity(), GalleryPresenter {
    private var mWidget: Widget? = null
    private var mFunction = 0
    private var mAllowSelectCount = 0
    private var mView: Contract.GalleryView<AlbumFile>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.album_activity_gallery)
        mView = GalleryView(this, this)
        val argument = intent.extras!!
        mWidget = argument.getParcelable(Album.KEY_INPUT_WIDGET)
        mFunction = argument.getInt(Album.KEY_INPUT_FUNCTION)
        mAllowSelectCount = argument.getInt(Album.KEY_INPUT_LIMIT_COUNT)
        mView!!.setupViews(mWidget!!, true)
        mView!!.bindData(sAlbumFiles)
        if (sCurrentPosition == 0) {
            onCurrentChanged(sCurrentPosition)
        } else {
            mView!!.setCurrentItem(sCurrentPosition)
        }
        setCheckedCount()
    }

    private fun setCheckedCount() {
        var completeText = getString(R.string.album_menu_finish)
        completeText += "($sCheckedCount / $mAllowSelectCount)"
        mView!!.setCompleteText(completeText)
    }

    override fun clickItem(position: Int) {}
    override fun longClickItem(position: Int) {}
    override fun onCurrentChanged(position: Int) {
        sCurrentPosition = position
        mView!!.setTitle("${(sCurrentPosition + 1)} / ${sAlbumFiles!!.size}")
        val albumFile = sAlbumFiles!![position]
        mView!!.setChecked(albumFile.isChecked)
        mView!!.setLayerDisplay(albumFile.isDisable)
        if (albumFile.mediaType == AlbumFile.Companion.TYPE_VIDEO) {
            mView!!.setDuration(AlbumUtils.convertDuration(albumFile.duration))
            mView!!.setDurationDisplay(true)
        } else {
            mView!!.setDurationDisplay(false)
        }
    }

    override fun onCheckedChanged() {
        val albumFile = sAlbumFiles!![sCurrentPosition]
        if (albumFile!!.isChecked) {
            albumFile.isChecked = false
            sCallback!!.onPreviewChanged(albumFile)
            sCheckedCount--
        } else {
            if (sCheckedCount >= mAllowSelectCount) {
                val messageRes: Int
                messageRes = when (mFunction) {
                    Album.FUNCTION_CHOICE_IMAGE -> {
                        R.plurals.album_check_image_limit
                    }
                    Album.FUNCTION_CHOICE_VIDEO -> {
                        R.plurals.album_check_video_limit
                    }
                    Album.FUNCTION_CHOICE_ALBUM -> {
                        R.plurals.album_check_album_limit
                    }
                    else -> {
                        throw AssertionError("This should not be the case.")
                    }
                }
                mView!!.toast(
                    resources.getQuantityString(
                        messageRes,
                        mAllowSelectCount,
                        mAllowSelectCount
                    )
                )
                mView!!.setChecked(false)
            } else {
                albumFile.isChecked = true
                sCallback!!.onPreviewChanged(albumFile)
                sCheckedCount++
            }
        }
        setCheckedCount()
    }

    override fun complete() {
        if (sCheckedCount == 0) {
            val messageRes: Int
            messageRes = when (mFunction) {
                Album.FUNCTION_CHOICE_IMAGE -> {
                    R.string.album_check_image_little
                }
                Album.FUNCTION_CHOICE_VIDEO -> {
                    R.string.album_check_video_little
                }
                Album.FUNCTION_CHOICE_ALBUM -> {
                    R.string.album_check_album_little
                }
                else -> {
                    throw AssertionError("This should not be the case.")
                }
            }
            mView!!.toast(messageRes)
        } else {
            sCallback!!.onPreviewComplete()
            finish()
        }
    }

    override fun onBackPressed() {
        finish()
    }

    override fun finish() {
        sAlbumFiles = null
        sCheckedCount = 0
        sCurrentPosition = 0
        sCallback = null
        super.finish()
    }

    interface Callback {
        /**
         * Complete the preview.
         */
        fun onPreviewComplete()

        /**
         * Check or uncheck a item.
         *
         * @param albumFile target item.
         */
        fun onPreviewChanged(albumFile: AlbumFile)
    }

    companion object {
        var sAlbumFiles: ArrayList<AlbumFile>? = null
        var sCheckedCount = 0
        var sCurrentPosition = 0
        var sCallback: Callback? = null
    }
}