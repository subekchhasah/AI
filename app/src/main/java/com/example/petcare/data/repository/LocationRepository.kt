package com.example.petcare.data.repository

import com.example.petcare.data.local.dao.PetLocationDao
import com.example.petcare.data.local.entities.PetLocation
import kotlinx.coroutines.flow.Flow

class LocationRepository(private val locationDao: PetLocationDao) {
    fun getAllLocations(): Flow<List<PetLocation>> = locationDao.getAllLocations()
    fun getLocationsByType(type: String): Flow<List<PetLocation>> = locationDao.getLocationsByType(type)
    fun getLocationById(id: Long): Flow<PetLocation?> = locationDao.getLocationById(id)
    suspend fun insertLocation(location: PetLocation): Long = locationDao.insertLocation(location)
    suspend fun updateLocation(location: PetLocation) = locationDao.updateLocation(location)
    suspend fun deleteLocation(location: PetLocation) = locationDao.deleteLocation(location)
}
