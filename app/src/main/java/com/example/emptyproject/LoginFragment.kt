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
const val EMAIL = "email"
const val PASSWORD = "password"

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var editTextEmail: EditText? = null
    private var editTextPassword: EditText? = null
    private var buttonLogin: Button? = null
    private var textViewError: TextView? = null
    private var progressBar: ProgressBar? = null
    private var dataSendListener: OnDataSendListener? = null
    private var errorOccurred: Boolean = false
    private var errorText: String = ""

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val editTextEmail = editTextEmail ?: return
        val editTextPassword = editTextPassword ?: return
        outState.putString(EMAIL, editTextEmail.text.toString())
        outState.putString(PASSWORD, editTextPassword.text.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeFields(view)

        val editTextEmail = editTextEmail ?: return
        val editTextPassword = editTextPassword ?: return
        val buttonLogin = buttonLogin ?: return

        if (savedInstanceState != null) {
            val email = savedInstanceState.getString(EMAIL)
            val password = savedInstanceState.getString(PASSWORD)

            editTextEmail.text = Editable.Factory.getInstance().newEditable(email)
            editTextPassword.text = Editable.Factory.getInstance().newEditable(password)
        }

        setButtonLoginClickListener(buttonLogin)
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
            if (!isLoginSuccessful(editTextEmail, editTextPassword)) {
                return@setOnClickListener
            }
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            val jsonObject = JSONObject()
            jsonObject.put("email", email)
            jsonObject.put("password", password)

            val loginTask = LoginTask()
            loginTask.execute(jsonObject.toString(), email)
        }
    }

    private fun isLoginSuccessful(email: EditText, password: EditText): Boolean {
        email.error = null
        password.error = null

        if (email.text.toString() != "" && password.text.toString() != "") {
            return true
        }

        if (email.text.toString() == "") {
            email.error = getString(R.string.error_empty_field)
        }

        if (password.text.toString() == "") {
            password.error = getString(R.string.error_empty_field)
        }
        return false
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
                        if (jsonObject.getString("status") == "error") {
                            val jsonMessage = jsonObject.getString("message")
                            return "Error: $jsonMessage"
                        }
                        var token = jsonObject.getString("token")
                        val jsonTokenObject = JSONObject()
                        jsonTokenObject.put("token", token)
                        token = jsonTokenObject.toString()

                        val getProfileTask = ProfileTask()
                        getProfileTask.execute(token, params[1])
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
            textViewError.text = result
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

                        val date = Date(jsonObject.getInt("birthDate") * 1000L)
                        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
                        val birthDate: String = dateFormat.format(date)

                        val profile = Profile(
                            email = params[1],
                            firstName = jsonObject.getString("firstName"),
                            lastName = jsonObject.getString("lastName"),
                            birthDate = birthDate,
                            notes = jsonObject.getString("notes")
                        ) as Serializable

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

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
        }
    }
}


