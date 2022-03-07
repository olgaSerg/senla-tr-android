package com.example.emptyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout


class MainActivity : AppCompatActivity(), ListFragment.OnFragmentSendDataListener, EditFileFragment.OnRefreshFilesListListener {
    private var lastOpenFileName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val listFragment = ListFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_list_container, listFragment)
            commit()
        }
    }

    override fun onResume() {
        super.onResume()
        if (findViewById<FrameLayout>(R.id.layout_land) != null) return
        val editFileFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_edit_file_container) ?: return
        supportFragmentManager.beginTransaction().apply {
            remove(editFileFragment)
            commit()
        }
    }

    private fun editFile(args: Bundle) {
        val editFileFragment = EditFileFragment()
        val listFragment = supportFragmentManager.findFragmentById(R.id.fragment_list_container)
        editFileFragment.arguments = args
        if (findViewById<FrameLayout>(R.id.layout_land) == null) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_edit_file_container, editFileFragment)
                if (listFragment != null) {
                    hide(listFragment)
                }
                addToBackStack(null)
                commit()
            }
        } else {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_edit_file_container, editFileFragment)
                addToBackStack(null)
                commit()
            }
        }
    }

    override fun onEditFile(fileName: String) {
        if (fileName != lastOpenFileName) {
            val args = Bundle()
            args.putBoolean(IS_NEW_FILE, false)
            args.putString(FILE_NAME, fileName)

            editFile(args)
            if (findViewById<FrameLayout>(R.id.layout_land) != null) {
                lastOpenFileName = fileName
            }
        }
    }

    override fun onCreateFile() {
        val args = Bundle()
        args.putBoolean(IS_NEW_FILE, true)

        editFile(args)
        lastOpenFileName = null
    }

    override fun onRefreshFilesList() {
        val listFragment = ListFragment()
        val editFragment = supportFragmentManager.findFragmentById(R.id.fragment_edit_file_container)
        val fileContentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_edit_file_container)
        if (fileContentFragment != null) {
            if (findViewById<FrameLayout>(R.id.layout_land) == null) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_list_container, listFragment)
                    if (editFragment != null) {
                        hide(editFragment)
                    }
                    commit()
                }
            } else {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_list_container, listFragment)
                    commit()
                }
            }
        }
    }

    companion object {
        const val IS_NEW_FILE = "isNewFile"
        const val FILE_NAME = "fileName"
        const val DIRECTORY = "documents"
    }
}