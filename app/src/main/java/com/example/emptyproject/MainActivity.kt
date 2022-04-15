package com.example.emptyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import java.io.Serializable

class MainActivity : AppCompatActivity(), LoginFragment.OnDataSendListener, ProfileFragment.OnSendClickLogout {

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
        val profileFragment = ProfileFragment.newInstance()
        val args = Bundle()
        args.putSerializable(PROFILE, profile)
        profileFragment.arguments = args

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
