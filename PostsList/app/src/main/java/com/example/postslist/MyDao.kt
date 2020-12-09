package com.example.postslist

import androidx.room.*

@Dao
interface MyDao {
    @Insert
    fun insert(post : Post)

    @Delete
    fun deleteFromDao(post : Post)
}
