package com.example.images_list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class ImageViewHolder(val root : View) : RecyclerView.ViewHolder(root) {
    fun bind(image: Image) {
        root.description.text = image.description;
        root.url.text = image.url;
    }
}