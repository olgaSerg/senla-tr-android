package com.example.emptyproject.providers

import bolts.CancellationToken
import bolts.Task
import com.example.emptyproject.Profile
import com.example.emptyproject.State
import com.example.emptyproject.fragments.LoginFragment
import com.example.emptyproject.fragments.TOKEN
import com.example.emptyproject.fragments.URL
import com.example.emptyproject.models.ProfileModel
import com.example.emptyproject.models.ResponseModel
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

const val URL_NEW = "httpS://pub.zame-dev.org/senla-training-addition/lesson-21.php?method="

class UpdateProfileProvider {

    val updateProfile: Task<Unit>? = null

    companion object {
        fun updateProfileAsync(token: String, loginFragment: LoginFragment, cancellationToken: CancellationToken): Task<Unit> {
            val state = loginFragment.getState()

            return Task.callInBackground {
                if (cancellationToken.isCancellationRequested) {
                    throw LoginTaskProvider.CancellationException()
                }
                getProfile(token, state)
            }.onSuccess({
                if (cancellationToken.isCancellationRequested) {
                    throw LoginTaskProvider.CancellationException()
                }
                loginFragment.dataSendListener!!.sendProfile(it.result)
            }, Task.UI_THREAD_EXECUTOR)
        }

        private fun getProfile(token: String, state: State): Profile {
            val jsonTokenObject = JSONObject()
            jsonTokenObject.put(TOKEN, token)
            jsonTokenObject.toString()

            val client = OkHttpClient()
            val requestBody = jsonTokenObject.toString().toRequestBody()
            val request = Request.Builder()
                .method("POST", requestBody)
                .url(URL_NEW + "profile")
                .build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                if (response.body != null) {
                    val jsonData: String = response.body!!.string()
                    val gson = Gson()
                    val profileModel = gson.fromJson(jsonData, ProfileModel::class.java)
                    if (profileModel.status == STATUS_OK) {

                        return fillProfileObject(profileModel, state.email)
                    } else {
                        val responseModel =
                            gson.fromJson(jsonData, ResponseModel::class.java)
                        val jsonMessage = responseModel.message
                        throw ProfileException("Error: $jsonMessage")
                    }
                }
            }

            throw ProfileException("Error: ")
        }

        private fun fillProfileObject(profileModel: ProfileModel, email: String): Profile {
            return Profile(
                email = email,
                firstName = profileModel.firstName,
                lastName = profileModel.lastName,
                birthDate = getBirthDateFormatted(profileModel.birthDate),
                notes = profileModel.notes
            )
        }

        private fun getBirthDateFormatted(birthDate: Int): String {
            val date = Date(birthDate * 1000L)
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
            return dateFormat.format(date)
        }
    }

    class ProfileException(message: String) : Exception(message) {}
}
