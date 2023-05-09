package com.yehia.mira_file_picker.sheet.model

import okhttp3.MultipartBody
import java.io.File
import java.io.Serializable

data class FileData(
    var file: File,
    var name: String,
    val size: String,
    var path: String,
    var extension: String,
    var filePart: MultipartBody.Part?,
    val mediaType: String,
    var Thumbnail: String,
    var ThumbnailPart: MultipartBody.Part?,
    var compressImage: String,
    var compressImageSize: String,
    var compressImagePart: MultipartBody.Part?,
    var duration: String,
) : Serializable {
    constructor(
        file: File,
        name: String,
        size: String,
        path: String,
        extension: String,
        mediaType: String,
        filePart: MultipartBody.Part,
    ) : this(
        file, name, size, path, extension, filePart, mediaType, "", null, "", "", null, ""
    )

    constructor(
    ) : this(
        File(""), "", "", "", "", null, "", "", null, "", "", null, ""
    )
}