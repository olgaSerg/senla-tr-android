package com.example.emptyproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import java.io.Serializable

class RegistrationActivity : AppCompatActivity() {
    var editTextLogin: EditText? = null
    var editTextPassword: EditText? = null
    var editTextPasswordConfirmation: EditText? = null
    var editTextName: EditText? = null
    var editTextSurname: EditText? = null
    var buttonCompleteRegistration: Button? = null
    var checkBox: CheckBox? = null
    var editTextInformation: EditText? = null
    var radioGroup: RadioGroup? = null
    var buttonBack: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration)

        editTextLogin = findViewById(R.id.edit_text_login_registration)
        editTextPassword = findViewById(R.id.edit_text_password_registration)
        editTextPasswordConfirmation = findViewById(R.id.edit_text_password_confirmation)
        editTextName = findViewById(R.id.edit_text_name_registration)
        editTextSurname = findViewById(R.id.edit_text_surname_registration)
        radioGroup = findViewById(R.id.radio_group)
        buttonCompleteRegistration = findViewById(R.id.button_complete_registration)
        editTextInformation = findViewById(R.id.edit_text_additional_information)
        checkBox = findViewById(R.id.check_box_agreement)
        buttonBack = findViewById(R.id.button_back)

        val editTextLogin = editTextLogin ?: return
        val editTextPassword = editTextPassword ?: return
        val editTextPasswordConfirmation = editTextPasswordConfirmation ?: return
        val editTextName = editTextName?: return
        val editTextSurname = editTextSurname ?: return
        val radioGroup = radioGroup ?: return
        val buttonCompleteRegistration = buttonCompleteRegistration ?: return
        val editTextAdditionalInformation = editTextInformation ?: return
        val checkBox = checkBox ?: return
        val buttonBack = buttonBack ?: return

        val list = listOf(editTextLogin, editTextPassword, editTextPasswordConfirmation, editTextName, editTextSurname)

        buttonCompleteRegistration.setOnClickListener {

            val validationResults = listOf(
                checkMinLoginLength(editTextLogin),
                checkPasswordsMatch(editTextPassword, editTextPasswordConfirmation),
                checkMinPasswordLength(editTextPassword),
                checkFields(list),
            )

            val isFormValid = validationResults.all { it }

            if (isFormValid) {
                val intent = Intent(this, InformationActivity::class.java)
                val information = Information(
                    login = editTextLogin.text.toString(),
                    password = editTextPassword.text.toString(),
                    name = editTextName.text.toString(),
                    surname = editTextSurname.text.toString(),
                    gender = getGender(radioGroup),
                    additionalInformation = editTextAdditionalInformation.text.toString()
                )
                intent.putExtra("information", information as Serializable)
                intent.putExtra("source", "registration")
                startActivity(intent)
            }
        }

        checkBox.setOnClickListener {
            buttonCompleteRegistration.isEnabled = checkBox.isChecked
        }

        buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun checkFields(list: List<EditText>): Boolean {
        var allFilled = true
        for (each in list) {
            if (each.text.toString() == "") {
                each.error = "Заполните пустое поле"
                allFilled = false
            }
        }
        return allFilled
    }

    private fun checkMinLoginLength(login: EditText): Boolean {
        if (login.length() < 4) {
            login.error = "Минимальная длина 4 символа"
            return false
        }
        return true
    }

    private fun checkMinPasswordLength(password: EditText): Boolean {
        if (password.length() < 8) {
            password.error = "Минимальная длина 8 символов"
            return false
        }
        return true
    }

    private fun checkPasswordsMatch(password: EditText, passwordConfirmation: EditText): Boolean {
        if (password.text.toString() != passwordConfirmation.text.toString()) {
            password.error = "Пароли не совпадают"
            passwordConfirmation.error = "Пароли не совпадают"
            return false
        }
        return true
    }

    private fun getGender(radioGroup: RadioGroup): String {
        val radioButtonID: Int = radioGroup.checkedRadioButtonId
        val gendersMapping = hashMapOf(
            R.id.radio_button_male to "М",
            R.id.radio_button_female to "Ж",
            R.id.radio_button_indeterminate to "Н"
        )
        return gendersMapping.getValue(radioButtonID)
    }

    data class Information(
        val login: String? = null,
        val password: String? = null,
        val name: String? = null,
        val surname: String? = null,
        val gender: String? = null,
        val additionalInformation: String? = null
    ) : Serializable
}