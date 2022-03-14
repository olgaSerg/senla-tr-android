package com.example.emptyproject
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import java.io.File

class ListFragment : Fragment(R.layout.fragment_list) {

    private var filesListView : ListView? = null
    private var buttonCreate: Button? = null
    private var adapter: ArrayAdapter<FileObject>? = null
    private var fragmentSendDataListener: OnFragmentSendDataListener? = null

    companion object {

        fun newInstance(): Fragment {
            return ListFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filesListView = view.findViewById(R.id.list_view)
        buttonCreate = view.findViewById(R.id.button_create)
        val filesListView = filesListView ?: return

        createFilesDirectory()

        setClickListener()
        val filesList = getFilesList()
        setFilesListAdapter(filesList, filesListView)
        setItemClickListener(filesListView)
    }

    override fun onResume() {
        super.onResume()
        filesListView ?: return

        val filesList = getFilesList()
        setFilesListAdapter(filesList, filesListView!!)
    }

    private fun setClickListener() {
        val buttonCreate = buttonCreate ?: return
        val fragmentSendDataListener = fragmentSendDataListener ?: return

        buttonCreate.setOnClickListener {
            fragmentSendDataListener.onCreateFile()
        }
    }

    interface OnFragmentSendDataListener {
        fun onEditFile(fileName: String)
        fun onCreateFile()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentSendDataListener = try {
            context as OnFragmentSendDataListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement interface OnFragmentInteractionListener")
        }
    }

    private fun createFilesDirectory() {
        val activity = activity ?: return
        val file = File(activity.filesDir, MainActivity.DIRECTORY)
        file.mkdir()
    }

    private fun getFilesList(): ArrayList<FileObject> {
        val activity = activity ?: return ArrayList()
        val subDirectory = MainActivity.DIRECTORY

        val directory = File(activity.filesDir, subDirectory)
        return directory.getDirectoryFiles()
    }

    private fun setFilesListAdapter(filesList: ArrayList<FileObject>, filesListView: ListView) {
        adapter = FilesListAdapter(requireActivity(), filesList)
        filesListView.adapter = adapter
    }

    private fun setItemClickListener(filesListView: ListView) {
        filesListView.setOnItemClickListener { listView, itemView, position, id ->
            val currentFile: FileObject = adapter?.getItem(position) ?: return@setOnItemClickListener
            val fragmentSendDataListener = fragmentSendDataListener ?: return@setOnItemClickListener

            fragmentSendDataListener.onEditFile(currentFile.name)
        }
    }
}