package com.example.petcare.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pet_locations",
    foreignKeys = [
        ForeignKey(
            entity = Pet::class,
            parentColumns = ["petId"],
            childColumns = ["petId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["petId"])]
)
data class PetLocation(
    @PrimaryKey(autoGenerate = true)
    val locationId: Long = 0,
    val petId: Long? = null,
    val name: String,
    val type: String, // Veterinary Clinic, Grooming Salon, Dog Park, Pet Supply Store, Animal Shelter
    val latitude: Double,
    val longitude: Double,
    val address: String = "",
    val notes: String = ""
)
