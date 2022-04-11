package com.yehia.album_media.widget.photoview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * Created by Yan Zhenjie on 2017/3/31.
 */
class AttacherImageView : AppCompatImageView {
    private var mAttacher: PhotoViewAttacher? = null

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    )

    fun setAttacher(attacher: PhotoViewAttacher?) {
        mAttacher = attacher
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        if (mAttacher != null) {
            mAttacher!!.update()
        }
    }
}