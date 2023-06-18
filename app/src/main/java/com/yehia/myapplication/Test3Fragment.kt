package com.yehia.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aait.miranewfilepiker.databinding.FragmentTestBinding

class Test3Fragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentTestBinding

    @SuppressLint("NotifyDataSetChanged")
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
        binding.ivChooseFile2.text = "dgsduhjgrpfuoxigsf"
        return binding.root
    }

    override fun onClick(v: View?) {

    }
}