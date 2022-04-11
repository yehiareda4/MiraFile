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
package com.yehia.album_media.mvp

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar

/**
 * Created by YanZhenjie on 2017/12/8.
 */
internal abstract class Source<Host>(val host: Host) {
    abstract fun prepare()
    abstract fun setActionBar(actionBar: Toolbar?)
    abstract val menuInflater: MenuInflater
    abstract val menu: Menu?
    abstract fun setMenuClickListener(selectedListener: MenuClickListener?)
    abstract fun setDisplayHomeAsUpEnabled(showHome: Boolean)
    abstract fun setHomeAsUpIndicator(@DrawableRes icon: Int)
    abstract fun setHomeAsUpIndicator(icon: Drawable?)
    abstract fun setTitle(title: CharSequence?)
    abstract fun setTitle(@StringRes title: Int)
    abstract fun setSubTitle(title: CharSequence?)
    abstract fun setSubTitle(@StringRes title: Int)
    abstract val context: Context?
    abstract val view: View?
    abstract fun closeInputMethod()
    internal interface MenuClickListener {
        fun onHomeClick()
        fun onMenuClick(item: MenuItem)
    }
}