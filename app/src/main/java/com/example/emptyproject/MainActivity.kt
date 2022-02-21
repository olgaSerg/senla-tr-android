package com.example.emptyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

private const val USER_NAME: String = "Name"
private const val USER_PASSWORD: String = "Password"

class MainActivity : AppCompatActivity() {

    private var buttonLogin: Button? = null
    private var buttonRegistration: Button? = null
    private var editTextName: EditText? = null
    private var editTextPassword: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonLogin = findViewById(R.id.button_login)
        buttonRegistration = findViewById(R.id.button_register)
        editTextName = findViewById(R.id.edit_text_person_name)
        editTextPassword = findViewById(R.id.edit_text_password)

        val editTextName = editTextName ?: return
        val editTextPassword = editTextPassword ?: return
        val buttonLogin = buttonLogin ?: return
        val buttonRegistration = buttonRegistration ?: return

        buttonLogin.setOnClickListener {
            login(editTextName, editTextPassword)
        }
        buttonRegistration.setOnClickListener {
            checkRegistration(editTextName, editTextPassword)
        }
    }

    private fun login(name: EditText, password: EditText) {
        if (isLoginFieldValid(name, password)) {
            Toast.makeText(applicationContext, getString(R.string.authorization_successful), Toast.LENGTH_SHORT)
                .show()
        }

        if (name.text.toString() != USER_NAME && name.text.toString() != "") {
            name.error = getString(R.string.invalid_username)
        } else if (name.text.toString() == "") {
            name.error = getString(R.string.empty_field)
        }

        if (password.text.toString() != USER_PASSWORD && password.text.toString() != "") {
            password.error = getString(R.string.invalid_password)
        } else if (password.text.toString() == "") {
            password.error = getString(R.string.empty_field)
        }
    }

    private fun checkRegistration(name: EditText, password: EditText) {
        if (name.text.toString() != "" && password.text.toString() != "") {
            Toast.makeText(applicationContext, getString(R.string.registration_successful), Toast.LENGTH_SHORT).
                show()
        } else {
            Toast.makeText(applicationContext, getString(R.string.empty_field), Toast.LENGTH_SHORT).
                show()
        }
    }

    private fun isLoginFieldValid(name: EditText, password: EditText): Boolean {
        return (name.text.toString() == USER_NAME && password.text.toString() == USER_PASSWORD)
    }
}
