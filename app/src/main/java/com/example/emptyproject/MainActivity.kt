package com.example.emptyproject

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"

class MainActivity : AppCompatActivity() {
    var buttonLogin: Button? = null
    var buttonRegistration: Button? = null
    var buttonAbout: Button? = null

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.training)
        buttonLogin = findViewById(R.id.button_entrance)
        val buttonLogin = buttonLogin ?: return
        buttonRegistration = findViewById(R.id.button_registration)
        val buttonRegistration = buttonRegistration ?: return
        buttonAbout = findViewById(R.id.button_about)
        val buttonAbout = buttonAbout ?: return

        buttonLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        buttonRegistration.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
        buttonAbout.setOnClickListener {
            val address: Uri = Uri.parse("https://developer.android.com/training/basics/firstapp/starting-activity")
            val openLinkIntent = Intent(Intent.ACTION_VIEW, address)

            if (openLinkIntent.resolveActivity(packageManager) != null) {
                startActivity(openLinkIntent)
            } else {
                Log.d("Intent", "Не получается обработать намерение!")
            }
        }
    }
}