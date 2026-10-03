package com.example.petcare.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.petcare.`data`.local.entities.CareTask
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class CareTaskDao_Impl(
  __db: RoomDatabase,
) : CareTaskDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfCareTask: EntityInsertAdapter<CareTask>

  private val __deleteAdapterOfCareTask: EntityDeleteOrUpdateAdapter<CareTask>

  private val __updateAdapterOfCareTask: EntityDeleteOrUpdateAdapter<CareTask>
  init {
    this.__db = __db
    this.__insertAdapterOfCareTask = object : EntityInsertAdapter<CareTask>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `care_tasks` (`taskId`,`petId`,`routineId`,`title`,`description`,`category`,`scheduledDate`,`scheduledTime`,`frequency`,`requiredSupplies`,`notes`,`priority`,`reminderEnabled`,`isCompleted`,`completedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: CareTask) {
        statement.bindLong(1, entity.taskId)
        val _tmpPetId: Long? = entity.petId
        if (_tmpPetId == null) {
          statement.bindNull(2)
        } else {
          statement.bindLong(2, _tmpPetId)
        }
        val _tmpRoutineId: Long? = entity.routineId
        if (_tmpRoutineId == null) {
          statement.bindNull(3)
        } else {
          statement.bindLong(3, _tmpRoutineId)
        }
        statement.bindText(4, entity.title)
        statement.bindText(5, entity.description)
        statement.bindText(6, entity.category)
        statement.bindText(7, entity.scheduledDate)
        statement.bindText(8, entity.scheduledTime)
        statement.bindText(9, entity.frequency)
        statement.bindText(10, entity.requiredSupplies)
        statement.bindText(11, entity.notes)
        statement.bindText(12, entity.priority)
        val _tmp: Int = if (entity.reminderEnabled) 1 else 0
        statement.bindLong(13, _tmp.toLong())
        val _tmp_1: Int = if (entity.isCompleted) 1 else 0
        statement.bindLong(14, _tmp_1.toLong())
        val _tmpCompletedAt: Long? = entity.completedAt
        if (_tmpCompletedAt == null) {
          statement.bindNull(15)
        } else {
          statement.bindLong(15, _tmpCompletedAt)
        }
      }
    }
    this.__deleteAdapterOfCareTask = object : EntityDeleteOrUpdateAdapter<CareTask>() {
      protected override fun createQuery(): String = "DELETE FROM `care_tasks` WHERE `taskId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: CareTask) {
        statement.bindLong(1, entity.taskId)
      }
    }
    this.__updateAdapterOfCareTask = object : EntityDeleteOrUpdateAdapter<CareTask>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `care_tasks` SET `taskId` = ?,`petId` = ?,`routineId` = ?,`title` = ?,`description` = ?,`category` = ?,`scheduledDate` = ?,`scheduledTime` = ?,`frequency` = ?,`requiredSupplies` = ?,`notes` = ?,`priority` = ?,`reminderEnabled` = ?,`isCompleted` = ?,`completedAt` = ? WHERE `taskId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: CareTask) {
        statement.bindLong(1, entity.taskId)
        val _tmpPetId: Long? = entity.petId
        if (_tmpPetId == null) {
          statement.bindNull(2)
        } else {
          statement.bindLong(2, _tmpPetId)
        }
        val _tmpRoutineId: Long? = entity.routineId
        if (_tmpRoutineId == null) {
          statement.bindNull(3)
        } else {
          statement.bindLong(3, _tmpRoutineId)
        }
        statement.bindText(4, entity.title)
        statement.bindText(5, entity.description)
        statement.bindText(6, entity.category)
        statement.bindText(7, entity.scheduledDate)
        statement.bindText(8, entity.scheduledTime)
        statement.bindText(9, entity.frequency)
        statement.bindText(10, entity.requiredSupplies)
        statement.bindText(11, entity.notes)
        statement.bindText(12, entity.priority)
        val _tmp: Int = if (entity.reminderEnabled) 1 else 0
        statement.bindLong(13, _tmp.toLong())
        val _tmp_1: Int = if (entity.isCompleted) 1 else 0
        statement.bindLong(14, _tmp_1.toLong())
        val _tmpCompletedAt: Long? = entity.completedAt
        if (_tmpCompletedAt == null) {
          statement.bindNull(15)
        } else {
          statement.bindLong(15, _tmpCompletedAt)
        }
        statement.bindLong(16, entity.taskId)
      }
    }
  }

  public override suspend fun insertTask(task: CareTask): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfCareTask.insertAndReturnId(_connection, task)
    _result
  }

  public override suspend fun deleteTask(task: CareTask): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfCareTask.handle(_connection, task)
  }

  public override suspend fun updateTask(task: CareTask): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfCareTask.handle(_connection, task)
  }

  public override fun getTasksByDate(date: String): Flow<List<CareTask>> {
    val _sql: String = "SELECT * FROM care_tasks WHERE scheduledDate = ? ORDER BY scheduledTime ASC"
    return createFlow(__db, false, arrayOf("care_tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, date)
        val _columnIndexOfTaskId: Int = getColumnIndexOrThrow(_stmt, "taskId")
        val _columnIndexOfPetId: Int = getColumnIndexOrThrow(_stmt, "petId")
        val _columnIndexOfRoutineId: Int = getColumnIndexOrThrow(_stmt, "routineId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfScheduledDate: Int = getColumnIndexOrThrow(_stmt, "scheduledDate")
        val _columnIndexOfScheduledTime: Int = getColumnIndexOrThrow(_stmt, "scheduledTime")
        val _columnIndexOfFrequency: Int = getColumnIndexOrThrow(_stmt, "frequency")
        val _columnIndexOfRequiredSupplies: Int = getColumnIndexOrThrow(_stmt, "requiredSupplies")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfReminderEnabled: Int = getColumnIndexOrThrow(_stmt, "reminderEnabled")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfCompletedAt: Int = getColumnIndexOrThrow(_stmt, "completedAt")
        val _result: MutableList<CareTask> = mutableListOf()
        while (_stmt.step()) {
          val _item: CareTask
          val _tmpTaskId: Long
          _tmpTaskId = _stmt.getLong(_columnIndexOfTaskId)
          val _tmpPetId: Long?
          if (_stmt.isNull(_columnIndexOfPetId)) {
            _tmpPetId = null
          } else {
            _tmpPetId = _stmt.getLong(_columnIndexOfPetId)
          }
          val _tmpRoutineId: Long?
          if (_stmt.isNull(_columnIndexOfRoutineId)) {
            _tmpRoutineId = null
          } else {
            _tmpRoutineId = _stmt.getLong(_columnIndexOfRoutineId)
          }
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpScheduledDate: String
          _tmpScheduledDate = _stmt.getText(_columnIndexOfScheduledDate)
          val _tmpScheduledTime: String
          _tmpScheduledTime = _stmt.getText(_columnIndexOfScheduledTime)
          val _tmpFrequency: String
          _tmpFrequency = _stmt.getText(_columnIndexOfFrequency)
          val _tmpRequiredSupplies: String
          _tmpRequiredSupplies = _stmt.getText(_columnIndexOfRequiredSupplies)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          val _tmpPriority: String
          _tmpPriority = _stmt.getText(_columnIndexOfPriority)
          val _tmpReminderEnabled: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfReminderEnabled).toInt()
          _tmpReminderEnabled = _tmp != 0
          val _tmpIsCompleted: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp_1 != 0
          val _tmpCompletedAt: Long?
          if (_stmt.isNull(_columnIndexOfCompletedAt)) {
            _tmpCompletedAt = null
          } else {
            _tmpCompletedAt = _stmt.getLong(_columnIndexOfCompletedAt)
          }
          _item = CareTask(_tmpTaskId,_tmpPetId,_tmpRoutineId,_tmpTitle,_tmpDescription,_tmpCategory,_tmpScheduledDate,_tmpScheduledTime,_tmpFrequency,_tmpRequiredSupplies,_tmpNotes,_tmpPriority,_tmpReminderEnabled,_tmpIsCompleted,_tmpCompletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAllTasks(): Flow<List<CareTask>> {
    val _sql: String = "SELECT * FROM care_tasks ORDER BY isCompleted ASC, scheduledDate ASC, scheduledTime ASC"
    return createFlow(__db, false, arrayOf("care_tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfTaskId: Int = getColumnIndexOrThrow(_stmt, "taskId")
        val _columnIndexOfPetId: Int = getColumnIndexOrThrow(_stmt, "petId")
        val _columnIndexOfRoutineId: Int = getColumnIndexOrThrow(_stmt, "routineId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfScheduledDate: Int = getColumnIndexOrThrow(_stmt, "scheduledDate")
        val _columnIndexOfScheduledTime: Int = getColumnIndexOrThrow(_stmt, "scheduledTime")
        val _columnIndexOfFrequency: Int = getColumnIndexOrThrow(_stmt, "frequency")
        val _columnIndexOfRequiredSupplies: Int = getColumnIndexOrThrow(_stmt, "requiredSupplies")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfReminderEnabled: Int = getColumnIndexOrThrow(_stmt, "reminderEnabled")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfCompletedAt: Int = getColumnIndexOrThrow(_stmt, "completedAt")
        val _result: MutableList<CareTask> = mutableListOf()
        while (_stmt.step()) {
          val _item: CareTask
          val _tmpTaskId: Long
          _tmpTaskId = _stmt.getLong(_columnIndexOfTaskId)
          val _tmpPetId: Long?
          if (_stmt.isNull(_columnIndexOfPetId)) {
            _tmpPetId = null
          } else {
            _tmpPetId = _stmt.getLong(_columnIndexOfPetId)
          }
          val _tmpRoutineId: Long?
          if (_stmt.isNull(_columnIndexOfRoutineId)) {
            _tmpRoutineId = null
          } else {
            _tmpRoutineId = _stmt.getLong(_columnIndexOfRoutineId)
          }
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpScheduledDate: String
          _tmpScheduledDate = _stmt.getText(_columnIndexOfScheduledDate)
          val _tmpScheduledTime: String
          _tmpScheduledTime = _stmt.getText(_columnIndexOfScheduledTime)
          val _tmpFrequency: String
          _tmpFrequency = _stmt.getText(_columnIndexOfFrequency)
          val _tmpRequiredSupplies: String
          _tmpRequiredSupplies = _stmt.getText(_columnIndexOfRequiredSupplies)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          val _tmpPriority: String
          _tmpPriority = _stmt.getText(_columnIndexOfPriority)
          val _tmpReminderEnabled: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfReminderEnabled).toInt()
          _tmpReminderEnabled = _tmp != 0
          val _tmpIsCompleted: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp_1 != 0
          val _tmpCompletedAt: Long?
          if (_stmt.isNull(_columnIndexOfCompletedAt)) {
            _tmpCompletedAt = null
          } else {
            _tmpCompletedAt = _stmt.getLong(_columnIndexOfCompletedAt)
          }
          _item = CareTask(_tmpTaskId,_tmpPetId,_tmpRoutineId,_tmpTitle,_tmpDescription,_tmpCategory,_tmpScheduledDate,_tmpScheduledTime,_tmpFrequency,_tmpRequiredSupplies,_tmpNotes,_tmpPriority,_tmpReminderEnabled,_tmpIsCompleted,_tmpCompletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getTasksByPetAndDate(petId: Long, date: String): Flow<List<CareTask>> {
    val _sql: String = "SELECT * FROM care_tasks WHERE petId = ? AND scheduledDate = ? ORDER BY scheduledTime ASC"
    return createFlow(__db, false, arrayOf("care_tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, petId)
        _argIndex = 2
        _stmt.bindText(_argIndex, date)
        val _columnIndexOfTaskId: Int = getColumnIndexOrThrow(_stmt, "taskId")
        val _columnIndexOfPetId: Int = getColumnIndexOrThrow(_stmt, "petId")
        val _columnIndexOfRoutineId: Int = getColumnIndexOrThrow(_stmt, "routineId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfScheduledDate: Int = getColumnIndexOrThrow(_stmt, "scheduledDate")
        val _columnIndexOfScheduledTime: Int = getColumnIndexOrThrow(_stmt, "scheduledTime")
        val _columnIndexOfFrequency: Int = getColumnIndexOrThrow(_stmt, "frequency")
        val _columnIndexOfRequiredSupplies: Int = getColumnIndexOrThrow(_stmt, "requiredSupplies")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfReminderEnabled: Int = getColumnIndexOrThrow(_stmt, "reminderEnabled")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfCompletedAt: Int = getColumnIndexOrThrow(_stmt, "completedAt")
        val _result: MutableList<CareTask> = mutableListOf()
        while (_stmt.step()) {
          val _item: CareTask
          val _tmpTaskId: Long
          _tmpTaskId = _stmt.getLong(_columnIndexOfTaskId)
          val _tmpPetId: Long?
          if (_stmt.isNull(_columnIndexOfPetId)) {
            _tmpPetId = null
          } else {
            _tmpPetId = _stmt.getLong(_columnIndexOfPetId)
          }
          val _tmpRoutineId: Long?
          if (_stmt.isNull(_columnIndexOfRoutineId)) {
            _tmpRoutineId = null
          } else {
            _tmpRoutineId = _stmt.getLong(_columnIndexOfRoutineId)
          }
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpScheduledDate: String
          _tmpScheduledDate = _stmt.getText(_columnIndexOfScheduledDate)
          val _tmpScheduledTime: String
          _tmpScheduledTime = _stmt.getText(_columnIndexOfScheduledTime)
          val _tmpFrequency: String
          _tmpFrequency = _stmt.getText(_columnIndexOfFrequency)
          val _tmpRequiredSupplies: String
          _tmpRequiredSupplies = _stmt.getText(_columnIndexOfRequiredSupplies)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          val _tmpPriority: String
          _tmpPriority = _stmt.getText(_columnIndexOfPriority)
          val _tmpReminderEnabled: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfReminderEnabled).toInt()
          _tmpReminderEnabled = _tmp != 0
          val _tmpIsCompleted: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp_1 != 0
          val _tmpCompletedAt: Long?
          if (_stmt.isNull(_columnIndexOfCompletedAt)) {
            _tmpCompletedAt = null
          } else {
            _tmpCompletedAt = _stmt.getLong(_columnIndexOfCompletedAt)
          }
          _item = CareTask(_tmpTaskId,_tmpPetId,_tmpRoutineId,_tmpTitle,_tmpDescription,_tmpCategory,_tmpScheduledDate,_tmpScheduledTime,_tmpFrequency,_tmpRequiredSupplies,_tmpNotes,_tmpPriority,_tmpReminderEnabled,_tmpIsCompleted,_tmpCompletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getTasksByPetId(petId: Long): Flow<List<CareTask>> {
    val _sql: String = "SELECT * FROM care_tasks WHERE petId = ? ORDER BY scheduledDate DESC, scheduledTime ASC"
    return createFlow(__db, false, arrayOf("care_tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, petId)
        val _columnIndexOfTaskId: Int = getColumnIndexOrThrow(_stmt, "taskId")
        val _columnIndexOfPetId: Int = getColumnIndexOrThrow(_stmt, "petId")
        val _columnIndexOfRoutineId: Int = getColumnIndexOrThrow(_stmt, "routineId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfScheduledDate: Int = getColumnIndexOrThrow(_stmt, "scheduledDate")
        val _columnIndexOfScheduledTime: Int = getColumnIndexOrThrow(_stmt, "scheduledTime")
        val _columnIndexOfFrequency: Int = getColumnIndexOrThrow(_stmt, "frequency")
        val _columnIndexOfRequiredSupplies: Int = getColumnIndexOrThrow(_stmt, "requiredSupplies")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfReminderEnabled: Int = getColumnIndexOrThrow(_stmt, "reminderEnabled")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfCompletedAt: Int = getColumnIndexOrThrow(_stmt, "completedAt")
        val _result: MutableList<CareTask> = mutableListOf()
        while (_stmt.step()) {
          val _item: CareTask
          val _tmpTaskId: Long
          _tmpTaskId = _stmt.getLong(_columnIndexOfTaskId)
          val _tmpPetId: Long?
          if (_stmt.isNull(_columnIndexOfPetId)) {
            _tmpPetId = null
          } else {
            _tmpPetId = _stmt.getLong(_columnIndexOfPetId)
          }
          val _tmpRoutineId: Long?
          if (_stmt.isNull(_columnIndexOfRoutineId)) {
            _tmpRoutineId = null
          } else {
            _tmpRoutineId = _stmt.getLong(_columnIndexOfRoutineId)
          }
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpScheduledDate: String
          _tmpScheduledDate = _stmt.getText(_columnIndexOfScheduledDate)
          val _tmpScheduledTime: String
          _tmpScheduledTime = _stmt.getText(_columnIndexOfScheduledTime)
          val _tmpFrequency: String
          _tmpFrequency = _stmt.getText(_columnIndexOfFrequency)
          val _tmpRequiredSupplies: String
          _tmpRequiredSupplies = _stmt.getText(_columnIndexOfRequiredSupplies)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          val _tmpPriority: String
          _tmpPriority = _stmt.getText(_columnIndexOfPriority)
          val _tmpReminderEnabled: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfReminderEnabled).toInt()
          _tmpReminderEnabled = _tmp != 0
          val _tmpIsCompleted: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp_1 != 0
          val _tmpCompletedAt: Long?
          if (_stmt.isNull(_columnIndexOfCompletedAt)) {
            _tmpCompletedAt = null
          } else {
            _tmpCompletedAt = _stmt.getLong(_columnIndexOfCompletedAt)
          }
          _item = CareTask(_tmpTaskId,_tmpPetId,_tmpRoutineId,_tmpTitle,_tmpDescription,_tmpCategory,_tmpScheduledDate,_tmpScheduledTime,_tmpFrequency,_tmpRequiredSupplies,_tmpNotes,_tmpPriority,_tmpReminderEnabled,_tmpIsCompleted,_tmpCompletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getTaskById(taskId: Long): Flow<CareTask?> {
    val _sql: String = "SELECT * FROM care_tasks WHERE taskId = ? LIMIT 1"
    return createFlow(__db, false, arrayOf("care_tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, taskId)
        val _columnIndexOfTaskId: Int = getColumnIndexOrThrow(_stmt, "taskId")
        val _columnIndexOfPetId: Int = getColumnIndexOrThrow(_stmt, "petId")
        val _columnIndexOfRoutineId: Int = getColumnIndexOrThrow(_stmt, "routineId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfScheduledDate: Int = getColumnIndexOrThrow(_stmt, "scheduledDate")
        val _columnIndexOfScheduledTime: Int = getColumnIndexOrThrow(_stmt, "scheduledTime")
        val _columnIndexOfFrequency: Int = getColumnIndexOrThrow(_stmt, "frequency")
        val _columnIndexOfRequiredSupplies: Int = getColumnIndexOrThrow(_stmt, "requiredSupplies")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfReminderEnabled: Int = getColumnIndexOrThrow(_stmt, "reminderEnabled")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfCompletedAt: Int = getColumnIndexOrThrow(_stmt, "completedAt")
        val _result: CareTask?
        if (_stmt.step()) {
          val _tmpTaskId: Long
          _tmpTaskId = _stmt.getLong(_columnIndexOfTaskId)
          val _tmpPetId: Long?
          if (_stmt.isNull(_columnIndexOfPetId)) {
            _tmpPetId = null
          } else {
            _tmpPetId = _stmt.getLong(_columnIndexOfPetId)
          }
          val _tmpRoutineId: Long?
          if (_stmt.isNull(_columnIndexOfRoutineId)) {
            _tmpRoutineId = null
          } else {
            _tmpRoutineId = _stmt.getLong(_columnIndexOfRoutineId)
          }
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpScheduledDate: String
          _tmpScheduledDate = _stmt.getText(_columnIndexOfScheduledDate)
          val _tmpScheduledTime: String
          _tmpScheduledTime = _stmt.getText(_columnIndexOfScheduledTime)
          val _tmpFrequency: String
          _tmpFrequency = _stmt.getText(_columnIndexOfFrequency)
          val _tmpRequiredSupplies: String
          _tmpRequiredSupplies = _stmt.getText(_columnIndexOfRequiredSupplies)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          val _tmpPriority: String
          _tmpPriority = _stmt.getText(_columnIndexOfPriority)
          val _tmpReminderEnabled: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfReminderEnabled).toInt()
          _tmpReminderEnabled = _tmp != 0
          val _tmpIsCompleted: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp_1 != 0
          val _tmpCompletedAt: Long?
          if (_stmt.isNull(_columnIndexOfCompletedAt)) {
            _tmpCompletedAt = null
          } else {
            _tmpCompletedAt = _stmt.getLong(_columnIndexOfCompletedAt)
          }
          _result = CareTask(_tmpTaskId,_tmpPetId,_tmpRoutineId,_tmpTitle,_tmpDescription,_tmpCategory,_tmpScheduledDate,_tmpScheduledTime,_tmpFrequency,_tmpRequiredSupplies,_tmpNotes,_tmpPriority,_tmpReminderEnabled,_tmpIsCompleted,_tmpCompletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTaskByIdDirect(taskId: Long): CareTask? {
    val _sql: String = "SELECT * FROM care_tasks WHERE taskId = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, taskId)
        val _columnIndexOfTaskId: Int = getColumnIndexOrThrow(_stmt, "taskId")
        val _columnIndexOfPetId: Int = getColumnIndexOrThrow(_stmt, "petId")
        val _columnIndexOfRoutineId: Int = getColumnIndexOrThrow(_stmt, "routineId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfScheduledDate: Int = getColumnIndexOrThrow(_stmt, "scheduledDate")
        val _columnIndexOfScheduledTime: Int = getColumnIndexOrThrow(_stmt, "scheduledTime")
        val _columnIndexOfFrequency: Int = getColumnIndexOrThrow(_stmt, "frequency")
        val _columnIndexOfRequiredSupplies: Int = getColumnIndexOrThrow(_stmt, "requiredSupplies")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfReminderEnabled: Int = getColumnIndexOrThrow(_stmt, "reminderEnabled")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfCompletedAt: Int = getColumnIndexOrThrow(_stmt, "completedAt")
        val _result: CareTask?
        if (_stmt.step()) {
          val _tmpTaskId: Long
          _tmpTaskId = _stmt.getLong(_columnIndexOfTaskId)
          val _tmpPetId: Long?
          if (_stmt.isNull(_columnIndexOfPetId)) {
            _tmpPetId = null
          } else {
            _tmpPetId = _stmt.getLong(_columnIndexOfPetId)
          }
          val _tmpRoutineId: Long?
          if (_stmt.isNull(_columnIndexOfRoutineId)) {
            _tmpRoutineId = null
          } else {
            _tmpRoutineId = _stmt.getLong(_columnIndexOfRoutineId)
          }
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpScheduledDate: String
          _tmpScheduledDate = _stmt.getText(_columnIndexOfScheduledDate)
          val _tmpScheduledTime: String
          _tmpScheduledTime = _stmt.getText(_columnIndexOfScheduledTime)
          val _tmpFrequency: String
          _tmpFrequency = _stmt.getText(_columnIndexOfFrequency)
          val _tmpRequiredSupplies: String
          _tmpRequiredSupplies = _stmt.getText(_columnIndexOfRequiredSupplies)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          val _tmpPriority: String
          _tmpPriority = _stmt.getText(_columnIndexOfPriority)
          val _tmpReminderEnabled: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfReminderEnabled).toInt()
          _tmpReminderEnabled = _tmp != 0
          val _tmpIsCompleted: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp_1 != 0
          val _tmpCompletedAt: Long?
          if (_stmt.isNull(_columnIndexOfCompletedAt)) {
            _tmpCompletedAt = null
          } else {
            _tmpCompletedAt = _stmt.getLong(_columnIndexOfCompletedAt)
          }
          _result = CareTask(_tmpTaskId,_tmpPetId,_tmpRoutineId,_tmpTitle,_tmpDescription,_tmpCategory,_tmpScheduledDate,_tmpScheduledTime,_tmpFrequency,_tmpRequiredSupplies,_tmpNotes,_tmpPriority,_tmpReminderEnabled,_tmpIsCompleted,_tmpCompletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getCompletedTaskCount(date: String): Flow<Int> {
    val _sql: String = "SELECT COUNT(*) FROM care_tasks WHERE scheduledDate = ? AND isCompleted = 1"
    return createFlow(__db, false, arrayOf("care_tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, date)
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getOutstandingTaskCount(date: String): Flow<Int> {
    val _sql: String = "SELECT COUNT(*) FROM care_tasks WHERE scheduledDate = ? AND isCompleted = 0"
    return createFlow(__db, false, arrayOf("care_tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, date)
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getTotalCompletedCount(): Flow<Int> {
    val _sql: String = "SELECT COUNT(*) FROM care_tasks WHERE isCompleted = 1"
    return createFlow(__db, false, arrayOf("care_tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getTotalOutstandingCount(): Flow<Int> {
    val _sql: String = "SELECT COUNT(*) FROM care_tasks WHERE isCompleted = 0"
    return createFlow(__db, false, arrayOf("care_tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
