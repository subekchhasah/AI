package com.example.petcare.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.petcare.data.local.PetCareDatabase
import com.example.petcare.data.local.entities.MedicalRecord
import com.example.petcare.data.repository.MedicalRepository
import kotlinx.coroutines.launch

class MedicalViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MedicalRepository
    val allMedicalRecords: LiveData<List<MedicalRecord>>

    init {
        val dao = PetCareDatabase.getDatabase(application).medicalRecordDao()
        repository = MedicalRepository(dao)
        allMedicalRecords = repository.getAllMedicalRecords().asLiveData()
    }

    fun getRecordsForPet(petId: Long): LiveData<List<MedicalRecord>> {
        return repository.getRecordsByPetId(petId).asLiveData()
    }

    fun getRecordById(id: Long): LiveData<MedicalRecord?> {
        return repository.getRecordById(id).asLiveData()
    }

    fun saveMedicalRecord(
        id: Long,
        petId: Long?,
        type: String,
        title: String,
        date: String,
        vet: String,
        clinic: String,
        notes: String,
        onComplete: (Long) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val record = MedicalRecord(
                    medicalRecordId = if (id > 0) id else 0,
                    petId = petId,
                    recordType = type,
                    title = title,
                    date = date,
                    veterinarian = vet,
                    clinic = clinic,
                    notes = notes
                )
                val resultId = if (id > 0) {
                    repository.updateMedicalRecord(record)
                    id
                } else {
                    repository.insertMedicalRecord(record)
                }
                onComplete(resultId)
            } catch (e: Exception) {
                e.printStackTrace()
                onComplete(-1L)
            }
        }
    }

    fun deleteRecord(record: MedicalRecord, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                repository.deleteMedicalRecord(record)
                onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                onComplete()
            }
        }
    }
}
