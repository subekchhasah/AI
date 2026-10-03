package com.example.petcare.data.repository

import com.example.petcare.data.local.dao.PetDao
import com.example.petcare.data.local.entities.Pet
import kotlinx.coroutines.flow.Flow

class PetRepository(private val petDao: PetDao) {
    fun getPetsByUserId(userId: Long): Flow<List<Pet>> = petDao.getPetsByUserId(userId)
    fun getPetById(petId: Long): Flow<Pet?> = petDao.getPetById(petId)
    suspend fun getPetByIdDirect(petId: Long): Pet? = petDao.getPetByIdDirect(petId)
    suspend fun insertPet(pet: Pet): Long = petDao.insertPet(pet)
    suspend fun updatePet(pet: Pet) = petDao.updatePet(pet)
    suspend fun deletePet(pet: Pet) = petDao.deletePet(pet)
}
