package com.example.emptyproject

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.io.File

class EditFileActivity : AppCompatActivity() {
    var textViewFile: TextView? = null
    var editTextFile: EditText? = null
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_file)

        textViewFile = findViewById(R.id.edit_text_file)
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

        openFile(textViewFile, newTextSize, newTextColor)

    }
    override fun onPause() {
        super.onPause()
        editTextFile = findViewById(R.id.edit_text_file)
        val editTextFile = editTextFile ?: return
        saveFile(editTextFile)
    }

    private fun saveFile(editText: EditText) {
        applicationContext.openFileOutput(FILENAME, Context.MODE_PRIVATE).use {
            it.write(editText.text.toString().toByteArray())
        }
        Toast.makeText(applicationContext, "Файл сохранён", Toast.LENGTH_SHORT).show()
    }

    private fun openFile(textView: TextView, textSize:Float, textColor: Int) {
        val textFromFile =
            File(applicationContext.filesDir, FILENAME)
                .bufferedReader()
                .use { it.readText(); }
        textView.text = textFromFile
        textView.textSize = textSize
        textView.setTextColor(textColor)
    }

    private fun loadPreferencesTextSize() : String? {
        return sharedPreferences?.getString(TEXT_SIZE, null)
    }

    private fun loadPreferencesTextColor() : String? {
        return sharedPreferences?.getString(TEXT_COLOR, null)
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
}
