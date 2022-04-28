package com.example.emptyproject.providers

import bolts.CancellationToken
import bolts.Task
import com.example.emptyproject.ApiInterface
import com.example.emptyproject.Profile
import com.example.emptyproject.State
import com.example.emptyproject.fragments.LoginFragment
import com.example.emptyproject.models.ProfileModel
import com.example.emptyproject.models.TokenRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class ProfileTaskProvider {

    fun loadProfileAsync(
        token: String,
        loginFragment: LoginFragment,
        cancellationToken: CancellationToken
    ): Task<Unit> {
        val state = loginFragment.getState()

        return Task.callInBackground {
            if (cancellationToken.isCancellationRequested) {
                throw LoginTaskProvider.CancellationException()
            }
            state?.let { getProfile(token, it) }
        }.onSuccess({
            if (cancellationToken.isCancellationRequested) {
                throw LoginTaskProvider.CancellationException()
            }
            loginFragment.dataSendListener?.sendProfile(it.result)
            state?.profile = it.result
        }, Task.UI_THREAD_EXECUTOR)
    }

    private fun getProfile(token: String, state: State): Profile {
        val tokenRequest = TokenRequest(token)
        return fillProfile(tokenRequest, state)
    }

    private fun fillProfile(tokenRequest: TokenRequest, state: State): Profile {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService: ApiInterface = retrofit.create(ApiInterface::class.java)
        val call: Call<ProfileModel> = apiService.sendProfileRequest(tokenRequest)
        val response: Response<ProfileModel> = call.execute()
        if (!response.isSuccessful) throw IOException()

        if (response.body() != null) {
            if (response.body()?.status == STATUS_OK) {
                return fillProfileObject(response.body()!!, state.email)
            } else {
                throw ProfileException("Error: ProfileException")
            }
        }
        throw ProfileException("Error: ProfileException")
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

    class ProfileException(message: String) : Exception(message) {}
}