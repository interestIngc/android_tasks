package com.example.animapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loading.startAnimation(animation(0F, 1F, 400L))
    }

    override fun onStop() {
        super.onStop()
        loading.animation.cancel()
    }
}