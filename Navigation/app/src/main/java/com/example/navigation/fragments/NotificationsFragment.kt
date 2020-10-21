package com.example.navigation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.navigation.R
import com.example.navigation.navigate

class NotificationsFragment : Fragment() {
    private var notsNumber : Int = 0
    private var notsNumberStr : String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notsNumber = NotificationsFragmentArgs.fromBundle(requireArguments()).number
        notsNumberStr = NotificationsFragmentArgs.fromBundle(requireArguments()).numberStr
        view.findViewById<TextView>(R.id.notsNumber).text = notsNumberStr
        val bn = view.findViewById<Button>(R.id.nots_button) as Button
        bn.setOnClickListener {
            val currNum = notsNumber + 1
            val currStr = "$notsNumberStr->${currNum}"
            navigate(NotificationsFragmentDirections.actionNotificationsFragmentSelf(currNum, currStr))
        }
    }
}