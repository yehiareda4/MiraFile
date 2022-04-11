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
package com.yehia.album_media.app.camera

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import com.yehia.album_media.Action
import com.yehia.album_media.Album
import com.yehia.album_media.R
import com.yehia.album_media.mvp.BaseActivity
import com.yehia.album_media.util.AlbumUtils
import com.yehia.album_media.util.SystemBar
import java.io.File

/**
 * Created by YanZhenjie on 2017/8/16.
 */
class CameraActivity : BaseActivity() {

    private var mFunction = 0
    private var mCameraFilePath: String? = null
    private var mQuality = 0
    private var mLimitDuration: Long = 0
    private var mLimitBytes: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBar.setStatusBarColor(this, Color.TRANSPARENT)
        SystemBar.setNavigationBarColor(this, Color.TRANSPARENT)
        SystemBar.invasionNavigationBar(this)
        SystemBar.invasionNavigationBar(this)
        if (savedInstanceState != null) {
            mFunction = savedInstanceState.getInt(INSTANCE_CAMERA_FUNCTION)
            mCameraFilePath = savedInstanceState.getString(INSTANCE_CAMERA_FILE_PATH)
            mQuality = savedInstanceState.getInt(INSTANCE_CAMERA_QUALITY)
            mLimitDuration = savedInstanceState.getLong(INSTANCE_CAMERA_DURATION)
            mLimitBytes = savedInstanceState.getLong(INSTANCE_CAMERA_BYTES)
        } else {
            val bundle = intent.extras!!
            mFunction = bundle.getInt(Album.KEY_INPUT_FUNCTION)
            mCameraFilePath = bundle.getString(Album.KEY_INPUT_FILE_PATH)
            mQuality = bundle.getInt(Album.KEY_INPUT_CAMERA_QUALITY)
            mLimitDuration = bundle.getLong(Album.KEY_INPUT_CAMERA_DURATION)
            mLimitBytes = bundle.getLong(Album.KEY_INPUT_CAMERA_BYTES)
            when (mFunction) {
                Album.FUNCTION_CAMERA_IMAGE -> {
                    if (TextUtils.isEmpty(mCameraFilePath)) mCameraFilePath =
                        AlbumUtils.randomJPGPath(this)
                    requestPermission(
                        BaseActivity.PERMISSION_TAKE_PICTURE,
                        CODE_PERMISSION_IMAGE
                    )
                }
                Album.FUNCTION_CAMERA_VIDEO -> {
                    if (TextUtils.isEmpty(mCameraFilePath)) mCameraFilePath =
                        AlbumUtils.randomMP4Path(this)
                    requestPermission(
                        BaseActivity.Companion.PERMISSION_TAKE_VIDEO,
                        CODE_PERMISSION_VIDEO
                    )
                }
                else -> {
                    throw AssertionError("This should not be the case.")
                }
            }
        }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(INSTANCE_CAMERA_FUNCTION, mFunction)
        outState.putString(INSTANCE_CAMERA_FILE_PATH, mCameraFilePath)
        outState.putInt(INSTANCE_CAMERA_QUALITY, mQuality)
        outState.putLong(INSTANCE_CAMERA_DURATION, mLimitDuration)
        outState.putLong(INSTANCE_CAMERA_BYTES, mLimitBytes)
        super.onSaveInstanceState(outState)
    }

    override fun onPermissionGranted(code: Int) {
        when (code) {
            CODE_PERMISSION_IMAGE -> {
                AlbumUtils.takeImage(this, CODE_ACTIVITY_TAKE_IMAGE, File(mCameraFilePath))
            }
            CODE_PERMISSION_VIDEO -> {
                AlbumUtils.takeVideo(
                    this,
                    CODE_ACTIVITY_TAKE_VIDEO,
                    File(mCameraFilePath),
                    mQuality,
                    mLimitDuration,
                    mLimitBytes
                )
            }
            else -> {
                throw AssertionError("This should not be the case.")
            }
        }
    }

    override fun onPermissionDenied(code: Int) {
        val messageRes = when (mFunction) {
            Album.FUNCTION_CAMERA_IMAGE -> {
                R.string.album_permission_camera_image_failed_hint
            }
            Album.FUNCTION_CAMERA_VIDEO -> {
                R.string.album_permission_camera_video_failed_hint
            }
            else -> {
                throw AssertionError("This should not be the case.")
            }
        }
        AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(R.string.album_title_permission_failed)
            .setMessage(messageRes)
            .setPositiveButton(R.string.album_ok) { dialog, which -> callbackCancel() }
            .show()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CODE_ACTIVITY_TAKE_IMAGE, CODE_ACTIVITY_TAKE_VIDEO -> {
                if (resultCode == RESULT_OK) {
                    callbackResult()
                } else {
                    callbackCancel()
                }
            }
            else -> {
                throw AssertionError("This should not be the case.")
            }
        }
    }

    private fun callbackResult() {
        if (sResult != null) sResult!!.onAction(mCameraFilePath!!)
        sResult = null
        sCancel = null
        finish()
    }

    private fun callbackCancel() {
        if (sCancel != null) sCancel!!.onAction("User canceled.")
        sResult = null
        sCancel = null
        finish()
    }

    companion object {
        private const val INSTANCE_CAMERA_FUNCTION = "INSTANCE_CAMERA_FUNCTION"
        private const val INSTANCE_CAMERA_FILE_PATH = "INSTANCE_CAMERA_FILE_PATH"
        private const val INSTANCE_CAMERA_QUALITY = "INSTANCE_CAMERA_QUALITY"
        private const val INSTANCE_CAMERA_DURATION = "INSTANCE_CAMERA_DURATION"
        private const val INSTANCE_CAMERA_BYTES = "INSTANCE_CAMERA_BYTES"
        private const val CODE_PERMISSION_IMAGE = 1
        private const val CODE_PERMISSION_VIDEO = 2
        private const val CODE_ACTIVITY_TAKE_IMAGE = 1
        private const val CODE_ACTIVITY_TAKE_VIDEO = 2
        var sResult: Action<String>? = null
        var sCancel: Action<String>? = null
    }
}