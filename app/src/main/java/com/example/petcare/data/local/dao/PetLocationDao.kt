package com.example.petcare.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.petcare.data.local.entities.PetLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface PetLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: PetLocation): Long

    @Update
    suspend fun updateLocation(location: PetLocation)

    @Delete
    suspend fun deleteLocation(location: PetLocation)

    @Query("SELECT * FROM pet_locations ORDER BY name ASC")
    fun getAllLocations(): Flow<List<PetLocation>>

    @Query("SELECT * FROM pet_locations WHERE type = :type ORDER BY name ASC")
    fun getLocationsByType(type: String): Flow<List<PetLocation>>

    @Query("SELECT * FROM pet_locations WHERE locationId = :id LIMIT 1")
    fun getLocationById(id: Long): Flow<PetLocation?>
}
