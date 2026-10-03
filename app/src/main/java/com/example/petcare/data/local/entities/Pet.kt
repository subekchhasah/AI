package com.example.petcare.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pets",
    indices = [Index(value = ["userId"])]
)
data class Pet(
    @PrimaryKey(autoGenerate = true)
    val petId: Long = 0,
    val userId: Long = 1L,
    val name: String,
    val species: String,
    val breed: String,
    val dateOfBirth: String,
    val gender: String,
    val weight: Double,
    val dietaryPreferences: String = "",
    val allergies: String = "",
    val vaccinationHistory: String = "",
    val medicalNotes: String = "",
    val favouriteToys: String = "",
    val notes: String = "",
    val imageUri: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
