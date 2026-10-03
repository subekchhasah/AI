package com.example.petcare.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.petcare.data.local.entities.CareTask
import kotlinx.coroutines.flow.Flow

@Dao
interface CareTaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: CareTask): Long

    @Update
    suspend fun updateTask(task: CareTask)

    @Delete
    suspend fun deleteTask(task: CareTask)

    @Query("SELECT * FROM care_tasks WHERE scheduledDate = :date ORDER BY scheduledTime ASC")
    fun getTasksByDate(date: String): Flow<List<CareTask>>

    @Query("SELECT * FROM care_tasks ORDER BY isCompleted ASC, scheduledDate ASC, scheduledTime ASC")
    fun getAllTasks(): Flow<List<CareTask>>

    @Query("SELECT * FROM care_tasks WHERE petId = :petId AND scheduledDate = :date ORDER BY scheduledTime ASC")
    fun getTasksByPetAndDate(petId: Long, date: String): Flow<List<CareTask>>

    @Query("SELECT * FROM care_tasks WHERE petId = :petId ORDER BY scheduledDate DESC, scheduledTime ASC")
    fun getTasksByPetId(petId: Long): Flow<List<CareTask>>

    @Query("SELECT * FROM care_tasks WHERE taskId = :taskId LIMIT 1")
    fun getTaskById(taskId: Long): Flow<CareTask?>

    @Query("SELECT * FROM care_tasks WHERE taskId = :taskId LIMIT 1")
    suspend fun getTaskByIdDirect(taskId: Long): CareTask?

    @Query("SELECT COUNT(*) FROM care_tasks WHERE scheduledDate = :date AND isCompleted = 1")
    fun getCompletedTaskCount(date: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM care_tasks WHERE scheduledDate = :date AND isCompleted = 0")
    fun getOutstandingTaskCount(date: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM care_tasks WHERE isCompleted = 1")
    fun getTotalCompletedCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM care_tasks WHERE isCompleted = 0")
    fun getTotalOutstandingCount(): Flow<Int>
}
