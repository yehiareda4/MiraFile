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

import android.R
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.GenericLifecycleObserver
import androidx.lifecycle.Lifecycle
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.yehia.album_media.mvp.Source.MenuClickListener

/**
 *
 * View of MVP.
 * Created by YanZhenjie on 2017/7/17.
 */
abstract class BaseView<Presenter : BasePresenter?> private constructor(
    private val mSource: Source<*>,
    presenter: Presenter?
) {
    val presenter: Presenter

    constructor(activity: Activity, presenter: Presenter?) : this(
        ActivitySource(activity),
        presenter
    )

    constructor(view: View, presenter: Presenter) : this(ViewSource(view), presenter)

    private fun resume() {
        onResume()
    }

    protected fun onResume() {}
    private fun pause() {
        onPause()
    }

    protected fun onPause() {}
    private fun stop() {
        onStop()
    }

    protected fun onStop() {}
    private fun destroy() {
        closeInputMethod()
        onDestroy()
    }

    protected fun onDestroy() {}

    /**
     * Set actionBar.
     */
    protected fun setActionBar(actionBar: Toolbar?) {
        mSource.setActionBar(actionBar)
        invalidateOptionsMenu()
    }

    /**
     * ReCreate menu.
     */
    protected fun invalidateOptionsMenu() {
        val menu = mSource.menu
        menu?.let { onCreateOptionsMenu(it) }
    }

    /**
     * Get menu inflater.
     */
    protected val menuInflater: MenuInflater?
        protected get() = mSource.menuInflater

    /**
     * Create menu.
     */
    protected open fun onCreateOptionsMenu(menu: Menu) {}
    private fun optionsItemSelected(item: MenuItem) {
        if (item.itemId == R.id.home) {
            if (!onInterceptToolbarBack()) {
                presenter!!.bye()
            }
        } else {
            onOptionsItemSelected(item)
        }
    }

    /**
     * When the menu is clicked.
     */
    protected open fun onOptionsItemSelected(item: MenuItem) {}

    /**
     * Intercept the return button.
     */
    protected fun onInterceptToolbarBack(): Boolean {
        return false
    }

    protected fun openInputMethod(view: View) {
        view.requestFocus()
        val manager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    protected fun closeInputMethod() {
        mSource.closeInputMethod()
    }

    protected fun setDisplayHomeAsUpEnabled(showHome: Boolean) {
        mSource.setDisplayHomeAsUpEnabled(showHome)
    }

    protected fun setHomeAsUpIndicator(@DrawableRes icon: Int) {
        mSource.setHomeAsUpIndicator(icon)
    }

    protected fun setHomeAsUpIndicator(icon: Drawable?) {
        mSource.setHomeAsUpIndicator(icon)
    }

    fun setTitle(title: String) {
        mSource.setTitle(title)
    }

    fun setTitle(@StringRes title: Int) {
        mSource.setTitle(title)
    }

    fun setSubTitle(title: String) {
        mSource.setSubTitle(title)
    }

    fun setSubTitle(@StringRes title: Int) {
        mSource.setSubTitle(title)
    }

    protected val context: Context?
        protected get() = mSource.context!!
    protected val resources: Resources
        protected get() = context!!.resources

    fun getText(@StringRes id: Int): CharSequence {
        return context!!.getText(id)
    }

    fun getString(@StringRes id: Int): String {
        return context!!.getString(id)
    }

    fun getString(@StringRes id: Int, vararg formatArgs: Any?): String {
        return context!!.getString(id, *formatArgs)
    }

    fun getDrawable(@DrawableRes id: Int): Drawable? {
        return ContextCompat.getDrawable(mSource.context!!, id)
    }

    @ColorInt
    fun getColor(@ColorRes id: Int): Int {
        return ContextCompat.getColor(mSource.context!!, id)
    }

    fun getStringArray(@ArrayRes id: Int): Array<String> {
        return resources.getStringArray(id)
    }

    fun getIntArray(@ArrayRes id: Int): IntArray {
        return resources.getIntArray(id)
    }

    fun showMessageDialog(@StringRes title: Int, @StringRes message: Int) {
        showMessageDialog(getText(title), getText(message))
    }

    fun showMessageDialog(@StringRes title: Int, message: CharSequence?) {
        showMessageDialog(getText(title), message)
    }

    fun showMessageDialog(title: CharSequence?, @StringRes message: Int) {
        showMessageDialog(title, getText(message))
    }

    fun showMessageDialog(title: CharSequence?, message: CharSequence?) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(com.yehia.album_media.R.string.album_ok) { dialog, which -> }
            .create()
        alertDialog.show()
    }

    fun showConfirmDialog(
        @StringRes title: Int,
        @StringRes message: Int,
        confirmClickListener: OnDialogClickListener
    ) {
        showConfirmDialog(getText(title), getText(message), confirmClickListener)
    }

    fun showConfirmDialog(
        @StringRes title: Int,
        message: CharSequence?,
        confirmClickListener: OnDialogClickListener
    ) {
        showConfirmDialog(getText(title), message, confirmClickListener)
    }

    fun showConfirmDialog(
        title: CharSequence?,
        @StringRes message: Int,
        confirmClickListener: OnDialogClickListener
    ) {
        showConfirmDialog(title, getText(message), confirmClickListener)
    }

    fun showConfirmDialog(
        title: CharSequence?,
        message: CharSequence?,
        confirmClickListener: OnDialogClickListener
    ) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(com.yehia.album_media.R.string.album_cancel) { dialog, which -> }
            .setPositiveButton(com.yehia.album_media.R.string.album_confirm) { dialog, which ->
                confirmClickListener.onClick(
                    which
                )
            }
            .create()
        alertDialog.show()
    }

    fun showMessageDialog(
        @StringRes title: Int, @StringRes message: Int,
        cancelClickListener: OnDialogClickListener, confirmClickListener: OnDialogClickListener
    ) {
        showMessageDialog(
            getText(title),
            getText(message),
            cancelClickListener,
            confirmClickListener
        )
    }

    fun showMessageDialog(
        @StringRes title: Int, message: CharSequence?,
        cancelClickListener: OnDialogClickListener, confirmClickListener: OnDialogClickListener
    ) {
        showMessageDialog(getText(title), message, cancelClickListener, confirmClickListener)
    }

    fun showMessageDialog(
        title: CharSequence?, @StringRes message: Int,
        cancelClickListener: OnDialogClickListener, confirmClickListener: OnDialogClickListener
    ) {
        showMessageDialog(title, getText(message), cancelClickListener, confirmClickListener)
    }

    fun showMessageDialog(
        title: CharSequence?, message: CharSequence?,
        cancelClickListener: OnDialogClickListener, confirmClickListener: OnDialogClickListener
    ) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(com.yehia.album_media.R.string.album_cancel) { dialog, which ->
                cancelClickListener.onClick(
                    which
                )
            }
            .setPositiveButton(com.yehia.album_media.R.string.album_confirm) { dialog, which ->
                confirmClickListener.onClick(
                    which
                )
            }
            .create()
        alertDialog.show()
    }

    interface OnDialogClickListener {
        fun onClick(which: Int)
    }

    fun toast(message: CharSequence?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun toast(@StringRes message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun snackBar(message: CharSequence?) {
        val snackbar =
            Snackbar.make(mSource.view!!, message!!, BaseTransientBottomBar.LENGTH_INDEFINITE)
        val view = snackbar.view
        view.setBackgroundColor(getColor(com.yehia.album_media.R.color.albumColorPrimaryBlack))
        val textView = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        snackbar.show()
    }

    fun snackBar(@StringRes message: Int) {
        val snackbar =
            Snackbar.make(mSource.view!!, message, BaseTransientBottomBar.LENGTH_INDEFINITE)
        val view = snackbar.view
        view.setBackgroundColor(getColor(com.yehia.album_media.R.color.albumColorPrimaryBlack))
        val textView = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        snackbar.show()
    }

    init {
        this.presenter = presenter!!
        mSource.prepare()
        invalidateOptionsMenu()
        mSource.setMenuClickListener(object : MenuClickListener {
            override fun onHomeClick() {
                presenter!!.bye()
            }

            override fun onMenuClick(item: MenuItem) {
                optionsItemSelected(item)
            }
        })
        presenter!!.lifecycle.addObserver(GenericLifecycleObserver { source, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                resume()
            } else if (event == Lifecycle.Event.ON_PAUSE) {
                pause()
            } else if (event == Lifecycle.Event.ON_STOP) {
                stop()
            } else if (event == Lifecycle.Event.ON_DESTROY) {
                destroy()
            }
        })
    }
}