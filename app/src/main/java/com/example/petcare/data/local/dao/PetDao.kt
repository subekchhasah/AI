package com.example.petcare.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.petcare.data.local.entities.Pet
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: Pet): Long

    @Update
    suspend fun updatePet(pet: Pet)

    @Delete
    suspend fun deletePet(pet: Pet)

    @Query("SELECT * FROM pets WHERE userId = :userId ORDER BY name ASC")
    fun getPetsByUserId(userId: Long): Flow<List<Pet>>

    @Query("SELECT * FROM pets WHERE petId = :petId LIMIT 1")
    fun getPetById(petId: Long): Flow<Pet?>

    @Query("SELECT * FROM pets WHERE petId = :petId LIMIT 1")
    suspend fun getPetByIdDirect(petId: Long): Pet?
}
