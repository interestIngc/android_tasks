package com.example.contactslist

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import ru.ifmo.ctddev.startsev.demo.fetchAllContacts
import java.security.Permission
import java.security.Permissions

class MainActivity : AppCompatActivity() {
    companion object {
        const val MyRequestId = 2
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.READ_CONTACTS),
                    MyRequestId)
        } else {
            show_contacts()
        }
    }


    fun show_contacts() {
        val viewManager = LinearLayoutManager(this)
        recycler_view.apply {
            layoutManager = viewManager
            val curr_adapter = ContactAdapter(fetchAllContacts()) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${it.phoneNumber}"))
                startActivity(intent)
            }
            adapter = curr_adapter
            Toast.makeText(
                this@MainActivity, resources.getQuantityString(
                    R.plurals.numberOfContacts, curr_adapter.itemCount,
                    curr_adapter.itemCount
                ), Toast.LENGTH_LONG
            ).show()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MyRequestId -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    show_contacts()
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }
}