package com.yehia.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.yehia.mira_file_picker.sheet.PickerTypesSheet
import com.yehia.mira_file_picker.sheet.PickerTypesSheet.Companion.MIME_TYPE_AUDIO
import com.yehia.mira_file_picker.sheet.PickerTypesSheet.Companion.MIME_TYPE_DOC
import com.yehia.mira_file_picker.sheet.PickerTypesSheet.Companion.MIME_TYPE_IMAGE
import com.yehia.mira_file_picker.sheet.PickerTypesSheet.Companion.MIME_TYPE_PDF
import com.yehia.mira_file_picker.sheet.PickerTypesSheet.Companion.MIME_TYPE_PPT
import com.yehia.mira_file_picker.sheet.PickerTypesSheet.Companion.MIME_TYPE_RAR
import com.yehia.mira_file_picker.sheet.PickerTypesSheet.Companion.MIME_TYPE_TEXT
import com.yehia.mira_file_picker.sheet.PickerTypesSheet.Companion.MIME_TYPE_VIDEO
import com.yehia.mira_file_picker.sheet.PickerTypesSheet.Companion.MIME_TYPE_XLS
import com.yehia.mira_file_picker.sheet.PickerTypesSheet.Companion.MIME_TYPE_ZIP
import com.yehia.myapplication.databinding.FragmentTestBinding

class Test2Fragment : Fragment(), View.OnClickListener {

    private lateinit var typesSheet: PickerTypesSheet
    private lateinit var binding: FragmentTestBinding
    private var selectedFiles: MutableList<String>? = null
    private var adapter: ItemAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTestBinding.inflate(
            layoutInflater,
            container,
            false
        )

        selectedFiles = ArrayList<String>()
        adapter = ItemAdapter(selectedFiles, requireContext())
        binding.rvFiles.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        binding.rvFiles.adapter = adapter
        binding.ivChooseFile.setOnClickListener(this)
        binding.tvChooseFile.setOnClickListener(this)

        val types: MutableList<String> = ArrayList()
        types.add(MIME_TYPE_AUDIO)
        types.add(MIME_TYPE_TEXT)
        types.add(MIME_TYPE_IMAGE)
        types.add(MIME_TYPE_VIDEO)
        types.add(MIME_TYPE_PDF)
        types.add(MIME_TYPE_ZIP)
        types.add(MIME_TYPE_RAR)
        types.add(MIME_TYPE_DOC)
        types.add(MIME_TYPE_PPT)
        types.add(MIME_TYPE_XLS)

        typesSheet = PickerTypesSheet(
            this,
            types,
            camera = false,
            multiple = true
        ) { file ->
            selectedFiles?.add(file.name)
            adapter!!.notifyDataSetChanged()
        }

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(v: View?) {
       typesSheet.show()
    }
}