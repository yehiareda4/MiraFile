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
package com.yehia.album_media.app

import android.app.Activity
import android.content.res.Configuration
import android.view.View
import android.widget.CompoundButton
import com.yehia.album_media.Album
import com.yehia.album_media.AlbumFolder
import com.yehia.album_media.api.widget.Widget
import com.yehia.album_media.mvp.BasePresenter
import com.yehia.album_media.mvp.BaseView

/**
 * Created by YanZhenjie on 2018/4/7.
 */
class Contract {
    interface AlbumPresenter : BasePresenter {
        /**
         * Click the folder switch.
         */
        fun clickFolderSwitch()

        /**
         * Click camera.
         */
        fun clickCamera(v: View?)

        /**
         * Try to check item.
         *
         * @param button view.
         * @param position position of item.
         */
        fun tryCheckItem(button: CompoundButton, position: Int)

        /**
         * Try to preview item.
         *
         * @param position position of item.
         */
        fun tryPreviewItem(position: Int)

        /**
         * Preview the checked items.
         */
        fun tryPreviewChecked()

        /**
         * Complete.
         */
        fun complete()
    }

    abstract class AlbumView(activity: Activity, presenter: AlbumPresenter?) :
        BaseView<AlbumPresenter?>(activity, presenter) {
        /**
         * Set some properties of the view.
         *
         * @param widget [Widget].
         * @param column the count of columns.
         * @param hasCamera the camera is enabled.
         * @param choiceMode choice mode, one of [Album.FUNCTION_CHOICE_ALBUM], [     ][Album.FUNCTION_CHOICE_IMAGE] or [Album.FUNCTION_CHOICE_VIDEO].
         */
        abstract fun setupViews(widget: Widget?, column: Int, hasCamera: Boolean, choiceMode: Int)

        /**
         * Set the loading visibility.
         *
         * @param display true is displayed, otherwise it is not displayed.
         */
        abstract fun setLoadingDisplay(display: Boolean)

        /**
         * Should be re-layout.
         *
         * @param newConfig config.
         */
        abstract fun onConfigurationChanged(newConfig: Configuration)

        /**
         * Set the complete menu visibility.
         *
         * @param display true is displayed, otherwise it is not displayed.
         */
        abstract fun setCompleteDisplay(display: Boolean)

        /**
         * Bind folder.
         *
         * @param albumFolder [AlbumFolder].
         */
        abstract fun bindAlbumFolder(albumFolder: AlbumFolder?)

        /**
         * Notify item was inserted.
         *
         * @param position position of item.
         */
        abstract fun notifyInsertItem(position: Int)

        /**
         * Notify item was changed.
         *
         * @param position position of item.
         */
        abstract fun notifyItem(position: Int)

        /**
         * Set checked count.
         *
         * @param count the number of items checked.
         */
        abstract fun setCheckedCount(count: Int)
    }

    interface NullPresenter : BasePresenter {
        /**
         * Take a picture.
         */
        fun takePicture()

        /**
         * Take a video.
         */
        fun takeVideo()
    }

    abstract class NullView(activity: Activity, presenter: NullPresenter?) :
        BaseView<NullPresenter?>(activity, presenter) {
        /**
         * Set some properties of the view.
         *
         * @param widget [Widget].
         */
        abstract fun setupViews(widget: Widget?)

        /**
         * Set the message of page.
         *
         * @param message message.
         */
        abstract fun setMessage(message: Int)

        /**
         * Set the button visibility.
         *
         * @param display true is displayed, otherwise it is not displayed.
         */
        abstract fun setMakeImageDisplay(display: Boolean)

        /**
         * Set the button visibility.
         *
         * @param display true is displayed, otherwise it is not displayed.
         */
        abstract fun setMakeVideoDisplay(display: Boolean)
    }

    interface GalleryPresenter : BasePresenter {
        fun clickItem(position: Int)
        fun longClickItem(position: Int)

        /**
         * Set the current position of item .
         */
        fun onCurrentChanged(position: Int)

        /**
         * Try to check the current item.
         */
        fun onCheckedChanged()

        /**
         * Complete.
         */
        fun complete()
    }

    abstract class GalleryView<Data>(activity: Activity, presenter: GalleryPresenter?) :
        BaseView<GalleryPresenter?>(activity, presenter) {
        /**
         * Set some properties of the view.
         *
         * @param widget [Widget].
         * @param checkable show the checkbox.
         */
        abstract fun setupViews(widget: Widget, checkable: Boolean)

        /**
         * Bind data.
         *
         * @param dataList data.
         */
        abstract fun bindData(dataList: List<Data>?)

        /**
         * Set the position of the item to be displayed.
         *
         * @param position position.
         */
        abstract fun setCurrentItem(position: Int)

        /**
         * Set duration visibility.
         *
         * @param display true is displayed, otherwise it is not displayed.
         */
        abstract fun setDurationDisplay(display: Boolean)

        /**
         * Set duration.
         *
         * @param duration duration.
         */
        abstract fun setDuration(duration: String)

        /**
         * Changes the checked state of this button.
         *
         * @param checked true to check the button, false to uncheck it.
         */
        abstract fun setChecked(checked: Boolean)

        /**
         * Set bottom visibility.
         *
         * @param display true is displayed, otherwise it is not displayed.
         */
        abstract fun setBottomDisplay(display: Boolean)

        /**
         * Set layer visibility.
         *
         * @param display true is displayed, otherwise it is not displayed.
         */
        abstract fun setLayerDisplay(display: Boolean)

        /**
         * Set the complete button text.
         *
         * @param text text.
         */
        abstract fun setCompleteText(text: String)
    }
}