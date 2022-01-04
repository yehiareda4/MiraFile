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
import com.yehia.mira_file_picker.sheet.model.Type
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class PickerTypesSheet(
    fragment: Fragment,
    private val types: MutableList<String>,
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

        types.forEach {
            val type = when (it) {
                MIME_TYPE_AUDIO -> {
                    Type(it, getString(R.string.voice), "", R.drawable.ic_voice, false, multiple)
                }
                MIME_TYPE_TEXT -> {
                    Type(it, getString(R.string.text), "", R.drawable.ic_text, false, multiple)
                }
                MIME_TYPE_IMAGE -> {
                    Type(
                        it,
                        getString(R.string.images),
                        "",
                        R.drawable.ic_gallary,
                        camera,
                        multiple
                    )
                }
                MIME_TYPE_VIDEO -> {
                    Type(it, getString(R.string.video), "", R.drawable.ic_video, camera, multiple)
                }
                MIME_TYPE_PDF -> {
                    Type(it, getString(R.string.pdf), "pdf", R.drawable.ic_pdf, false, multiple)
                }
                MIME_TYPE_ZIP -> {
                    Type(
                        it,
                        getString(R.string.compress),
                        "zip",
                        R.drawable.ic_zip,
                        false,
                        multiple
                    )
                }
                MIME_TYPE_RAR -> {
                    Type(
                        it,
                        getString(R.string.compress),
                        "rar",
                        R.drawable.ic_rar,
                        false,
                        multiple
                    )
                }
                MIME_TYPE_DOC -> {
                    Type(it, getString(R.string.word), "doc", R.drawable.ic_doc, false, multiple)
                }
                MIME_TYPE_PPT -> {
                    Type(
                        it,
                        getString(R.string.powerPoint),
                        "ppt",
                        R.drawable.ic_ppt,
                        false,
                        multiple
                    )
                }
                MIME_TYPE_XLS -> {
                    Type(it, getString(R.string.excel), "xls", R.drawable.ic_xls, false, multiple)
                }
                else -> {
                    Type(
                        it,
                        getString(R.string.any_type),
                        "",
                        R.drawable.ic_any_type,
                        false,
                        multiple
                    )
                }
            }

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
    }

    private fun addFile(file: File) {

        val fileData = FileData(
            file, file.name, FileUtils.getReadableFileSize(file.length().toInt()),
            file.path, file.extension
        )
        if (file.extension.isNullOrEmpty()) {
            fileData.path += type.extension
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
                if (file.extension.isNullOrEmpty()) {
                    fileData.compressPath += type.extension
                }
            }
        } else {
            val thumbnail = getThumbnail(requireContext(), file)
            fileData.Thumbnail = thumbnail
        }

        resultFile(fileData)
    }
}