package com.example.postslist

import android.app.Activity
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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


    private var call : Call<List<Post>>? = null

    private lateinit var add : Button

    private var myPostAdapter : PostAdapter? = null

    private var list : MutableList<Post>? = null

    private var resp : Call<Post>? = null

    private var currId : Long = 150

    var broadcastReceiver : BroadcastReceiver? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        add = findViewById<Button>(R.id.button)
        broadcastReceiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val operation = intent?.getStringExtra("operation")
                if (operation == "post") {
                    val message = intent.getIntExtra("message", 0)
                    val post = intent.getParcelableExtra<Post>("post")
                    if (message == 0) {
                        Toast.makeText(this@MainActivity, "can't connect to the API", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "$post \nPosting was successful!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    sendData(post)
                } else if (operation == "delete") {
                    val text = intent.getStringExtra("text")
                    Log.i("text", "${text.isNullOrEmpty()}")
                    Toast.makeText(this@MainActivity, text, Toast.LENGTH_LONG).show()
                } else if (operation == "run") {
                    val executed = intent.getBooleanExtra("has_list", false)
                    if (!executed) {
                        Toast.makeText(this@MainActivity, "can't connect with the API", Toast.LENGTH_LONG).show()
                    } else {
                        list = intent.getParcelableArrayListExtra("list")
                        displayPosts(list)
                    }
                }
            }
            
        }
        registerReceiver(broadcastReceiver, IntentFilter("response").apply {
            addCategory(Intent.CATEGORY_DEFAULT)
        })
        if (savedInstanceState == null) {
            makeRun()
        } else {
            list = savedInstanceState.getParcelableArrayList("list")
            displayPosts(list)
        }
        button.setOnClickListener {
            val intent : Intent = Intent(this@MainActivity, EditTextActivity::class.java)
            startActivityForResult(intent, request_access_type)
        }
        reload.setOnClickListener {
            makeRun()
        }
    }



    private fun makePost(title: String, body: String) {
        val service_intent = Intent(this@MainActivity, MyService::class.java)
        service_intent.putExtra("operation", "post")
        service_intent.putExtra("title", title)
        service_intent.putExtra("body", body)
        startService(service_intent)
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

    private fun makeRun() {
        val intent_service = Intent(this@MainActivity, MyService::class.java)
        intent_service.putExtra("operation", "run")
        startService(intent_service)
    }


    private fun makeDelete(post: Post) {
        val service_intent = Intent(this@MainActivity, MyService::class.java)
        service_intent.putExtra("operation", "delete")
        service_intent.putExtra("post", post)
        startService(service_intent)
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

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }
}