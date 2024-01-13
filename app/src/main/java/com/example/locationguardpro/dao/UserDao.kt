package com.example.locationguardpro.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.locationguardpro.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserById(userId: Long): User?
}
