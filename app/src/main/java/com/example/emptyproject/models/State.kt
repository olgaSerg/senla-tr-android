package com.example.emptyproject.models

import java.io.Serializable

class State(
    var profile: Profile? = null,
    var email: String = "",
    var password: String = "",
    var errorText: String = "",
    var token: String? = null,
    var isTaskStarted: Boolean = false
): Serializable {}