package com.example.emptyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

const val USER_NAME: String = "Name"
const val USER_PASSWORD: String = "Password"

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
            checkLogin(editTextName, editTextPassword)
        }
        buttonRegistration.setOnClickListener {
            checkRegistration(editTextName, editTextPassword)
        }
    }

    private fun checkLogin(name: EditText, password: EditText) {
        if (name.text.toString() == USER_NAME && password.text.toString() == USER_PASSWORD) {
            val toast = Toast.makeText(applicationContext, "Авторизация прошла успешно", Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        if (name.text.toString() != USER_NAME && name.text.toString() != "") {
            name.error = "Неверное имя пользователя"
        } else if (name.text.toString() == "") {
            name.error = "Заполните пустое поле"
        }

        if (password.text.toString() != USER_PASSWORD && password.text.toString() != "") {
            password.error = "Неверный пароль"
        } else if (password.text.toString() == "") {
            password.error = "Заполните пустое поле"
        }
    }

    private fun checkRegistration(name: EditText, password: EditText) {
        if (name.text.toString() != "" && password.text.toString() != "") {
            val toast = Toast.makeText(applicationContext, "Регистрация прошла успешно", Toast.LENGTH_SHORT)
            toast.show()
        } else {
            val toast = Toast.makeText(applicationContext, "Заполните пустое поле", Toast.LENGTH_SHORT)
            toast.show()
        }
    }
}



