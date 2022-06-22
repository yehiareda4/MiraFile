package com.yehia.mira_file_picker.sheet.model

data class Type(
    val key: String,
    val name: String,
    val image: Int,
    val mediaType: String,
    val camera: Boolean = false,
    val multiple: Boolean = false,
    var extension: String = "",
)
