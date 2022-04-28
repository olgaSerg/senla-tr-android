package com.example.emptyproject.models

import com.google.gson.annotations.SerializedName

class ProfileModel(
    @SerializedName("status")
    val status: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("birthDate")
    val birthDate: Int,
    @SerializedName("notes")
    val notes: String
) {}