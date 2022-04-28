package com.example.emptyproject.models

import java.io.Serializable

data class Profile(
    var email: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var birthDate: String? = null,
    var notes: String? = null,
) : Serializable {}
