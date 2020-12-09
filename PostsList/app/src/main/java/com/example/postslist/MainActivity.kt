package com.example.postslist

import android.app.Activity
import android.app.Notification
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.IntegerRes
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Completable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import java.util.concurrent.Callable

val request_access_type = 10
class MainActivity : AppCompatActivity() {

    companion object {
    }
    private var call : Call<List<Post>>? = null

    private lateinit var add : Button

    private var myPostAdapter : PostAdapter? = null

    private var list : MutableList<Post>? = null

    private var resp : Call<Post>? = null

    private var currId : Long = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        add = findViewById<Button>(R.id.button)
        if (savedInstanceState == null) {
            run()
        } else {
            list = savedInstanceState.getParcelableArrayList("list")
            displayPosts(list)
        }
        button.setOnClickListener {
            val intent : Intent = Intent(this@MainActivity, EditTextActivity::class.java)
            startActivityForResult(intent, request_access_type)
        }
        reload.setOnClickListener {
            run()
        }
    }

    private fun post(title : String, body : String) : Post? {
        resp = MyApp.instance.myApi.doPost(title, body, 1)
        var currPost : Post? = null
        resp?.enqueue(object : Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                Log.i("post", "error while posting occured", t)
            }
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                Log.i("response", "${response.body()}")
                if (response.isSuccessful) {
                    currPost = response.body()
                    currId++
                    sendData(response.body())
                    Toast.makeText(
                        this@MainActivity,
                        "${response.body()} \nPosting was successful!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(this@MainActivity, "can't connect to the API", Toast.LENGTH_LONG).show()
                }
            }
        })
        return currPost
    }

    private fun makePost(title: String, body: String) {
        var post = post(title, body)
        val postDao = MyApp.instance.myDb.postDAO()
        if (post == null) {
            post = Post(1, currId, title, body)
            sendData(post)
            currId++
        }
        Thread {
            postDao.insert(post)
        }.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == request_access_type) {
            if (resultCode == Activity.RESULT_OK) {
                val title = data?.getStringExtra("title")
                val body = data?.getStringExtra("body")
                if (title != null && body != null) {
                    makePost(title, body)
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    internal fun sendData(post : Post?) {
        if (post != null) {
            myPostAdapter?.addItem(post)
        }
    }


    private fun run() {
        call = MyApp.instance.myApi.get()
        call?.enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    list = body as MutableList<Post>
                    displayPosts(list)
                } else {
                    Toast.makeText(this@MainActivity, "can't connect with API, sorry", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "connection error occured, sorry", Toast.LENGTH_LONG).show()
            }
        }
        )
    }

    private fun delete(post : Post) {
        if (post.id == null) {
            return
        }
        val resp = MyApp.instance.myApi.delete(post.id)
        Toast.makeText(this@MainActivity, "$resp", Toast.LENGTH_LONG).show()
    }

    private fun makeDelete(post: Post) {
        delete(post)
        val postDao = MyApp.instance.myDb.postDAO()
//        Completable.fromAction(object : Action {
//            override fun run() {
//                postDao.deleteFromDao(post)
//            }
//        }).subscribe()
        Thread {
            postDao.deleteFromDao(post)
        }.start()
    }


    private fun displayPosts(result : MutableList<Post>?) {
        if (result == null) {
            return
        }
        val viewManager = LinearLayoutManager(this@MainActivity)
        myPostAdapter = PostAdapter(result) {
            makeDelete(it)
        }
        recycle_view.apply {
            layoutManager = viewManager
            adapter = myPostAdapter
        }
        add.visibility = View.VISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("list", list as ArrayList<out Parcelable>)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        list = savedInstanceState.getParcelableArrayList("list")
    }
}