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

import android.content.Context
import java.util.*

/**
 *
 * Album config.
 * Created by Yan Zhenjie on 2017/3/31.
 */
class AlbumConfig private constructor(builder: Builder) {
    /**
     * Get [AlbumLoader].
     *
     * @return [AlbumLoader].
     */
    val albumLoader: AlbumLoader

    /**
     * Get [Locale].
     *
     * @return [Locale].
     */
    val locale: Locale

    class Builder(context: Context?) {
        var mLoader: AlbumLoader? = null
        var mLocale: Locale? = null

        /**
         * Set album loader.
         *
         * @param loader [AlbumLoader].
         * @return [Builder].
         */
        fun setAlbumLoader(loader: AlbumLoader?): Builder {
            mLoader = loader
            return this
        }

        /**
         * Set locale for language.
         *
         * @param locale [Locale].
         * @return [Builder].
         */
        fun setLocale(locale: Locale?): Builder {
            mLocale = locale
            return this
        }

        /**
         * Create AlbumConfig.
         *
         * @return [AlbumConfig].
         */
        fun build(): AlbumConfig {
            return AlbumConfig(this)
        }
    }

    companion object {
        /**
         * Create a new builder.
         */
        fun newBuilder(context: Context?): Builder {
            return Builder(context)
        }
    }

    init {
        albumLoader =
            if (builder.mLoader == null) AlbumLoader.Companion.DEFAULT else builder.mLoader!!
        locale = if (builder.mLocale == null) Locale.getDefault() else builder.mLocale!!
    }
}