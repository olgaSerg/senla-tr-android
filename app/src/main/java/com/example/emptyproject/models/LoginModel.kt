package com.example.emptyproject.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginModel(
    @Json(name = "email")
    val userEmail: String,

    @Json(name = "password")
    val userPassword: String
) {}
