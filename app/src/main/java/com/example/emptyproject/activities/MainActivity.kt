package com.example.emptyproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.emptyproject.*
import com.example.emptyproject.fragments.MainFragment
import com.example.emptyproject.fragments.SettingFragment
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


class MainActivity : AppCompatActivity(), MainFragment.OnFragmentClickListener,
    NavDrawerAdapter.OnClickNavigationDrawerMenu {

    private var recyclerView: RecyclerView? = null
    private var toolbar: MaterialToolbar? = null
    private var drawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        toolbar = findViewById(R.id.toolbar_main)
        drawerLayout = findViewById(R.id.drawer_layout)

        val recyclerView = recyclerView ?: return
        val toolbar = toolbar ?: return
        val drawerLayout = drawerLayout ?: return

        toolbar.setNavigationOnClickListener {
            hideKeyboard()
            drawerLayout.openDrawer(Gravity.LEFT)
        }

//        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        val itemsNavigation = createItemsNavigationArray()
        sendClickPosition(itemsNavigation[0])

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = NavDrawerAdapter(itemsNavigation)
    }

    private fun createItemsNavigationArray(): ArrayList<NavDrawerItem> {
        return arrayListOf(
            NavDrawerItem(
                getString(R.string.main_screen),
                R.drawable.ic_main_screen_24,
                MainFragment.newInstance()),
            NavDrawerItem(
                getString(R.string.settings),
                R.drawable.ic_settings_24,
                SettingFragment.newInstance())
        )
    }

    override fun clickResultButton(result: String) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("result", result)
        startActivity(intent)
    }

    override fun sendClickPosition(selectedNavItem: NavDrawerItem) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, selectedNavItem.fragment)
            commit()
        }

        if (toolbar != null) {
            toolbar!!.title = selectedNavItem.name
        }
        if (drawerLayout != null) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
        }
    }

    private fun hideKeyboard() {
        val activity = this
        val view = activity.currentFocus

        if (view != null) {
            val inputMethodManager: InputMethodManager =
                (activity.getSystemService(Context.INPUT_METHOD_SERVICE) ?: return) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}