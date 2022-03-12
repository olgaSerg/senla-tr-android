package com.example.emptyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.isGone
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), ListFragment.OnFragmentSendDataListener, EditFileFragment.OnRefreshFilesListListener {

    private var lastOpenFileName: String? = null
    private var toolbar: Toolbar? = null
    private var drawerLayout: DrawerLayout? = null
    private var navigationView: NavigationView? = null
    private var listContainer : LinearLayout? = null
    private var mainContainer : FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainScreenFragment = MainScreenFragment()
        val listFragment = ListFragment()
        val calculatorFragment = CalculatorFragment()
        listContainer = findViewById(R.id.list_container)
        mainContainer = findViewById(R.id.fragment_main_screen)

        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.navigation_view)

        val toolbar = toolbar ?: return

        if (savedInstanceState == null) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_main_screen, mainScreenFragment)
            commit()
            }
        }

        toolbar.title = getString(R.string.main_screen_title)

        initializeActionBarDrawerToggle()
        setNavigationDrawerListener(listFragment, calculatorFragment,mainScreenFragment)
    }

    private fun initializeActionBarDrawerToggle() {
        val drawerLayout = drawerLayout ?: return
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        ) {}

        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }

    private fun setNavigationDrawerListener(
        listFragment: ListFragment,
        calculatorFragment: CalculatorFragment,
        mainScreenFragment: MainScreenFragment
    ) {
        val listContainer = findViewById<LinearLayout>(R.id.list_container)
        val mainContainer = findViewById<FrameLayout>(R.id.fragment_main_screen)
        val drawerLayout = drawerLayout ?: return
        val navigationView = navigationView ?: return
        navigationView.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.main ->
                    supportFragmentManager.beginTransaction().apply {
                        if (findViewById<FrameLayout>(R.id.layout_land) != null) {
                            mainContainer.isGone = false
                            listContainer.isGone = true
                        }
                        replace(R.id.fragment_main_screen, mainScreenFragment)
                        addToBackStack(null)
                        commit()
                        toolbar?.title = getString(R.string.main_screen_title)
                    }
                R.id.text_editor -> supportFragmentManager.beginTransaction().apply {
                    if (findViewById<FrameLayout>(R.id.layout_land) == null) {
                        replace(R.id.fragment_main_screen, listFragment)
                    } else {
                        mainContainer.isGone = true
                        listContainer.isGone = false
                        replace(R.id.fragment_list_container, listFragment)
                        addToBackStack(null)
                    }
                    commit()
                    toolbar?.title = getString(R.string.edit_title)
                }
                R.id.calculator -> supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_main_screen, calculatorFragment)
                    if (findViewById<FrameLayout>(R.id.layout_land) != null) {
                        listContainer.isGone = true
                        mainContainer.isGone = false
                    }
                    commit()
                    toolbar?.title = getString(R.string.calculator_title)
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
        editFileFragment.arguments = args
        if (findViewById<FrameLayout>(R.id.layout_land) == null) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_main_screen, editFileFragment)
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
                    replace(R.id.fragment_main_screen, listFragment)
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