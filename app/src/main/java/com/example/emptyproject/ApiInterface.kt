package com.example.emptyproject

import com.example.emptyproject.models.LoginModel
import com.example.emptyproject.models.ProfileModel
import com.example.emptyproject.models.TokenRequest
import com.example.emptyproject.models.TokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {

    @POST("/senla-training-addition/lesson-20.php?method=login")
    fun sendTokenRequest(
        @Body loginModel: LoginModel
    ): Call<TokenResponse>

    @POST("/senla-training-addition/lesson-{lessonNumber}.php?method=profile")
    fun sendProfileRequest(
        @Path("lessonNumber") lessonNumber: String,
        @Body tokenRequest: TokenRequest
    ): Call<ProfileModel>

}