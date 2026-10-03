package com.example.petcare.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.petcare.data.local.PetCareDatabase
import com.example.petcare.data.local.entities.PetLocation
import com.example.petcare.data.repository.LocationRepository
import kotlinx.coroutines.launch

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: LocationRepository
    val allLocations: LiveData<List<PetLocation>>

    init {
        val dao = PetCareDatabase.getDatabase(application).petLocationDao()
        repository = LocationRepository(dao)
        allLocations = repository.getAllLocations().asLiveData()
    }

    fun getLocationsByType(type: String): LiveData<List<PetLocation>> {
        return repository.getLocationsByType(type).asLiveData()
    }

    fun getLocationById(id: Long): LiveData<PetLocation?> {
        return repository.getLocationById(id).asLiveData()
    }

    fun saveLocation(
        id: Long,
        petId: Long?,
        name: String,
        type: String,
        lat: Double,
        lng: Double,
        address: String,
        notes: String,
        onComplete: (Long) -> Unit,
    ) {
        viewModelScope.launch {
            val location = PetLocation(
                locationId = if (id > 0) id else 0,
                petId = petId,
                name = name,
                type = type,
                latitude = lat,
                longitude = lng,
                address = address,
                notes = notes
            )
            val resultId = if (id > 0) {
                repository.updateLocation(location)
                id
            } else {
                repository.insertLocation(location)
            }
            onComplete(resultId)
        }
    }

    fun deleteLocation(location: PetLocation, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.deleteLocation(location)
            onComplete()
        }
    }
}
