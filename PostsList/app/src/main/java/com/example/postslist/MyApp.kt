package com.example.postslist

import android.app.Application
import androidx.room.Room

class MyApp : Application() {
    companion object {
        lateinit var instance : MyApp
            private set
    }

    lateinit var myApi : MyApi
        private set

    lateinit var myDb: PostDatabase

    override fun onCreate() {
        super.onCreate()
        myApi = makeApi()
        myDb = Room.databaseBuilder(this, PostDatabase::class.java, "database").build()
        instance = this
    }


}