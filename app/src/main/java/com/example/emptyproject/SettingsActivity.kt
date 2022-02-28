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

    private var radioGroupTextSize: RadioGroup? = null
    private var radioGroupTextColor: RadioGroup? = null
    private var radioButtonColorBlack: RadioButton? = null
    private var radioButtonColorRed: RadioButton? = null
    private var radioButtonColorBlue: RadioButton? = null
    private var radioButtonTextSmall: RadioButton? = null
    private var radioButtonTextMiddle: RadioButton? = null
    private var radioButtonTextLarge: RadioButton? = null
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initializeFields()
        getObjectSharedPreferences()
        setCheckedTextColor()
        setCheckedTextSize()
    }

    override fun onPause() {
        super.onPause()
        val radioGroupTextColor = radioGroupTextColor ?: return
        val radioGroupTextSize = radioGroupTextSize ?: return

        val textSize = getCheckedRadioButton(radioGroupTextSize)
        val textColor = getCheckedRadioButton(radioGroupTextColor)

        savePreferences(textSize, textColor)
    }

    private fun initializeFields() {
        radioButtonColorBlack = findViewById(R.id.radio_button_black)
        radioButtonColorRed = findViewById(R.id.radio_button_red)
        radioButtonColorBlue = findViewById(R.id.radio_button_blue)
        radioButtonTextSmall = findViewById(R.id.radio_button_small)
        radioButtonTextMiddle = findViewById(R.id.radio_button_middle)
        radioButtonTextLarge = findViewById(R.id.radio_button_large)
        radioGroupTextColor = findViewById(R.id.radio_group_text_color)
        radioGroupTextSize = findViewById(R.id.radio_group_text_size)
    }

    private fun savePreferences(textSize: String, textColor: String) {
        val e: SharedPreferences.Editor = sharedPreferences!!.edit()
        e.putString(TEXT_SIZE, textSize)
        e.putString(TEXT_COLOR, textColor)
        e.apply()
    }

    private fun setCheckedTextColor() {
        val checkedTextColor = loadPreferencesTextColor() ?: getString(R.string.black)
        val textColors = hashMapOf(
            getString(R.string.black) to radioButtonColorBlack,
            getString(R.string.red) to radioButtonColorRed,
            getString(R.string.blue) to radioButtonColorBlue
        )

        textColors.getValue(checkedTextColor)?.isChecked = true
    }

    private fun setCheckedTextSize() {
        val checkedTextSize = loadPreferencesTextSize() ?: getString(R.string.small)
        val textSizes = hashMapOf(
            getString(R.string.small) to radioButtonTextSmall,
            getString(R.string.middle) to radioButtonTextMiddle,
            getString(R.string.large) to radioButtonTextLarge
        )

        textSizes.getValue(checkedTextSize)?.isChecked = true
    }

    private fun getObjectSharedPreferences() {
        sharedPreferences = getSharedPreferences(
            TEXT_EDITOR_SETTINGS,
            Context.MODE_PRIVATE)
    }

    private fun loadPreferencesTextSize() : String? {
        return sharedPreferences?.getString(TEXT_SIZE, null)
    }

    private fun loadPreferencesTextColor() : String? {
        return sharedPreferences?.getString(TEXT_COLOR, null)
    }

    private fun getCheckedRadioButton(radioGroup: RadioGroup): String {
        val checkedRadioButtonId: Int = radioGroup.checkedRadioButtonId
        val selectedRadioButton = findViewById<RadioButton>(checkedRadioButtonId)
        return selectedRadioButton.text.toString()
    }
}
