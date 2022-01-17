package com.yehia.mira_file_picker.sheet

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.yehia.mira_file_picker.FileUtils
import com.yehia.mira_file_picker.FileUtils.getThumbnail
import com.yehia.mira_file_picker.MiraFilePickerActivity
import com.yehia.mira_file_picker.R
import com.yehia.mira_file_picker.databinding.SheetTypesBinding
import com.yehia.mira_file_picker.sheet.model.FileData
import com.yehia.mira_file_picker.sheet.model.SelectedType
import com.yehia.mira_file_picker.sheet.model.Type
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class PickerTypesSheet(
    private val fragment: Fragment,
    private val types: MutableList<SelectedType>,
    private val camera: Boolean = false,
    private val multiple: Boolean = false,
    val resultFile: (FileData) -> Unit
) : BaseBottomSheetFragment<SheetTypesBinding>() {

    companion object {
        const val MIME_TYPE_AUDIO = "audio/*"
        const val MIME_TYPE_TEXT = "text/*"
        const val MIME_TYPE_IMAGE = "image/*"
        const val MIME_TYPE_VIDEO = "video/*"
        const val MIME_TYPE_PDF = "application/pdf"
        const val MIME_TYPE_ZIP = "application/zip"
        const val MIME_TYPE_RAR = "application/rar"
        const val MIME_TYPE_DOC = "application/doc"
        const val MIME_TYPE_PPT = "application/ppt"
        const val MIME_TYPE_XLS = "application/xls"
    }

    override fun getFragmentView(): Int = R.layout.sheet_types
    private lateinit var adapter: TypesAdapter
    lateinit var type: Type
    var previewRequest: ActivityResultLauncher<Intent>

    private val typesList: MutableList<Type> = ArrayList()

    init {
        previewRequest =
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    if (it.data?.data != null) {
                        val file: File = FileUtils.getFile(requireContext(), it.data?.data)
                        addFile(file)
                    }
                    if (it.data?.clipData != null) {
                        for (i in 0 until it.data?.clipData?.itemCount!!) {
                            val uri: Uri = it.data?.clipData?.getItemAt(i)?.uri!!
                            val file: File = FileUtils.getFile(requireContext(), uri)
                            addFile(file)
                        }
                    }
                }
            }
    }

    override fun afterCreateView() {
        typesList.clear()

        dialog?.setOnDismissListener {

        }

        if (types.size > 1) {
            types.forEach {
                val type = createType(it)

                typesList.add(type)
            }
            adapter = TypesAdapter(typesList) {
                type = it
                val intent = Intent(activity, MiraFilePickerActivity::class.java)
                intent.putExtra("multiple", type.multiple)
                intent.putExtra("type", type.key)
                intent.putExtra("camera", type.camera)
                previewRequest.launch(intent)
            }

            val gridLayoutManager = GridLayoutManager(requireContext(), 4)
            binding.rvTypes.layoutManager = gridLayoutManager
            binding.rvTypes.adapter = adapter
        } else {
            type = createType(types[0])
            val intent = Intent(activity, MiraFilePickerActivity::class.java)
            intent.putExtra("multiple", type.multiple)
            intent.putExtra("type", type.key)
            intent.putExtra("camera", type.camera)
            previewRequest.launch(intent)
        }
    }

    private fun createType(it: SelectedType): Type {
        return when (it.key) {
            MIME_TYPE_AUDIO -> {
                Type(
                    it.key,
                    getString(R.string.voice),
                    "",
                    R.drawable.ic_voice,
                    false,
                    multiple,
                    it.keyMultipart, "audio"
                )
            }
            MIME_TYPE_TEXT -> {
                Type(
                    it.key, getString(R.string.text), "", R.drawable.ic_text, false, multiple,
                    it.keyMultipart, "text"
                )
            }
            MIME_TYPE_IMAGE -> {
                Type(
                    it.key,
                    getString(R.string.images),
                    "",
                    R.drawable.ic_gallary,
                    camera,
                    multiple,
                    it.keyMultipart, "image"
                )
            }
            MIME_TYPE_VIDEO -> {
                Type(
                    it.key,
                    getString(R.string.video),
                    "",
                    R.drawable.ic_video,
                    camera,
                    multiple,
                    it.keyMultipart, "video"
                )
            }
            MIME_TYPE_PDF -> {
                Type(
                    it.key, getString(R.string.pdf), "pdf", R.drawable.ic_pdf, false, multiple,
                    it.keyMultipart, "pdf"
                )
            }
            MIME_TYPE_ZIP -> {
                Type(
                    it.key,
                    getString(R.string.compress),
                    "zip",
                    R.drawable.ic_zip,
                    false,
                    multiple,
                    it.keyMultipart, "zip"
                )
            }
            MIME_TYPE_RAR -> {
                Type(
                    it.key,
                    getString(R.string.compress),
                    "rar",
                    R.drawable.ic_rar,
                    false,
                    multiple,
                    it.keyMultipart,
                    "rar"
                )
            }
            MIME_TYPE_DOC -> {
                Type(
                    it.key,
                    getString(R.string.word),
                    "doc",
                    R.drawable.ic_doc,
                    false,
                    multiple,
                    it.keyMultipart,
                    "doc"
                )
            }
            MIME_TYPE_PPT -> {
                Type(
                    it.key,
                    getString(R.string.powerPoint),
                    "ppt",
                    R.drawable.ic_ppt,
                    false,
                    multiple,
                    it.keyMultipart,
                    "ppt",
                )
            }
            MIME_TYPE_XLS -> {
                Type(
                    it.key,
                    getString(R.string.excel),
                    "xls",
                    R.drawable.ic_xls,
                    false,
                    multiple,
                    it.keyMultipart,
                    "xls",
                )
            }
            else -> {
                Type(
                    it.key,
                    getString(R.string.any_type),
                    "",
                    R.drawable.ic_any_type,
                    false,
                    multiple,
                    it.keyMultipart, ""
                )
            }
        }
    }

    private fun addFile(file: File) {
        val fileData = FileData(
            file, file.name, FileUtils.getReadableFileSize(file.length().toInt()),
            file.path, file.extension, type.mediaType
        )
        if (file.extension.isEmpty()) {
            fileData.path += type.extension
        }
        if (file.extension.isEmpty()) {
            fileData.name += ".${type.extension}"
        }
        if (type.key == MIME_TYPE_IMAGE) {
            GlobalScope.launch {
                val compressedFile =
                    Compressor.compress(requireContext(), file, Dispatchers.Main)
                fileData.compressFile = compressedFile
                fileData.compressName = compressedFile.name
                fileData.compressSize =
                    FileUtils.getReadableFileSize(compressedFile.length().toInt())
                fileData.compressPath = compressedFile.path
                if (file.extension.isEmpty()) {
                    fileData.compressPath += type.extension
                }
            }
        } else {
            val thumbnail = getThumbnail(requireContext(), file)
            fileData.Thumbnail = thumbnail
        }
        dialog?.dismiss()

        resultFile(fileData)
    }

    fun show() {
        if (this.isAdded) {
            this.dialog!!.show()
        } else {
            this.show(fragment.childFragmentManager, "")
        }
    }

    fun convertFileToMultipart(
        path: String?,
        key: String,
        contentType: MediaType?
    ): MultipartBody.Part? {
        return if (path != null) {
            val file = File(path)
            val requestBody: RequestBody = file.asRequestBody(contentType)
            val body: MultipartBody.Part =
                createFormData(key, file.name, requestBody)
            body
        } else {
            null
        }
    }
}