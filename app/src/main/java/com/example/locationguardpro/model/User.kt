package com.example.locationguardpro.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0,
    val username: String,
    val email: String,
    val password: String,
    val isAdmin: Boolean = false
)