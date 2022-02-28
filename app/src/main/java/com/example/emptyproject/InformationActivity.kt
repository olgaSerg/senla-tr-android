package com.example.emptyproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

private const val SAVED_VALUES = "savedValues"

class InformationActivity : AppCompatActivity() {
    private var textViewFirstValue: TextView? = null
    private var textViewSecondValue: TextView? = null
    private var textViewThirdValue: TextView? = null
    private var textViewFourthValue: TextView? = null
    private var textViewFifthValue: TextView? = null
    private var buttonSave: Button? = null
    private var buttonCalculator: Button? = null
    private var textViewCurrentValue: TextView? = null
    private var sharedPreferences: SharedPreferences? = null
    private var textViewList = mutableListOf<TextView>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.information)

        initializeFields()

        val textViewFirstValue = textViewFirstValue ?: return
        val textViewSecondValue = textViewSecondValue ?: return
        val textViewThirdValue = textViewThirdValue ?: return
        val textViewFourthValue = textViewFourthValue ?: return
        val textViewFifthValue = textViewFifthValue ?: return
        val buttonSave = buttonSave?: return
        val buttonCalculator = buttonCalculator ?: return

        textViewList = mutableListOf(
            textViewFirstValue,
            textViewSecondValue,
            textViewThirdValue,
            textViewFourthValue,
            textViewFifthValue
        )

        getObjectSharedPreferences()
        setClickListeners(buttonCalculator, buttonSave)
        loadPreferences()
    }

    private fun initializeFields() {
        textViewFirstValue = findViewById(R.id.text_view_first_value)
        textViewSecondValue = findViewById(R.id.text_view_second_value)
        textViewThirdValue = findViewById(R.id.text_view_third_value)
        textViewFourthValue = findViewById(R.id.text_view_fourth_value)
        textViewFifthValue = findViewById(R.id.text_view_fifth_value)
        textViewCurrentValue = findViewById(R.id.text_view_current_value)
        buttonSave = findViewById(R.id.button_save)
        buttonCalculator = findViewById(R.id.button_calculator)
    }

    private fun getObjectSharedPreferences() {
        sharedPreferences = getSharedPreferences(
            SAVED_VALUES,
            Context.MODE_PRIVATE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val textViewCurrentValue = textViewCurrentValue ?: return
            if (data == null) {
                return
            }
            val name = data.getStringExtra(CURRENT_VALUE)
            textViewCurrentValue.text = name
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    private fun setClickListeners(buttonCalculator: Button, buttonSave: Button) {
        buttonCalculator.setOnClickListener {
            val intent = Intent(this, CalculatorActivity::class.java)
            startActivityForResult(intent, 0)
        }
        buttonSave.setOnClickListener {
            for (i in 0..3) {
                textViewList[i].text = textViewList[i + 1].text
            }
            if (textViewCurrentValue != null) {
                textViewList[4].text = textViewCurrentValue!!.text
                savePreferences()
            } else {
                return@setOnClickListener
            }
        }
    }

    private fun savePreferences() {
        val savedValuesList = arrayListOf<String>()
        for (i in 0 until textViewList.size) {
            savedValuesList.add(textViewList[i].text.toString())
        }
        sharedPreferences?.let {
            val e: SharedPreferences.Editor = it.edit()
            e.putString(SAVED_VALUES, savedValuesList.joinToString())
            e.apply()
        }
    }

    private fun loadPreferences() {
        val savedValues = sharedPreferences?.getString(SAVED_VALUES, null) ?: return
//        Log.v("msg", savedValues)
        val list = savedValues.split(", ")
        for (i in list.indices) {
            textViewList[i].text = list[i]
        }
    }
}