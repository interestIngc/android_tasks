package com.example.navigation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.navigation.R
import com.example.navigation.navigate

class DashFragment : Fragment() {
    private var dashNumber : Int = 0
    private var dashNumberStr : String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashNumber = DashFragmentArgs.fromBundle(requireArguments()).number
        dashNumberStr = DashFragmentArgs.fromBundle(requireArguments()).numberStr
        view.findViewById<TextView>(R.id.dashNumber).text = dashNumberStr
        val bn = view.findViewById<Button>(R.id.dash_button) as Button
        bn.setOnClickListener {
            val currNum = dashNumber + 1
            val currStr = "$dashNumberStr->$currNum"
            navigate(DashFragmentDirections.actionDashFragmentSelf(currNum, currStr))
        }
    }

}