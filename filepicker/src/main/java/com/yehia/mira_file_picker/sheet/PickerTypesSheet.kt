package com.yehia.mira_file_picker.sheet

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.yehia.album.AlbumFile
import com.yehia.mira_file_picker.FileUtils
import com.yehia.mira_file_picker.FileUtils.getFile
import com.yehia.mira_file_picker.R
import com.yehia.mira_file_picker.databinding.SheetTypesBinding
import com.yehia.mira_file_picker.pickit.PickiT
import com.yehia.mira_file_picker.pickit.PickiTCallbacks
import com.yehia.mira_file_picker.sheet.model.FileData
import com.yehia.mira_file_picker.sheet.model.Type
import com.yehia.mira_file_picker.sheet.util.Keys.MIME_TYPE_VIDEO
import com.yehia.mira_file_picker.sheet.util.createType
import com.yehia.mira_file_picker.sheet.util.openSingleType
import com.yehia.mira_file_picker.sheet.util.preparePart
import com.yehia.mira_file_picker.sheet.util.preparePartThumbnail
import java.io.File

class PickerTypesSheet(
    private val fragment: Fragment,
    private val types: MutableList<String>,
    private val partName: String = "image",
    private val thumbnailPartName: String = "",
    private val camera: Boolean = false,
    val multiple: Boolean = false,
    var multipleCount: Int = 1,
    private val colorPrim: Int = R.color.gray_al_mai,
    private val colorAcc: Int = R.color.green_al_mai,
    private val colorTxt: Int = com.yehia.album.R.color.black_al_mai,
    val resultFile: (FileData, Boolean) -> Unit
) : BaseBottomSheetFragment<SheetTypesBinding>(SheetTypesBinding::inflate) {

    private var pathScopeEx: String = ""
    var lastImage: ArrayList<AlbumFile> = ArrayList()
    private var lastfile: AlbumFile? = null
    private var dismissed: Boolean = false
    private var sizeList: Int = 0

    private var maxFile: Boolean = false
    private lateinit var pickiT: PickiT

    private lateinit var adapter: TypesAdapter
    lateinit var type: Type
    private var previewRequest: ActivityResultLauncher<Intent>

    private val typesList: MutableList<Type> = ArrayList()

    init {
        previewRequest =
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {

                    if (it.data?.data != null) {
                        (fragment.requireActivity()).startPickTCallbacks()
                        if (multipleCount != 0) {
                            if (sizeList < multipleCount) {
                                sizeList += 1
                                maxFile = false
                                pushPath(it.data!!.data!!)
                            } else {
                                maxFile = true
                            }
                        } else {
                            pushPath(it.data!!.data!!)
                        }
                    }
                    if (it.data?.clipData != null) {
                        for (i in 0 until it.data?.clipData?.itemCount!!) {
                            val uri: Uri = it.data?.clipData?.getItemAt(i)?.uri!!

                            if (multipleCount != 0) {
                                if (sizeList < multipleCount) {
                                    sizeList += 1
                                    (fragment.requireActivity()).startPickTCallbacks()
                                    maxFile = false
                                    pushPath(uri)
                                } else {
                                    maxFile = true
                                }
                            } else {
                                (fragment.requireActivity()).startPickTCallbacks()
                                pushPath(uri)
                            }
                        }
                    }
                }
            }
    }

    override fun onResume() {
        super.onResume()
        if (dismissed) {
            dialog!!.dismiss()
        }
        this.dialog?.setOnDismissListener {
            dismissed = true
        }
    }

    private fun pushPath(data: Uri) {
        val path = FileUtils.getPath(activity, data)
        pathScopeEx = getFile(context, data).extension
        if (path != null) {
            val uri =
                FileUtils.createCopyAndReturnRealPath(
                    (fragment.requireActivity()),
                    data
                )
            pickiT.getPath(uri!!.toUri(), Build.VERSION.SDK_INT)
        } else {
            pickiT.getPath(data, Build.VERSION.SDK_INT)
        }
    }

    override fun afterCreateView() {
        typesList.clear()

        if (types.size > 1) {
            types.forEach {
                val type = fragment.createType(camera, multiple, it)

                typesList.add(type)
            }
            adapter = TypesAdapter(typesList) {
                type = it

                (fragment.requireActivity()).openSingleType(
                    type = type,
                    multipleCount = multipleCount,
                    sizeList = sizeList,
                    lastImage = lastImage,
                    colorPrim = colorPrim,
                    colorAcc = colorAcc,
                    colorTxt = colorTxt,
                    previewRequest = previewRequest,
                ) { resultFile ->
                    if (resultFile != null) {
                        lastfile = resultFile
                        addFile(File(resultFile.path!!))
                    }
                }
            }
            val span = if (typesList.size > 3) {
                4
            } else {
                typesList.size
            }

            val gridLayoutManager = GridLayoutManager(activity, span)
            binding.rvTypes.layoutManager = gridLayoutManager
            binding.rvTypes.adapter = adapter
        } else {
            type = fragment.createType(camera, multiple, types[0])

            (fragment.requireActivity()).openSingleType(
                type = type,
                multipleCount = multipleCount,
                sizeList = sizeList,
                lastImage = lastImage,
                colorPrim = colorPrim,
                colorAcc = colorAcc,
                colorTxt = colorTxt,
                previewRequest = previewRequest,
            ) { resultFile ->
                if (resultFile != null) {
                    lastfile = resultFile
                    addFile(File(resultFile.path!!))
                }
            }
        }
    }

    private fun addFile(file: File) {
        val fileData = FileData(
            file, file.name, FileUtils.getReadableFileSize(file.length().toInt()),
            file.path, file.extension, type.mediaType, preparePart(
                type, file, if (file.extension.isEmpty()) {
                    "${file.name}.${type.extension.ifEmpty { pathScopeEx }}"
                } else {
                    file.name
                }, partName
            )
        )
        if (file.extension.isEmpty()) {
            fileData.path += ".${type.extension.ifEmpty { pathScopeEx }}"
        }
        if (file.extension.isEmpty()) {
            fileData.name += ".${type.extension.ifEmpty { pathScopeEx }}"
        }
        if (fileData.extension.isEmpty()) {
            fileData.extension = type.extension.ifEmpty { pathScopeEx }
        }
        if (type.key == MIME_TYPE_VIDEO) {
            if (lastfile != null) {
                val thumbnail = lastfile!!.thumbPath
                fileData.Thumbnail = thumbnail
                val thumbnailFile = File(fileData.Thumbnail)
                fileData.ThumbnailPart = preparePartThumbnail(
                    thumbnailFile,
                    thumbnailFile.name,
                    thumbnailPartName
                )

                fileData.duration = (lastfile?.duration ?: 0).toString()
                lastfile = null
            }
        }
        pathScopeEx = ""
        dialog?.dismiss()

        resultFile(fileData, maxFile)
    }

    fun show(sizeList: Int = 0, type: Type? = null, cleanImages: Boolean = false): Boolean {
        dismissed = false
        this.sizeList = sizeList

        if (sizeList > multipleCount && multiple) {
            return false
        }
        if (type != null) {
            this.type = type
        } else {
            this.type = fragment.createType(camera, multiple, types[0])
        }

        if (types.size == 1 || type != null) {
            (fragment.requireActivity()).openSingleType(
                type = this.type,
                multipleCount = multipleCount,
                sizeList = sizeList,
                lastImage = if (cleanImages || multipleCount == 1) java.util.ArrayList() else lastImage,
                colorPrim = colorPrim,
                colorAcc = colorAcc,
                colorTxt = colorTxt,
                previewRequest = previewRequest,
            ) { resultFile ->
                if (resultFile != null) {
                    lastfile = resultFile
                    addFile(File(resultFile.path!!))
                }
            }
        } else {
            if (this.isAdded) {
                this.dialog!!.show()
            } else {
                this.show((fragment.requireActivity()).supportFragmentManager, "")
            }
        }
        return true
    }

    override fun onStart() {
        super.onStart()

        (fragment.requireActivity()).startPickTCallbacks()
    }

    private fun Activity.startPickTCallbacks() {
        pickiT = PickiT(this, object : PickiTCallbacks {
            override fun PickiTonUriReturned() {
            }

            override fun PickiTonStartListener() {
            }

            override fun PickiTonProgressUpdate(progress: Int) {
            }

            override fun PickiTonCompleteListener(
                path: String,
                wasDriveFile: Boolean,
                wasUnknownProvider: Boolean,
                wasSuccessful: Boolean,
                Reason: String
            ) {
                addFile(File(path))
            }

            override fun PickiTonMultipleCompleteListener(
                paths: ArrayList<String?>?,
                wasSuccessful: Boolean,
                Reason: String
            ) {
                paths?.forEachIndexed { index, it ->
                    if (multipleCount != 0) {
                        if (index <= multipleCount) {
                            addFile(File(it!!))
                        }
                    } else {
                        addFile(File(it!!))
                    }
                }
            }

        }, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.bottomSheetFileDialogStyle)
    }
}