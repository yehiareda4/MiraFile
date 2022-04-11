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

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.view.SupportMenuInflater
import androidx.appcompat.widget.*
import androidx.core.content.ContextCompat
import com.yehia.album_media.*

/**
 * Created by YanZhenjie on 2017/12/8.
 */
internal class ViewSource(view: View) : Source<View?>(view) {
    private var mActionBar: Toolbar? = null
    private var mActionBarIcon: Drawable? = null
    private var mMenuItemSelectedListener: MenuClickListener? = null
    override fun prepare() {
        val toolbar: Toolbar = host!!.findViewById(R.id.toolbar)
        setActionBar(toolbar)
    }

    override fun setActionBar(actionBar: Toolbar?) {
        mActionBar = actionBar
        if (mActionBar != null) {
            mActionBar!!.setOnMenuItemClickListener { item ->
                if (mMenuItemSelectedListener != null) {
                    mMenuItemSelectedListener!!.onMenuClick(item)
                }
                true
            }
            mActionBar!!.setNavigationOnClickListener {
                if (mMenuItemSelectedListener != null) {
                    mMenuItemSelectedListener!!.onHomeClick()
                }
            }
            mActionBarIcon = mActionBar!!.navigationIcon
        }
    }

    override val menuInflater: MenuInflater
        @SuppressLint("RestrictedApi")
        get() = SupportMenuInflater(context)
    override val menu: Menu?
        get() = if (mActionBar == null) null else mActionBar!!.menu

    override fun setMenuClickListener(selectedListener: MenuClickListener?) {
        mMenuItemSelectedListener = selectedListener
    }

    override fun setDisplayHomeAsUpEnabled(showHome: Boolean) {
        if (mActionBar != null) {
            if (showHome) {
                mActionBar!!.navigationIcon = mActionBarIcon
            } else {
                mActionBar!!.navigationIcon = null
            }
        }
    }

    override fun setHomeAsUpIndicator(@DrawableRes icon: Int) {
        setHomeAsUpIndicator(ContextCompat.getDrawable(context!!, icon))
    }

    override fun setHomeAsUpIndicator(icon: Drawable?) {
        mActionBarIcon = icon
        if (mActionBar != null) mActionBar!!.navigationIcon = icon
    }

    override fun setTitle(title: CharSequence?) {
        if (mActionBar != null) mActionBar!!.title = title
    }

    override fun setTitle(@StringRes title: Int) {
        if (mActionBar != null) mActionBar!!.setTitle(title)
    }

    override fun setSubTitle(title: CharSequence?) {
        if (mActionBar != null) mActionBar!!.subtitle = title
    }

    override fun setSubTitle(@StringRes title: Int) {
        if (mActionBar != null) mActionBar!!.setSubtitle(title)
    }

    override val context: Context?
        get() = host!!.context
    override val view: View?
        get() = host

    override fun closeInputMethod() {
        val focusView = view!!.findFocus()
        if (focusView != null) {
            val manager =
                context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(focusView.windowToken, 0)
        }
    }
}