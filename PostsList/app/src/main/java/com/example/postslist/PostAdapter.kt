package com.example.postslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class PostAdapter(private val users : MutableList<Post>, private val onClick : (Post) -> Unit) :
    RecyclerView.Adapter<PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val holder = PostViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false))
        holder.root.delete.setOnClickListener {
            onClick(users[holder.adapterPosition])
            users.removeAt(holder.adapterPosition)
            notifyDataSetChanged()
        }
        return holder
    }

    internal fun addItem(post : Post) {
        users.add(post)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) = holder.bind(users[position])

}