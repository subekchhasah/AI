package com.example.petcare.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.petcare.`data`.local.entities.MedicalRecord
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
public class MedicalRecordDao_Impl(
  __db: RoomDatabase,
) : MedicalRecordDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfMedicalRecord: EntityInsertAdapter<MedicalRecord>

  private val __deleteAdapterOfMedicalRecord: EntityDeleteOrUpdateAdapter<MedicalRecord>

  private val __updateAdapterOfMedicalRecord: EntityDeleteOrUpdateAdapter<MedicalRecord>
  init {
    this.__db = __db
    this.__insertAdapterOfMedicalRecord = object : EntityInsertAdapter<MedicalRecord>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `medical_records` (`medicalRecordId`,`petId`,`recordType`,`title`,`date`,`veterinarian`,`clinic`,`notes`,`attachmentUri`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: MedicalRecord) {
        statement.bindLong(1, entity.medicalRecordId)
        val _tmpPetId: Long? = entity.petId
        if (_tmpPetId == null) {
          statement.bindNull(2)
        } else {
          statement.bindLong(2, _tmpPetId)
        }
        statement.bindText(3, entity.recordType)
        statement.bindText(4, entity.title)
        statement.bindText(5, entity.date)
        statement.bindText(6, entity.veterinarian)
        statement.bindText(7, entity.clinic)
        statement.bindText(8, entity.notes)
        val _tmpAttachmentUri: String? = entity.attachmentUri
        if (_tmpAttachmentUri == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpAttachmentUri)
        }
      }
    }
    this.__deleteAdapterOfMedicalRecord = object : EntityDeleteOrUpdateAdapter<MedicalRecord>() {
      protected override fun createQuery(): String = "DELETE FROM `medical_records` WHERE `medicalRecordId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: MedicalRecord) {
        statement.bindLong(1, entity.medicalRecordId)
      }
    }
    this.__updateAdapterOfMedicalRecord = object : EntityDeleteOrUpdateAdapter<MedicalRecord>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `medical_records` SET `medicalRecordId` = ?,`petId` = ?,`recordType` = ?,`title` = ?,`date` = ?,`veterinarian` = ?,`clinic` = ?,`notes` = ?,`attachmentUri` = ? WHERE `medicalRecordId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: MedicalRecord) {
        statement.bindLong(1, entity.medicalRecordId)
        val _tmpPetId: Long? = entity.petId
        if (_tmpPetId == null) {
          statement.bindNull(2)
        } else {
          statement.bindLong(2, _tmpPetId)
        }
        statement.bindText(3, entity.recordType)
        statement.bindText(4, entity.title)
        statement.bindText(5, entity.date)
        statement.bindText(6, entity.veterinarian)
        statement.bindText(7, entity.clinic)
        statement.bindText(8, entity.notes)
        val _tmpAttachmentUri: String? = entity.attachmentUri
        if (_tmpAttachmentUri == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpAttachmentUri)
        }
        statement.bindLong(10, entity.medicalRecordId)
      }
    }
  }

  public override suspend fun insertMedicalRecord(record: MedicalRecord): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfMedicalRecord.insertAndReturnId(_connection, record)
    _result
  }

  public override suspend fun deleteMedicalRecord(record: MedicalRecord): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfMedicalRecord.handle(_connection, record)
  }

  public override suspend fun updateMedicalRecord(record: MedicalRecord): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfMedicalRecord.handle(_connection, record)
  }

  public override fun getRecordsByPetId(petId: Long): Flow<List<MedicalRecord>> {
    val _sql: String = "SELECT * FROM medical_records WHERE petId = ? ORDER BY date DESC"
    return createFlow(__db, false, arrayOf("medical_records")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, petId)
        val _columnIndexOfMedicalRecordId: Int = getColumnIndexOrThrow(_stmt, "medicalRecordId")
        val _columnIndexOfPetId: Int = getColumnIndexOrThrow(_stmt, "petId")
        val _columnIndexOfRecordType: Int = getColumnIndexOrThrow(_stmt, "recordType")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfVeterinarian: Int = getColumnIndexOrThrow(_stmt, "veterinarian")
        val _columnIndexOfClinic: Int = getColumnIndexOrThrow(_stmt, "clinic")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfAttachmentUri: Int = getColumnIndexOrThrow(_stmt, "attachmentUri")
        val _result: MutableList<MedicalRecord> = mutableListOf()
        while (_stmt.step()) {
          val _item: MedicalRecord
          val _tmpMedicalRecordId: Long
          _tmpMedicalRecordId = _stmt.getLong(_columnIndexOfMedicalRecordId)
          val _tmpPetId: Long?
          if (_stmt.isNull(_columnIndexOfPetId)) {
            _tmpPetId = null
          } else {
            _tmpPetId = _stmt.getLong(_columnIndexOfPetId)
          }
          val _tmpRecordType: String
          _tmpRecordType = _stmt.getText(_columnIndexOfRecordType)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpVeterinarian: String
          _tmpVeterinarian = _stmt.getText(_columnIndexOfVeterinarian)
          val _tmpClinic: String
          _tmpClinic = _stmt.getText(_columnIndexOfClinic)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          val _tmpAttachmentUri: String?
          if (_stmt.isNull(_columnIndexOfAttachmentUri)) {
            _tmpAttachmentUri = null
          } else {
            _tmpAttachmentUri = _stmt.getText(_columnIndexOfAttachmentUri)
          }
          _item = MedicalRecord(_tmpMedicalRecordId,_tmpPetId,_tmpRecordType,_tmpTitle,_tmpDate,_tmpVeterinarian,_tmpClinic,_tmpNotes,_tmpAttachmentUri)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAllMedicalRecords(): Flow<List<MedicalRecord>> {
    val _sql: String = "SELECT * FROM medical_records ORDER BY date DESC"
    return createFlow(__db, false, arrayOf("medical_records")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfMedicalRecordId: Int = getColumnIndexOrThrow(_stmt, "medicalRecordId")
        val _columnIndexOfPetId: Int = getColumnIndexOrThrow(_stmt, "petId")
        val _columnIndexOfRecordType: Int = getColumnIndexOrThrow(_stmt, "recordType")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfVeterinarian: Int = getColumnIndexOrThrow(_stmt, "veterinarian")
        val _columnIndexOfClinic: Int = getColumnIndexOrThrow(_stmt, "clinic")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfAttachmentUri: Int = getColumnIndexOrThrow(_stmt, "attachmentUri")
        val _result: MutableList<MedicalRecord> = mutableListOf()
        while (_stmt.step()) {
          val _item: MedicalRecord
          val _tmpMedicalRecordId: Long
          _tmpMedicalRecordId = _stmt.getLong(_columnIndexOfMedicalRecordId)
          val _tmpPetId: Long?
          if (_stmt.isNull(_columnIndexOfPetId)) {
            _tmpPetId = null
          } else {
            _tmpPetId = _stmt.getLong(_columnIndexOfPetId)
          }
          val _tmpRecordType: String
          _tmpRecordType = _stmt.getText(_columnIndexOfRecordType)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpVeterinarian: String
          _tmpVeterinarian = _stmt.getText(_columnIndexOfVeterinarian)
          val _tmpClinic: String
          _tmpClinic = _stmt.getText(_columnIndexOfClinic)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          val _tmpAttachmentUri: String?
          if (_stmt.isNull(_columnIndexOfAttachmentUri)) {
            _tmpAttachmentUri = null
          } else {
            _tmpAttachmentUri = _stmt.getText(_columnIndexOfAttachmentUri)
          }
          _item = MedicalRecord(_tmpMedicalRecordId,_tmpPetId,_tmpRecordType,_tmpTitle,_tmpDate,_tmpVeterinarian,_tmpClinic,_tmpNotes,_tmpAttachmentUri)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getRecordById(id: Long): Flow<MedicalRecord?> {
    val _sql: String = "SELECT * FROM medical_records WHERE medicalRecordId = ? LIMIT 1"
    return createFlow(__db, false, arrayOf("medical_records")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfMedicalRecordId: Int = getColumnIndexOrThrow(_stmt, "medicalRecordId")
        val _columnIndexOfPetId: Int = getColumnIndexOrThrow(_stmt, "petId")
        val _columnIndexOfRecordType: Int = getColumnIndexOrThrow(_stmt, "recordType")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfVeterinarian: Int = getColumnIndexOrThrow(_stmt, "veterinarian")
        val _columnIndexOfClinic: Int = getColumnIndexOrThrow(_stmt, "clinic")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfAttachmentUri: Int = getColumnIndexOrThrow(_stmt, "attachmentUri")
        val _result: MedicalRecord?
        if (_stmt.step()) {
          val _tmpMedicalRecordId: Long
          _tmpMedicalRecordId = _stmt.getLong(_columnIndexOfMedicalRecordId)
          val _tmpPetId: Long?
          if (_stmt.isNull(_columnIndexOfPetId)) {
            _tmpPetId = null
          } else {
            _tmpPetId = _stmt.getLong(_columnIndexOfPetId)
          }
          val _tmpRecordType: String
          _tmpRecordType = _stmt.getText(_columnIndexOfRecordType)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpVeterinarian: String
          _tmpVeterinarian = _stmt.getText(_columnIndexOfVeterinarian)
          val _tmpClinic: String
          _tmpClinic = _stmt.getText(_columnIndexOfClinic)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          val _tmpAttachmentUri: String?
          if (_stmt.isNull(_columnIndexOfAttachmentUri)) {
            _tmpAttachmentUri = null
          } else {
            _tmpAttachmentUri = _stmt.getText(_columnIndexOfAttachmentUri)
          }
          _result = MedicalRecord(_tmpMedicalRecordId,_tmpPetId,_tmpRecordType,_tmpTitle,_tmpDate,_tmpVeterinarian,_tmpClinic,_tmpNotes,_tmpAttachmentUri)
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
