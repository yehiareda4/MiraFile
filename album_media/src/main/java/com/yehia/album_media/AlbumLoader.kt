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
package com.yehia.album_media

import android.widget.ImageView

/**
 *
 * Used to load the preview, it should be customized.
 * Created by Yan Zhenjie on 2017/3/31.
 */
interface AlbumLoader {
    /**
     * Load a preview of the album file.
     *
     * @param imageView [ImageView].
     * @param albumFile the media object may be a picture or video.
     */
    fun load(imageView: ImageView?, albumFile: AlbumFile)

    /**
     * Load thumbnails of pictures or videos, either local file or remote file.
     *
     * @param imageView [ImageView].
     * @param url       The url of the file, local path or remote path.
     */
    fun load(imageView: ImageView?, url: String)

    companion object {
        val DEFAULT: AlbumLoader = object : AlbumLoader {
            override fun load(imageView: ImageView?, albumFile: AlbumFile) {}
            override fun load(imageView: ImageView?, url: String) {}
        }
    }
}