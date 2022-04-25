package com.example.emptyproject.providers

import bolts.CancellationToken
import bolts.Task
import com.example.emptyproject.R
import com.example.emptyproject.fragments.LoginFragment
import com.example.emptyproject.fragments.TOKEN
import com.example.emptyproject.fragments.URL
import com.example.emptyproject.models.LoginModel
import com.example.emptyproject.models.ResponseModel
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.net.SocketTimeoutException

const val STATUS_OK = "ok"
const val STATUS_ERROR = "error"

class LoginTaskProvider {

    companion object {
        fun loginAsync(
            loginFragment: LoginFragment,
            cancellationToken: CancellationToken
        ): Task<Unit> {

            val state = loginFragment.getState()
            return Task.call({
                state?.isTaskStarted = true
                loginFragment.displayState()
            }, Task.UI_THREAD_EXECUTOR).onSuccess(get_token@{
                if (cancellationToken.isCancellationRequested) {
                    throw CancellationException()
                }

                if (state?.token == null && state != null) {
                    val loginModel = LoginModel(state.email, state.password)
                    state.token = getToken(loginModel)
                }
                return@get_token state?.token
            }, Task.BACKGROUND_EXECUTOR).onSuccessTask {
                it.result?.let { it1 ->
                    ProfileTaskProvider.loadProfileAsync(
                        it1,
                        loginFragment,
                        cancellationToken
                    )
                }
            }.continueWith(finish@{
                state?.isTaskStarted = false

                if (it.isFaulted && state != null) {
                    when (it.error) {
                        is SocketTimeoutException -> {
                            state.errorText = loginFragment.getString(R.string.error_connection)
                        }
                        is IOException -> {
                            state.errorText = loginFragment.getString(R.string.error_message)
                        }
                        is LoginException -> {
                            state.errorText = it.error.message.toString()
                        }
                        is ProfileTaskProvider.ProfileException -> {
                            state.errorText = it.error.message.toString()
                        }
                        is CancellationException -> {
                            return@finish
                        }
                    }
                }
                loginFragment.displayState()
            }, Task.UI_THREAD_EXECUTOR)
        }

        private fun getToken(loginModel: LoginModel): String {
            val gson = Gson()
            val loginModelGson = gson.toJson(loginModel)

            val client = OkHttpClient()
            val requestBody = loginModelGson.toRequestBody()
            val request = Request.Builder()
                .method("POST", requestBody)
                .url(URL + "login")
                .build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException()

                if (response.body != null) {
                    val jsonData: String? = response.body?.string()
                    val responseModel =
                        gson.fromJson(jsonData, ResponseModel::class.java)
                    if (responseModel.status == STATUS_ERROR) {
                        val jsonMessage = responseModel.message
                        throw LoginException("Error: $jsonMessage")
                    }

                    if (responseModel.status == STATUS_OK) {
                        val jsonObject = JSONObject(jsonData)
                        return jsonObject.getString(TOKEN)

                    }
                }
            }

            throw LoginException("Error: ")
        }
    }

    class LoginException(message: String) : Exception(message) {}
    class CancellationException() : Exception() {}
}