package com.example.postslist

import android.app.Activity
import android.content.Intent
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
            sendData(title, body)
            finish()
        }
    }

    private fun sendData(title : String, body : String) {
        val data : Intent = Intent()
        data.putExtra("title", title)
        data.putExtra("body", body)
        setResult(Activity.RESULT_OK, data)
    }

}