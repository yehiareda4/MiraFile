package com.yehia.mira_file_picker.sheet.util

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.yanzhenjie.album.Action
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumConfig
import com.yanzhenjie.album.AlbumFile
import com.yanzhenjie.album.api.widget.Widget
import com.yehia.mira_file_picker.R
import java.util.*

object AlbumUtil {

    fun Context.openAlbum(
        Counter: Int,
        ImagesFiles: ArrayList<AlbumFile?>?,
        action: Action<ArrayList<AlbumFile?>?>,
        camera: Boolean = false
    ) {
        Album.initialize(
            AlbumConfig.newBuilder(this)
                .setAlbumLoader(MediaLoader())
                .setLocale(Locale(Locale.getDefault().language)).build()
        )
        Album.image(this) // Image and video mix options.
            .multipleChoice() // Multi-Mode, Single-Mode: singleChoice().
            .columnCount(3) // The number of columns in the page list.
            .selectCount(Counter) // Choose up to a few images.
            .camera(camera) // Whether the camera appears in the Item.
            .checkedList(ImagesFiles) // To reverse the list.
            .widget(
                Widget.newDarkBuilder(this)
                    .title("")
                    .statusBarColor(ContextCompat.getColor(this, R.color.gray))
                    .toolBarColor(ContextCompat.getColor(this, R.color.gray))
                    .navigationBarColor(ContextCompat.getColor(this, R.color.white))
                    .mediaItemCheckSelector(
                        Color.BLUE,
                        Color.BLUE
                    ) // Image or video selection box.
                    .bucketItemCheckSelector(
                        Color.RED,
                        Color.YELLOW
                    ) // Select the folder selection box.
                    .build()
            )
            .onResult(action)
            .onCancel {
                // The Client canceled the operation.
            }
            .start()
    }

    fun Context.openVideoAlbum(
        Counter: Int,
        ImagesFiles: ArrayList<AlbumFile?>?,
        action: Action<ArrayList<AlbumFile?>?>,
        camera: Boolean = false
    ) {
        Album.initialize(
            AlbumConfig.newBuilder(this)
                .setAlbumLoader(MediaLoader())
                .setLocale(Locale(Locale.getDefault().language)).build()
        )
        Album.video(this) // Image and video mix options.
            .multipleChoice() // Multi-Mode, Single-Mode: singleChoice().
            .columnCount(3) // The number of columns in the page list.
            .selectCount(Counter) // Choose up to a few images.
            .camera(camera) // Whether the camera appears in the Item.
            .checkedList(ImagesFiles) // To reverse the list.
            .widget(
                Widget.newDarkBuilder(this)
                    .title("")
                    .statusBarColor(ContextCompat.getColor(this, R.color.gray))
                    .toolBarColor(ContextCompat.getColor(this, R.color.gray))
                    .navigationBarColor(ContextCompat.getColor(this, R.color.white))
                    .mediaItemCheckSelector(
                        Color.BLUE,
                        Color.BLUE
                    ) // Image or video selection box.
                    .bucketItemCheckSelector(
                        Color.RED,
                        Color.YELLOW
                    ) // Select the folder selection box.
                    .build()
            )
            .onResult(action)
            .onCancel {
                // The Client canceled the operation.
            }
            .start()
    }

    fun Context.openAlbum(action: Action<ArrayList<AlbumFile?>?>) {
        Album.initialize(
            AlbumConfig.newBuilder(this)
                .setAlbumLoader(MediaLoader())
                .setLocale(Locale.getDefault()).build()
        )
        Album.image(this) // Image and video mix options.
            .singleChoice() // Multi-Mode, Single-Mode: singleChoice().
            .columnCount(3) // The number of columns in the page list.
            .camera(true) // Whether the camera appears in the Item.
            .widget(
                Widget.newDarkBuilder(this)
                    .title("")
                    .statusBarColor(ContextCompat.getColor(this, R.color.gray))
                    .toolBarColor(ContextCompat.getColor(this, R.color.gray))
                    .navigationBarColor(ContextCompat.getColor(this, R.color.white))
                    .mediaItemCheckSelector(
                        Color.BLUE,
                        Color.BLUE
                    ) // Image or video selection box.
                    .bucketItemCheckSelector(
                        Color.RED,
                        Color.YELLOW
                    ) // Select the folder selection box.
                    .build()
            )
            .onResult(action)
            .onCancel {
                // The Client canceled the operation.
            }
            .start()
    }

}