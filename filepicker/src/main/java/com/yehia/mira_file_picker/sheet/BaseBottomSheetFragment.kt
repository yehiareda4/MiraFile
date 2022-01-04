package com.yehia.mira_file_picker.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment<T : ViewDataBinding> : BottomSheetDialogFragment() {

    private var isInitialized: Boolean = false

    lateinit var binding: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!::binding.isInitialized) {
            binding = DataBindingUtil.inflate(
                layoutInflater,
                getFragmentView(),
                container,
                false
            )

            isInitialized = false
            afterCreateView()
        } else {
            isInitialized = true
            afterInitializedBinding()
        }

//        backDefaultKey()

        return binding.root
    }

    open fun afterCreateView() {

    }

    open fun afterInitializedBinding() {

    }

    abstract fun getFragmentView(): Int

//    private fun backDefaultKey() {
//        requireView().isFocusableInTouchMode = true
//        requireView().requestFocus()
//
//        requireView().setOnKeyListener(object : View.OnKeyListener {
//            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
//                if (event.action == KeyEvent.ACTION_DOWN) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//                        onBack()
//                        return true
//                    }
//                }
//                return false
//            }
//        })
//    }
//
//    fun onBack() {
//        dismiss()
//    }
}