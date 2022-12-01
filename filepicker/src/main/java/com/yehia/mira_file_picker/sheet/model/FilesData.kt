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
//    var compressFile: File?,
//    var compressName: String,
//    var compressSize: String,
//    var compressPath: String,
//    var compressPart: MultipartBody.Part?,
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
        file, name, size, path, extension, filePart, mediaType, "", null
    )

    constructor(
    ) : this(
        File(""), "", "", "", "", null, "", "", null
    )
}