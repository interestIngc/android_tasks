package com.example.images_list

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.IBinder
import android.os.PersistableBundle
import android.util.Log
import kotlinx.android.synthetic.main.image.*
import java.lang.ref.WeakReference
import java.net.URL
import java.util.concurrent.ConcurrentHashMap
import com.example.images_list.ByteArray
import com.example.images_list.RESPONSE
import com.example.images_list.URL

class ImageActivity : AppCompatActivity() {
    var broadcastReceiver : BroadcastReceiver? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.extras?.getString("url")
        setContentView(R.layout.image)
        val service_intent = Intent(this@ImageActivity, MyService::class.java).
        putExtra("url", url)
        startService(service_intent)
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, intent: Intent?) {
                Log.i("receiving", "")
                val byteArray = intent?.getByteArrayExtra(ByteArray)
                if (byteArray != null) {
                    Log.i("received", "${byteArray.size}")
                    val byteImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                    image.setImageBitmap(byteImage)
                }
            }
        }
        registerReceiver(broadcastReceiver, IntentFilter(RESPONSE).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

}