package com.example.emptyproject

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.io.Serializable
import android.app.Activity
import java.lang.ClassCastException


const val URL = "https://pub.zame-dev.org/senla-training-addition/lesson-20.php?method="

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var editTextEmail: EditText? = null
    private var editTextPassword: EditText? = null
    private var buttonLogin: Button? = null
    private var textViewError: TextView? = null
    private var progressBar: ProgressBar? = null
    var dataSendListener: OnDataSendListener? = null

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

        editTextEmail = view.findViewById(R.id.edit_text_email)
        editTextPassword = view.findViewById(R.id.edit_text_password)
        buttonLogin = view.findViewById(R.id.button_login)
        textViewError = view.findViewById(R.id.text_view_error)
        progressBar = view.findViewById(R.id.progress_bar)

        val editTextEmail = editTextEmail ?: return
        val editTextPassword = editTextPassword ?: return
        val buttonLogin = buttonLogin ?: return

        buttonLogin.setOnClickListener {
            if (!checkLogin(editTextEmail, editTextPassword)) {
                return@setOnClickListener
            }
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            val jsonObject = JSONObject("{\"email\":\"$email\", \"password\":\"$password\"}")
            val myTask = LoginTask()
            myTask.execute(jsonObject.toString(), email)
        }
    }

    private fun checkLogin(email: EditText, password: EditText): Boolean {
        email.error = null
        password.error = null

        if (email.text.toString() != "" && password.text.toString() != "") {
            return true
        }

        if (email.text.toString() == "") {
            email.error = "Заполните пустое поле"
        }

        if (password.text.toString() == "") {
            password.error = "Заполните пустое поле"
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
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val jsonData: String = response.body!!.string()
                val jsonObject = JSONObject(jsonData)
                if (jsonObject.getString("status") == "error") {
                    val jsonMessage = jsonObject.getString("message")
                    return "Error: $jsonMessage"
                }
                var token = jsonObject.getString("token")
                token = JSONObject("{\"token\":\"$token\"}").toString()
                val getProfileTask = ProfileTask()
                getProfileTask.execute(token, params[1])
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

    inner class ProfileTask(): AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String? {
            val client = OkHttpClient()
            val requestBody = params[0]?.toRequestBody()
            val request = Request.Builder()
                .method("POST", requestBody)
                .url(URL + "profile")
                .build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val jsonData: String = response.body!!.string()
                val jsonObject = JSONObject(jsonData)


                val profile = Profile(
                    email = params[1],
                    firstName = jsonObject.getString("firstName"),
                    lastName = jsonObject.getString("lastName"),
                    birthDate = jsonObject.getString("birthDate"),
                    notes = jsonObject.getString("notes")
                ) as Serializable
                println(profile)

                dataSendListener?.sendProfile(profile)

//                val bundle = Bundle()
//                bundle.putSerializable("profile", profile)
                return null
            }
        }
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
        }
    }
}


