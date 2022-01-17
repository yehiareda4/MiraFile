package com.yehia.mira_file_picker.sheet.model

import okhttp3.MediaType

data class Type(
    val key: String,
    val name: String,
    var extension: String,
    val image: Int,
    val camera: Boolean = false,
    val multiple: Boolean = false,
    val keyMultipart: String,
    val mediaType: String,
)
