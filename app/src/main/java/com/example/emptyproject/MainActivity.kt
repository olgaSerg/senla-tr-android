package com.example.emptyproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import java.io.File

const val IS_NEW_FILE = "isNewFile"
const val FILE_NAME = "fileName"

class MainActivity : AppCompatActivity() {
    private var filesListView : ListView? = null
    private var buttonCreate: Button? = null
    private var adapter: ArrayAdapter<FileObject>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        filesListView = findViewById(R.id.list_view)
        buttonCreate = findViewById(R.id.button_create)
        val filesListView = filesListView ?: return

        createFilesDirectory()

        setClickListener()
        val filesList = getFilesList()
        setFilesListAdapter(filesList, filesListView)
        setItemClickListener(filesListView)
    }

    override fun onResume() {
        super.onResume()
        if (filesListView != null) {
            val filesList = getFilesList()
            setFilesListAdapter(filesList, filesListView!!)
        }
    }

    private fun setClickListener() {
        val buttonCreate = buttonCreate ?: return
        buttonCreate.setOnClickListener {
            val intent = Intent(this, EditFileActivity::class.java)
            intent.putExtra(IS_NEW_FILE, true)
            startActivity(intent)
        }
    }

    private fun createFilesDirectory() {
        val file = File(filesDir, "documents")
        file.mkdir()
    }

    private fun getFilesList(): ArrayList<FileObject> {
        val subDirectory = "documents"
        val directory = File(filesDir, subDirectory)
        return Utils.getDirectoryFiles(directory)
    }

    private fun setFilesListAdapter(filesList: ArrayList<FileObject>, filesListView: ListView) {
        adapter = FilesListAdapter(this, filesList)
        filesListView.adapter = adapter
    }

    private fun setItemClickListener(filesListView: ListView) {
        filesListView.setOnItemClickListener { listView, itemView, position, id ->
            val currentFile: FileObject = adapter!!.getItem(position)!!
            val intent = Intent(
                this@MainActivity,
                EditFileActivity::class.java
            )
            intent.putExtra(IS_NEW_FILE, false)
            intent.putExtra(FILE_NAME, currentFile.name)
            startActivity(intent)
        }
    }
}
