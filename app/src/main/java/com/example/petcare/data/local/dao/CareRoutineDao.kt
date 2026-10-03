package com.example.petcare.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.petcare.data.local.entities.CareRoutine
import kotlinx.coroutines.flow.Flow

@Dao
interface CareRoutineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: CareRoutine): Long

    @Update
    suspend fun updateRoutine(routine: CareRoutine)

    @Delete
    suspend fun deleteRoutine(routine: CareRoutine)

    @Query("SELECT * FROM care_routines WHERE petId = :petId ORDER BY routineName ASC")
    fun getRoutinesByPetId(petId: Long): Flow<List<CareRoutine>>

    @Query("SELECT * FROM care_routines WHERE routineId = :routineId LIMIT 1")
    fun getRoutineById(routineId: Long): Flow<CareRoutine?>
}
