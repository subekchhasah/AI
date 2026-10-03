package com.example.petcare.data.repository

import com.example.petcare.data.local.dao.MedicalRecordDao
import com.example.petcare.data.local.entities.MedicalRecord
import kotlinx.coroutines.flow.Flow

class MedicalRepository(private val medicalRecordDao: MedicalRecordDao) {
    fun getRecordsByPetId(petId: Long): Flow<List<MedicalRecord>> = medicalRecordDao.getRecordsByPetId(petId)
    fun getAllMedicalRecords(): Flow<List<MedicalRecord>> = medicalRecordDao.getAllMedicalRecords()
    fun getRecordById(id: Long): Flow<MedicalRecord?> = medicalRecordDao.getRecordById(id)
    suspend fun insertMedicalRecord(record: MedicalRecord): Long = medicalRecordDao.insertMedicalRecord(record)
    suspend fun updateMedicalRecord(record: MedicalRecord) = medicalRecordDao.updateMedicalRecord(record)
    suspend fun deleteMedicalRecord(record: MedicalRecord) = medicalRecordDao.deleteMedicalRecord(record)
}
