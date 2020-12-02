package com.example.postslist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class PostViewHolder(val root : View) : RecyclerView.ViewHolder(root) {
    fun bind(post : Post) {
        root.title.text = post.title
        root.body.text = post.body
    }
}