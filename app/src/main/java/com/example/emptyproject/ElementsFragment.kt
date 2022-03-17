package com.example.emptyproject

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream

class ElementsFragment : Fragment(R.layout.elements_fragment) {

    var elements: ArrayList<Element> = arrayListOf(Element("olga", 1))
    var recyclerView: RecyclerView? = null

    companion object {

        const val LIST = "list"
        fun newInstance(): Fragment {
            return ElementsFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            loadElementsFromFile()
        } else {
            elements = savedInstanceState.getParcelableArrayList<Element>(LIST) as ArrayList<Element>
        }

        recyclerView = view.findViewById(R.id.recycler_view)
        val recyclerView = recyclerView ?: return
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = ElementAdapter(elements)
        Log.v("ViewCreated", "onViewCreated()")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(LIST, elements)
    }

    private fun loadElementsFromFile() {
        if (!File(requireActivity().filesDir.path + "/temp.out").exists()) return
        val fis = FileInputStream(requireActivity().filesDir.path + "/temp.out")
        val oin = ObjectInputStream(fis)
        elements.clear()
        while (fis.available() > 0) {
            val element: Element = oin.readObject() as Element
            elements.add(element)
        }
        recyclerView!!.adapter!!.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.v("DestroyView", "onDestroyView()")
    }
}