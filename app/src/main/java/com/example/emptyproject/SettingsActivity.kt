package com.example.emptyproject

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.RadioButton


const val TEXT_EDITOR_SETTINGS = "settings"
const val TEXT_SIZE = "textSize"
const val TEXT_COLOR = "textColor"

class SettingsActivity : AppCompatActivity() {

    var radioGroupTextSize: RadioGroup? = null
    var radioGroupTextColor: RadioGroup? = null
    var radioButtonColorBlack: RadioButton? = null
    var radioButtonColorRed: RadioButton? = null
    var radioButtonColorBlue: RadioButton? = null
    var radioButtonTextSmall: RadioButton? = null
    var radioButtonTextMiddle: RadioButton? = null
    var radioButtonTextLarge: RadioButton? = null
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initializeFields()

        sharedPreferences = getSharedPreferences(
            TEXT_EDITOR_SETTINGS,
            Context.MODE_PRIVATE)

        setCheckedTextColor()
        setCheckedTextSize()
        loadPreferencesTextSize()
    }

    override fun onPause() {
        super.onPause()
        radioGroupTextColor = findViewById(R.id.radio_group_text_color)
        radioGroupTextSize = findViewById(R.id.radio_group_text_size)

        val radioGroupTextColor = radioGroupTextColor ?: return
        val radioGroupTextSize = radioGroupTextSize ?: return

        val textSize = getCheckedRadioButton(radioGroupTextSize)
        val textColor = getCheckedRadioButton(radioGroupTextColor)

        sharedPreferences = getSharedPreferences(
            TEXT_EDITOR_SETTINGS,
            Context.MODE_PRIVATE)

        savePreferences(textSize, textColor)
    }

    private fun getCheckedRadioButton(radioGroup: RadioGroup): String {
        val checkedRadioButtonId: Int = radioGroup.checkedRadioButtonId
        val selectedRadioButton = findViewById<RadioButton>(checkedRadioButtonId)
        return selectedRadioButton.text.toString()
    }

    private fun savePreferences(textSize: String, textColor: String) {
        val e: SharedPreferences.Editor = sharedPreferences!!.edit()
        e.putString(TEXT_SIZE, textSize)
        e.putString(TEXT_COLOR, textColor)
        e.apply()
    }

    private fun loadPreferencesTextSize() : String? {
        return sharedPreferences?.getString(TEXT_SIZE, null)
    }

    private fun loadPreferencesTextColor() : String? {
        return sharedPreferences?.getString(TEXT_COLOR, null)
    }

    private fun setCheckedTextColor() {
        val checkedTextColor = loadPreferencesTextColor() ?: "Black"
        val textColors = hashMapOf(
            "Black" to radioButtonColorBlack,
            "Red" to radioButtonColorRed,
            "Blue" to radioButtonColorBlue
        )

        textColors.getValue(checkedTextColor)?.isChecked = true
    }

    private fun setCheckedTextSize() {
        val checkedTextSize = loadPreferencesTextSize() ?: "small"
        val textSizes = hashMapOf(
            "small" to radioButtonTextSmall,
            "middle" to radioButtonTextMiddle,
            "large" to radioButtonTextLarge
        )

        textSizes.getValue(checkedTextSize)?.isChecked = true
    }

    private fun initializeFields() {
        radioButtonColorBlack = findViewById(R.id.radio_button_black)
        radioButtonColorRed = findViewById(R.id.radio_button_red)
        radioButtonColorBlue = findViewById(R.id.radio_button_blue)
        radioButtonTextSmall = findViewById(R.id.radio_button_small)
        radioButtonTextMiddle = findViewById(R.id.radio_button_middle)
        radioButtonTextLarge = findViewById(R.id.radio_button_large)
    }
}
