package com.example.emptyproject

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Activity) {
    companion object {
        const val SETTINGS = "settings"
        const val CHECKED_RADIO_BUTTON = "checked_button"
    }

    private val sharedPreferences by lazy { context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE) }

    fun savePreferences(checkedRadioButton: String) {
        val e: SharedPreferences.Editor = sharedPreferences.edit()
        e.putString(CHECKED_RADIO_BUTTON, checkedRadioButton)
        e.apply()
    }

    fun loadPreferencesSelectedRadioButton(): String {
        return sharedPreferences.getString(CHECKED_RADIO_BUTTON, null)
            ?: RegexpMode.SPACES_TO_HYPHENS
    }
}