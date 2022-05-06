package com.example.emptyproject.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenResponse(
    @Json(name = "status")
    var status: String,

    @Json(name = "token")
    var token: String,

    @Json(name = "message")
    val message: String?
)
