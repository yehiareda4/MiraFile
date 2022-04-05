package com.yehia.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.yehia.mira_file_picker.sheet.PickerTypesSheet
import com.yehia.mira_file_picker.sheet.PickerTypesSheet.Companion.MIME_TYPE_IMAGE
import com.yehia.mira_file_picker.sheet.PickerTypesSheet.Companion.MIME_TYPE_PDF
import com.yehia.mira_file_picker.sheet.model.FileData
import com.yehia.myapplication.databinding.FragmentTestBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Test2Fragment : Fragment(), View.OnClickListener {

    private lateinit var typesSheet: PickerTypesSheet
    private lateinit var binding: FragmentTestBinding
    private var selectedFiles: MutableList<FileData>? = null
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

        selectedFiles = ArrayList()
        adapter = ItemAdapter(
            selectedFiles, requireActivity()
        ) {

            provideRetrofit().create(api::class.java)
                .uploadFileAsync(it.filePart)
                .enqueue(object : Callback<Any> {
                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                        Log.d(TAG, "onResponse: $response")
                        Toast.makeText(requireContext(), "onResponse", Toast.LENGTH_LONG).show()
                    }

                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        Log.d(TAG, "onfail: $t")
                        Toast.makeText(requireContext(), "onfail", Toast.LENGTH_LONG).show()
                    }

                })

            true
        }
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
//        types.add(MIME_TYPE_AUDIO)
//        types.add(MIME_TYPE_TEXT)
        types.add(MIME_TYPE_IMAGE)
//        types.add(MIME_ALL_TYPE)
        types.add(MIME_TYPE_PDF)
//        types.add(MIME_TYPE_ZIP)
//        types.add(MIME_TYPE_RAR)
//        types.add(MIME_TYPE_DOC)
//        types.add(MIME_TYPE_PPT)
//        types.add(MIME_TYPE_XLS)

        typesSheet = PickerTypesSheet(
            requireActivity() as AppCompatActivity,
            this,
            types, "file",
            camera = true,
            multiple = true,
            multipleCount = 5,
        ) { file, maxFile ->
            selectedFiles?.add(file)
            adapter!!.notifyDataSetChanged()
            if (maxFile) {
                Toast.makeText(requireContext(), "maxFile", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(v: View?) {
        typesSheet.show(selectedFiles!!.size)
    }

    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://abr-almodon.4hoste.com/api/")
            .build()
    }
}