package com.example.images_list

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.URL
import org.json.simple.parser.JSONParser
import java.io.IOException

class MainActivity : AppCompatActivity() {
    companion object {
        const val key = "09a9cfc90ace7036c83b7ef2ff4da5c940f28befd1c5bcc6560d6143bb40385a963565893ab44a3da97f8"
        const val query = "memes"
        const val count = 100
    }

    private var imageList : List<Image>? = null

    internal fun display_preview_images(result : List<Image>) {
        imageList = result
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

    class GetImagesPreview(activity : MainActivity) : AsyncTask<String, Void, List<Image>>() {

        private val refer = WeakReference(activity)

        override fun doInBackground(vararg params : String): List<Image>? {
            val image_list = mutableListOf<Image>()
            val string_url = "https://api.vk.com/method/photos.search?q=${query}&access_token=${key}&v=5.102&count=${count}"
            try {
                InputStreamReader(URL(string_url).openConnection().getInputStream()).use {
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

        override fun onPostExecute(result: List<Image>) {
            val activity = refer.get()
            activity?.display_preview_images(result)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        if (imageList == null) {
            GetImagesPreview(this@MainActivity).execute()
        }
    }
}