package com.example.emptyproject

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast

const val FILENAME = "data.txt"

class CreateFileActivity : AppCompatActivity() {
    var editTextCreateFile: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_file)
    }

    override fun onPause() {
        super.onPause()
        editTextCreateFile = findViewById(R.id.edit_text_create_file)
        editTextCreateFile?.let { saveFile(it) }
    }

    private fun saveFile(editText: EditText) {
        applicationContext.openFileOutput(FILENAME, Context.MODE_PRIVATE).use {
            it.write(editText.text.toString().toByteArray())
        }
        Toast.makeText(applicationContext, "Файл сохранён", Toast.LENGTH_SHORT).show()
    }
}