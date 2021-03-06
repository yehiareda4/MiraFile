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
import com.yehia.mira_file_picker.sheet.util.AlbumUtil.openVideoAlbum
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
                multiple = multiple,
            )
        }
        Keys.MIME_TYPE_AUDIO -> {
            Type(
                type,
                this.getString(R.string.voice),
                R.drawable.ic_voice,
                "audio/*",
                multiple = multiple,
            )
        }
        Keys.MIME_TYPE_TEXT -> {
            Type(
                type,
                this.getString(R.string.text),
                R.drawable.ic_text,
                "text/*",
                multiple = multiple,
            )
        }
        Keys.MIME_TYPE_IMAGE -> {
            Type(
                type,
                this.getString(R.string.images),
                R.drawable.ic_gallary,
                "image/*",
                camera,
                multiple
            )
        }
        Keys.MIME_TYPE_VIDEO -> {
            Type(
                type,
                this.getString(R.string.video),
                R.drawable.ic_video,
                "video/*",
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
    colorTxt: Int = R.color.black_al_mai,
    previewRequest: ActivityResultLauncher<Intent>? = null,
    resultGallery: (AlbumFile?) -> Unit = {}
) {
    when (type.key) {
        Keys.MIME_TYPE_IMAGE -> {
            this.openImageAlbum(
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
        Keys.MIME_TYPE_VIDEO -> {
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
        okhttp3.MediaType.parse(type.mediaType),
        file
    )
    return MultipartBody.Part.createFormData(
        partName,
        fileName,
        requestFile
    )
}

fun preparePartThumbnail(
    type: Type, file: File, fileName: String, thumbnailPartName: String
): MultipartBody.Part {
    val requestFile = RequestBody.create(
        okhttp3.MediaType.parse(type.mediaType),
        file
    )
    return MultipartBody.Part.createFormData(
        thumbnailPartName,
        fileName,
        requestFile
    )
}