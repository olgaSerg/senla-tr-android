package com.example.emptyproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import androidx.recyclerview.widget.SimpleItemAnimator

class MainActivity : AppCompatActivity(), MainFragment.OnFragmentClickListener, NavDrawerAdapter.OnClickNavigationDrawerMenu {

    private var recyclerView: RecyclerView? = null
    private var toolbar: MaterialToolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        toolbar = findViewById(R.id.topAppBar)
        val recyclerView = recyclerView ?: return

        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        val itemsNavigation = createItemsNavigationArray()
        sendClickPosition(itemsNavigation[0])

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = NavDrawerAdapter(this, itemsNavigation)
    }

    private fun createItemsNavigationArray(): ArrayList<NavDrawerItem> {
        return arrayListOf(
            NavDrawerItem(
                getString(R.string.main_screen),
                R.drawable.ic_main_screen_24,
                { MainFragment.newInstance() }),
            NavDrawerItem(
                getString(R.string.settings),
                R.drawable.ic_settings_24,
                { SettingFragment.newInstance() })
        )
    }

    override fun clickResultButton(result: String) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("result", result)
        startActivity(intent)
    }

    override fun sendClickPosition(selectedNavItem: NavDrawerItem) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, selectedNavItem.fragmentFunction())
            toolbar?.title = selectedNavItem.name
            commit()
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
    }
}