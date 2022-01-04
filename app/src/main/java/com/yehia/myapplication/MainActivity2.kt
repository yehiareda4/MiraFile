package com.yehia.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, Test2Fragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }
}