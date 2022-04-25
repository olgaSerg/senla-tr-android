package com.example.emptyproject

import java.io.Serializable

class State(
    var email: String = "",
    var password: String = "",
    var errorText: String = "",
    var token: String? = null,
    var isTaskStarted: Boolean = false
): Serializable {}