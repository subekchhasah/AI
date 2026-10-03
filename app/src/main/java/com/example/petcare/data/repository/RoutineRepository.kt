package com.example.petcare.data.repository

import com.example.petcare.data.local.dao.CareRoutineDao
import com.example.petcare.data.local.entities.CareRoutine
import kotlinx.coroutines.flow.Flow

class RoutineRepository(private val routineDao: CareRoutineDao) {
    fun getRoutinesByPetId(petId: Long): Flow<List<CareRoutine>> = routineDao.getRoutinesByPetId(petId)
    fun getRoutineById(routineId: Long): Flow<CareRoutine?> = routineDao.getRoutineById(routineId)
    suspend fun insertRoutine(routine: CareRoutine): Long = routineDao.insertRoutine(routine)
    suspend fun updateRoutine(routine: CareRoutine) = routineDao.updateRoutine(routine)
    suspend fun deleteRoutine(routine: CareRoutine) = routineDao.deleteRoutine(routine)
}
