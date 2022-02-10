package com.example.emptyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.awt.font.TextAttribute

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val editTextFirstNumber = findViewById<EditText>(R.id.edit_text_first_number)
        val editTextSecondNumber = findViewById<EditText>(R.id.edit_text_second_number)
        val textViewResult = findViewById<TextView>(R.id.text_view_result)
        val button = findViewById<Button>(R.id.button_sum)

        button.setOnClickListener {
            if (editTextFirstNumber.text.toString().isNotEmpty() && editTextSecondNumber.text.toString().isNotEmpty()) {
                val firstNumber = editTextFirstNumber.text.toString().toInt()
                val secondNumber = editTextSecondNumber.text.toString().toInt()
                textViewResult.text = sum(firstNumber, secondNumber).toString()

            }
        }
        editTextFirstNumber.setOnClickListener {
            editTextFirstNumber.text = null
        }
        editTextSecondNumber.setOnClickListener {
            editTextSecondNumber.text = null
        }
    }
    private fun sum(a: Int, b: Int): Int {
        return a + b
    }
}

