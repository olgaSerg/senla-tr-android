package com.example.emptyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import java.io.FileOutputStream
import java.io.ObjectOutputStream


class MainActivity : AppCompatActivity() {

    private var buttonAdd: ActionMenuItemView? = null
    private var buttonSave: ActionMenuItemView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonAdd = findViewById(R.id.add)
        buttonSave = findViewById(R.id.save)

        val buttonAdd = buttonAdd ?: return
        val buttonSave = buttonSave ?: return

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.main_fragment_container, ElementsFragment.newInstance())
                commit()
            }
        }

        setAddButtonListener(buttonAdd)
        setSaveButtonListener(buttonSave)
    }

    private fun setAddButtonListener(button: ActionMenuItemView) {
        button.setOnClickListener {
        val fragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as ElementsFragment
            fragment.elements.add(Element("", 0))
            fragment.recyclerView!!.adapter!!.notifyDataSetChanged()
        }
    }

    private fun setSaveButtonListener(button: ActionMenuItemView) {
        button.setOnClickListener {
            val fragment =
                supportFragmentManager.findFragmentById(R.id.main_fragment_container) as ElementsFragment
            val fos = FileOutputStream(filesDir.path + "/temp.out")
            val oos = ObjectOutputStream(fos)
            for (element in fragment.elements) {
                oos.writeObject(element)
            }
            oos.flush()
            fos.close()
            oos.close()
            Toast.makeText(this,"File save", Toast.LENGTH_SHORT).show()
        }
    }
}
