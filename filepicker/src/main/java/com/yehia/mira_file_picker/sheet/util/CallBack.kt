package com.yehia.mira_file_picker.sheet.util

import com.yehia.mira_file_picker.sheet.model.FileData

interface CallBack {
    fun singleFiles(fileData: FileData) {}

    fun multiFiles(files: MutableList<FileData>) {}
}