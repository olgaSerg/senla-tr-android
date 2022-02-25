package com.example.emptyproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

private const val SAVED_VALUES = "savedValues"

class InformationActivity : AppCompatActivity() {
    var textViewFirstValue: TextView? = null
    var textViewSecondValue: TextView? = null
    var textViewThirdValue: TextView? = null
    var textViewFourthValue: TextView? = null
    var textViewFifthValue: TextView? = null
    var buttonSave: TextView? = null
    var buttonCalculator: TextView? = null
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
        val textViewCurrentValue = textViewCurrentValue ?: return
        val buttonSave = buttonSave?: return
        val buttonCalculator = buttonCalculator ?: return

        sharedPreferences = getSharedPreferences(
            SAVED_VALUES,
            Context.MODE_PRIVATE)

        textViewList = mutableListOf(
            textViewFirstValue,
            textViewSecondValue,
            textViewThirdValue,
            textViewFourthValue,
            textViewFifthValue
        )

        loadPreferences()

        buttonCalculator.setOnClickListener {
            val intent = Intent(this, CalculatorActivity::class.java)
            startActivityForResult(intent, 0)
        }

        buttonSave.setOnClickListener {
            for (i in 0..3) {
                textViewList[i].text = textViewList[i + 1].text
            }
            textViewList[4].text = textViewCurrentValue.text
            savePreferences()
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val textViewCurrentValue = textViewCurrentValue ?: return
        if (data == null) {
            return
        }
        val name = data.getStringExtra(CURRENT_VALUE)
        textViewCurrentValue.text = name
    }

    private fun loadPreferences() {
        val savedValues = sharedPreferences?.getString(SAVED_VALUES, null) ?: return
//        Log.v("msg", savedValues)
        val list = savedValues.split(", ")
        for (i in list.indices) {
            textViewList[i].text = list[i]
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
}