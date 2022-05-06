package com.example.emptyproject.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenRequest(
    @Json(name = "token")
    var token: String
)