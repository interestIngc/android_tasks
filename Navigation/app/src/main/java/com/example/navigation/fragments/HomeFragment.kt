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

class HomeFragment : Fragment() {
    private var homeNumber : Int = 0
    private var homeNumberStr : String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeNumber = HomeFragmentArgs.fromBundle(requireArguments()).number
        homeNumberStr = HomeFragmentArgs.fromBundle(requireArguments()).numberStr
        view.findViewById<TextView>(R.id.home_number).text = homeNumberStr
        val bn = view.findViewById<Button>(R.id.home_button) as Button
        bn.setOnClickListener {
            val currNum = homeNumber + 1
            val currStr = "$homeNumberStr->${currNum}"
            navigate(HomeFragmentDirections.actionHomeFragmentSelf(currNum, currStr))
        }
    }
}