package com.example.emptyproject

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream

class EditFileFragment : Fragment(R.layout.fragment_edit_file) {

    private var saveDataListener: OnRefreshFilesListListener? = null
    private var editTextCreateFile: EditText? = null
    private var fileName: String? = null

    interface OnRefreshFilesListListener {
        fun onRefreshFilesList()
    }

    companion object {
        fun newInstance(isNewFile: Boolean, fileName: String?): EditFileFragment {
            val args = Bundle()
            args.putBoolean(MainActivity.IS_NEW_FILE, isNewFile)
            args.putString(MainActivity.FILE_NAME, fileName)

            val editFileFragment = EditFileFragment()
            editFileFragment.arguments = args
            return editFileFragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        saveDataListener = try {
            context as OnRefreshFilesListListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement interface onRefreshFilesListListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        editTextCreateFile = view.findViewById(R.id.edit_text_create_file)
        val editTextCreateFile = editTextCreateFile ?: return

        val isNewFile = requireArguments().getBoolean(MainActivity.IS_NEW_FILE, false)
        if (!isNewFile) {
            fileName = requireArguments().getString(MainActivity.FILE_NAME) ?: return
            showText(editTextCreateFile, fileName!!)
        }
    }

    override fun onPause() {
        super.onPause()
        editTextCreateFile?.let { saveFile(it) }

        saveDataListener?.onRefreshFilesList()
    }

    private fun openFile(fileName: String): String {
        val activity = activity ?: return ""
        return File(activity.filesDir.path, "/${MainActivity.DIRECTORY}/${Uri.encode(fileName)}")
            .bufferedReader()
            .use { it.readText(); }
    }

    private fun showText(editText: EditText, fileName: String) {
        val textFromFile = openFile(fileName)

        editText.setText(textFromFile)
    }

    private fun saveFile(editText: EditText) {
        val activity = activity ?: return
        var newText = editText.text.toString()
        val linesArray = newText.lines().toMutableList()

        val newFileName = createFileName(linesArray)

        newText = linesArray.joinToString(separator = "\n")

        val path = activity.filesDir.path
        val file = File(path, "/${MainActivity.DIRECTORY}/${Uri.encode("$newFileName.txt")}")
        FileOutputStream(file).use {
            it.write(newText.toByteArray())
        }

        if (fileName != null) {
            deleteOldFile(path, file)
        }

        Toast.makeText(activity, getString(R.string.file_saved), Toast.LENGTH_SHORT).show()
    }

    private fun createFileName(linesArray: MutableList<String>): String {
        var defaultFileName = linesArray[0]
        defaultFileName = defaultFileName.trim()
        if (defaultFileName.isEmpty()) {
            defaultFileName = getString(R.string.empty_doc_name)
        }

        val filesSet = createFilesSet()

        if (filesSet.contains("$defaultFileName.txt")) {
            var fileNumber = 1
            while (filesSet.contains("$defaultFileName ($fileNumber).txt")) {
                fileNumber++
            }
            defaultFileName = "$defaultFileName ($fileNumber)"
        }

        linesArray[0] = defaultFileName

        return defaultFileName
    }

    private fun createFilesSet(): MutableSet<String> {
        val activity = activity ?: return mutableSetOf()
        val filesList = File(activity.filesDir.path, MainActivity.DIRECTORY).getDirectoryFiles()
        val filesSet = mutableSetOf<String>()
        for (fileObject in filesList) {
            if (fileObject.name != fileName) {
                filesSet.add(fileObject.name)
            }
        }
        return filesSet
    }

    private fun deleteOldFile(path: String, file: File) {
        val oldFile = File(path, "/${MainActivity.DIRECTORY}/${Uri.encode(fileName)}")
        if (file.path != oldFile.path) {
            oldFile.delete()
        }
    }
}