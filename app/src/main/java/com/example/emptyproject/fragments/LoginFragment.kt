package com.example.emptyproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.content.Context
import androidx.core.widget.addTextChangedListener
import bolts.Task
import java.lang.ClassCastException
import java.util.*
import bolts.CancellationTokenSource
import com.example.emptyproject.MainActivity
import com.example.emptyproject.Profile
import com.example.emptyproject.R
import com.example.emptyproject.State
import com.example.emptyproject.providers.LoginTaskProvider
import com.example.emptyproject.providers.ProfileTaskProvider
import java.io.IOException
import java.net.SocketTimeoutException

const val URL = "https://pub.zame-dev.org/senla-training-addition/lesson-20.php?method="
const val TOKEN = "token"

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var editTextEmail: EditText? = null
    private var editTextPassword: EditText? = null
    private var buttonLogin: Button? = null
    private var textViewError: TextView? = null
    private var progressBar: ProgressBar? = null
    private var dataSendListener: OnDataSendListener? = null
    private var state: State? = null
    private var cancellationTokenSource: CancellationTokenSource? = null

    companion object {

        fun newInstance(state: State): Fragment {
            val args = Bundle()
            args.putSerializable(MainActivity.STATE, state)
            val loginFragment = LoginFragment()
            loginFragment.arguments = args
            return loginFragment
        }
    }

    interface OnDataSendListener {
        fun sendProfile(profile: Profile)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        cancellationTokenSource = CancellationTokenSource()

        dataSendListener = try {
            activity as OnDataSendListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement OnDataSendListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeFields(view)

        val buttonLogin = buttonLogin ?: return
        val editTextEmail = editTextEmail ?: return
        val editTextPassword = editTextPassword ?: return
        state = arguments?.getSerializable(MainActivity.STATE) as State
        val state = state ?: return

        displayState()
        setButtonLoginClickListener(buttonLogin)

        if (state.isTaskStarted) {
            buttonLogin.callOnClick()
        }

        editTextEmail.addTextChangedListener {
            state.email = it.toString()
        }
        editTextPassword.addTextChangedListener {
            state.password = it.toString()
        }
    }

    override fun onPause() {
        super.onPause()

        cancellationTokenSource?.cancel()
    }

    private fun initializeFields(view: View) {
        editTextEmail = view.findViewById(R.id.edit_text_email)
        editTextPassword = view.findViewById(R.id.edit_text_password)
        buttonLogin = view.findViewById(R.id.button_login)
        textViewError = view.findViewById(R.id.text_view_error)
        progressBar = view.findViewById(R.id.progress_bar)
    }

    private fun displayState() {
        val textViewError = textViewError ?: return
        val editTextEmail = editTextEmail ?: return
        val editTextPassword = editTextPassword ?: return
        val buttonLogin = buttonLogin ?: return
        val progressBar = progressBar ?: return
        val state = state ?: return

        textViewError.text = state.errorText
        editTextEmail.setText(state.email)
        editTextPassword.setText(state.password)

        if (state.isTaskStarted) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }

        state.isTaskStarted = !buttonLogin.isEnabled
    }

    private fun setButtonLoginClickListener(button: Button) {
        val editTextEmail = editTextEmail ?: return
        val editTextPassword = editTextPassword ?: return

        button.setOnClickListener {
            state?.errorText = ""
            displayState()
            if (isFieldEmpty(editTextEmail) || isFieldEmpty(editTextPassword)) {
                showError(editTextEmail)
                showError(editTextPassword)
                return@setOnClickListener
            }

            cancellationTokenSource = CancellationTokenSource()

            if (cancellationTokenSource != null) {
                loginAsync()
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

    private fun loginAsync() {
        if (state != null) {
            Task.call({
                state!!.isTaskStarted = true
                displayState()
            }, Task.UI_THREAD_EXECUTOR).onSuccessTask {
                val loginTaskProvider = LoginTaskProvider()
                loginTaskProvider.loginAsync(state!!, cancellationTokenSource!!.token)
            }.continueWith(finish@{
                state?.isTaskStarted = false
                if (it.isFaulted) {
                    when (it.error) {
                        is SocketTimeoutException -> {
                            state?.errorText = getString(R.string.error_connection)
                        }
                        is IOException -> {
                            state?.errorText = getString(R.string.error_message)
                        }
                        is LoginException -> {
                            state?.errorText = it.error.message.toString()
                        }
                        is ProfileTaskProvider.ProfileException -> {
                            state?.errorText = it.error.message.toString()
                        }
                        is CancellationException -> {
                            return@finish
                        }
                    }
                }
                displayState()
                dataSendListener?.sendProfile(it.result)
            }, Task.UI_THREAD_EXECUTOR)
        }
    }

    class LoginException(message: String) : Exception(message)
    class CancellationException : Exception()
}
