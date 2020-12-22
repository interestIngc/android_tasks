package com.example.postslist

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MyService : Service() {


    private var resp : Call<Post>? = null

    private var call : Call<List<Post>>? = null

    private var list : MutableList<Post>? = null

    private var delete_resp : Call<Any>? = null

    private var currId : Long = 110

    private fun sending(currPost : Post?, title: String, body: String, message : Int) {
        val postDao = MyApp.instance.myDb.postDAO()
        if (currPost != null) {
            GlobalScope.launch(Dispatchers.IO) {
                postDao.insert(currPost)
            }
        }
        sendBroadcast(Intent().apply {
            action = "response"
            addCategory(Intent.CATEGORY_DEFAULT)
            putExtra("operation", "post")
            putExtra("post", currPost)
            putExtra("title", title)
            putExtra("body", body)
            putExtra("message", message)
        })
    }


    private fun post(title : String, body : String) {
        var currPost : Post? = null
        resp = MyApp.instance.myApi.doPost(title, body, 1)
        resp?.enqueue(object : Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                Log.i("post", "error while posting occured", t)
                sending(Post(1, currId++, title, body), title, body, 0)
            }
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                Log.i("response", "${response.body()}")
                if (response.isSuccessful) {
                    currPost = response.body()
                    sending(currPost, title, body, 1)
                } else {
                    sending(Post(1, currId++, title, body), title, body, 0)
                }
            }
        })
    }

    private fun send_delete(text : String, post : Post?) {
        val postDao = MyApp.instance.myDb.postDAO()
        if (post != null) {
            val job : Job = GlobalScope.launch(Dispatchers.IO) {
                postDao.deleteFromDao(post)
            }
        }
        sendBroadcast(Intent().apply {
            action = "response"
            addCategory(Intent.CATEGORY_DEFAULT)
            putExtra("operation", "delete")
            putExtra("text", text)
            putExtra("post", post)
        })
    }

    private fun delete(post : Post) {
        if (post.id == null) {
            return
        }
        delete_resp = MyApp.instance.myApi.delete(post.id)
        delete_resp?.enqueue(object: Callback<Any> {
            override fun onFailure(call: Call<Any>, t: Throwable) {
                val text = "can't connect with API, sorry"
                send_delete(text, post)
            }
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                val text = "$response \n delete was successful"
                send_delete(text, post)
            }
        })
    }

    private fun send_run(newList : List<Post>?) {
        if (newList != null) {
            list = newList as MutableList<Post>
        }
        if (list == null) {
            sendBroadcast(Intent().apply {
                action = "response"
                addCategory(Intent.CATEGORY_DEFAULT)
                putExtra("operation", "run")
                putExtra("has_list", false)
            })
            return
        }
        val currList = list as ArrayList<Post>
        sendBroadcast(Intent().apply {
            action = "response"
            addCategory(Intent.CATEGORY_DEFAULT)
            putExtra("has_list", true)
            putExtra("operation", "run")
            putExtra("list", currList)
        })
    }

    private fun run() {
        call = MyApp.instance.myApi.get()
        var currList : List<Post>? = null
        call?.enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    Log.i("get_posts", "successful")
                    val body = response.body()
                    currList = body
                    send_run(currList)
                } else {
                    send_run(currList)
                }
            }
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Log.i("run", "failed execution")
                send_run(currList)
            }
        }
        )
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val curr_operation = intent?.extras?.getString("operation")
        if (curr_operation == "post") {
            val title = intent.extras?.getString("title")
            val body = intent.extras?.getString("body")
            if (title != null && body != null) {
                post(title, body)
            }
        }
        if (curr_operation == "delete") {
            val currPost = intent.extras?.getParcelable<Post>("post")
            if (currPost != null) {
                delete(currPost)
            }
        }
        if (curr_operation == "run") {
            run()
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        list?.clear()
        call?.cancel()
        resp?.cancel()
        delete_resp?.cancel()
        super.onDestroy()
    }
}
