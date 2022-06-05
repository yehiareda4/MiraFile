package com.yehia.mira_file_picker.sheet.util

import android.app.Activity
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.yehia.album.Action
import com.yehia.album.Album
import com.yehia.album.AlbumConfig
import com.yehia.album.AlbumFile
import com.yehia.album.api.widget.Widget
import com.yehia.mira_file_picker.R
import java.util.*

object AlbumUtil {

    fun Activity.openAlbum(
        Counter: Int,
        ImagesFiles: ArrayList<AlbumFile>,
        action: Action<ArrayList<AlbumFile>>,
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
                    .buttonStyle( // Used to configure the style of button when the image/video is not found.
                        Widget.ButtonStyle.newLightBuilder(this) // With Widget's Builder model.
                            .setButtonSelector(Color.WHITE, Color.WHITE) // Button selector.
                            .build()
                    )
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
            .onCancel(object : Action<String> {
                override fun onAction(result: String) {

                }
            })
            .start()
    }

    fun Activity.openVideoAlbum(
        Counter: Int,
        ImagesFiles: ArrayList<AlbumFile>,
        action: Action<ArrayList<AlbumFile>>,
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
                    .buttonStyle( // Used to configure the style of button when the image/video is not found.
                        Widget.ButtonStyle.newLightBuilder(this) // With Widget's Builder model.
                            .setButtonSelector(Color.WHITE, Color.WHITE) // Button selector.
                            .build()
                    )
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
            .onCancel(object : Action<String> {
                override fun onAction(result: String) {

                }
            })
            .start()
    }

    fun Activity.openAlbum(action: Action<ArrayList<AlbumFile>>) {
        Album.initialize(
            AlbumConfig.newBuilder(this)
                .setAlbumLoader(MediaLoader())
                .setLocale(Locale.getDefault()).build()
        )
        Album.image(this) // Image and video mix options.
            .multipleChoice() // Multi-Mode, Single-Mode: singleChoice().
            .columnCount(3) // The number of columns in the page list.
            .selectCount(1) // Choose up to a few images.
            .checkedList(ArrayList()) // To reverse the list.
            .camera(true) // Whether the camera appears in the Item.
            .widget(
                Widget.newDarkBuilder(this)
                    .title("")
                    .statusBarColor(ContextCompat.getColor(this, R.color.gray))
                    .toolBarColor(ContextCompat.getColor(this, R.color.gray))
                    .navigationBarColor(ContextCompat.getColor(this, R.color.white))
                    .buttonStyle( // Used to configure the style of button when the image/video is not found.
                        Widget.ButtonStyle.newLightBuilder(this) // With Widget's Builder model.
                            .setButtonSelector(Color.WHITE, Color.WHITE) // Button selector.
                            .build()
                    )
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
            .onCancel(object : Action<String> {
                override fun onAction(result: String) {

                }
            })
            .start()
    }

}