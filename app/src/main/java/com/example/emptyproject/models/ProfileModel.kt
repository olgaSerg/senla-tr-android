package com.example.emptyproject.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProfileModel(
    @Json(name = "status")
    val status: String,
    @Json(name = "firstName")
    val firstName: String,
    @Json(name = "lastName")
    val lastName: String,
    @Json(name = "birthDate")
    val birthDate: Int,
    @Json(name = "notes")
    val notes: String
) {}