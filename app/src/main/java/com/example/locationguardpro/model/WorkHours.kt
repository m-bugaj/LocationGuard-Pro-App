package com.example.locationguardpro.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "work_hours",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("userId")]
)
data class WorkHours(
    @PrimaryKey(autoGenerate = true)
    val workHoursId: Long = 0,
    val userId: Long,
    val date: String, // Przyjmuję, że data jest przechowywana jako tekst
    val hoursWorked: Int
)