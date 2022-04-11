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
package com.yehia.album_media

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.annotation.IntDef
import androidx.fragment.app.Fragment
import com.yehia.album_media.api.*
import com.yehia.album_media.api.camera.AlbumCamera
import com.yehia.album_media.api.camera.Camera
import com.yehia.album_media.api.choice.AlbumChoice
import com.yehia.album_media.api.choice.Choice
import com.yehia.album_media.api.choice.ImageChoice
import com.yehia.album_media.api.choice.VideoChoice
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 *
 * Entrance.
 * Created by Yan Zhenjie on 2016/10/23.
 */
object Album {
    // All.
    const val KEY_INPUT_WIDGET = "KEY_INPUT_WIDGET"
    const val KEY_INPUT_CHECKED_LIST = "KEY_INPUT_CHECKED_LIST"

    // Album.
    const val KEY_INPUT_FUNCTION = "KEY_INPUT_FUNCTION"
    const val FUNCTION_CHOICE_IMAGE = 0
    const val FUNCTION_CHOICE_VIDEO = 1
    const val FUNCTION_CHOICE_ALBUM = 2
    const val FUNCTION_CAMERA_IMAGE = 0
    const val FUNCTION_CAMERA_VIDEO = 1
    const val KEY_INPUT_CHOICE_MODE = "KEY_INPUT_CHOICE_MODE"
    const val MODE_MULTIPLE = 1
    const val MODE_SINGLE = 2
    const val KEY_INPUT_COLUMN_COUNT = "KEY_INPUT_COLUMN_COUNT"
    const val KEY_INPUT_ALLOW_CAMERA = "KEY_INPUT_ALLOW_CAMERA"
    const val KEY_INPUT_LIMIT_COUNT = "KEY_INPUT_LIMIT_COUNT"

    // Gallery.
    const val KEY_INPUT_CURRENT_POSITION = "KEY_INPUT_CURRENT_POSITION"
    const val KEY_INPUT_GALLERY_CHECKABLE = "KEY_INPUT_GALLERY_CHECKABLE"

    // Camera.
    const val KEY_INPUT_FILE_PATH = "KEY_INPUT_FILE_PATH"
    const val KEY_INPUT_CAMERA_QUALITY = "KEY_INPUT_CAMERA_QUALITY"
    const val KEY_INPUT_CAMERA_DURATION = "KEY_INPUT_CAMERA_DURATION"
    const val KEY_INPUT_CAMERA_BYTES = "KEY_INPUT_CAMERA_BYTES"

    // Filter.
    const val KEY_INPUT_FILTER_VISIBILITY = "KEY_INPUT_FILTER_VISIBILITY"
    private var sAlbumConfig: AlbumConfig? = null

    /**
     * Initialize Album.
     *
     * @param albumConfig [AlbumConfig].
     */
    fun initialize(albumConfig: AlbumConfig?) {
        if (sAlbumConfig == null) sAlbumConfig = albumConfig else Log.w(
            "Album",
            IllegalStateException("Illegal operation, only allowed to configure once.")
        )
    }

    /**
     * Get the album configuration.
     */
    val albumConfig: AlbumConfig?
        get() {
            if (sAlbumConfig == null) {
                sAlbumConfig = AlbumConfig.Companion.newBuilder(null).build()
            }
            return sAlbumConfig
        }

    /**
     * Open the camera from the activity.
     */
    fun camera(context: Context): Camera<ImageCameraWrapper, VideoCameraWrapper> {
        return AlbumCamera(context)
    }

    /**
     * Select images.
     */
    fun image(context: Context?): Choice<ImageMultipleWrapper, ImageSingleWrapper> {
        return ImageChoice(context)
    }

    /**
     * Select videos.
     */
    fun video(context: Context?): Choice<VideoMultipleWrapper, VideoSingleWrapper> {
        return VideoChoice(context)
    }

    /**
     * Select images and videos.
     */
    fun album(context: Context?): Choice<AlbumMultipleWrapper, AlbumSingleWrapper> {
        return AlbumChoice(context!!)
    }

    /**
     * Preview picture.
     */
    fun gallery(context: Context?): GalleryWrapper {
        return GalleryWrapper(context)
    }

    /**
     * Preview Album.
     */
    fun galleryAlbum(context: Context?): GalleryAlbumWrapper {
        return GalleryAlbumWrapper(context)
    }

    /**
     * Open the camera from the activity.
     */
    fun camera(activity: Activity): Camera<ImageCameraWrapper, VideoCameraWrapper> {
        return AlbumCamera(activity)
    }

    /**
     * Select images.
     */
    fun image(activity: Activity?): Choice<ImageMultipleWrapper, ImageSingleWrapper> {
        return ImageChoice(activity)
    }

    /**
     * Select videos.
     */
    fun video(activity: Activity?): Choice<VideoMultipleWrapper, VideoSingleWrapper> {
        return VideoChoice(activity)
    }

    /**
     * Select images and videos.
     */
    fun album(activity: Activity?): Choice<AlbumMultipleWrapper, AlbumSingleWrapper> {
        return AlbumChoice(activity!!)
    }

    /**
     * Preview picture.
     */
    fun gallery(activity: Activity?): BasicGalleryWrapper<GalleryWrapper, String, String, String> {
        return GalleryWrapper(activity)
    }

    /**
     * Preview Album.
     */
    fun galleryAlbum(activity: Activity?): BasicGalleryWrapper<GalleryAlbumWrapper, AlbumFile, String, AlbumFile> {
        return GalleryAlbumWrapper(activity)
    }

    /**
     * Open the camera from the activity.
     */
    fun camera(fragment: Fragment): Camera<ImageCameraWrapper, VideoCameraWrapper> {
        return AlbumCamera(fragment.requireActivity())
    }

    /**
     * Select images.
     */
    fun image(fragment: Fragment): Choice<ImageMultipleWrapper, ImageSingleWrapper> {
        return ImageChoice(fragment.activity)
    }

    /**
     * Select videos.
     */
    fun video(fragment: Fragment): Choice<VideoMultipleWrapper, VideoSingleWrapper> {
        return VideoChoice(fragment.activity)
    }

    /**
     * Select images and videos.
     */
    fun album(fragment: Fragment): Choice<AlbumMultipleWrapper, AlbumSingleWrapper> {
        return AlbumChoice(fragment.requireActivity())
    }

    /**
     * Preview picture.
     */
    fun gallery(fragment: Fragment): BasicGalleryWrapper<GalleryWrapper, String, String, String> {
        return GalleryWrapper(fragment.activity)
    }

    /**
     * Preview Album.
     */
    fun galleryAlbum(fragment: Fragment): BasicGalleryWrapper<GalleryAlbumWrapper, AlbumFile, String, AlbumFile> {
        return GalleryAlbumWrapper(fragment.activity)
    }

    @IntDef(FUNCTION_CHOICE_IMAGE, FUNCTION_CHOICE_VIDEO, FUNCTION_CHOICE_ALBUM)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class ChoiceFunction

    @IntDef(FUNCTION_CAMERA_IMAGE, FUNCTION_CAMERA_VIDEO)
    @Retention(RetentionPolicy.SOURCE)
    annotation class CameraFunction

    @IntDef(MODE_MULTIPLE, MODE_SINGLE)
    @Retention(RetentionPolicy.SOURCE)
    annotation class ChoiceMode
}