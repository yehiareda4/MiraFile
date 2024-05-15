package com.yehia.mira_file_picker.sheet

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
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
import com.yehia.mira_file_picker.sheet.util.*
import com.yehia.mira_file_picker.sheet.util.Keys.MIME_TYPE_IMAGE
import com.yehia.mira_file_picker.sheet.util.Keys.MIME_TYPE_VIDEO
import java.io.File

class PickerTypesActivitySheet(
    private val activity: AppCompatActivity,
    private val types: MutableList<String>,
    var partName: String = "image",
    var thumbnailPartName: String = "",
    private val camera: Boolean = false,
    var multiple: Boolean = false,
    var multipleCount: Int = 1,
    var crop: Boolean = true,
    var cropOval: Boolean = true,
    private val colorPrim: Int = R.color.gray_al_mai,
    private val colorAcc: Int = R.color.green_al_mai,
    private val colorTxt: Int = com.yehia.album.R.color.black_al_mai,
    private val callBack: CallBack = object : CallBack {},
    val resultFile: (FileData, Boolean) -> Unit = { _, _ ->

    }
) : BaseBottomSheetFragment<SheetTypesBinding>(SheetTypesBinding::inflate) {

    private var pathScopeEx: String = ""
    var lastImage: ArrayList<AlbumFile> = ArrayList()
    private var lastfile: AlbumFile? = null
    private var dismissed: Boolean = false
    private var sizeList: Int = 0
    private var max: Int = 0

    private var maxFile: Boolean = false
    private lateinit var pickiT: PickiT

    private lateinit var adapter: TypesAdapter
    lateinit var type: Type
    private var previewRequest: ActivityResultLauncher<Intent>

    private val typesList: MutableList<Type> = ArrayList()

    private var files: MutableList<FileData> = ArrayList()

    init {
        previewRequest =
            activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    if (it.data?.data != null) {
                        (activity).startPickTCallbacks()
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
                                max =
                                    if (it.data?.clipData?.itemCount!! > multipleCount) multipleCount else it.data?.clipData?.itemCount!!
                                if (sizeList < multipleCount) {
                                    sizeList += 1
                                    (activity).startPickTCallbacks()
                                    maxFile = false
                                    pushPath(uri)
                                } else {
                                    maxFile = true
                                }
                            } else {
                                (activity).startPickTCallbacks()
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
        if (path != null) {
            pathScopeEx = getFile(activity, data).extension
            val uri = FileUtils.createCopyAndReturnRealPath(
                (activity), data
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
                val type = activity.createType(camera, multiple, it)

                typesList.add(type)
            }
            adapter = TypesAdapter(typesList) {
                type = it
                startSingleType()
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
            type = activity.createType(camera, multiple, types[0])
            startSingleType()
        }
    }

    @SuppressLint("ResourceType")
    private fun addFile(file: File) {
        activity.lifecycleScope.launchWhenCreated {
            val fileData = FileData(
                file,
                file.name,
                FileUtils.getReadableFileSize(file.length().toInt()),
                file.path,
                file.extension,
                type.mediaType,
                preparePart(
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
                        thumbnailFile, thumbnailFile.name, thumbnailPartName
                    )

                    fileData.duration = (lastfile?.duration ?: 0).toString()
                    lastfile = null
                }
            }
            if (type.key == MIME_TYPE_IMAGE) {
                fileData.compressImage = fileData.file.compressImage(activity)
                fileData.compressImageSize =
                    FileUtils.getReadableFileSize(File(fileData.compressImage).length().toInt())
                fileData.compressImagePart = preparePart(
                    type, File(fileData.compressImage), if (file.extension.isEmpty()) {
                        "${file.name}.${type.extension.ifEmpty { pathScopeEx }}"
                    } else {
                        file.name
                    }, partName
                )
            }
            pathScopeEx = ""
            dialog?.dismiss()

            resultFile(fileData, maxFile)
            if (multiple) {
                files.add(fileData)
                if (max == files.size) {
                    callBack.multiFiles(files)
                }
            } else {
                callBack.singleFiles(fileData)
            }
        }
    }

    fun show(sizeList: Int = 0, type: Type? = null, cleanImages: Boolean = false): Boolean {
        dismissed = false
        files = ArrayList()
        this.sizeList = sizeList

        if (sizeList > multipleCount && multiple) {
            return false
        }
        if (type != null) {
            this.type = type
        } else {
            this.type = activity.createType(camera, multiple, types[0])
        }

        if (types.size == 1 || type != null) {
            startSingleType(cleanImages)
        } else {
            if (this.isAdded) {
                this.dialog!!.show()
            } else {
                this.show(activity.supportFragmentManager, "")
            }
        }
        return true
    }

    private fun startSingleType(cleanImages: Boolean = false) {
        (activity).openSingleType2(
            type = this.type,
            multipleCount = multipleCount,
            sizeList = sizeList,
            crop = crop,
            cropOval = cropOval,
            lastImage = if (cleanImages || multipleCount == 1) java.util.ArrayList() else lastImage,
            colorPrim = colorPrim,
            colorAcc = colorAcc,
            colorTxt = colorTxt,
            previewRequest = previewRequest,
            previewRequest
        ) { resultFile, max ->
            this.max = max
            if (resultFile != null) {
                lastfile = resultFile
                addFile(File(resultFile.path!!))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        try {
            (activity).startPickTCallbacks()
        } catch (_: Exception) {
        }
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
                paths: ArrayList<String?>?, wasSuccessful: Boolean, Reason: String
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