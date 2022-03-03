package com.example.emptyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream

class EditFileActivity : AppCompatActivity() {
    private var editTextCreateFile: EditText? = null
    private var fileName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_file)
        
        editTextCreateFile = findViewById(R.id.edit_text_create_file)
        val editTextCreateFile = editTextCreateFile ?: return

        val isNewFile = intent.getBooleanExtra(IS_NEW_FILE, false)
        if (!isNewFile) {
            fileName = intent.getStringExtra(FILE_NAME) ?: return
            showText(editTextCreateFile, fileName!!)
        }
    }

    override fun onPause() {
        super.onPause()
        editTextCreateFile?.let { saveFile(it) }
    }

    private fun openFile(fileName:String): String {
        return File(applicationContext.filesDir.path, "/documents/$fileName")
            .bufferedReader()
            .use { it.readText(); }
    }

    private fun showText(editText: EditText, fileName: String) {
        val textFromFile = openFile(fileName)

        editText.setText(textFromFile)
    }

    private fun saveFile(editText: EditText) {
        var newText = editText.text.toString()
        val linesArray = newText.lines().toMutableList()

        var newFileName = createFileName(linesArray)

        newText = linesArray.joinToString(separator = "\n")
        val path = applicationContext.filesDir.absolutePath
        val file = File(path, "/documents/${newFileName}.txt")
        FileOutputStream(file).use {
            it.write(newText.toByteArray())
        }

        if (fileName != null) {
            deleteOldFile(path, file)
        }

        Toast.makeText(applicationContext, getString(R.string.file_saved), Toast.LENGTH_SHORT).show()
    }

    private fun createFileName(linesArray: MutableList<String>): String {
        var newFileName = linesArray[0]
        newFileName = newFileName.trim()
        if (newFileName.isEmpty()) {
            newFileName = getString(R.string.empty_doc_name)
        }

        val filesSet = createFilesSet()

        if (filesSet.contains("$newFileName.txt")) {
            var fileNumber = 1
            while (filesSet.contains("$newFileName ($fileNumber).txt")) {
                fileNumber++
            }
            newFileName = "$newFileName ($fileNumber)"
        }

        linesArray[0] = newFileName

        return newFileName
    }

    private fun createFilesSet(): MutableSet<String>{
        val filesList = Utils.getDirectoryFiles(File(filesDir, "documents"))
        val filesSet = mutableSetOf<String>()
        for (fileObject in filesList) {
            if (fileObject.name != fileName) {
                filesSet.add(fileObject.name)
            }
        }
        return filesSet
    }

    private fun deleteOldFile(path: String, file: File) {
        val oldFile = File(path, "/documents/${fileName}")
        if (file.path != oldFile.path) {
            oldFile.delete()
        }
    }
}