package com.yehia.mira_file_picker.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.aait.sa.ui.base.util.Inflate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) :
    BottomSheetDialogFragment() {

    private var isInitialized: Boolean = false

    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (_binding == null) {
            _binding = inflate.invoke(inflater, container, false)
            afterCreateView()
        } else {
            isInitialized = true
        }

        return binding.root
    }

    open fun afterCreateView() {

    }

    override fun onStart() {
        super.onStart()

        afterInitializedBinding()
    }

    open fun afterInitializedBinding() {

    }
}