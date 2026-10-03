package com.example.petcare.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.petcare.`data`.local.entities.CareRoutine
import javax.`annotation`.processing.Generated
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
public class CareRoutineDao_Impl(
  __db: RoomDatabase,
) : CareRoutineDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfCareRoutine: EntityInsertAdapter<CareRoutine>

  private val __deleteAdapterOfCareRoutine: EntityDeleteOrUpdateAdapter<CareRoutine>

  private val __updateAdapterOfCareRoutine: EntityDeleteOrUpdateAdapter<CareRoutine>
  init {
    this.__db = __db
    this.__insertAdapterOfCareRoutine = object : EntityInsertAdapter<CareRoutine>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `care_routines` (`routineId`,`petId`,`routineName`,`description`,`frequency`,`startDate`,`endDate`,`notes`,`imageUri`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: CareRoutine) {
        statement.bindLong(1, entity.routineId)
        statement.bindLong(2, entity.petId)
        statement.bindText(3, entity.routineName)
        statement.bindText(4, entity.description)
        statement.bindText(5, entity.frequency)
        statement.bindText(6, entity.startDate)
        statement.bindText(7, entity.endDate)
        statement.bindText(8, entity.notes)
        val _tmpImageUri: String? = entity.imageUri
        if (_tmpImageUri == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpImageUri)
        }
        statement.bindLong(10, entity.createdAt)
      }
    }
    this.__deleteAdapterOfCareRoutine = object : EntityDeleteOrUpdateAdapter<CareRoutine>() {
      protected override fun createQuery(): String = "DELETE FROM `care_routines` WHERE `routineId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: CareRoutine) {
        statement.bindLong(1, entity.routineId)
      }
    }
    this.__updateAdapterOfCareRoutine = object : EntityDeleteOrUpdateAdapter<CareRoutine>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `care_routines` SET `routineId` = ?,`petId` = ?,`routineName` = ?,`description` = ?,`frequency` = ?,`startDate` = ?,`endDate` = ?,`notes` = ?,`imageUri` = ?,`createdAt` = ? WHERE `routineId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: CareRoutine) {
        statement.bindLong(1, entity.routineId)
        statement.bindLong(2, entity.petId)
        statement.bindText(3, entity.routineName)
        statement.bindText(4, entity.description)
        statement.bindText(5, entity.frequency)
        statement.bindText(6, entity.startDate)
        statement.bindText(7, entity.endDate)
        statement.bindText(8, entity.notes)
        val _tmpImageUri: String? = entity.imageUri
        if (_tmpImageUri == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpImageUri)
        }
        statement.bindLong(10, entity.createdAt)
        statement.bindLong(11, entity.routineId)
      }
    }
  }

  public override suspend fun insertRoutine(routine: CareRoutine): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfCareRoutine.insertAndReturnId(_connection, routine)
    _result
  }

  public override suspend fun deleteRoutine(routine: CareRoutine): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfCareRoutine.handle(_connection, routine)
  }

  public override suspend fun updateRoutine(routine: CareRoutine): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfCareRoutine.handle(_connection, routine)
  }

  public override fun getRoutinesByPetId(petId: Long): Flow<List<CareRoutine>> {
    val _sql: String = "SELECT * FROM care_routines WHERE petId = ? ORDER BY routineName ASC"
    return createFlow(__db, false, arrayOf("care_routines")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, petId)
        val _columnIndexOfRoutineId: Int = getColumnIndexOrThrow(_stmt, "routineId")
        val _columnIndexOfPetId: Int = getColumnIndexOrThrow(_stmt, "petId")
        val _columnIndexOfRoutineName: Int = getColumnIndexOrThrow(_stmt, "routineName")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfFrequency: Int = getColumnIndexOrThrow(_stmt, "frequency")
        val _columnIndexOfStartDate: Int = getColumnIndexOrThrow(_stmt, "startDate")
        val _columnIndexOfEndDate: Int = getColumnIndexOrThrow(_stmt, "endDate")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfImageUri: Int = getColumnIndexOrThrow(_stmt, "imageUri")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<CareRoutine> = mutableListOf()
        while (_stmt.step()) {
          val _item: CareRoutine
          val _tmpRoutineId: Long
          _tmpRoutineId = _stmt.getLong(_columnIndexOfRoutineId)
          val _tmpPetId: Long
          _tmpPetId = _stmt.getLong(_columnIndexOfPetId)
          val _tmpRoutineName: String
          _tmpRoutineName = _stmt.getText(_columnIndexOfRoutineName)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpFrequency: String
          _tmpFrequency = _stmt.getText(_columnIndexOfFrequency)
          val _tmpStartDate: String
          _tmpStartDate = _stmt.getText(_columnIndexOfStartDate)
          val _tmpEndDate: String
          _tmpEndDate = _stmt.getText(_columnIndexOfEndDate)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          val _tmpImageUri: String?
          if (_stmt.isNull(_columnIndexOfImageUri)) {
            _tmpImageUri = null
          } else {
            _tmpImageUri = _stmt.getText(_columnIndexOfImageUri)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item = CareRoutine(_tmpRoutineId,_tmpPetId,_tmpRoutineName,_tmpDescription,_tmpFrequency,_tmpStartDate,_tmpEndDate,_tmpNotes,_tmpImageUri,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getRoutineById(routineId: Long): Flow<CareRoutine?> {
    val _sql: String = "SELECT * FROM care_routines WHERE routineId = ? LIMIT 1"
    return createFlow(__db, false, arrayOf("care_routines")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, routineId)
        val _columnIndexOfRoutineId: Int = getColumnIndexOrThrow(_stmt, "routineId")
        val _columnIndexOfPetId: Int = getColumnIndexOrThrow(_stmt, "petId")
        val _columnIndexOfRoutineName: Int = getColumnIndexOrThrow(_stmt, "routineName")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfFrequency: Int = getColumnIndexOrThrow(_stmt, "frequency")
        val _columnIndexOfStartDate: Int = getColumnIndexOrThrow(_stmt, "startDate")
        val _columnIndexOfEndDate: Int = getColumnIndexOrThrow(_stmt, "endDate")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfImageUri: Int = getColumnIndexOrThrow(_stmt, "imageUri")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: CareRoutine?
        if (_stmt.step()) {
          val _tmpRoutineId: Long
          _tmpRoutineId = _stmt.getLong(_columnIndexOfRoutineId)
          val _tmpPetId: Long
          _tmpPetId = _stmt.getLong(_columnIndexOfPetId)
          val _tmpRoutineName: String
          _tmpRoutineName = _stmt.getText(_columnIndexOfRoutineName)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpFrequency: String
          _tmpFrequency = _stmt.getText(_columnIndexOfFrequency)
          val _tmpStartDate: String
          _tmpStartDate = _stmt.getText(_columnIndexOfStartDate)
          val _tmpEndDate: String
          _tmpEndDate = _stmt.getText(_columnIndexOfEndDate)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          val _tmpImageUri: String?
          if (_stmt.isNull(_columnIndexOfImageUri)) {
            _tmpImageUri = null
          } else {
            _tmpImageUri = _stmt.getText(_columnIndexOfImageUri)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _result = CareRoutine(_tmpRoutineId,_tmpPetId,_tmpRoutineName,_tmpDescription,_tmpFrequency,_tmpStartDate,_tmpEndDate,_tmpNotes,_tmpImageUri,_tmpCreatedAt)
        } else {
          _result = null
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
