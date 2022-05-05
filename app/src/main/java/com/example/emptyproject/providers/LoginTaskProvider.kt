package com.example.emptyproject.providers

import bolts.CancellationToken
import bolts.Task
import com.example.emptyproject.Profile
import com.example.emptyproject.State
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

const val STATUS_OK = "ok"
const val STATUS_ERROR = "error"

class LoginTaskProvider {

    fun loginAsync(
        state: State,
        cancellationToken: CancellationToken
    ): Task<Profile> {
        return Task.call({
            if (cancellationToken.isCancellationRequested) {
                throw LoginFragment.CancellationException()
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
                    throw LoginFragment.LoginException("Error: $jsonMessage")
                }

                if (responseModel.status == STATUS_OK) {
                    val jsonObject = JSONObject(jsonData)
                    return jsonObject.getString(TOKEN)

                }
            }
        }

        throw LoginFragment.LoginException("Error: ")
    }
}