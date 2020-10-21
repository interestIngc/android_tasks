package com.example.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.example.navigation.fragments.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private var current_nav_controller : LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            setUpBottomNavigation()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (current_nav_controller?.value?.navigateUp() == false) {
            finish()
        }
        return true
    }

    private fun setUpBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navGraphIds = listOf(R.navigation.home,
            R.navigation.dash,
            R.navigation.notifications)
        val controller = bottomNavigationView.setupWithNavController(navGraphIds = navGraphIds,
        fragmentManager = supportFragmentManager, containerId = R.id.my_nav_host_fragment,
        intent = intent)
        current_nav_controller = controller
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setUpBottomNavigation()
    }

}