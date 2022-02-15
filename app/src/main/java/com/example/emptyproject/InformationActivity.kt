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

        val message1 = intent.getStringExtra("name")
        val message2 = intent.getStringExtra("password")
        "Логин: $message1 \nПароль: $message2".also { textViewInformation.text = it }

        val message3 = intent.getStringExtra("login")
        val message4 = intent.getStringExtra("password")
        val message5 = intent.getStringExtra("name")
        val message6 = intent.getStringExtra("surname")
        val message7 = intent.getStringExtra("gender")
        val message8 = intent.getStringExtra("AddInformation")
        "Логин: $message3 \nПароль: $message4 \nИмя: $message5 \nФамилия: $message6 \nПол: $message7 \nДополнительная информация: $message8".also { textViewInformation.text = it }

        buttonBack = findViewById(R.id.button_back_information)
        val buttonBack = buttonBack ?: return
        buttonBack.setOnClickListener {
            finish()
        }
    }
}