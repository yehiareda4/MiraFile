package com.yehia.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aait.miranewfilepiker.R
import com.aait.miranewfilepiker.databinding.FragmentTest2Binding

class Test1Fragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentTest2Binding
    private lateinit var test2Fragment: Test2Fragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTest2Binding.inflate(
            layoutInflater,
            container,
            false
        )
        binding.ivChooseFile.setOnClickListener(this)

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(v: View?) {
        findNavController().navigate(R.id.action_test1Fragment_to_test2Fragment)
    }
}