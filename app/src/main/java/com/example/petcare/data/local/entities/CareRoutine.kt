package com.example.petcare.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "care_routines",
    foreignKeys = [
        ForeignKey(
            entity = Pet::class,
            parentColumns = ["petId"],
            childColumns = ["petId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["petId"])]
)
data class CareRoutine(
    @PrimaryKey(autoGenerate = true)
    val routineId: Long = 0,
    val petId: Long,
    val routineName: String,
    val description: String = "",
    val frequency: String, // Daily, Weekly, Recurring
    val startDate: String,
    val endDate: String = "",
    val notes: String = "",
    val imageUri: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
