package com.yehia.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yehia.myapplication.databinding.ActivityMainBinding

class MainActivityK : AppCompatActivity() {

    private lateinit var test2Fragment: Test1Fragment
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())
    }

    override fun onStart() {
        super.onStart()
        if (!::test2Fragment.isInitialized) {
            test2Fragment = Test1Fragment()
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, test2Fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }
}