package com.example.petcare.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "care_tasks",
    foreignKeys = [
        ForeignKey(
            entity = Pet::class,
            parentColumns = ["petId"],
            childColumns = ["petId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = CareRoutine::class,
            parentColumns = ["routineId"],
            childColumns = ["routineId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["petId"]), Index(value = ["routineId"])]
)
data class CareTask(
    @PrimaryKey(autoGenerate = true)
    val taskId: Long = 0,
    val petId: Long? = null,
    val routineId: Long? = null,
    val title: String,
    val description: String = "",
    val category: String, // Feeding, Exercise, Grooming, Medication, Healthcare, Cleaning, Other
    val scheduledDate: String, // YYYY-MM-DD
    val scheduledTime: String, // HH:mm
    val frequency: String = "Daily",
    val requiredSupplies: String = "",
    val notes: String = "",
    val priority: String = "Normal", // Low, Normal, High
    val reminderEnabled: Boolean = true,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null
)
