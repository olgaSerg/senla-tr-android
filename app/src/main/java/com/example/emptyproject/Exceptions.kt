package com.example.emptyproject

class Exceptions {
    class LoginException(message: String) : Exception(message) {}
    class CancellationException() : Exception() {}
}