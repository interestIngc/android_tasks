package com.example.postslist

import android.app.Application

class MyApp : Application() {
    companion object {
        lateinit var instance : MyApp
            private set
    }

    lateinit var myApi : MyApi
        private set

    override fun onCreate() {
        super.onCreate()
        myApi = makeApi()
        instance = this
    }
}