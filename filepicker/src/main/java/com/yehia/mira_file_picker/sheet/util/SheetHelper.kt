package com.yehia.mira_file_picker.sheet.util

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.yehia.album.AlbumFile
import com.yehia.mira_file_picker.MiraFilePickerActivity
import com.yehia.mira_file_picker.R
import com.yehia.mira_file_picker.sheet.model.Type
import com.yehia.mira_file_picker.sheet.util.AlbumUtil.openGalleryAlbum
import com.yehia.mira_file_picker.sheet.util.AlbumUtil.openImageAlbum
import com.yehia.mira_file_picker.sheet.util.AlbumUtil.openSingleAlbum
import com.yehia.mira_file_picker.sheet.util.AlbumUtil.openVideoAlbum
import com.yehia.mira_file_picker.sheet.util.Keys.MIME_TYPE_IMAGE
import com.yehia.mira_file_picker.sheet.util.Keys.MIME_TYPE_VIDEO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

fun Fragment.createType(camera: Boolean, multiple: Boolean, type: String): Type {
    return when (type) {
        Keys.MIME_ALL_TYPE -> {
            Type(
                type,
                this.getString(R.string.any_type),
                R.drawable.ic_any_type,
                "application/*",
                "application/",
                multiple = multiple,
            )
        }
        Keys.MIME_TYPE_AUDIO -> {
            Type(
                type,
                this.getString(R.string.voice),
                R.drawable.ic_voice,
                "audio/*",
                "audio/",
                multiple = multiple,
            )
        }
        Keys.MIME_TYPE_TEXT -> {
            Type(
                type,
                this.getString(R.string.text),
                R.drawable.ic_text,
                "text/*",
                "text/",
                multiple = multiple,
            )
        }
        MIME_TYPE_IMAGE -> {
            Type(
                type,
                this.getString(R.string.images),
                R.drawable.ic_gallary,
                "image/*",
                "image/",
                camera,
                multiple
            )
        }
        MIME_TYPE_VIDEO -> {
            Type(
                type,
                this.getString(R.string.video),
                R.drawable.ic_video,
                "video/*",
                "video/",
                camera,
                multiple,
                "mp4",
            )
        }
        Keys.MIME_TYPE_PDF -> {
            Type(
                type,
                this.getString(R.string.pdf),
                R.drawable.ic_pdf,
                "application/pdf",
                "application/",
                multiple = multiple,
                extension = "pdf"
            )
        }
        Keys.MIME_TYPE_ZIP -> {
            Type(
                type,
                this.getString(R.string.compress),
                R.drawable.ic_zip,
                "application/zip",
                "application/",
                multiple = multiple,
                extension = "zip",
            )
        }
        Keys.MIME_TYPE_RAR -> {
            Type(
                type,
                this.getString(R.string.compress),
                R.drawable.ic_rar,
                "application/rar",
                "application/",
                multiple = multiple,
                extension = "rar",
            )
        }
        Keys.MIME_TYPE_DOC -> {
            Type(
                type,
                this.getString(R.string.word),
                R.drawable.ic_doc,
                "application/doc",
                "application/",
                multiple = multiple,
                extension = "doc",
            )
        }
        Keys.MIME_TYPE_DOCX -> {
            Type(
                type,
                this.getString(R.string.word),
                R.drawable.ic_doc,
                "application/docx",
                "application/",
                multiple = multiple,
                extension = "docx",
            )
        }
        Keys.MIME_TYPE_PPT -> {
            Type(
                type,
                this.getString(R.string.powerPoint),
                R.drawable.ic_ppt,
                "application/ppt",
                "application/",
                multiple = multiple,
                extension = "ppt",
            )
        }
        Keys.MIME_TYPE_PPTX -> {
            Type(
                type,
                this.getString(R.string.powerPoint),
                R.drawable.ic_ppt,
                "application/pptx",
                "application/",
                multiple = multiple,
                extension = "pptx",
            )
        }
        Keys.MIME_TYPE_XLS -> {
            Type(
                type,
                this.getString(R.string.excel),
                R.drawable.ic_xls,
                "application/xls",
                "application/",
                multiple = multiple,
                extension = "xls",
            )
        }
        else -> {
            Type(
                type,
                this.getString(R.string.any_type),
                R.drawable.ic_any_type,
                "application/*",
                "application/",
                multiple = multiple,
            )
        }
    }
}

fun Activity.openSingleType(
    type: Type,
    multipleCount: Int = 1,
    sizeList: Int = 0,
    lastImage: ArrayList<AlbumFile> = ArrayList(),
    colorPrim: Int = R.color.gray_al_mai,
    colorAcc: Int = R.color.gray_al_mai,
    colorTxt: Int = com.yehia.album.R.color.black_al_mai,
    previewRequest: ActivityResultLauncher<Intent>? = null,
    resultGallery: (AlbumFile?) -> Unit = {}
) {
    when (type.key) {
        MIME_TYPE_IMAGE -> {
            if (multipleCount == 1) {
                lastImage.clear()

                this.openSingleAlbum(
                    colorPrim, colorAcc, colorTxt,
                ) { result ->
                    if (result.isNotEmpty()) {
                        result.forEach { itx ->
                            if (!lastImage.contains(itx)) {
                                lastImage.add(itx)
                                resultGallery(itx)
                            }
                        }
                    }
                }
            } else {
                this.openImageAlbum(
                    multipleCount - sizeList, lastImage, type.camera, colorPrim, colorAcc, colorTxt,
                ) { result ->
                    if (result.isNotEmpty()) {
                        result.forEach { itx ->
                            if (!lastImage.contains(itx)) {
                                lastImage.add(itx)
                                resultGallery(itx)
                            }
                        }
                    }
                }
            }
        }
        MIME_TYPE_VIDEO -> {
            this.openVideoAlbum(
                multipleCount - sizeList,
                lastImage,
                type.camera, colorPrim, colorAcc, colorTxt
            ) { result ->
                if (!result.isNullOrEmpty()) {
                    result.forEach { itx ->
                        if (!lastImage.contains(itx)) {
                            lastImage.add(itx)
                            resultGallery(itx)
                        }
                    }
                }
            }
        }
        Keys.MIME_TYPE_GALLERY -> {
            this.openGalleryAlbum(
                multipleCount - sizeList, lastImage, type.camera, colorPrim, colorAcc, colorTxt,
            ) { result ->
                if (!result.isNullOrEmpty()) {
                    result.forEach { itx ->
                        if (!lastImage.contains(itx)) {
                            lastImage.add(itx)
                            resultGallery(itx)
                        }
                    }
                }
            }
        }
        else -> {
            val intent = Intent(this, MiraFilePickerActivity::class.java)
            intent.putExtra("multiple", type.multiple)
            intent.putExtra("type", type.key)
            intent.putExtra("camera", type.camera)
            previewRequest!!.launch(intent)
        }
    }
}

fun preparePart(
    type: Type, file: File, fileName: String, partName: String
): MultipartBody.Part {
    val requestFile = RequestBody.create(
        okhttp3.MediaType.parse(
            "${type.mediaType2}${file.extension}"
        ),
        file
    )
    return MultipartBody.Part.createFormData(
        partName,
        fileName,
        requestFile
    )
}

fun preparePartThumbnail(
    file: File, fileName: String, thumbnailPartName: String
): MultipartBody.Part {
    val requestFile = RequestBody.create(
        okhttp3.MediaType.parse("image/png"),
        file
    )
    return MultipartBody.Part.createFormData(
        thumbnailPartName,
        fileName,
        requestFile
    )
}