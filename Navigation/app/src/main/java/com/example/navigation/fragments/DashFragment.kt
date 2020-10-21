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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_dash, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashNumber = DashFragmentArgs.fromBundle(requireArguments()).number
        view.findViewById<TextView>(R.id.dashNumber).text = dashNumber.toString()
        val bn = view.findViewById<Button>(R.id.dash_button) as Button
        bn.setOnClickListener {
            navigate(DashFragmentDirections.actionDashFragmentSelf(dashNumber + 1))
        }
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putInt("dashNumber", dashNumber)
//    }
}