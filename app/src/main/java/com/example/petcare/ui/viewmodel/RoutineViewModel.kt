package com.example.petcare.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.petcare.data.local.PetCareDatabase
import com.example.petcare.data.local.entities.CareRoutine
import com.example.petcare.data.repository.RoutineRepository
import kotlinx.coroutines.launch

class RoutineViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RoutineRepository

    init {
        val routineDao = PetCareDatabase.getDatabase(application).careRoutineDao()
        repository = RoutineRepository(routineDao)
    }

    fun getRoutinesForPet(petId: Long): LiveData<List<CareRoutine>> {
        return repository.getRoutinesByPetId(petId).asLiveData()
    }

    fun getRoutineById(routineId: Long): LiveData<CareRoutine?> {
        return repository.getRoutineById(routineId).asLiveData()
    }

    fun saveRoutine(
        routineId: Long,
        petId: Long,
        name: String,
        description: String,
        frequency: String,
        startDate: String,
        endDate: String,
        notes: String,
        onComplete: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val routine = CareRoutine(
                routineId = if (routineId > 0) routineId else 0,
                petId = petId,
                routineName = name,
                description = description,
                frequency = frequency,
                startDate = startDate,
                endDate = endDate,
                notes = notes
            )
            val id = if (routineId > 0) {
                repository.updateRoutine(routine)
                routineId
            } else {
                repository.insertRoutine(routine)
            }
            onComplete(id)
        }
    }

    fun deleteRoutine(routine: CareRoutine, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.deleteRoutine(routine)
            onComplete()
        }
    }
}
