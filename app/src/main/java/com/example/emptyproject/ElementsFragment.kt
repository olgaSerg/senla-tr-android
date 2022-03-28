package com.example.emptyproject

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import java.io.ObjectInputStream
import java.io.FileInputStream

class ElementsFragment : Fragment(R.layout.elements_fragment) {

    private var elements: ArrayList<Element> = arrayListOf(Element("olga", 1))
    private var recyclerView: RecyclerView? = null
    private var buttonAdd: ActionMenuItemView? = null
    private var buttonSave: ActionMenuItemView? = null

    companion object {

        const val LIST = "list"
        fun newInstance(): Fragment {
            return ElementsFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeFields(view)

        val buttonAdd = buttonAdd ?: return
        val buttonSave = buttonSave ?: return
        val recyclerView = recyclerView ?: return

        elements = if (savedInstanceState == null) {
            loadElementsFromFile() as ArrayList<Element>
        } else {
            savedInstanceState.getParcelableArrayList<Element>(LIST) as ArrayList<Element>
        }

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = ElementAdapter(elements)

        setAddButtonListener(buttonAdd)
        setSaveButtonListener(buttonSave)
    }

    private fun initializeFields(view: View) {
        buttonAdd = view.findViewById(R.id.menu_button_add)
        buttonSave = view.findViewById(R.id.menu_button_save)
        recyclerView = view.findViewById(R.id.recycler_view)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(LIST, elements)
    }

    private fun loadElementsFromFile(): List<Element> {
        val elements = mutableListOf<Element>()
        if (!File(requireActivity().filesDir.path + "/temp.out").exists()) return elements
        val fis = FileInputStream(requireActivity().filesDir.path + "/temp.out")
        val oin = ObjectInputStream(fis)
        while (fis.available() > 0) {
            val element: Element = oin.readObject() as Element
            elements.add(element)
        }
        return elements
    }

    private fun setAddButtonListener(button: ActionMenuItemView) {
        button.setOnClickListener {
            elements.add(Element("", 0))
            recyclerView?.adapter?.notifyItemChanged(elements.size)
        }
    }

    private fun setSaveButtonListener(button: ActionMenuItemView) {
        button.setOnClickListener {
            val activity = activity ?: return@setOnClickListener
            val fos = FileOutputStream(activity.filesDir.path + "/temp.out")
            val oos = ObjectOutputStream(fos)
            for (element in elements) {
                oos.writeObject(element)
            }
            oos.flush()
            fos.close()
            oos.close()
            Toast.makeText(activity,"File save", Toast.LENGTH_SHORT).show()
        }
    }
}