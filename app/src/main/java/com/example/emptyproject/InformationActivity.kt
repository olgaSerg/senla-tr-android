package com.example.emptyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class InformationActivity : AppCompatActivity() {
    var textViewInformation: TextView? = null
    var buttonBack: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.information)

        textViewInformation = findViewById(R.id.text_view_information)
        val textViewInformation = textViewInformation ?: return

        if (KEY_SIGN_UP == "login") {
            val name = intent.getStringExtra("name")
            val password = intent.getStringExtra("password")

            textViewInformation.text = """
                Логин: $name
                Пароль: $password
            """.trimIndent()
        } else {
            val information = intent.getSerializableExtra("information") as PersonInformation
            textViewInformation.text = """
                Логин: ${information.login}
                Пароль: ${information.password}
                Имя: ${information.name}
                Фамилия: ${information.surname}
                Пол: ${information.gender}
                Дополнительная информация: ${information.additionalInformation}
            """.trimIndent()
        }

        buttonBack = findViewById(R.id.button_back_information)
        val buttonBack = buttonBack ?: return

        buttonBack.setOnClickListener {
            finish()
        }
    }
}