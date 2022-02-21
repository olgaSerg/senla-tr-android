package com.example.emptyproject

import java.io.Serializable

data class PersonInformation (
    val login: String? = null,
    val password: String? = null,
    val name: String? = null,
    val surname: String? = null,
    val gender: String? = null,
    val additionalInformation: String? = null
    ) : Serializable
