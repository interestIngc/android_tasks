package com.example.images_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter(private val users : List<Image>, private val onClick : (Image) -> Unit) :
        RecyclerView.Adapter<ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val holder = ImageViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false))
        holder.root.setOnClickListener {
            onClick(users[holder.adapterPosition])
        }
        return holder
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) = holder.bind(users[position])
}