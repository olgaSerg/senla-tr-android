package com.example.emptyproject

import android.app.Application

class App : Application() {
    private val state = State()
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        var instance: App? = null
            private set
    }

    fun getState(): State {
        return state
    }
}