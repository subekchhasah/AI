package com.example.petcare.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.petcare.`data`.local.entities.PetLocation
import javax.`annotation`.processing.Generated
import kotlin.Double
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
public class PetLocationDao_Impl(
  __db: RoomDatabase,
) : PetLocationDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfPetLocation: EntityInsertAdapter<PetLocation>

  private val __deleteAdapterOfPetLocation: EntityDeleteOrUpdateAdapter<PetLocation>

  private val __updateAdapterOfPetLocation: EntityDeleteOrUpdateAdapter<PetLocation>
  init {
    this.__db = __db
    this.__insertAdapterOfPetLocation = object : EntityInsertAdapter<PetLocation>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `pet_locations` (`locationId`,`petId`,`name`,`type`,`latitude`,`longitude`,`address`,`notes`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: PetLocation) {
        statement.bindLong(1, entity.locationId)
        val _tmpPetId: Long? = entity.petId
        if (_tmpPetId == null) {
          statement.bindNull(2)
        } else {
          statement.bindLong(2, _tmpPetId)
        }
        statement.bindText(3, entity.name)
        statement.bindText(4, entity.type)
        statement.bindDouble(5, entity.latitude)
        statement.bindDouble(6, entity.longitude)
        statement.bindText(7, entity.address)
        statement.bindText(8, entity.notes)
      }
    }
    this.__deleteAdapterOfPetLocation = object : EntityDeleteOrUpdateAdapter<PetLocation>() {
      protected override fun createQuery(): String = "DELETE FROM `pet_locations` WHERE `locationId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: PetLocation) {
        statement.bindLong(1, entity.locationId)
      }
    }
    this.__updateAdapterOfPetLocation = object : EntityDeleteOrUpdateAdapter<PetLocation>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `pet_locations` SET `locationId` = ?,`petId` = ?,`name` = ?,`type` = ?,`latitude` = ?,`longitude` = ?,`address` = ?,`notes` = ? WHERE `locationId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: PetLocation) {
        statement.bindLong(1, entity.locationId)
        val _tmpPetId: Long? = entity.petId
        if (_tmpPetId == null) {
          statement.bindNull(2)
        } else {
          statement.bindLong(2, _tmpPetId)
        }
        statement.bindText(3, entity.name)
        statement.bindText(4, entity.type)
        statement.bindDouble(5, entity.latitude)
        statement.bindDouble(6, entity.longitude)
        statement.bindText(7, entity.address)
        statement.bindText(8, entity.notes)
        statement.bindLong(9, entity.locationId)
      }
    }
  }

  public override suspend fun insertLocation(location: PetLocation): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfPetLocation.insertAndReturnId(_connection, location)
    _result
  }

  public override suspend fun deleteLocation(location: PetLocation): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfPetLocation.handle(_connection, location)
  }

  public override suspend fun updateLocation(location: PetLocation): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfPetLocation.handle(_connection, location)
  }

  public override fun getAllLocations(): Flow<List<PetLocation>> {
    val _sql: String = "SELECT * FROM pet_locations ORDER BY name ASC"
    return createFlow(__db, false, arrayOf("pet_locations")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfLocationId: Int = getColumnIndexOrThrow(_stmt, "locationId")
        val _columnIndexOfPetId: Int = getColumnIndexOrThrow(_stmt, "petId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfLatitude: Int = getColumnIndexOrThrow(_stmt, "latitude")
        val _columnIndexOfLongitude: Int = getColumnIndexOrThrow(_stmt, "longitude")
        val _columnIndexOfAddress: Int = getColumnIndexOrThrow(_stmt, "address")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _result: MutableList<PetLocation> = mutableListOf()
        while (_stmt.step()) {
          val _item: PetLocation
          val _tmpLocationId: Long
          _tmpLocationId = _stmt.getLong(_columnIndexOfLocationId)
          val _tmpPetId: Long?
          if (_stmt.isNull(_columnIndexOfPetId)) {
            _tmpPetId = null
          } else {
            _tmpPetId = _stmt.getLong(_columnIndexOfPetId)
          }
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
          val _tmpLatitude: Double
          _tmpLatitude = _stmt.getDouble(_columnIndexOfLatitude)
          val _tmpLongitude: Double
          _tmpLongitude = _stmt.getDouble(_columnIndexOfLongitude)
          val _tmpAddress: String
          _tmpAddress = _stmt.getText(_columnIndexOfAddress)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          _item = PetLocation(_tmpLocationId,_tmpPetId,_tmpName,_tmpType,_tmpLatitude,_tmpLongitude,_tmpAddress,_tmpNotes)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getLocationsByType(type: String): Flow<List<PetLocation>> {
    val _sql: String = "SELECT * FROM pet_locations WHERE type = ? ORDER BY name ASC"
    return createFlow(__db, false, arrayOf("pet_locations")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, type)
        val _columnIndexOfLocationId: Int = getColumnIndexOrThrow(_stmt, "locationId")
        val _columnIndexOfPetId: Int = getColumnIndexOrThrow(_stmt, "petId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfLatitude: Int = getColumnIndexOrThrow(_stmt, "latitude")
        val _columnIndexOfLongitude: Int = getColumnIndexOrThrow(_stmt, "longitude")
        val _columnIndexOfAddress: Int = getColumnIndexOrThrow(_stmt, "address")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _result: MutableList<PetLocation> = mutableListOf()
        while (_stmt.step()) {
          val _item: PetLocation
          val _tmpLocationId: Long
          _tmpLocationId = _stmt.getLong(_columnIndexOfLocationId)
          val _tmpPetId: Long?
          if (_stmt.isNull(_columnIndexOfPetId)) {
            _tmpPetId = null
          } else {
            _tmpPetId = _stmt.getLong(_columnIndexOfPetId)
          }
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
          val _tmpLatitude: Double
          _tmpLatitude = _stmt.getDouble(_columnIndexOfLatitude)
          val _tmpLongitude: Double
          _tmpLongitude = _stmt.getDouble(_columnIndexOfLongitude)
          val _tmpAddress: String
          _tmpAddress = _stmt.getText(_columnIndexOfAddress)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          _item = PetLocation(_tmpLocationId,_tmpPetId,_tmpName,_tmpType,_tmpLatitude,_tmpLongitude,_tmpAddress,_tmpNotes)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getLocationById(id: Long): Flow<PetLocation?> {
    val _sql: String = "SELECT * FROM pet_locations WHERE locationId = ? LIMIT 1"
    return createFlow(__db, false, arrayOf("pet_locations")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfLocationId: Int = getColumnIndexOrThrow(_stmt, "locationId")
        val _columnIndexOfPetId: Int = getColumnIndexOrThrow(_stmt, "petId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfLatitude: Int = getColumnIndexOrThrow(_stmt, "latitude")
        val _columnIndexOfLongitude: Int = getColumnIndexOrThrow(_stmt, "longitude")
        val _columnIndexOfAddress: Int = getColumnIndexOrThrow(_stmt, "address")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _result: PetLocation?
        if (_stmt.step()) {
          val _tmpLocationId: Long
          _tmpLocationId = _stmt.getLong(_columnIndexOfLocationId)
          val _tmpPetId: Long?
          if (_stmt.isNull(_columnIndexOfPetId)) {
            _tmpPetId = null
          } else {
            _tmpPetId = _stmt.getLong(_columnIndexOfPetId)
          }
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
          val _tmpLatitude: Double
          _tmpLatitude = _stmt.getDouble(_columnIndexOfLatitude)
          val _tmpLongitude: Double
          _tmpLongitude = _stmt.getDouble(_columnIndexOfLongitude)
          val _tmpAddress: String
          _tmpAddress = _stmt.getText(_columnIndexOfAddress)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          _result = PetLocation(_tmpLocationId,_tmpPetId,_tmpName,_tmpType,_tmpLatitude,_tmpLongitude,_tmpAddress,_tmpNotes)
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
