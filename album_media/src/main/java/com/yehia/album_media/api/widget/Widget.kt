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
package com.yehia.album_media.api.widget

import android.content.Context
import android.content.res.ColorStateList
import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.yehia.album_media.R
import com.yehia.album_media.api.widget.Widget.ButtonStyle
import com.yehia.album_media.util.AlbumUtils.getColorStateList
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Created by YanZhenjie on 2017/8/16.
 */
class Widget : Parcelable {
    @IntDef(STYLE_DARK, STYLE_LIGHT)
    @Retention(RetentionPolicy.SOURCE)
    annotation class UiStyle

    private var mContext: Context? = null

    @get:UiStyle
    var uiStyle: Int
        private set

    @get:ColorInt
    var statusBarColor: Int
        private set

    @get:ColorInt
    var toolBarColor: Int
        private set

    @get:ColorInt
    var navigationBarColor: Int
        private set
    var title: String?
        private set
    var mediaItemCheckSelector: ColorStateList?
        private set
    var bucketItemCheckSelector: ColorStateList?
        private set
    var buttonStyle: ButtonStyle?
        private set

    private constructor(builder: Builder) {
        mContext = builder.mContext
        uiStyle = builder.mUiStyle
        statusBarColor =
            if (builder.mStatusBarColor == 0) getColor(R.color.albumColorPrimaryDark) else builder.mStatusBarColor
        toolBarColor =
            if (builder.mToolBarColor == 0) getColor(R.color.albumColorPrimary) else builder.mToolBarColor
        navigationBarColor =
            if (builder.mNavigationBarColor == 0) getColor(R.color.albumColorPrimaryBlack) else builder.mNavigationBarColor
        title =
            if (TextUtils.isEmpty(builder.mTitle)) mContext!!.getString(R.string.album_title) else builder.mTitle
        mediaItemCheckSelector = if (builder.mMediaItemCheckSelector == null) getColorStateList(
            getColor(
                R.color.albumSelectorNormal
            ), getColor(R.color.albumColorPrimary)
        ) else builder.mMediaItemCheckSelector
        bucketItemCheckSelector = if (builder.mBucketItemCheckSelector == null) getColorStateList(
            getColor(
                R.color.albumSelectorNormal
            ), getColor(R.color.albumColorPrimary)
        ) else builder.mBucketItemCheckSelector
        buttonStyle = if (builder.mButtonStyle == null) ButtonStyle.newDarkBuilder(mContext)
            .build() else builder.mButtonStyle
    }

    protected constructor(`in`: Parcel) {
        uiStyle = `in`.readInt()
        statusBarColor = `in`.readInt()
        toolBarColor = `in`.readInt()
        navigationBarColor = `in`.readInt()
        title = `in`.readString()
        mediaItemCheckSelector = `in`.readParcelable(ColorStateList::class.java.classLoader)
        bucketItemCheckSelector = `in`.readParcelable(ColorStateList::class.java.classLoader)
        buttonStyle = `in`.readParcelable(ButtonStyle::class.java.classLoader)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(uiStyle)
        dest.writeInt(statusBarColor)
        dest.writeInt(toolBarColor)
        dest.writeInt(navigationBarColor)
        dest.writeString(title)
        dest.writeParcelable(mediaItemCheckSelector, flags)
        dest.writeParcelable(bucketItemCheckSelector, flags)
        dest.writeParcelable(buttonStyle, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    private fun getColor(colorId: Int): Int {
        return ContextCompat.getColor(mContext!!, colorId)
    }

    class Builder(val mContext: Context, @param:UiStyle val mUiStyle: Int) {
        var mStatusBarColor = 0
        var mToolBarColor = 0
        var mNavigationBarColor = 0
        var mTitle: String? = null
        var mMediaItemCheckSelector: ColorStateList? = null
        var mBucketItemCheckSelector: ColorStateList? = null
        var mButtonStyle: ButtonStyle? = null

        /**
         * Status bar color.
         */
        fun statusBarColor(@ColorInt color: Int): Builder {
            mStatusBarColor = color
            return this
        }

        /**
         * Toolbar color.
         */
        fun toolBarColor(@ColorInt color: Int): Builder {
            mToolBarColor = color
            return this
        }

        /**
         * Virtual navigation bar.
         */
        fun navigationBarColor(@ColorInt color: Int): Builder {
            mNavigationBarColor = color
            return this
        }

        /**
         * Set the title of the Toolbar.
         */
        fun title(@StringRes title: Int): Builder {
            return title(mContext.getString(title))
        }

        /**
         * Set the title of the Toolbar.
         */
        fun title(title: String?): Builder {
            mTitle = title
            return this
        }

        /**
         * The color of the `Media Item` selector.
         */
        fun mediaItemCheckSelector(
            @ColorInt normalColor: Int,
            @ColorInt highLightColor: Int
        ): Builder {
            mMediaItemCheckSelector = getColorStateList(normalColor, highLightColor)
            return this
        }

        /**
         * The color of the `Bucket Item` selector.
         */
        fun bucketItemCheckSelector(
            @ColorInt normalColor: Int,
            @ColorInt highLightColor: Int
        ): Builder {
            mBucketItemCheckSelector = getColorStateList(normalColor, highLightColor)
            return this
        }

        /**
         * Set the style of the Button.
         */
        fun buttonStyle(buttonStyle: ButtonStyle?): Builder {
            mButtonStyle = buttonStyle
            return this
        }

        /**
         * Create target.
         */
        fun build(): Widget {
            return Widget(this)
        }
    }

    class ButtonStyle : Parcelable {
        private var mContext: Context? = null
        var uiStyle: Int
            private set
        var buttonSelector: ColorStateList?
            private set

        private constructor(builder: Builder) {
            mContext = builder.mContext
            uiStyle = builder.mUiStyle
            buttonSelector = if (builder.mButtonSelector == null) getColorStateList(
                ContextCompat.getColor(
                    mContext!!, R.color.albumColorPrimary
                ),
                ContextCompat.getColor(mContext!!, R.color.albumColorPrimaryDark)
            ) else builder.mButtonSelector
        }

        protected constructor(`in`: Parcel) {
            uiStyle = `in`.readInt()
            buttonSelector = `in`.readParcelable(ColorStateList::class.java.classLoader)
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(uiStyle)
            dest.writeParcelable(buttonSelector, flags)
        }

        override fun describeContents(): Int {
            return 0
        }

        class Builder(val mContext: Context?, @param:UiStyle val mUiStyle: Int) {
            var mButtonSelector: ColorStateList? = null

            /**
             * Set button click effect.
             *
             * @param normalColor    normal color.
             * @param highLightColor feedback color.
             */
            fun setButtonSelector(
                @ColorInt normalColor: Int,
                @ColorInt highLightColor: Int
            ): Builder {
                mButtonSelector = getColorStateList(normalColor, highLightColor)
                return this
            }

            fun build(): ButtonStyle {
                return ButtonStyle(this)
            }
        }

        companion object {
            /**
             * Use when the Button are dark.
             */
            fun newDarkBuilder(context: Context?): Builder {
                return Builder(context, STYLE_DARK)
            }

            /**
             * Use when the Button are light.
             */
            fun newLightBuilder(context: Context?): Builder {
                return Builder(context, STYLE_LIGHT)
            }

            @JvmField
            val CREATOR: Parcelable.Creator<ButtonStyle> =
                object : Parcelable.Creator<ButtonStyle> {
                    override fun createFromParcel(`in`: Parcel): ButtonStyle? {
                        return ButtonStyle(`in`)
                    }

                    override fun newArray(size: Int): Array<ButtonStyle?> {
                        return arrayOfNulls(size)
                    }
                }
        }
    }

    companion object {
        const val STYLE_LIGHT = 1
        const val STYLE_DARK = 2

        /**
         * Use when the status bar and the Toolbar are dark.
         */
        fun newDarkBuilder(context: Context): Builder {
            return Builder(context, STYLE_DARK)
        }

        /**
         * Use when the status bar and the Toolbar are light.
         */
        fun newLightBuilder(context: Context): Builder {
            return Builder(context, STYLE_LIGHT)
        }

        @JvmField
        val CREATOR: Parcelable.Creator<Widget> = object : Parcelable.Creator<Widget> {
            override fun createFromParcel(`in`: Parcel): Widget {
                return Widget(`in`)
            }

            override fun newArray(size: Int): Array<Widget?> {
                return arrayOfNulls(size)
            }
        }

        /**
         * Create default widget.
         */
        fun getDefaultWidget(context: Context): Widget {
            return newDarkBuilder(context)
                .statusBarColor(ContextCompat.getColor(context, R.color.albumColorPrimaryDark))
                .toolBarColor(ContextCompat.getColor(context, R.color.albumColorPrimary))
                .navigationBarColor(ContextCompat.getColor(context, R.color.albumColorPrimaryBlack))
                .title(R.string.album_title)
                .mediaItemCheckSelector(
                    ContextCompat.getColor(context, R.color.albumSelectorNormal),
                    ContextCompat.getColor(context, R.color.albumColorPrimary)
                )
                .bucketItemCheckSelector(
                    ContextCompat.getColor(context, R.color.albumSelectorNormal),
                    ContextCompat.getColor(context, R.color.albumColorPrimary)
                )
                .buttonStyle(
                    ButtonStyle.newDarkBuilder(context)
                        .setButtonSelector(
                            ContextCompat.getColor(context, R.color.albumColorPrimary),
                            ContextCompat.getColor(context, R.color.albumColorPrimaryDark)
                        )
                        .build()
                )
                .build()
        }
    }

}