package com.example.emptyproject

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class EditFileActivity : AppCompatActivity(), EditFileFragment.OnRefreshFilesListListener {

    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish()
            return
        }

        setContentView(R.layout.activity_edit_file)

        toolbar = findViewById(R.id.toolbar_edit_file)
        val toolbar = toolbar ?: return

        toolbar.title = getString(R.string.edit_title)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
//        return true
//    }
}