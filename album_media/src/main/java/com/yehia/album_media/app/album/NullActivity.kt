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

import android.content.Intent
import android.os.Bundle
import com.yehia.album_media.*
import com.yehia.album_media.api.widget.Widget
import com.yehia.album_media.app.Contract
import com.yehia.album_media.app.Contract.NullPresenter
import com.yehia.album_media.mvp.BaseActivity

/**
 * Created by YanZhenjie on 2017/3/28.
 */
class NullActivity : BaseActivity(), NullPresenter {
    private var mWidget: Widget? = null
    private var mQuality = 1
    private var mLimitDuration: Long = 0
    private var mLimitBytes: Long = 0
    private var mView: Contract.NullView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.album_activity_null)
        mView = NullView(this, this)
        val argument = intent.extras!!
        val function = argument.getInt(Album.KEY_INPUT_FUNCTION)
        val hasCamera = argument.getBoolean(Album.KEY_INPUT_ALLOW_CAMERA)
        mQuality = argument.getInt(Album.KEY_INPUT_CAMERA_QUALITY)
        mLimitDuration = argument.getLong(Album.KEY_INPUT_CAMERA_DURATION)
        mLimitBytes = argument.getLong(Album.KEY_INPUT_CAMERA_BYTES)
        mWidget = argument.getParcelable(Album.KEY_INPUT_WIDGET)
        mView!!.setupViews(mWidget)
        mView!!.setTitle(mWidget!!.title)
        when (function) {
            Album.FUNCTION_CHOICE_IMAGE -> {
                mView!!.setMessage(R.string.album_not_found_image)
                mView!!.setMakeVideoDisplay(false)
            }
            Album.FUNCTION_CHOICE_VIDEO -> {
                mView!!.setMessage(R.string.album_not_found_video)
                mView!!.setMakeImageDisplay(false)
            }
            Album.FUNCTION_CHOICE_ALBUM -> {
                mView!!.setMessage(R.string.album_not_found_album)
            }
            else -> {
                throw AssertionError("This should not be the case.")
            }
        }
        if (!hasCamera) {
            mView!!.setMakeImageDisplay(false)
            mView!!.setMakeVideoDisplay(false)
        }
    }

    override fun takePicture() {
        Album.camera(this)
            .image()
            .onResult(mCameraAction)
            .start()
    }

    override fun takeVideo() {
        Album.camera(this)
            .video()
            .quality(mQuality)
            .limitDuration(mLimitDuration)
            .limitBytes(mLimitBytes)
            .onResult(mCameraAction)
            .start()
    }

    private val mCameraAction: Action<String> = object :Action<String> {
        override fun onAction(result: String) {
            val intent = Intent()
            intent.putExtra(KEY_OUTPUT_IMAGE_PATH, result)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    companion object {
        private const val KEY_OUTPUT_IMAGE_PATH = "KEY_OUTPUT_IMAGE_PATH"
        fun parsePath(intent: Intent?): String? {
            return intent!!.getStringExtra(KEY_OUTPUT_IMAGE_PATH)
        }
    }
}