package com.example.locationguardpro.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.locationguardpro.model.WorkHours

@Dao
interface WorkHoursDao {
    @Insert
    suspend fun insertWorkHours(workHours: WorkHours)

    @Query("SELECT * FROM work_hours WHERE userId = :userId")
    suspend fun getWorkHoursByUserId(userId: Long): List<WorkHours>

    @Query("SELECT SUM(hoursWorked) FROM work_hours WHERE userId = :userId")
    suspend fun getTotalHoursWorkedByUser(userId: Long): Int

    @Query("SELECT SUM(hoursWorked) AS totalHours FROM work_hours WHERE userId = :userId AND date = :selectedDate")
    suspend fun getTotalHoursForDate(userId: Long, selectedDate: String): Double
}