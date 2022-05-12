package com.example.emptyproject.providers

import bolts.CancellationToken
import bolts.Task
import com.example.emptyproject.ApiInterface
import com.example.emptyproject.Exceptions
import com.example.emptyproject.models.Profile
import com.example.emptyproject.models.State
import com.example.emptyproject.models.ProfileModel
import com.example.emptyproject.models.TokenRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class ProfileTaskProvider {

    fun loadProfileAsync(
        state: State,
        cancellationToken: CancellationToken,
        refresh: Boolean
    ): Task<Profile> {
        var profile = Profile()
        return Task.callInBackground {
            if (cancellationToken.isCancellationRequested) {
                throw CancellationException()
            }
            if (state.token != null) {
                profile = getProfile(state.token!!, state, refresh)
            }
            profile
        }.onSuccess({
            if (cancellationToken.isCancellationRequested) {
                throw CancellationException()
            }
            state.profile = it.result
            if (state.profile == null) {
                throw ProfileException("Error: ProfileException")
            }
            state.profile
        }, Task.BACKGROUND_EXECUTOR)
    }

    private fun getProfile(token: String, state: State, refresh: Boolean): Profile {
        val tokenRequest = TokenRequest(token)
        return fillProfile(tokenRequest, state, refresh)
    }

    @Throws(Exceptions.LoginException::class)
    private fun fillProfile(tokenRequest: TokenRequest, state: State, refresh: Boolean): Profile {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val apiService: ApiInterface = retrofit.create(ApiInterface::class.java)
        val call: Call<ProfileModel> =
            if (refresh)
                apiService.sendProfileRequest("21", tokenRequest)
            else
                apiService.sendProfileRequest("20", tokenRequest)
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

    class ProfileException(message: String) : Exception(message)
    class CancellationException() : Exception()
}