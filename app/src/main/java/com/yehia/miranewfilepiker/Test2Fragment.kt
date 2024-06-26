package com.yehia.miranewfilepiker

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.aait.miranewfilepiker.R
import com.aait.miranewfilepiker.databinding.FragmentTestBinding
import com.yehia.mira_file_picker.sheet.PickerTypesSheet
import com.yehia.mira_file_picker.sheet.model.FileData
import com.yehia.mira_file_picker.sheet.util.CallBack
import com.yehia.mira_file_picker.sheet.util.Keys
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Test2Fragment : Fragment(), View.OnClickListener {

    private lateinit var typesSheet: PickerTypesSheet
    private lateinit var binding: FragmentTestBinding
    private var selectedFiles: MutableList<FileData> = ArrayList()
    private var adapter: ItemAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTestBinding.inflate(
            layoutInflater, container, false
        )
        binding.ivChooseFile2.setOnClickListener {
            findNavController().navigate(R.id.action_test2Fragment_to_test3Fragment)
        }
        selectedFiles = ArrayList()
        adapter = ItemAdapter(
            selectedFiles, requireActivity()
        ) {

            provideRetrofit().create(api::class.java).uploadFileAsync(it.filePart!!)
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
                requireContext(), DividerItemDecoration.VERTICAL
            )
        )
        binding.rvFiles.adapter = adapter
        binding.ivChooseFile.setOnClickListener(this)
        binding.tvChooseFile.setOnClickListener(this)

        val types: MutableList<String> = ArrayList()
//        types.add(Keys.MIME_TYPE_VIDEO)
//        types.add(Keys.MIME_TYPE_TEXT)
        types.add(Keys.MIME_TYPE_IMAGE)
//        types.add(Keys.MIME_ALL_TYPE)
//        types.add(Keys.MIME_TYPE_PDF)
//        types.add(Keys.MIME_TYPE_ZIP)
//        types.add(Keys.MIME_TYPE_RAR)
//        types.add(Keys.MIME_TYPE_DOC)
//        types.add(Keys.MIME_TYPE_PPT)
//        types.add(Keys.MIME_TYPE_XLS)

        typesSheet = PickerTypesSheet(this,
            types,
            "file",
            camera = true,
            colorPrim = R.color.purple_200,
            colorAcc = R.color.purple_700,
            colorTxt = R.color.black,
            thumbnailPartName = "dsigfdifdks",
            callBack = object : CallBack {
                override fun singleFiles(fileData: FileData) {
                    binding.ivChooseFile.setImageURI(fileData.Thumbnail.toUri())
                    selectedFiles?.add(fileData)
                    adapter!!.notifyDataSetChanged()
                }

                override fun multiFiles(files: MutableList<FileData>) {
                    selectedFiles?.clear()
                    binding.ivChooseFile.setImageURI(files[0].Thumbnail.toUri())
                    selectedFiles?.addAll(files)
                    adapter!!.notifyDataSetChanged()
                }
            })

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(v: View?) {
        typesSheet.show()
    }

    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://abr-almodon.4hoste.com/api/").build()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        childFragmentManager
    }
}