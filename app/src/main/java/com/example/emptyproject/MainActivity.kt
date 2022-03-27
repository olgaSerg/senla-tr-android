package com.example.emptyproject

import android.content.Intent
import android.content.res.Configuration
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
    private var state = MAIN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listContainer = findViewById(R.id.list_container)
        mainContainer = findViewById(R.id.fragment_main_screen)
        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.navigation_view)

        initializeActionBarDrawerToggle()
        setNavigationDrawerListener()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        state = savedInstanceState.getString(STATE).toString()
    }

    override fun onResume() {
        super.onResume()
        restoreState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE, state)
    }

    private fun handleNavigationItemSelected(itemId: Int) {
        val drawerLayout = drawerLayout ?: return
        when (itemId) {
            R.id.main -> handleMainItemSelected()
            R.id.text_editor -> handleEditorItemSelected()
            R.id.calculator -> handleCalculatorItemSelected()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun handleMainItemSelected() {
        if (mainContainer == null && listContainer == null) return
        supportFragmentManager.beginTransaction().apply {
            if (isLandscapeLayout()) {
                mainContainer?.isGone = false
                listContainer?.isGone = true
            }
            replace(R.id.fragment_main_screen, MainScreenFragment.newInstance())
            supportFragmentManager.findFragmentById(R.id.fragment_edit_file_container)
                ?.let { remove(it) }
            commit()
            toolbar?.title = getString(R.string.main_screen_title)
            state = MAIN
        }
    }

    private fun handleEditorItemSelected() {
        if (mainContainer == null && listContainer == null) return
        supportFragmentManager.beginTransaction().apply {
            val containerId = if (!isLandscapeLayout()) {
                R.id.fragment_main_screen
            } else {
                R.id.fragment_list_container
            }
            if (isLandscapeLayout()) {
                mainContainer?.isGone = true
                listContainer?.isGone = false
            }
            openListFragment(containerId)
            commit()
            toolbar?.title = getString(R.string.edit_title)
            state = TEXT_EDITOR
        }
    }

    private fun handleCalculatorItemSelected() {
        if (mainContainer == null && listContainer == null) return
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_main_screen, CalculatorFragment.newInstance())
            if (isLandscapeLayout()) {
                mainContainer?.isGone = false
                listContainer?.isGone = true
            }
            supportFragmentManager.findFragmentById(R.id.fragment_edit_file_container)
                ?.let { remove(it) }
            commit()
            toolbar?.title = getString(R.string.calculator_title)
            state = CALCULATOR
        }
    }

    private fun restoreState() {
        when (state) {
            MAIN -> handleNavigationItemSelected(R.id.main)
            TEXT_EDITOR-> handleNavigationItemSelected(R.id.text_editor)
            CALCULATOR -> handleNavigationItemSelected(R.id.calculator)
        }
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

    private fun setNavigationDrawerListener() {
        val navigationView = navigationView ?: return
        navigationView.setNavigationItemSelectedListener {
            handleNavigationItemSelected(it.itemId)
            true
        }
    }

    private fun editFile(isNewFile: Boolean, fileName: String?) {
        if (isLandscapeLayout()) {
            val editFileFragment = EditFileFragment.newInstance(isNewFile, fileName)
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_edit_file_container, editFileFragment)
                commit()
            }
        } else {
            val intent = Intent(this, EditFileActivity::class.java)
            intent.putExtra(IS_NEW_FILE, isNewFile)
            intent.putExtra(FILE_NAME, fileName)
            startActivity(intent)
        }
    }

    private fun isLandscapeLayout() : Boolean {
        return resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    override fun onEditFile(fileName: String) {
        if (fileName != lastOpenFileName) {
            editFile(false, fileName)
            if (findViewById<FrameLayout>(R.id.layout_land) != null) {
                lastOpenFileName = fileName
            }
        }
    }

    override fun onCreateFile() {
        editFile(true, null)
        lastOpenFileName = null
    }

    override fun onRefreshFilesList() {
        if (isLandscapeLayout()) {
           openListFragment(R.id.fragment_list_container)
        }
    }

    private fun openListFragment(containerId: Int) {
        supportFragmentManager.beginTransaction().apply {
            replace(containerId, ListFragment.newInstance())
            commit()
        }
    }
    companion object {
        const val IS_NEW_FILE = "isNewFile"
        const val FILE_NAME = "fileName"
        const val DIRECTORY = "documents"
        const val MAIN = "main"
        const val TEXT_EDITOR = "text_editor"
        const val CALCULATOR = "calculator"
        const val STATE = "state"
    }
}