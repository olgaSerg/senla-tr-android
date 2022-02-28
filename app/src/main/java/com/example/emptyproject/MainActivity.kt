package com.example.emptyproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.io.File

class MainActivity : AppCompatActivity() {
    private var buttonCreate: Button? = null
    private var buttonRead: Button? = null
    private var buttonEdit: Button? = null
    private var buttonSettings: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeFields()

        val buttonCreate = buttonCreate ?: return
        val buttonRead = buttonRead ?: return
        val buttonEdit = buttonEdit ?: return
        val buttonSettings = buttonSettings ?: return

        setListeners(buttonCreate, buttonSettings, buttonRead, buttonEdit)
    }

    private fun initializeFields() {
        buttonCreate = findViewById(R.id.button_create)
        buttonRead = findViewById(R.id.button_read)
        buttonEdit = findViewById(R.id.button_edit)
        buttonSettings = findViewById(R.id.button_setting)
    }

    private fun setListeners(buttonCreate: Button, buttonSettings: Button, buttonRead: Button, buttonEdit: Button) {
        buttonCreate.setOnClickListener {
            val intent = Intent(this, CreateFileActivity::class.java)
            startActivity(intent)
        }

        buttonSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        buttonRead.setOnClickListener {
            val intent = Intent(this, ReadFileActivity::class.java)
            startActivity(intent)
        }

        buttonEdit.setOnClickListener {
            val intent = Intent(this, EditFileActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        val buttonRead = buttonRead ?: return
        val buttonEdit = buttonEdit ?: return

        if (File(applicationContext.filesDir, FILENAME).exists()) {
            buttonRead.isEnabled = true
            buttonEdit.isEnabled = true
        }
    }
}
