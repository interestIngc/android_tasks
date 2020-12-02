package com.example.postslist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private var call : Call<List<Post>>? = null

    private var list : List<Post>? = null

    private lateinit var add : Button

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
            startActivity(intent)
        }
    }


    private fun run() {
        call = MyApp.instance.myApi.get()
        call?.enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    list = body
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
        val postId = post.id
        val resp = MyApp.instance.myApi.delete(post.id)
        Toast.makeText(this@MainActivity, "${resp}; post with id $postId deleted!", Toast.LENGTH_LONG).show()
    }



    private fun displayPosts(result : List<Post>?) {
        if (result == null) {
            return
        }
        val viewManager = LinearLayoutManager(this@MainActivity)
        recycle_view.apply {
            layoutManager = viewManager
            adapter = PostAdapter(result) {
                delete(it)
            }
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