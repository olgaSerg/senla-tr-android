package com.example.emptyproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class EditFileActivity : AppCompatActivity(), EditFileFragment.OnRefreshFilesListListener {

    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_file)

        setToolbar()
        loadEditFileFragment()
    }

    private fun setToolbar() {
        toolbar = findViewById(R.id.toolbar_edit_file)
        val toolbar = toolbar ?: return

        toolbar.title = getString(R.string.edit_title)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun loadEditFileFragment() {
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}