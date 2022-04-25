package com.example.emptyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.emptyproject.fragments.LoginFragment
import com.example.emptyproject.fragments.ProfileFragment
import java.io.Serializable

class MainActivity : AppCompatActivity(), LoginFragment.OnDataSendListener,
    ProfileFragment.OnSendClickLogout {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            loadLoginFragment()
        }
    }

    private fun loadLoginFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, LoginFragment.newInstance())
            commit()
        }
    }

    override fun sendProfile(profile: Serializable) {
        val profileFragment = ProfileFragment.newInstance(profile)

        loadProfileFragment(profileFragment)
    }

    private fun loadProfileFragment(profileFragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, profileFragment)
            commit()
        }
    }

    override fun clickLogout() {
        loadLoginFragment()
    }

    companion object {

        const val PROFILE = "profile"
    }
}
