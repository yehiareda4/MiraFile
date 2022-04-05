package com.yehia.mira_file_picker.sheet.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.yanzhenjie.album.AlbumFile
import com.yanzhenjie.album.AlbumLoader

class MediaLoader : AlbumLoader {

    override fun load(imageView: ImageView, albumFile: AlbumFile) {
        load(imageView, albumFile.path)
    }

    override fun load(imageView: ImageView, url: String?) {
        Glide.with(imageView.context).load(url)
            .into(imageView)
    }

}
