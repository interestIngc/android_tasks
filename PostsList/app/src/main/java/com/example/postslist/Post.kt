package com.example.postslist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(val userId : Int?, val id : Long?, val title : String?, val body : String?) :
    Parcelable