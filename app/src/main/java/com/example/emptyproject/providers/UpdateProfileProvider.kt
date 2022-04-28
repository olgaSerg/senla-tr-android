package com.example.emptyproject.providers

import bolts.CancellationToken
import bolts.Task
import com.example.emptyproject.ApiInterface
import com.example.emptyproject.models.Profile
import com.example.emptyproject.models.State
import com.example.emptyproject.models.ProfileModel
import com.example.emptyproject.models.TokenRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class UpdateProfileProvider {

    fun updateProfileAsync(state: State, cancellationToken: CancellationToken): Task<Unit> {

        return Task.callInBackground {
            if (cancellationToken.isCancellationRequested) {
                throw LoginTaskProvider.CancellationException()
            }
            state.profile = state.token?.let { getProfile(it, state) }
        }
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
        val call: Call<ProfileModel> = apiService.updateProfileRequest(tokenRequest)
        val response: Response<ProfileModel> = call.execute()
        if (!response.isSuccessful) throw IOException()

        if (response.body() != null) {
            if (response.body()?.status == STATUS_OK) {
                return fillProfileObject(response.body()!!, state.email)
            } else {
                throw ProfileTaskProvider.ProfileException("Error: ProfileException")
            }
        }
        throw ProfileTaskProvider.ProfileException("Error: ProfileException")
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
