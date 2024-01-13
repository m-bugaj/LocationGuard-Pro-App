package com.example.locationguardpro

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.locationguardpro.dao.UserDao
import com.example.locationguardpro.dao.WorkHoursDao
import com.example.locationguardpro.model.User
import com.example.locationguardpro.model.WorkHours

@Database(entities = [User::class, WorkHours::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun workHoursDao(): WorkHoursDao
}