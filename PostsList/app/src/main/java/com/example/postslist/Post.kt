package com.example.postslist

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Post(val userId : Int?, @PrimaryKey val id : Long?, val title : String?, val body : String?) :
    Parcelable