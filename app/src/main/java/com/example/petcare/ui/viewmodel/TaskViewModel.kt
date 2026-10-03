package com.example.petcare.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.petcare.data.local.PetCareDatabase
import com.example.petcare.data.local.entities.CareTask
import com.example.petcare.data.repository.TaskRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository
    val todayDateStr: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    val todaysTasks: LiveData<List<CareTask>>
    val allTasks: LiveData<List<CareTask>>
    val completedCount: LiveData<Int>
    val outstandingCount: LiveData<Int>

    init {
        val taskDao = PetCareDatabase.getDatabase(application).careTaskDao()
        repository = TaskRepository(taskDao)
        todaysTasks = repository.getAllTasks().asLiveData()
        allTasks = repository.getAllTasks().asLiveData()
        completedCount = repository.getTotalCompletedCount().asLiveData()
        outstandingCount = repository.getTotalOutstandingCount().asLiveData()
    }

    fun getTasksForPet(petId: Long): LiveData<List<CareTask>> {
        return repository.getTasksByPetId(petId).asLiveData()
    }

    fun getTaskById(taskId: Long): LiveData<CareTask?> {
        return repository.getTaskById(taskId).asLiveData()
    }

    fun toggleTaskCompletion(task: CareTask) {
        viewModelScope.launch {
            repository.setTaskCompleted(task, !task.isCompleted)
        }
    }

    fun saveTask(
        taskId: Long,
        petId: Long?,
        routineId: Long?,
        title: String,
        description: String,
        category: String,
        date: String,
        time: String,
        frequency: String,
        supplies: String,
        notes: String,
        priority: String,
        reminderEnabled: Boolean,
        onComplete: (Long) -> Unit,
    ) {
        viewModelScope.launch {
            val task = CareTask(
                taskId = if (taskId > 0) taskId else 0,
                petId = petId,
                routineId = routineId,
                title = title,
                description = description,
                category = category,
                scheduledDate = date,
                scheduledTime = time,
                frequency = frequency,
                requiredSupplies = supplies,
                notes = notes,
                priority = priority,
                reminderEnabled = reminderEnabled
            )
            val id = if (taskId > 0) {
                repository.updateTask(task)
                taskId
            } else {
                repository.insertTask(task)
            }

            if (reminderEnabled) {
                val savedTask = task.copy(taskId = id)
                try {
                    val formats = listOf(
                        SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()),
                        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()),
                        SimpleDateFormat("yyyy-MM-dd h:mm a", Locale.getDefault()),
                        SimpleDateFormat("yyyy-MM-dd H:m", Locale.getDefault())
                    )
                    var parsedTime: Long? = null
                    val dateTimeStr = "$date $time"
                    for (sdf in formats) {
                        try {
                            val parsed = sdf.parse(dateTimeStr)
                            if (parsed != null) {
                                parsedTime = parsed.time
                                break
                            }
                        } catch (e: Exception) {
                            // Try next format
                        }
                    }

                    var triggerTime = parsedTime ?: System.currentTimeMillis()
                    
                    // If time is in the past, schedule 3 seconds from now so the user can test the popup immediately
                    if (triggerTime <= System.currentTimeMillis()) {
                        triggerTime = System.currentTimeMillis() + 3000
                    }

                    com.example.petcare.notifications.NotificationScheduler.scheduleTaskReminder(
                        context = getApplication(),
                        task = savedTask,
                        petName = "Pet",
                        triggerTimeMillis = triggerTime
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            onComplete(id)
        }
    }

    fun deleteTask(task: CareTask, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.deleteTask(task)
            onComplete()
        }
    }
}