package com.example.postslist

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditTextActivity : AppCompatActivity() {

    private var resp : Call<Post>? = null
    private lateinit var myTitle : EditText

    private lateinit var myBody : EditText
    private lateinit var button : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("create", "created")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_edit_text)
        myTitle = findViewById<EditText>(R.id.my_title) as EditText
        myBody = findViewById<EditText>(R.id.my_body) as EditText
        button = findViewById<Button>(R.id.submit) as Button
        button.setOnClickListener {
            val title = myTitle.text.toString()
            val body = myBody.text.toString()
            Log.i("make_post", "${title.length}, ${body.length}")
            post(title, body)
            finish()
        }
    }


    private fun post(title : String, body : String) {
        resp = MyApp.instance.myApi.doPost(title, body, 1)
        resp?.enqueue(object : Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                Log.i("post", "error while posting occured", t)
            }
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                Log.i("response", "${response.body()}")
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@EditTextActivity,
                        "${response.body()} \nPosting was successful!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(this@EditTextActivity, "can't connect to the API", Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}