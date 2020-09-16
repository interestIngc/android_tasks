package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Button

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }
    lateinit var inputField : EditText
    lateinit var outputField : TextView
    var calc_expression : Double = 0.0
    var operation : String? = "="
    var buf : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inputField = findViewById(R.id.inputField)
        outputField = findViewById(R.id.outputField)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.i(TAG, "onSaveInstanceState")
        outState.putString("operation", operation)
        outState.putDouble("calc_expression", calc_expression)
        outState.putString("buf", buf)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i(TAG, "onRestoreInstanceState")
        calc_expression = savedInstanceState.getDouble("calc_expression")
        operation = savedInstanceState.getString("operation")
        buf = savedInstanceState.getString("buf").toString()
        outputField.text = calc_expression.toString()
    }
    fun is_number(num : String) : Boolean {
        var cnt = 0
        var cnt_nums = 0
        for (char in num) {
            if (char == '.') {
                cnt += 1
            } else if (char < '0' || char > '9') {
                return false
            } else {
                cnt_nums += 1
            }
        }
        if (cnt_nums > 0 && (cnt == 1 && cnt_nums + 1 == num.length || cnt == 0)) {
            return true
        }
        return false
    }

    fun onOperationClick(view : View) {
        val op : String = (view as Button).text.toString()
        if (buf.isNotEmpty() && !is_number(buf) || buf.isEmpty() && operation != "=") {
            outputField.text = getString(R.string.error)
            inputField.setText("")
            return
        }
        if (buf.isNotEmpty()) {
            calc(buf.toDouble())
        }
        buf = ""
        if (op == "=") {
            outputField.setText(calc_expression.toString())
            inputField.setText("")
        } else {
            inputField.append(" " + op + " ")
        }
        operation = op
    }

    fun calc(value : Double) {
        if (operation == "=") {
            calc_expression = value
            return
        }
        when (operation) {
            "+" -> {calc_expression += value}
            "-" -> {calc_expression -= value}
            "*" -> {calc_expression *= value}
            "/" -> {calc_expression /= value}
            else -> return
        }
    }

    fun clickNumber(view : View) {
        buf += (view as Button).text.toString()
        inputField.append(view.text.toString())
    }
    fun onDelete(view: View) {
        calc_expression = 0.0
        operation = "="
        inputField.setText("")
        outputField.setText("")
        buf = ""
    }
}