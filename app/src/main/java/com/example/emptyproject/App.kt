package com.example.emptyproject

import android.database.sqlite.SQLiteDatabase

import android.app.Application
import com.facebook.stetho.Stetho


class App : Application() {
    var dBHelper: DBHelper? = null
    private var db: SQLiteDatabase? = null
        private set

    override fun onCreate() {
        super.onCreate()

        instance = this
        dBHelper = DBHelper(this)
        db = dBHelper?.writableDatabase

        Stetho.initializeWithDefaults(this);
    }

    companion object {
        var instance: App? = null
            private set
    }
}