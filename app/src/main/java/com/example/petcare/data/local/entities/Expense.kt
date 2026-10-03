package com.example.petcare.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "expenses",
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
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val expenseId: Long = 0,
    val petId: Long? = null,
    val category: String, // Food, Grooming, Veterinary, Medication, Toys, Supplies, Other
    val description: String,
    val amount: Double,
    val date: String,
    val notes: String = ""
)
