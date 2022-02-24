package com.example.emptyproject

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import java.io.File

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

        val textSizes = hashMapOf(
            "small" to 14F,
            "middle" to 24F,
            "large" to 48F
        )

        val textColors = hashMapOf(
            "Black" to "#FF000000",
            "Red" to "#FF0000",
            "Blue" to "#001FCA"
        )

        val savedTextColor = loadPreferencesTextColor()
        val savedTextSize = loadPreferencesTextSize()
        var newTextColor = Color.parseColor("#FF000000")
        var newTextSize = 14F

        if (savedTextSize != null) {
            newTextSize = textSizes.getValue(savedTextSize)
        }

        if (savedTextColor != null) {
            newTextColor = Color.parseColor(textColors.getValue(savedTextColor))
        }
        openFile(textViewFile, newTextSize, newTextColor)
    }

    private fun openFile(textView: TextView, textSize:Float, textColor: Int) {
        val textFromFile =
            File(applicationContext.filesDir, FILENAME)
                .bufferedReader()
                .use { it.readText(); }

        val textLines = textFromFile.lines()
        val numberedLines = mutableListOf<String>()
        for (i in textLines.indices) {
            numberedLines.add("${i + 1}. ${textLines[i]}")
        }
        val textToShow = numberedLines.joinToString(separator = "\n")


        textView.text = textToShow
        textView.textSize = textSize
        textView.setTextColor(textColor)
    }

    private fun loadPreferencesTextSize() : String? {
        return sharedPreferences?.getString(TEXT_SIZE, null)
    }

    private fun loadPreferencesTextColor() : String? {
        return sharedPreferences?.getString(TEXT_COLOR, null)
    }
}
