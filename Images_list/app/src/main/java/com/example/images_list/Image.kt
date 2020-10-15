package com.example.images_list

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(val description: String?, val url: String?) : Parcelable
