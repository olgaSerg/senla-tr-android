package com.example.emptyproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class EditFileActivity : AppCompatActivity(), EditFileFragment.OnRefreshFilesListListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_file)

        val fileName = intent.getStringExtra(MainActivity.FILE_NAME)
        val isNewFile = intent.getBooleanExtra(MainActivity.IS_NEW_FILE, false)
        val editFileFragment = EditFileFragment.newInstance(isNewFile, fileName)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_edit_file_container, editFileFragment)
            commit()
        }
    }

    override fun onRefreshFilesList() {
    }
}