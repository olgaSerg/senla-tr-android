package com.example.emptyproject.models

import com.google.gson.annotations.SerializedName

class TokenRequest(
    @SerializedName("token")
    var token: String
)