package com.example.petcare.data.repository

import com.example.petcare.data.local.dao.CareTaskDao
import com.example.petcare.data.local.entities.CareTask
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: CareTaskDao) {
    fun getTasksByDate(date: String): Flow<List<CareTask>> = taskDao.getTasksByDate(date)
    fun getAllTasks(): Flow<List<CareTask>> = taskDao.getAllTasks()
    fun getTasksByPetAndDate(petId: Long, date: String): Flow<List<CareTask>> = taskDao.getTasksByPetAndDate(petId, date)
    fun getTasksByPetId(petId: Long): Flow<List<CareTask>> = taskDao.getTasksByPetId(petId)
    fun getTaskById(taskId: Long): Flow<CareTask?> = taskDao.getTaskById(taskId)
    suspend fun getTaskByIdDirect(taskId: Long): CareTask? = taskDao.getTaskByIdDirect(taskId)
    fun getCompletedTaskCount(date: String): Flow<Int> = taskDao.getCompletedTaskCount(date)
    fun getOutstandingTaskCount(date: String): Flow<Int> = taskDao.getOutstandingTaskCount(date)
    fun getTotalCompletedCount(): Flow<Int> = taskDao.getTotalCompletedCount()
    fun getTotalOutstandingCount(): Flow<Int> = taskDao.getTotalOutstandingCount()
    suspend fun insertTask(task: CareTask): Long = taskDao.insertTask(task)
    suspend fun updateTask(task: CareTask) = taskDao.updateTask(task)
    suspend fun deleteTask(task: CareTask) = taskDao.deleteTask(task)
    
    suspend fun setTaskCompleted(task: CareTask, isCompleted: Boolean) {
        val updatedTask = task.copy(
            isCompleted = isCompleted,
            completedAt = if (isCompleted) System.currentTimeMillis() else null
        )
        taskDao.updateTask(updatedTask)
    }
}
