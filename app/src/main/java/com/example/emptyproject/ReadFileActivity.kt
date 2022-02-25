package com.example.emptyproject

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import java.io.File
import java.lang.StringBuilder

class ReadFileActivity : AppCompatActivity() {
    var textViewFile: TextView? = null
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.read_file)

        textViewFile = findViewById(R.id.text_view_file)
        val textViewFile = textViewFile ?: return

        sharedPreferences = getSharedPreferences(
            TEXT_EDITOR_SETTINGS,
            Context.MODE_PRIVATE)

        val savedTextColor = loadPreferencesTextColor()
        val savedTextSize = loadPreferencesTextSize()
        var newTextColor = Color.parseColor("#FF000000")
        var newTextSize = 14F

        if (savedTextSize != null) {
            newTextSize = getTextSize(savedTextSize)
        }

        if (savedTextColor != null) {
            newTextColor = getTextColor(savedTextColor)
        }
        showText(textViewFile, newTextSize, newTextColor)
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
            "Black" to "#FF000000",
            "Red" to "#FF0000",
            "Blue" to "#001FCA"
        )

        return Color.parseColor(textColors.getValue(color))
    }

    private fun getTextSize(textSize: String): Float {
        val textSizes = hashMapOf(
            "small" to 14F,
            "middle" to 24F,
            "large" to 48F
        )

        return textSizes.getValue(textSize)
    }

    private fun showText(textView: TextView, textSize: Float, textColor: Int) {
        val textFromFile = openFile()
        val newText = StringBuilder("1. $textFromFile")
        var lineNumber = 1

        for (i in textFromFile.indices) {
            if (textFromFile[i] == '\n') {
                lineNumber++
            }
        }
        for (i in newText.length - 1 downTo 0) {
            if (newText[i] == '\n') {
                newText.insert(i + 1, "$lineNumber. ")
                lineNumber--
            }
        }

        textView.text = newText
        textView.textSize = textSize
        textView.setTextColor(textColor)
    }
}