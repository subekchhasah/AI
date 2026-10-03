package com.example.petcare.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.petcare.data.local.entities.MedicalRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicalRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicalRecord(record: MedicalRecord): Long

    @Update
    suspend fun updateMedicalRecord(record: MedicalRecord)

    @Delete
    suspend fun deleteMedicalRecord(record: MedicalRecord)

    @Query("SELECT * FROM medical_records WHERE petId = :petId ORDER BY date DESC")
    fun getRecordsByPetId(petId: Long): Flow<List<MedicalRecord>>

    @Query("SELECT * FROM medical_records ORDER BY date DESC")
    fun getAllMedicalRecords(): Flow<List<MedicalRecord>>

    @Query("SELECT * FROM medical_records WHERE medicalRecordId = :id LIMIT 1")
    fun getRecordById(id: Long): Flow<MedicalRecord?>
}
