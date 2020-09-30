package com.example.contactslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ifmo.ctddev.startsev.demo.Contact

class ContactAdapter(private val users : List<Contact>, private val onClick : (Contact) -> Unit) :
        RecyclerView.Adapter<ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val holder = ContactViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false))
        holder.root.setOnClickListener {
            onClick(users[holder.adapterPosition])
        }
        return holder
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) = holder.bind(users[position])
}