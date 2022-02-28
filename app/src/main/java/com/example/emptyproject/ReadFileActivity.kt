package com.example.emptyproject

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.widget.TextView
import java.io.File
import java.lang.StringBuilder

class ReadFileActivity : AppCompatActivity() {
    private var textViewFile: TextView? = null
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.read_file)

        textViewFile = findViewById(R.id.text_view_file)
        val textViewFile = textViewFile ?: return

        getObjectSharedPreferences()

        val newTextColor = getNewTextColor()
        val newTextSize = getNewTextSize()

        showText(textViewFile, newTextSize, newTextColor)
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

    private fun openFile(): String {
        return File(applicationContext.filesDir, FILENAME)
                .bufferedReader()
                .use { it.readText(); }
    }

    private fun getTextColor(color: String): Int {
        val textColors = hashMapOf(
            getString(R.string.black) to getColor(R.color.black),
            getString(R.string.red) to getColor(R.color.red),
            getString(R.string.blue) to getColor(R.color.blue)
        )

        return textColors.getValue(color)
    }

    private fun getTextSize(textSize: String): Float {
        val textSizes = hashMapOf(
            getString(R.string.small) to resources.getDimension(R.dimen.text_small_size),
            getString(R.string.middle) to resources.getDimension(R.dimen.text_middle_size),
            getString(R.string.large) to resources.getDimension(R.dimen.text_large_size)
        )

        return textSizes.getValue(textSize)
    }

    private fun getNewTextColor(): Int {
        val savedTextColor = loadPreferencesTextColor()

        return if (savedTextColor != null) {
            getTextColor(savedTextColor)
        } else
            getColor(R.color.black)
    }

    private fun getNewTextSize(): Float {
        val savedTextSize = loadPreferencesTextSize()

        return if (savedTextSize != null) {
            getTextSize(savedTextSize)
        } else {
            resources.getDimension(R.dimen.text_small_size)
        }
    }

    private fun showText(textView: TextView, textSize: Float, textColor: Int) {
        val textFromFile = openFile()
        val newText = StringBuilder()
        textFromFile.lines().forEachIndexed{ index, s -> newText.append("${index + 1}. $s\n") }

        textView.text = newText
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        textView.setTextColor(textColor)
    }
}