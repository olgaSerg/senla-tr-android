package com.example.emptyproject

import com.example.emptyproject.models.LoginModel
import com.example.emptyproject.models.ProfileModel
import com.example.emptyproject.models.TokenRequest
import com.example.emptyproject.models.TokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST("/senla-training-addition/lesson-20.php?method=login")
    fun sendTokenRequest(
        @Body loginModel: LoginModel
    ): Call<TokenResponse>

    @POST("/senla-training-addition/lesson-20.php?method=profile")
    fun sendProfileRequest(
        @Body tokenRequest: TokenRequest
    ): Call<ProfileModel>

    @POST("/senla-training-addition/lesson-21.php?method=profile")
    fun refreshProfileRequest(
        @Body tokenRequest: TokenRequest
    ): Call<ProfileModel>

}