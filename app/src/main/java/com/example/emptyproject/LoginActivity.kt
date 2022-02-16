package com.example.emptyproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class LoginActivity : AppCompatActivity() {
    var editTextLogin: EditText? = null
    var editTextPassword: EditText? = null
    var buttonLogin: Button? = null
    var buttonBack: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        editTextLogin = findViewById(R.id.edit_text_name)
        val editTextLogin = editTextLogin ?: return
        editTextPassword = findViewById(R.id.edit_text_password)
        val editTextPassword = editTextPassword ?: return
        buttonLogin = findViewById(R.id.button_login)
        val buttonLogin = buttonLogin ?: return

         buttonLogin.setOnClickListener {
             if (checkLogin(editTextLogin, editTextPassword)) {
                 val intent = Intent(this, InformationActivity::class.java)
                 intent.putExtra("name", editTextLogin.text.toString().trim())
                 intent.putExtra("password", editTextPassword.text.toString().trim())
                 intent.putExtra("source", "login")
                 startActivity(intent)
             }
         }

        buttonBack = findViewById(R.id.button_back)
        val buttonBack = buttonBack ?: return

        buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun checkLogin(login: EditText, password: EditText): Boolean {
        login.error = null
        password.error = null
        if (login.text.toString() != "" && password.text.toString() != "") {
            return true
        }
        if (login.text.toString() == "") {
            login.error = "Заполните пустое поле"
        }
        if (password.text.toString() == "") {
           password.error = "Заполните пустое поле"
        }
        return false
    }
}