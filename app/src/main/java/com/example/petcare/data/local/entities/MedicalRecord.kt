package com.example.petcare.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "medical_records",
    indices = [Index(value = ["petId"])]
)
data class MedicalRecord(
    @PrimaryKey(autoGenerate = true)
    val medicalRecordId: Long = 0,
    val petId: Long? = null,
    val recordType: String, // Vaccination, Vet Consultation, Medication, Health Check
    val title: String,
    val date: String,
    val veterinarian: String = "",
    val clinic: String = "",
    val notes: String = "",
    val attachmentUri: String? = null
)

