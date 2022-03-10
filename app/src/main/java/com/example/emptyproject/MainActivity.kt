package com.example.emptyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.isInvisible
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), ListFragment.OnFragmentSendDataListener, EditFileFragment.OnRefreshFilesListListener {

    private var lastOpenFileName: String? = null
    private var toolbar: Toolbar? = null
    private var drawerLayout: DrawerLayout? = null
    private var navigationView: NavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainScreenFragment = MainScreenFragment()
        val listFragment = ListFragment()
        val calculatorFragment = CalculatorFragment()
        val editFragment = EditFileFragment()
        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.navigation_view)

        val toolbar = toolbar ?: return
        val drawerLayout = drawerLayout ?: return
        val navigationView = navigationView ?: return

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_main_screen, mainScreenFragment)
            addToBackStack(null)
            commit()
        }

        toolbar.title = "Мультифунк"

        // Initialize the action bar drawer toggle instance
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        ) {}

        // Configure the drawer layout to add listener and show icon on toolbar
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        val listContainer = findViewById<FrameLayout>(R.id.list_container)
        // Set navigation view navigation item selected listener
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.main ->
                    supportFragmentManager.beginTransaction().apply {
                        if (findViewById<FrameLayout>(R.id.layout_land) != null) {
                            hide(listFragment)
                            hide(editFragment)
                            show(mainScreenFragment)
                            listContainer.isInvisible = true
                        } else {

                        }
                        replace(R.id.fragment_main_screen, mainScreenFragment)
                        commit()
                        toolbar.title = "Мультифунк"
                    }
                R.id.text_editor -> supportFragmentManager.beginTransaction().apply {
                    if (findViewById<FrameLayout>(R.id.layout_land) == null) {
                        replace(R.id.fragment_main_screen, listFragment)
                    } else {
                        listContainer.isInvisible = false
                        hide(mainScreenFragment)
                        hide(calculatorFragment)
                        replace(R.id.fragment_list_container, listFragment)
                        show(listFragment)
                        show(editFragment)
                    }
                    commit()
                    toolbar.title = "Редактирование"
                }
                R.id.calculator -> supportFragmentManager.beginTransaction().apply {
                    if (findViewById<FrameLayout>(R.id.layout_land) == null) {


                    } else {
                        val listContainer = findViewById<FrameLayout>(R.id.list_container)
                        listContainer.isInvisible = true
                        hide(listFragment)
                        show(calculatorFragment)
                    }
                    replace(R.id.fragment_main_screen, calculatorFragment)
                    commit()
                    toolbar.title = "Калькулятор"
                }
            }

            drawerLayout.closeDrawer(GravityCompat.START)
            true


//            val fragment = supportFragmentManager.findFragmentByTag(hashMapItems.getValue(it.itemId))
//            supportFragmentManager.beginTransaction().apply {
//                replace(R.id.fragment_list_container, fragment!!)
//                commit()
//                toolbar.title = "Главная"
//            }
        }
    }

        private fun editFile(args: Bundle) {
        val editFileFragment = EditFileFragment()
        val listFragment = supportFragmentManager.findFragmentById(R.id.fragment_list_container)
        editFileFragment.arguments = args
        if (findViewById<FrameLayout>(R.id.layout_land) == null) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_main_screen, editFileFragment)
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

    private val hashMapItems =hashMapOf(
        R.id.main to "main_screen_tag",
        R.id.text_editor to "files_list_tag",
        R.id.calculator to "calculator_tag"
    )


    companion object {
        const val IS_NEW_FILE = "isNewFile"
        const val FILE_NAME = "fileName"
        const val DIRECTORY = "documents"
    }
}