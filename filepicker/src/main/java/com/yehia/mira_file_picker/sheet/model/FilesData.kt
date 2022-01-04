package com.yehia.mira_file_picker.sheet.model

import android.graphics.Bitmap
import java.io.File
import java.io.Serializable

data class FileData(
    var file: File,
    var name: String,
    val size: String,
    var path: String,
    var extension: String,
    var Thumbnail: Bitmap?,
    var compressFile: File?,
    var compressName: String?,
    var compressSize: String?,
    var compressPath: String?,
) : Serializable {
    constructor(file: File, name: String, size: String, path: String, extension: String) :
            this(
                file, name, size, path, extension, null, null,
                "", "", ""
            )
}