package com.example.petcare.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.petcare.data.local.PetCareDatabase
import com.example.petcare.data.local.entities.Pet
import com.example.petcare.data.repository.PetRepository
import com.example.petcare.utils.SessionManager
import kotlinx.coroutines.launch

class PetViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PetRepository
    private val sessionManager: SessionManager = SessionManager(application)

    val pets: LiveData<List<Pet>>

    init {
        val petDao = PetCareDatabase.getDatabase(application).petDao()
        repository = PetRepository(petDao)
        pets = repository.getPetsByUserId(sessionManager.getUserId()).asLiveData()
    }

    fun getPetById(petId: Long): LiveData<Pet?> {
        return repository.getPetById(petId).asLiveData()
    }

    fun savePet(
        petId: Long,
        name: String,
        species: String,
        breed: String,
        dob: String,
        gender: String,
        weight: Double,
        diet: String,
        allergies: String,
        vaccinations: String,
        medicalNotes: String,
        favouriteToys: String,
        notes: String,
        imageUri: String? = null,
        onComplete: (Long) -> Unit,
    ) {
        viewModelScope.launch {
            val userId = sessionManager.getUserId()
            val pet = Pet(
                petId = if (petId > 0) petId else 0,
                userId = userId,
                name = name,
                species = species,
                breed = breed,
                dateOfBirth = dob,
                gender = gender,
                weight = weight,
                dietaryPreferences = diet,
                allergies = allergies,
                vaccinationHistory = vaccinations,
                medicalNotes = medicalNotes,
                favouriteToys = favouriteToys,
                notes = notes,
                imageUri = imageUri
            )
            val id = if (petId > 0) {
                repository.updatePet(pet)
                petId
            } else {
                repository.insertPet(pet)
            }
            onComplete(id)
        }
    }

    fun deletePet(pet: Pet, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.deletePet(pet)
            onComplete()
        }
    }
}
