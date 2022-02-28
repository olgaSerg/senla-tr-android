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
    private var textViewFile: EditText? = null
    private var editTextFile: EditText? = null
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_file)

        textViewFile = findViewById(R.id.edit_text_file)
        val textViewFile = textViewFile ?: return

        getObjectSharedPreferences()
        getNewTextColor()
        getNewTextSize()

        val newTextColor = getNewTextColor()
        val newTextSize = getNewTextSize()

        showText(textViewFile, newTextSize, newTextColor)
    }
    override fun onPause() {
        super.onPause()
        editTextFile = findViewById(R.id.edit_text_file)
        val editTextFile = editTextFile ?: return
        saveFile(editTextFile)
    }

    private fun getObjectSharedPreferences() {
        sharedPreferences = getSharedPreferences(
            TEXT_EDITOR_SETTINGS,
            Context.MODE_PRIVATE)
    }

    private fun saveFile(editText: EditText) {
        applicationContext.openFileOutput(FILENAME, Context.MODE_PRIVATE).use {
            it.write(editText.text.toString().toByteArray())
        }
        Toast.makeText(applicationContext, getString(R.string.file_saved), Toast.LENGTH_SHORT).show()
    }

    private fun openFile(): String {
        return File(applicationContext.filesDir, FILENAME)
                .bufferedReader()
                .use { it.readText(); }
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

        textView.text = textFromFile
        textView.textSize = textSize
        textView.setTextColor(textColor)
    }
}
