package com.example.emptyproject.models

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("status")
    var status: String,

    @SerializedName("token")
    var token: String,

    @SerializedName("message")
    val message: String
)
