package com.example.emptyproject

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.io.Serializable
import android.app.Activity
import android.text.Editable
import java.lang.ClassCastException
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.Date

const val URL = "https://pub.zame-dev.org/senla-training-addition/lesson-20.php?method="
const val ERROR = "error"
const val EMAIL = "email"
const val PASSWORD = "password"
const val TOKEN = "token"
const val IS_TASK_STARTED = "task_start"

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var editTextEmail: EditText? = null
    private var editTextPassword: EditText? = null
    private var buttonLogin: Button? = null
    private var textViewError: TextView? = null
    private var progressBar: ProgressBar? = null
    private var dataSendListener: OnDataSendListener? = null
    private var errorOccurred: Boolean = false
    private var errorText: String = ""
    private var loginTask: LoginTask? = null
    private var getProfileTask: ProfileTask? = null
    private var isTaskStarted: Boolean = false
    private var token: String? = null
    private var email: String? = null

    companion object {

        fun newInstance(): Fragment {
            return LoginFragment()
        }
    }

    interface OnDataSendListener {
        fun sendProfile(profile: Serializable)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        dataSendListener = try {
            activity as OnDataSendListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement OnDataSendListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeFields(view)

        val textViewError = textViewError ?: return
        val editTextEmail = editTextEmail ?: return
        val editTextPassword = editTextPassword ?: return
        val buttonLogin = buttonLogin ?: return

        if (savedInstanceState != null) {
            email = savedInstanceState.getString(EMAIL)
            val password = savedInstanceState.getString(PASSWORD)
            val textError = savedInstanceState.getString(ERROR)
            token = savedInstanceState.getString(TOKEN)
            isTaskStarted = savedInstanceState.getBoolean(IS_TASK_STARTED)

            textViewError.text = Editable.Factory.getInstance().newEditable(textError)
            editTextEmail.text = Editable.Factory.getInstance().newEditable(email)
            editTextPassword.text = Editable.Factory.getInstance().newEditable(password)
        }
        setButtonLoginClickListener(buttonLogin)

        if (isTaskStarted) {
            buttonLogin.callOnClick()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val textViewError = textViewError ?: return
        val editTextEmail = editTextEmail ?: return
        val editTextPassword = editTextPassword ?: return

        outState.putString(ERROR, textViewError.text.toString())
        outState.putString(EMAIL, editTextEmail.text.toString())
        outState.putString(PASSWORD, editTextPassword.text.toString())
        outState.putBoolean(IS_TASK_STARTED, isTaskStarted)
    }

    override fun onPause() {
        super.onPause()
        loginTask?.cancel(true)
        getProfileTask?.cancel(true)
    }

    private fun initializeFields(view: View) {
        editTextEmail = view.findViewById(R.id.edit_text_email)
        editTextPassword = view.findViewById(R.id.edit_text_password)
        buttonLogin = view.findViewById(R.id.button_login)
        textViewError = view.findViewById(R.id.text_view_error)
        progressBar = view.findViewById(R.id.progress_bar)
    }

    private fun setButtonLoginClickListener(button: Button) {
        val editTextEmail = editTextEmail ?: return
        val editTextPassword = editTextPassword ?: return

        button.setOnClickListener {
            textViewError?.text = ""
            if (isFieldEmpty(editTextEmail) || isFieldEmpty(editTextPassword)) {
                showError(editTextEmail)
                showError(editTextPassword)
                return@setOnClickListener
            }
            button.isEnabled = false
            email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            val jsonObject = JSONObject()
            jsonObject.put("email", email)
            jsonObject.put("password", password)

            loginTask = LoginTask()
            if (loginTask != null) {
                loginTask!!.execute(jsonObject.toString(), email)
                isTaskStarted = true
            }
        }
    }

    private fun isFieldEmpty(field: EditText): Boolean {
        if (field.text.isEmpty()) {
            return true
        }
        return false
    }

    private fun showError(field: EditText) {
        if (field.text.toString() == "") {
            field.error = getString(R.string.error_empty_field)
        }
    }

    inner class LoginTask : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            val progressBar = progressBar ?: return
            progressBar.visibility = View.VISIBLE

        }

        override fun doInBackground(vararg params: String?): String? {
            val client = OkHttpClient()
            val requestBody = params[0]?.toRequestBody()
            val request = Request.Builder()
                .method("POST", requestBody)
                .url(URL + "login")
                .build()
            try {
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    if (response.body != null) {
                        val jsonData: String = response.body!!.string()
                        val jsonObject = JSONObject(jsonData)
                        val errorString = catchError(jsonObject)

                        if (errorString == "") {
                            getProfileTask = ProfileTask()
                            if (getProfileTask != null) {
                                token = getToken(jsonObject)
                                getProfileTask!!.execute(token, params[1])
                            }
                        }
                        return errorString
                    }
                }
            } catch (e: SocketTimeoutException) {
                errorText = getString(R.string.error_connection)
                errorOccurred = true
                return errorText
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val textViewError = textViewError ?: return
            val progressBar = progressBar ?: return

            progressBar.visibility = View.GONE

            if (result != "") {
                textViewError.text = result
                val buttonLogin = buttonLogin ?: return
                buttonLogin.isEnabled = true
            }
        }

        private fun catchError(jsonObject: JSONObject): String {
            if (jsonObject.getString("status") == "error") {
                val jsonMessage = jsonObject.getString("message")
                return "Error: $jsonMessage"
            }
            return ""
        }

        private fun getToken(jsonObject: JSONObject): String {
            val token = jsonObject.getString("token")
            val jsonTokenObject = JSONObject()

            jsonTokenObject.put("token", token)

            return jsonTokenObject.toString()
        }
    }

    inner class ProfileTask() : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String? {
            val client = OkHttpClient()
            val requestBody = params[0]?.toRequestBody()
            val request = Request.Builder()
                .method("POST", requestBody)
                .url(URL + "profile")
                .build()
            try {
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    if (response.body != null) {
                        val jsonData: String = response.body!!.string()
                        val jsonObject = JSONObject(jsonData)

                        val profile = fillProfileObject(jsonObject, params[1])

                        dataSendListener?.sendProfile(profile)
                    }
                }
            } catch (e: SocketTimeoutException) {
                errorText = getString(R.string.error_connection)
                errorOccurred = true
                return errorText
            }
            return null
        }


        private fun fillProfileObject(jsonObject: JSONObject, email: String?): Serializable {
            return Profile(
                email = email,
                firstName = jsonObject.getString("firstName"),
                lastName = jsonObject.getString("lastName"),
                birthDate = getBirthDate(jsonObject),
                notes = jsonObject.getString("notes")
            )
        }

        private fun getBirthDate(jsonObject: JSONObject): String {
            val date = Date(jsonObject.getInt("birthDate") * 1000L)
            val dateFormat = SimpleDateFormat("dd.MM.yyyy")
            return dateFormat.format(date)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val buttonLogin = buttonLogin ?: return
            buttonLogin.isEnabled = true
            isTaskStarted = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (loginTask != null) {
            loginTask!!.cancel(true)
        }
    }



}


