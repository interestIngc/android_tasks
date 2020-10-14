package com.example.images_list

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.IBinder
import android.util.Log
import kotlinx.android.synthetic.main.image.*
import java.io.ByteArrayOutputStream
import java.lang.ref.WeakReference
import java.net.URL
import java.util.concurrent.ConcurrentHashMap

const val RESPONSE = "response"
const val ByteArray = "byteArray"
const val URL = "url"
class MyService : Service() {

    private var concurrentMap = ConcurrentHashMap<String?, ByteArray>()

    internal fun sending(result : Pair<ByteArray, String>?) {
        val pressed_image = result?.first
        if (pressed_image != null) {
            concurrentMap[result.second] = pressed_image
            sendBroadcast(Intent().apply {
                action = RESPONSE
                Log.i("send compressed", "${pressed_image.size}")
                addCategory(Intent.CATEGORY_DEFAULT)
                putExtra(ByteArray, pressed_image)
            })
        }
    }
    class LoadOneImage(service : MyService) : AsyncTask<String, Void, Pair<ByteArray, String>>() {

        private val ref = WeakReference(service)

        override fun doInBackground(vararg url: String): Pair<ByteArray, String> {
            val curr_url = url[0]
            var bitmapImage: Bitmap? = null
            val inputStream = URL(curr_url).openStream()
            bitmapImage = BitmapFactory.decodeStream(inputStream)
            val pressed_image = ByteArrayOutputStream().apply {
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, this)
            }.toByteArray()
            return pressed_image to curr_url
        }

        override fun onPostExecute(result: Pair<ByteArray, String>?) {
            val service = ref.get()
            if (result != null) {
                service?.sending(result)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented");
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val curUrl = intent?.extras?.getString("url")
        if (concurrentMap.containsKey(curUrl)) {
            val byteArray = concurrentMap[curUrl]
            if (byteArray != null && curUrl != null) {
                sending(byteArray to curUrl)
            }
        } else {
            if (curUrl != null) {
                LoadOneImage(this@MyService).execute(curUrl)
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        concurrentMap.clear()
        super.onDestroy()
    }
}
