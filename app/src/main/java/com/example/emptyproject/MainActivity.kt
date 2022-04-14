package com.example.emptyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.io.Serializable

class MainActivity : AppCompatActivity(), LoginFragment.OnDataSendListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, LoginFragment.newInstance())
            commit()
        }
    }

    override fun sendProfile(profile: Serializable) {
        val profileFragment = ProfileFragment.newInstance()
        val args = Bundle()
        args.putSerializable("profile", profile)
        profileFragment.arguments = args
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, profileFragment)
            commit()
        }
    }
}