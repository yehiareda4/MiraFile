package com.yehia.mira_file_picker.sheet.util

import android.app.Activity
import androidx.core.content.ContextCompat
import com.yehia.album.Action
import com.yehia.album.Album
import com.yehia.album.AlbumConfig
import com.yehia.album.AlbumFile
import com.yehia.album.Filter
import com.yehia.album.api.widget.Widget
import com.yehia.mira_file_picker.R
import java.util.*


object AlbumUtil {

    fun Activity.openGalleryAlbum(
        Counter: Int,
        ImagesFiles: ArrayList<AlbumFile>,
        camera: Boolean = false,
        colorPrim: Int = R.color.gray_al_mai,
        colorAcc: Int = R.color.gray_al_mai,
        colorTxt: Int = com.yehia.album.R.color.black_al_mai,
        action: Action<ArrayList<AlbumFile>>,
    ) {
        Album.initialize(
            AlbumConfig.newBuilder(this)
                .setAlbumLoader(MediaLoader())
                .setLocale(Locale(Locale.getDefault().language)).build()
        )
        Album.album(this) // Image and video mix options.
            .multipleChoice() // Multi-Mode, Single-Mode: singleChoice().
            .columnCount(3) // The number of columns in the page list.
            .selectCount(Counter) // Choose up to a few images.
            .camera(camera) // Whether the camera appears in the Item.
            .checkedList(ImagesFiles) // To reverse the list.
            .widget(
                Widget.newDarkBuilder(this)
                    .title("")
                    .statusBarColor(ContextCompat.getColor(this, colorPrim))
                    .toolBarColor(ContextCompat.getColor(this, colorPrim))
                    .navigationBarColor(ContextCompat.getColor(this, colorTxt))
                    .buttonStyle( // Used to configure the style of button when the image/video is not found.
                        Widget.ButtonStyle.newLightBuilder(this) // With Widget's Builder model.
                            .setButtonSelector(
                                ContextCompat.getColor(this, colorPrim),
                                ContextCompat.getColor(this, colorAcc)
                            ) // Button selector.
                            .build()
                    )
                    .mediaItemCheckSelector(
                        ContextCompat.getColor(this, colorAcc),
                        ContextCompat.getColor(this, colorPrim)
                    ) // Image or video selection box.
                    .bucketItemCheckSelector(
                        ContextCompat.getColor(this, colorAcc),
                        ContextCompat.getColor(this, colorPrim)
                    ) // Select the folder selection box.
                    .build()
            )
            .onResult(action)
            .onCancel { }
            .start()
    }

    fun Activity.openImageAlbum(
        Counter: Int,
        ImagesFiles: ArrayList<AlbumFile>,
        camera: Boolean = false,
        colorPrim: Int = R.color.gray_al_mai,
        colorAcc: Int = R.color.gray_al_mai,
        colorTxt: Int = com.yehia.album.R.color.black_al_mai,
        action: Action<ArrayList<AlbumFile>>,
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
                    .statusBarColor(ContextCompat.getColor(this, colorPrim))
                    .toolBarColor(ContextCompat.getColor(this, colorPrim))
                    .navigationBarColor(ContextCompat.getColor(this, colorTxt))
                    .buttonStyle( // Used to configure the style of button when the image/video is not found.
                        Widget.ButtonStyle.newLightBuilder(this) // With Widget's Builder model.
                            .setButtonSelector(
                                ContextCompat.getColor(this, colorPrim),
                                ContextCompat.getColor(this, colorAcc)
                            ) // Button selector.
                            .build()
                    )
                    .mediaItemCheckSelector(
                        ContextCompat.getColor(this, colorAcc),
                        ContextCompat.getColor(this, colorPrim)
                    ) // Image or video selection box.
                    .bucketItemCheckSelector(
                        ContextCompat.getColor(this, colorAcc),
                        ContextCompat.getColor(this, colorPrim)
                    ) // Select the folder selection box.
                    .build()
            )
            .onResult(action)
            .onCancel { }
            .start()
    }

    fun Activity.openVideoAlbum(
        Counter: Int,
        ImagesFiles: ArrayList<AlbumFile>,
        camera: Boolean = false,
        colorPrim: Int = R.color.gray_al_mai,
        colorAcc: Int = R.color.gray_al_mai,
        colorTxt: Int = com.yehia.album.R.color.black_al_mai,
        action: Action<ArrayList<AlbumFile>>,
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
                    .statusBarColor(ContextCompat.getColor(this, colorPrim))
                    .toolBarColor(ContextCompat.getColor(this, colorPrim))
                    .navigationBarColor(ContextCompat.getColor(this, colorTxt))
                    .buttonStyle( // Used to configure the style of button when the image/video is not found.
                        Widget.ButtonStyle.newLightBuilder(this) // With Widget's Builder model.
                            .setButtonSelector(
                                ContextCompat.getColor(this, colorPrim),
                                ContextCompat.getColor(this, colorAcc)
                            ) // Button selector.
                            .build()
                    )
                    .mediaItemCheckSelector(
                        ContextCompat.getColor(this, colorAcc),
                        ContextCompat.getColor(this, colorPrim)
                    ) // Image or video selection box.
                    .bucketItemCheckSelector(
                        ContextCompat.getColor(this, colorAcc),
                        ContextCompat.getColor(this, colorPrim)
                    ) // Select the folder selection box.
                    .build()
            )
            .onResult(action)
            .onCancel { }
            .start()
    }

    fun Activity.openImageAlbum(
        colorPrim: Int = R.color.gray_al_mai,
        colorAcc: Int = R.color.gray_al_mai,
        colorTxt: Int = com.yehia.album.R.color.black_al_mai,
        action: Action<ArrayList<AlbumFile>>,
    ) {
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
                    .statusBarColor(ContextCompat.getColor(this, colorPrim))
                    .toolBarColor(ContextCompat.getColor(this, colorPrim))
                    .navigationBarColor(ContextCompat.getColor(this, colorTxt))
                    .buttonStyle( // Used to configure the style of button when the image/video is not found.
                        Widget.ButtonStyle.newLightBuilder(this) // With Widget's Builder model.
                            .setButtonSelector(
                                ContextCompat.getColor(this, colorPrim),
                                ContextCompat.getColor(this, colorAcc)
                            ) // Button selector.
                            .build()
                    )
                    .mediaItemCheckSelector(
                        ContextCompat.getColor(this, colorAcc),
                        ContextCompat.getColor(this, colorPrim)
                    ) // Image or video selection box.
                    .bucketItemCheckSelector(
                        ContextCompat.getColor(this, colorAcc),
                        ContextCompat.getColor(this, colorPrim)
                    ) // Select the folder selection box.
                    .build()
            )
            .onResult(action)
            .onCancel { }
            .start()
    }

    fun Activity.openSingleAlbum(
        colorPrim: Int = R.color.gray_al_mai,
        colorAcc: Int = R.color.gray_al_mai,
        colorTxt: Int = com.yehia.album.R.color.black_al_mai,
        action: Action<ArrayList<AlbumFile>>,
    ) {
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
                    .statusBarColor(ContextCompat.getColor(this, colorPrim))
                    .toolBarColor(ContextCompat.getColor(this, colorPrim))
                    .navigationBarColor(ContextCompat.getColor(this, colorTxt))
                    .buttonStyle( // Used to configure the style of button when the image/video is not found.
                        Widget.ButtonStyle.newLightBuilder(this) // With Widget's Builder model.
                            .setButtonSelector(
                                ContextCompat.getColor(this, colorPrim),
                                ContextCompat.getColor(this, colorAcc)
                            ) // Button selector.
                            .build()
                    )
                    .mediaItemCheckSelector(
                        ContextCompat.getColor(this, colorAcc),
                        ContextCompat.getColor(this, colorPrim)
                    ) // Image or video selection box.
                    .bucketItemCheckSelector(
                        ContextCompat.getColor(this, colorAcc),
                        ContextCompat.getColor(this, colorPrim)
                    ) // Select the folder selection box.
                    .build()
            )
            .onResult(action)
            .onCancel { }
            .start()
    }

    fun Activity.openCamera(
        action: Action<String>,
    ) {
        Album.initialize(
            AlbumConfig.newBuilder(this)
                .setAlbumLoader(MediaLoader())
                .setLocale(Locale.getDefault()).build()
        )

        Album.camera(this)
            .video() // Record Video.
            .quality(1) // Video quality, [0, 1].
            .limitDuration(Long.MAX_VALUE) // The longest duration of the video is in milliseconds.
            .limitBytes(Long.MAX_VALUE) // Maximum size of the video, in bytes.
            .onResult(action)
            .onCancel { }
            .start()

    }

}