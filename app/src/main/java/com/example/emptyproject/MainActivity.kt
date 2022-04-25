package com.example.emptyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.emptyproject.fragments.LoginFragment
import com.example.emptyproject.fragments.ProfileFragment

private var state: State = State()

class MainActivity : AppCompatActivity(), LoginFragment.OnDataSendListener,
    ProfileFragment.OnSendClickLogout {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            loadLoginFragment(state)
        }
    }

    private fun loadLoginFragment(state: State) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, LoginFragment.newInstance(state))
            commit()
        }
    }

    override fun sendProfile(profile: Profile) {
        val profileFragment = ProfileFragment.newInstance(state)

        loadProfileFragment(profileFragment)
    }

    private fun loadProfileFragment(profileFragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, profileFragment)
            commit()
        }
    }

    override fun clickLogout() {
        state = State()
        loadLoginFragment(state)
    }

    companion object {
        const val STATE = "state"
    }
}
