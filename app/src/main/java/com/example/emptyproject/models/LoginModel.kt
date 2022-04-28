package com.example.emptyproject.models

import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("email")
    val userEmail: String,
    @SerializedName("password")
    val userPassword: String
) {}
