package com.example.locationguardpro

import android.app.Application
import androidx.room.Room

class MyApplication : Application() {

    lateinit var appDatabase: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()

        appDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app-database"
        ).build()
    }
}