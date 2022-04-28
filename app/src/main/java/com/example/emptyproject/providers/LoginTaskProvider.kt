package com.example.emptyproject.providers

import android.util.Log
import bolts.CancellationToken
import bolts.Task
import com.example.emptyproject.ApiInterface
import com.example.emptyproject.R
import com.example.emptyproject.fragments.LoginFragment
import com.example.emptyproject.models.LoginModel
import com.example.emptyproject.models.TokenResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException

const val STATUS_OK = "ok"
const val STATUS_ERROR = "error"
const val BASE_URL = "https://pub.zame-dev.org"

class LoginTaskProvider() {

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
            it.result?.let { token ->
                val profileTaskProvider = ProfileTaskProvider()
                profileTaskProvider.loadProfileAsync(
                    token,
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
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService: ApiInterface = retrofit.create(ApiInterface::class.java)
        val call: Call<TokenResponse> = apiService.sendTokenRequest(loginModel)
        val response: Response<TokenResponse> = call.execute()
        if (!response.isSuccessful) throw IOException()

        if (response.body() != null) {
            if (response.body()?.status == STATUS_ERROR) {
                throw LoginException("Error: ${response.body()?.message}")
            }

            if (response.body()?.status == STATUS_OK) {
                return response.body()?.token.toString()
            }
        }
        Log.v("!!", response.body().toString())
        throw LoginException("Error: ${response.body()?.message}")
    }

    class LoginException(message: String) : Exception(message) {}
    class CancellationException() : Exception() {}
}