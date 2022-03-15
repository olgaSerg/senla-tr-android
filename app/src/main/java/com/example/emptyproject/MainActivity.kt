package com.example.emptyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.content.Intent

class MainActivity : AppCompatActivity(), ListFragment.OnFragmentSendDataListener, EditFileFragment.OnRefreshFilesListListener {
    private var lastOpenFileName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_list_container, ListFragment.newInstance())
            commit()
        }
    }

    override fun onEditFile(fileName: String) {
        if (fileName != lastOpenFileName) {
            editFile(false, fileName)
            if (findViewById<FrameLayout>(R.id.layout_land) != null) {
                lastOpenFileName = fileName
            }
        }
    }

    override fun onCreateFile() {
        editFile(true, null)
        lastOpenFileName = null
    }

    override fun onRefreshFilesList() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_list_container, ListFragment.newInstance())
            commit()
        }
    }

    private fun editFile(isNewFile: Boolean, fileName: String?) {
        if (isLandscapeLayout()) {
            val editFileFragment = EditFileFragment.newInstance(isNewFile, fileName)
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_edit_file_container, editFileFragment)
                addToBackStack(null)
                commit()
            }
        } else {
            val intent = Intent(this, EditFileActivity::class.java)
            intent.putExtra(IS_NEW_FILE, isNewFile)
            intent.putExtra(FILE_NAME, fileName)
            startActivity(intent)
        }
    }

    private fun isLandscapeLayout() : Boolean {
        return findViewById<FrameLayout>(R.id.layout_land) != null
    }

    companion object {
        const val IS_NEW_FILE = "isNewFile"
        const val FILE_NAME = "fileName"
        const val DIRECTORY = "documents"
    }
}