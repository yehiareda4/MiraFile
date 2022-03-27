package com.yehia.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.frame, Test1Fragment())
//        transaction.addToBackStack(null)
//        transaction.commit()
    }
}