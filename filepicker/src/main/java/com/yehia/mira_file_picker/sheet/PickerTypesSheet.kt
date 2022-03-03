package com.yehia.mira_file_picker.sheet

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.yehia.mira_file_picker.FileUtils
import com.yehia.mira_file_picker.FileUtils.getThumbnail
import com.yehia.mira_file_picker.MiraFilePickerActivity
import com.yehia.mira_file_picker.R
import com.yehia.mira_file_picker.databinding.SheetTypesBinding
import com.yehia.mira_file_picker.pickit.PickiT
import com.yehia.mira_file_picker.pickit.PickiTCallbacks
import com.yehia.mira_file_picker.sheet.model.FileData
import com.yehia.mira_file_picker.sheet.model.Type
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File


class PickerTypesSheet(
    private val fragment: Fragment,
    private val types: MutableList<String>,
    private val camera: Boolean = false,
    private val multiple: Boolean = false,
    private var multipleCount: Int = 0,
    val resultFile: (FileData, Boolean) -> Unit
) : BaseBottomSheetFragment<SheetTypesBinding>(SheetTypesBinding::inflate) {

    private var sizeList: Int = 0

    companion object {
        const val MIME_TYPE_AUDIO = "audio/*"
        const val MIME_TYPE_TEXT = "text/*"
        const val MIME_TYPE_IMAGE = "image/*"
        const val MIME_TYPE_VIDEO = "video/*"
        const val MIME_TYPE_PDF = "application/pdf"
        const val MIME_TYPE_ZIP = "application/zip"
        const val MIME_TYPE_RAR = "application/rar"
        const val MIME_TYPE_DOC = "application/doc"
        const val MIME_TYPE_DOCX = "application/docx"
        const val MIME_TYPE_PPT = "application/ppt"
        const val MIME_TYPE_PPTX = "application/pptx"
        const val MIME_TYPE_XLS = "application/xls"
    }

    private var maxFile: Boolean = false
    private lateinit var pickiT: PickiT

    //    override fun getFragmentView(): Int = R.layout.sheet_types
    private lateinit var adapter: TypesAdapter
    lateinit var type: Type
    var previewRequest: ActivityResultLauncher<Intent>

    private val typesList: MutableList<Type> = ArrayList()

    init {
        previewRequest =
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {

                    if (it.data?.data != null) {
                        startLic()
                        if (multipleCount != 0) {
                            if (sizeList < multipleCount) {
                                sizeList += 1
                                maxFile = false
                                pickiT.getPath(it.data?.data, Build.VERSION.SDK_INT)
                            } else {
                                maxFile = true
                            }
                        } else {
                            pickiT.getPath(it.data?.data, Build.VERSION.SDK_INT)
                        }
                    }
                    if (it.data?.clipData != null) {
                        for (i in 0 until it.data?.clipData?.itemCount!!) {
                            val uri: Uri = it.data?.clipData?.getItemAt(i)?.uri!!

                            if (multipleCount != 0) {
                                if (sizeList < multipleCount) {
                                    sizeList += 1
                                    startLic()
                                    maxFile = false
                                    pickiT.getPath(uri, Build.VERSION.SDK_INT)
                                } else {
                                    maxFile = true
                                }
                            } else {
                                startLic()
                                pickiT.getPath(uri, Build.VERSION.SDK_INT)
                            }
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
            val span = if (typesList.size > 3) {
                4
            } else {
                typesList.size
            }

            val gridLayoutManager = GridLayoutManager(requireContext(), span)
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

    private fun createType(it: String): Type {
        return when (it) {
            MIME_TYPE_AUDIO -> {
                Type(
                    it,
                    getString(R.string.voice),
                    "",
                    R.drawable.ic_voice,
                    false,
                    multiple, "audio"
                )
            }
            MIME_TYPE_TEXT -> {
                Type(
                    it, getString(R.string.text), "", R.drawable.ic_text, false, multiple, "text"
                )
            }
            MIME_TYPE_IMAGE -> {
                Type(
                    it,
                    getString(R.string.images),
                    "",
                    R.drawable.ic_gallary,
                    camera,
                    multiple, "image"
                )
            }
            MIME_TYPE_VIDEO -> {
                Type(
                    it,
                    getString(R.string.video),
                    "",
                    R.drawable.ic_video,
                    camera,
                    multiple, "video"
                )
            }
            MIME_TYPE_PDF -> {
                Type(
                    it,
                    getString(R.string.pdf),
                    "pdf",
                    R.drawable.ic_pdf,
                    false,
                    multiple,
                    "application"
                )
            }
            MIME_TYPE_ZIP -> {
                Type(
                    it,
                    getString(R.string.compress),
                    "zip",
                    R.drawable.ic_zip,
                    false,
                    multiple, "application"
                )
            }
            MIME_TYPE_RAR -> {
                Type(
                    it,
                    getString(R.string.compress),
                    "rar",
                    R.drawable.ic_rar,
                    false,
                    multiple,
                    "application"
                )
            }
            MIME_TYPE_DOC -> {
                Type(
                    it,
                    getString(R.string.word),
                    "doc",
                    R.drawable.ic_doc,
                    false,
                    multiple,
                    "application"
                )
            }
            MIME_TYPE_DOCX -> {
                Type(
                    it,
                    getString(R.string.word),
                    "docx",
                    R.drawable.ic_doc,
                    false,
                    multiple,
                    "application"
                )
            }
            MIME_TYPE_PPT -> {
                Type(
                    it,
                    getString(R.string.powerPoint),
                    "ppt",
                    R.drawable.ic_ppt,
                    false,
                    multiple,
                    "application",
                )
            }
            MIME_TYPE_PPTX -> {
                Type(
                    it,
                    getString(R.string.powerPoint),
                    "pptx",
                    R.drawable.ic_ppt,
                    false,
                    multiple,
                    "application",
                )
            }
            MIME_TYPE_XLS -> {
                Type(
                    it,
                    getString(R.string.excel),
                    "xls",
                    R.drawable.ic_xls,
                    false,
                    multiple,
                    "application",
                )
            }
            else -> {
                Type(
                    it,
                    getString(R.string.any_type),
                    "",
                    R.drawable.ic_any_type,
                    false,
                    multiple, "application"
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
            fileData.path += ".${type.extension}"
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
                    fileData.compressPath += ".${type.extension}"
                }
            }
        } else {
            val thumbnail = getThumbnail(requireContext(), file)
            fileData.Thumbnail = thumbnail
        }
        dialog?.dismiss()

        resultFile(fileData, maxFile)
    }

    fun show() {
        if (this.isAdded) {
            if (types.size == 1) {
                type = createType(types[0])
                val intent = Intent(activity, MiraFilePickerActivity::class.java)
                intent.putExtra("multiple", type.multiple)
                intent.putExtra("type", type.key)
                intent.putExtra("camera", type.camera)
                previewRequest.launch(intent)
            } else {
                this.dialog!!.show()
            }
        } else {
            this.show(fragment.childFragmentManager, "")
        }
    }

    fun show(sizeList: Int) {
        this.sizeList = sizeList
        if (this.isAdded) {
            if (types.size == 1) {
                type = createType(types[0])
                val intent = Intent(activity, MiraFilePickerActivity::class.java)
                intent.putExtra("multiple", type.multiple)
                intent.putExtra("type", type.key)
                intent.putExtra("camera", type.camera)
                previewRequest.launch(intent)
            } else {
                this.dialog!!.show()
            }
        } else {
            this.show(fragment.childFragmentManager, "")
        }
    }

    override fun onStart() {
        super.onStart()

        startLic()
    }

    private fun startLic() {
        pickiT = PickiT(context, object : PickiTCallbacks {
            override fun PickiTonUriReturned() {
            }

            override fun PickiTonStartListener() {
            }

            override fun PickiTonProgressUpdate(progress: Int) {
            }

            override fun PickiTonCompleteListener(
                path: String?,
                wasDriveFile: Boolean,
                wasUnknownProvider: Boolean,
                wasSuccessful: Boolean,
                Reason: String?
            ) {
                if (path != null) {
                    addFile(File(path))
                }
            }

            override fun PickiTonMultipleCompleteListener(
                paths: java.util.ArrayList<String>?,
                wasSuccessful: Boolean,
                Reason: String?
            ) {
                paths?.forEachIndexed { index, it ->
                    if (multipleCount != 0) {
                        if (index <= multipleCount) {
                            addFile(File(it))
                        }
                    } else {
                        addFile(File(it))
                    }
                }
            }

        }, requireActivity())
    }
}