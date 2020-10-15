package com.example.images_list

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import org.json.simple.parser.JSONParser
import java.io.IOException
import java.util.ArrayList

const val key = "09a9cfc90ace7036c83b7ef2ff4da5c940f28befd1c5bcc6560d6143bb40385a963565893ab44a3da97f8"
const val query = "memes"
const val count = 50
const val IMAGE_LIST = "image_list"
class MainActivity : AppCompatActivity() {

    private var imageList : ArrayList<Image>? = null

    class GetImagesPreview(service: MainActivity) : AsyncTask<String, Void, ArrayList<Image>>() {

        private val refer = WeakReference(service)

        override fun doInBackground(vararg params : String): ArrayList<Image>? {
            Log.i("downloading images", "${params.size}")
            val image_list = ArrayList<Image>()
            val string_url = "https://api.vk.com/method/photos.search?q=${query}&access_token=${key}&v=5.102&count=${count}"
            try {
                InputStreamReader(java.net.URL(string_url).openConnection().getInputStream()).use {
                    val root = JSONParser().parse(it.readText()) as org.json.simple.JSONObject
                    val response = root["response"] as org.json.simple.JSONObject
                    val results = response["items"] as org.json.simple.JSONArray
                    for (result in results) {
                        result as org.json.simple.JSONObject
                        val description = result["text"] as String
                        val sizes = result["sizes"] as org.json.simple.JSONArray
                        val s = sizes.last() as org.json.simple.JSONObject
                        val my_url = s["url"] as String
                        image_list.add(Image(description, my_url))
                    }
                }
            } catch (e : IOException) {
                Log.e("connecting", "connection failed : ${e.message}", e)
                e.printStackTrace()
            }
            return image_list
        }
        override fun onPostExecute(array: ArrayList<Image>) {
            val service = refer.get()
            service?.updateList(array)
            service?.display_preview_images(array)
        }
    }

    internal fun updateList(result : ArrayList<Image>?) {
        imageList = result
    }

    internal fun display_preview_images(result : ArrayList<Image>?) {
        if (result == null) {
            return
        }
        val viewManager = LinearLayoutManager(this@MainActivity)
        recycler_view.apply {
            layoutManager = viewManager
            adapter = ImageAdapter(result) {
                val intent = Intent(this@MainActivity, ImageActivity::class.java)
                intent.putExtra("url", it.url)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null) {
            imageList = savedInstanceState.getParcelableArrayList(IMAGE_LIST)
        }
        if (imageList == null) {
            GetImagesPreview(this@MainActivity).execute()
        } else {
            display_preview_images(imageList)
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        Log.i("onSaveInstanceState", "outstate")
        outState.putParcelableArrayList(IMAGE_LIST, imageList as ArrayList<out Parcelable>)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i("onRestoreInstanceState", "get_image_list")
        imageList = savedInstanceState.getParcelableArrayList(IMAGE_LIST)
    }
}