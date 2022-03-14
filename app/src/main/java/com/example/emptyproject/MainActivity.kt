package com.example.emptyproject

import android.content.Intent
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

        listContainer = findViewById(R.id.list_container)
        mainContainer = findViewById(R.id.fragment_main_screen)
        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.navigation_view)

        val toolbar = toolbar ?: return

        if (savedInstanceState == null) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_main_screen, MainScreenFragment.newInstance())
            commit()
            }
        }
        else {
            val fragment = supportFragmentManager.findFragmentById(R.id.fragment_main_screen)
            if (isLandscapeLayout() && fragment is ListFragment) {
                supportFragmentManager.beginTransaction().apply {
                    mainContainer?.isGone = true
                    listContainer?.isGone = false
                    replace(R.id.fragment_list_container, ListFragment.newInstance())
                    replace(R.id.fragment_main_screen, ListFragment.newInstance())
                    commit()
                }
            } else {
                mainContainer?.isGone = false
                listContainer?.isGone = true
            }
        }

        toolbar.title = getString(R.string.main_screen_title)

        initializeActionBarDrawerToggle()
        setNavigationDrawerListener()
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
        val listContainer = findViewById<LinearLayout>(R.id.list_container)
        val mainContainer = findViewById<FrameLayout>(R.id.fragment_main_screen)
        val drawerLayout = drawerLayout ?: return
        val navigationView = navigationView ?: return
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.main ->
                    supportFragmentManager.beginTransaction().apply {
                        if (isLandscapeLayout()) {
                            mainContainer.isGone = false
                            listContainer.isGone = true
                        }
                        replace(R.id.fragment_main_screen, MainScreenFragment.newInstance())
                        commit()
                        toolbar?.title = getString(R.string.main_screen_title)
                    }
                R.id.text_editor -> supportFragmentManager.beginTransaction().apply {
                    if (!isLandscapeLayout()) {
                        replace(R.id.fragment_main_screen, ListFragment.newInstance())
                    } else {
                        mainContainer.isGone = true
                        listContainer.isGone = false
                        replace(R.id.fragment_list_container, ListFragment.newInstance())
                    }
                    commit()
                    toolbar?.title = getString(R.string.edit_title)
                }
                R.id.calculator -> supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_main_screen, CalculatorFragment.newInstance())
                    if (isLandscapeLayout()) {
                        listContainer.isGone = true
                        mainContainer.isGone = false
                    }
                    commit()
                    toolbar?.title = getString(R.string.calculator_title)
                }
            }

            drawerLayout.closeDrawer(GravityCompat.START)
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
            startActivity(intent);
        }
    }

    private fun isLandscapeLayout() : Boolean {
        return findViewById<FrameLayout>(R.id.layout_land) != null
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
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_list_container, ListFragment.newInstance())
                commit()
            }
        }
    }

    companion object {
        const val IS_NEW_FILE = "isNewFile"
        const val FILE_NAME = "fileName"
        const val DIRECTORY = "documents"
    }
}