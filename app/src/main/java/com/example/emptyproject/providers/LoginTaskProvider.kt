package com.example.emptyproject.providers

import bolts.CancellationToken
import bolts.Task
import com.example.emptyproject.ApiInterface
import com.example.emptyproject.Exceptions
import com.example.emptyproject.models.LoginModel
import com.example.emptyproject.models.Profile
import com.example.emptyproject.models.State
import com.example.emptyproject.models.TokenResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException

const val STATUS_OK = "ok"
const val STATUS_ERROR = "error"
const val BASE_URL = "https://pub.zame-dev.org"

class LoginTaskProvider {

    fun loginAsync(
        state: State,
        cancellationToken: CancellationToken
    ): Task<Profile> {
        return Task.call({
            if (cancellationToken.isCancellationRequested) {
                throw Exceptions.CancellationException()
            }
            if (state.token == null) {
                val loginModel = LoginModel(state.email, state.password)
                state.token = getToken(loginModel)
            }
        }, Task.BACKGROUND_EXECUTOR).onSuccessTask {
            val profileTaskProvider = ProfileTaskProvider()
            profileTaskProvider.loadProfileAsync(
                state,
                cancellationToken,
                false
            )
        }
    }

    @Throws(Exceptions.LoginException::class)
    private fun getToken(loginModel: LoginModel): String {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val apiService: ApiInterface = retrofit.create(ApiInterface::class.java)
        val call: Call<TokenResponse> = apiService.sendTokenRequest(loginModel)
        val response: Response<TokenResponse> = call.execute()
        if (!response.isSuccessful) throw IOException()

        if (response.body() != null) {
            if (response.body()?.status == STATUS_ERROR) {
                throw Exceptions.LoginException("Error: ${response.body()?.message}")
            }

            if (response.body()?.status == STATUS_OK) {
                return response.body()?.token.toString()
            }
        }
        throw Exceptions.LoginException("Error: ${response.body()?.message}")
    }
}
