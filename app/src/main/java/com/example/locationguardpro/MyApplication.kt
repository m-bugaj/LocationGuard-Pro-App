package com.example.locationguardpro

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.locationguardpro.model.User
import kotlinx.coroutines.runBlocking

class MyApplication : Application() {

    lateinit var appDatabase: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()

        appDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app-database"
        )
            .fallbackToDestructiveMigration()
            .build()

        // Sprawdź, czy domyślny użytkownik został już dodany
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)

        if (isFirstRun) {
            // Dodaj domyślnego użytkownika do bazy danych
            addDefaultUserToDatabase()

            // Ustaw flagę, że już dodano domyślnego użytkownika
            sharedPreferences.edit().putBoolean("isFirstRun", false).apply()
        }
    }

    private fun addDefaultUserToDatabase() {
        val defaultUser = User(username = "Jan Kowalski", email = "jan@kowalski.com", password = "Abcd1234")
        runBlocking {
            appDatabase.userDao().insertUser(defaultUser)
        }
    }
}