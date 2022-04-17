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

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import com.yehia.album_media.*
import com.yehia.album_media.api.widget.Widget
import com.yehia.album_media.app.Contract
import com.yehia.album_media.app.Contract.AlbumPresenter
import com.yehia.album_media.app.album.data.*
import com.yehia.album_media.impl.OnItemClickListener
import com.yehia.album_media.mvp.BaseActivity
import com.yehia.album_media.util.AlbumUtils
import com.yehia.album_media.widget.LoadingDialog
import com.yehia.mediascanner.MediaScanner
import java.io.File

/**
 *
 * Responsible for controlling the album data and the overall logic.
 * Created by Yan Zhenjie on 2016/10/17.
 */
class AlbumActivity : BaseActivity(), AlbumPresenter, MediaReadTask.Callback,
    GalleryActivity.Callback, PathConvertTask.Callback, ThumbnailBuildTask.Callback {

    private var mAlbumFolders: List<AlbumFolder?>? = null
    private var mCurrentFolder = 0
    private var mWidget: Widget? = null
    private var mFunction = 0
    private var mChoiceMode = 0
    private var mColumnCount = 0
    private var mHasCamera = false
    private var mLimitCount = 0
    private var mQuality = 0
    private var mLimitDuration: Long = 0
    private var mLimitBytes: Long = 0
    private var mFilterVisibility = false
    private var mCheckedList: ArrayList<AlbumFile>? = null
    private var mMediaScanner: MediaScanner? = null
    private var mView: Contract.AlbumView? = null
    private var mFolderDialog: FolderDialog? = null
    private var mCameraPopupMenu: PopupMenu? = null
    private var mLoadingDialog: LoadingDialog? = null
    private var mMediaReadTask: MediaReadTask? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeArgument()
        setContentView(createView())
        mView = AlbumView(this, this)
        mView!!.setupViews(mWidget, mColumnCount, mHasCamera, mChoiceMode)
        mView!!.setTitle(mWidget!!.title!!)
        mView!!.setCompleteDisplay(false)
        mView!!.setLoadingDisplay(true)

        requestPermission(PERMISSION_STORAGE, CODE_PERMISSION_STORAGE)
    }

    private fun initializeArgument() {
        val argument = intent.extras!!
        mWidget = argument.getParcelable(Album.KEY_INPUT_WIDGET)
        mFunction = argument.getInt(Album.KEY_INPUT_FUNCTION)
        mChoiceMode = argument.getInt(Album.KEY_INPUT_CHOICE_MODE)
        mColumnCount = argument.getInt(Album.KEY_INPUT_COLUMN_COUNT)
        mHasCamera = argument.getBoolean(Album.KEY_INPUT_ALLOW_CAMERA)
        mLimitCount = argument.getInt(Album.KEY_INPUT_LIMIT_COUNT)
        mQuality = argument.getInt(Album.KEY_INPUT_CAMERA_QUALITY)
        mLimitDuration = argument.getLong(Album.KEY_INPUT_CAMERA_DURATION)
        mLimitBytes = argument.getLong(Album.KEY_INPUT_CAMERA_BYTES)
        mFilterVisibility = argument.getBoolean(Album.KEY_INPUT_FILTER_VISIBILITY)
    }

    /**
     * Use different layouts depending on the style.
     *
     * @return layout id.
     */
    private fun createView(): Int {
        return when (mWidget?.uiStyle) {
            Widget.STYLE_DARK -> {
                R.layout.album_activity_album_dark
            }
            Widget.STYLE_LIGHT -> {
                R.layout.album_activity_album_light
            }
            else -> {
                throw AssertionError("This should not be the case.")
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mView!!.onConfigurationChanged(newConfig)
        if (mFolderDialog != null && !mFolderDialog!!.isShowing) mFolderDialog = null
    }

    override fun onPermissionGranted(code: Int) {
        val checkedList: ArrayList<AlbumFile> =
            intent.getParcelableArrayListExtra(Album.KEY_INPUT_CHECKED_LIST)!!
        val mediaReader =
            MediaReader(this, sSizeFilter, sMimeFilter, sDurationFilter, mFilterVisibility)
        mMediaReadTask = MediaReadTask(mFunction, checkedList, mediaReader, this)
        mMediaReadTask!!.execute()
    }

    override fun onPermissionDenied(code: Int) {
        AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(R.string.album_title_permission_failed)
            .setMessage(R.string.album_permission_storage_failed_hint)
            .setPositiveButton(R.string.album_ok) { dialog, which -> callbackCancel() }
            .show()
    }

    override fun onScanCallback(
        albumFolders: ArrayList<AlbumFolder>?,
        checkedFiles: ArrayList<AlbumFile>?
    ) {
        mMediaReadTask = null
        when (mChoiceMode) {
            Album.MODE_MULTIPLE -> {
                mView!!.setCompleteDisplay(true)
            }
            Album.MODE_SINGLE -> {
                mView!!.setCompleteDisplay(false)
            }
            else -> {
                throw AssertionError("This should not be the case.")
            }
        }
        mView!!.setLoadingDisplay(false)
        mAlbumFolders = albumFolders
        mCheckedList = checkedFiles
        if (mAlbumFolders!![0]!!.albumFiles!!.isEmpty()) {
            val intent = Intent(this, NullActivity::class.java)
            intent.putExtras(getIntent())
            startActivityForResult(intent, CODE_ACTIVITY_NULL)
        } else {
            showFolderAlbumFiles(0)
            val count = mCheckedList!!.size
            mView!!.setCheckedCount(count)
            mView!!.setSubTitle("$count/$mLimitCount")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CODE_ACTIVITY_NULL -> {
                if (resultCode == RESULT_OK) {
                    val imagePath: String = NullActivity.parsePath(data)!!
                    val mimeType = AlbumUtils.getMimeType(imagePath)
                    if (!TextUtils.isEmpty(mimeType)) mCameraAction.onAction(imagePath)
                } else {
                    callbackCancel()
                }
            }
        }
    }

    override fun clickFolderSwitch() {
        if (mFolderDialog == null) {
            mFolderDialog =
                FolderDialog(this, mWidget, mAlbumFolders, object : OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        mCurrentFolder = position
                        showFolderAlbumFiles(mCurrentFolder)
                    }
                })
        }
        if (!mFolderDialog!!.isShowing) mFolderDialog!!.show()
    }

    /**
     * Update data source.
     */
    private fun showFolderAlbumFiles(position: Int) {
        mCurrentFolder = position
        val albumFolder = mAlbumFolders!![position]
        mView!!.bindAlbumFolder(albumFolder)
    }

    override fun clickCamera(v: View?) {
        val hasCheckSize = mCheckedList!!.size
        if (hasCheckSize >= mLimitCount) {
            val messageRes: Int
            messageRes = when (mFunction) {
                Album.FUNCTION_CHOICE_IMAGE -> {
                    R.plurals.album_check_image_limit_camera
                }
                Album.FUNCTION_CHOICE_VIDEO -> {
                    R.plurals.album_check_video_limit_camera
                }
                Album.FUNCTION_CHOICE_ALBUM -> {
                    R.plurals.album_check_album_limit_camera
                }
                else -> {
                    throw AssertionError("This should not be the case.")
                }
            }
            mView!!.toast(resources.getQuantityString(messageRes, mLimitCount, mLimitCount))
        } else {
            when (mFunction) {
                Album.FUNCTION_CHOICE_IMAGE -> {
                    takePicture()
                }
                Album.FUNCTION_CHOICE_VIDEO -> {
                    takeVideo()
                }
                Album.FUNCTION_CHOICE_ALBUM -> {
                    if (mCameraPopupMenu == null) {
                        mCameraPopupMenu = PopupMenu(this, v!!)
                        mCameraPopupMenu!!.menuInflater.inflate(
                            R.menu.album_menu_item_camera,
                            mCameraPopupMenu!!.menu
                        )
                        mCameraPopupMenu!!.setOnMenuItemClickListener { item ->
                            val id = item.itemId
                            if (id == R.id.album_menu_camera_image) {
                                takePicture()
                            } else if (id == R.id.album_menu_camera_video) {
                                takeVideo()
                            }
                            true
                        }
                    }
                    mCameraPopupMenu!!.show()
                }
                else -> {
                    throw AssertionError("This should not be the case.")
                }
            }
        }
    }

    private fun takePicture() {
        val filePath: String
        filePath = if (mCurrentFolder == 0) {
            AlbumUtils.randomJPGPath()
        } else {
            val file = File(mAlbumFolders!![mCurrentFolder]!!.albumFiles!![0].path)
            AlbumUtils.randomJPGPath(file.parentFile)
        }
        Album.camera(this)
            .image()
            .filePath(filePath)
            ?.onResult(mCameraAction)
            ?.start()
    }

    private fun takeVideo() {
        val filePath: String = if (mCurrentFolder == 0) {
            AlbumUtils.randomMP4Path()
        } else {
            val file = File(mAlbumFolders!![mCurrentFolder]!!.albumFiles!![0].path)
            AlbumUtils.randomMP4Path(file.parentFile)
        }
        Album.camera(this)
            .video()
            .filePath(filePath)
            ?.quality(mQuality)
            ?.limitDuration(mLimitDuration)
            ?.limitBytes(mLimitBytes)
            ?.onResult(mCameraAction)
            ?.start()
    }

    private val mCameraAction: Action<String> = object : Action<String> {
        override fun onAction(result: String) {
            if (mMediaScanner == null) {
                mMediaScanner = MediaScanner(this@AlbumActivity)
            }
            mMediaScanner!!.scan(result)
            val conversion = PathConversion(sSizeFilter, sMimeFilter, sDurationFilter)
            val task = PathConvertTask(conversion, this@AlbumActivity)
            task.execute(result)
        }
    }

    override fun onConvertStart() {
        showLoadingDialog()
        mLoadingDialog!!.setMessage(R.string.album_converting)
    }

    override fun onConvertCallback(albumFile: AlbumFile) {
        albumFile.isChecked = !albumFile.isDisable
        if (albumFile.isDisable) {
            if (mFilterVisibility) addFileToList(albumFile) else mView!!.toast(getString(R.string.album_take_file_unavailable))
        } else {
            addFileToList(albumFile)
        }
        dismissLoadingDialog()
    }

    private fun addFileToList(albumFile: AlbumFile) {
        if (mCurrentFolder != 0) {
            val albumFiles: MutableList<AlbumFile>? = mAlbumFolders!![0]!!.albumFiles
            if (albumFiles!!.size > 0) albumFiles.add(0, albumFile) else albumFiles.add(albumFile)
        }
        val albumFolder = mAlbumFolders!![mCurrentFolder]
        val albumFiles: MutableList<AlbumFile>? = albumFolder!!.albumFiles
        if (albumFiles!!.isEmpty()) {
            albumFiles.add(albumFile)
            mView!!.bindAlbumFolder(albumFolder)
        } else {
            albumFiles.add(0, albumFile)
            mView!!.notifyInsertItem(if (mHasCamera) 1 else 0)
        }
        mCheckedList!!.add(albumFile)
        val count = mCheckedList!!.size
        mView!!.setCheckedCount(count)
        mView!!.setSubTitle("$count/$mLimitCount")
        when (mChoiceMode) {
            Album.MODE_SINGLE -> {
                callbackResult()
            }
            Album.MODE_MULTIPLE -> {}
            else -> {
                throw AssertionError("This should not be the case.")
            }
        }
    }

    override fun tryCheckItem(button: CompoundButton, position: Int) {
        val albumFile = mAlbumFolders!![mCurrentFolder]!!.albumFiles!![position]
        if (button.isChecked) {
            if (mCheckedList!!.size >= mLimitCount) {
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
                mView!!.toast(resources.getQuantityString(messageRes, mLimitCount, mLimitCount))
                button.isChecked = false
            } else {
                albumFile.isChecked = true
                mCheckedList!!.add(albumFile)
                setCheckedCount()
            }
        } else {
            albumFile.isChecked = false
            mCheckedList!!.remove(albumFile)
            setCheckedCount()
        }
    }

    private fun setCheckedCount() {
        val count = mCheckedList!!.size
        mView!!.setCheckedCount(count)
        mView!!.setSubTitle("$count/$mLimitCount")
    }

    override fun tryPreviewItem(position: Int) {
        when (mChoiceMode) {
            Album.MODE_SINGLE -> {
                val albumFile = mAlbumFolders!![mCurrentFolder]!!.albumFiles!![position]
                //                albumFile.setChecked(true);
//                mView.notifyItem(position);
                mCheckedList!!.add(albumFile)
                setCheckedCount()
                callbackResult()
            }
            Album.MODE_MULTIPLE -> {
                GalleryActivity.sAlbumFiles =
                    mAlbumFolders!![mCurrentFolder]!!.albumFiles
                GalleryActivity.sCheckedCount = mCheckedList!!.size
                GalleryActivity.sCurrentPosition = position
                GalleryActivity.sCallback = this
                val intent = Intent(this, GalleryActivity::class.java)
                intent.putExtras(getIntent())
                startActivity(intent)
            }
            else -> {
                throw AssertionError("This should not be the case.")
            }
        }
    }

    override fun tryPreviewChecked() {
        if (mCheckedList!!.size > 0) {
            GalleryActivity.sAlbumFiles = ArrayList(mCheckedList)
            GalleryActivity.sCheckedCount = mCheckedList!!.size
            GalleryActivity.sCurrentPosition = 0
            GalleryActivity.sCallback = this
            val intent = Intent(this, GalleryActivity::class.java)
            intent.putExtras(getIntent())
            startActivity(intent)
        }
    }

    override fun onPreviewComplete() {
        callbackResult()
    }

    override fun onPreviewChanged(albumFile: AlbumFile) {
        val albumFiles = mAlbumFolders!![mCurrentFolder]!!.albumFiles
        val position = albumFiles!!.indexOf(albumFile)
        val notifyPosition = if (mHasCamera) position + 1 else position
        mView!!.notifyItem(notifyPosition)
        if (albumFile!!.isChecked) {
            if (!mCheckedList!!.contains(albumFile)) mCheckedList!!.add(albumFile)
        } else {
            if (mCheckedList!!.contains(albumFile)) mCheckedList!!.remove(albumFile)
        }
        setCheckedCount()
    }

    override fun complete() {
        if (mCheckedList!!.isEmpty()) {
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
            callbackResult()
        }
    }

    override fun onBackPressed() {
        if (mMediaReadTask != null) mMediaReadTask!!.cancel(true)
        callbackCancel()
    }

    /**
     * Callback result action.
     */
    private fun callbackResult() {
        val task = ThumbnailBuildTask(this, mCheckedList, this)
        task.execute()
    }

    override fun onThumbnailStart() {
        showLoadingDialog()
        mLoadingDialog!!.setMessage(R.string.album_thumbnail)
    }

    override fun onThumbnailCallback(albumFiles: ArrayList<AlbumFile>?) {
        if (sResult != null) sResult!!.onAction(albumFiles)
        dismissLoadingDialog()
        finish()
    }

    /**
     * Callback cancel action.
     */
    private fun callbackCancel() {
        if (sCancel != null) sCancel!!.onAction("User canceled.")
        finish()
    }

    /**
     * Display loading dialog.
     */
    private fun showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog(this)
            mLoadingDialog!!.setupViews(mWidget)
        }
        if (!mLoadingDialog!!.isShowing) {
            mLoadingDialog!!.show()
        }
    }

    /**
     * Dismiss loading dialog.
     */
    fun dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog!!.isShowing) {
            mLoadingDialog!!.dismiss()
        }
    }

    override fun finish() {
        sSizeFilter = null
        sMimeFilter = null
        sDurationFilter = null
        sResult = null
        sCancel = null
        super.finish()
    }

    companion object {
        private const val CODE_ACTIVITY_NULL = 1
        private const val CODE_PERMISSION_STORAGE = 1
        var sSizeFilter: Filter<Long>? = null
        var sMimeFilter: Filter<String>? = null
        var sDurationFilter: Filter<Long>? = null
        var sResult: Action<ArrayList<AlbumFile>?>? = null
        var sCancel: Action<String>? = null
    }

}