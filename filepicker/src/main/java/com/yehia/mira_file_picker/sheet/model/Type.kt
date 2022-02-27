package com.yehia.mira_file_picker.sheet.model

data class Type(
    val key: String,
    val name: String,
    var extension: String,
    val image: Int,
    val camera: Boolean = false,
    val multiple: Boolean = false,
    val mediaType: String,
)
