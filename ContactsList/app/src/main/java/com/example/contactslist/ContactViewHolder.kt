package com.example.contactslist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*
import ru.ifmo.ctddev.startsev.demo.Contact

class ContactViewHolder(val root : View) : RecyclerView.ViewHolder(root) {
    fun bind(contact : Contact) {
        root.name.text = contact.name;
        root.phoneNumber.text = contact.phoneNumber;
    }
}