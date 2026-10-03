package com.example.petcare.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.petcare.`data`.local.entities.Pet
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
public class PetDao_Impl(
  __db: RoomDatabase,
) : PetDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfPet: EntityInsertAdapter<Pet>

  private val __deleteAdapterOfPet: EntityDeleteOrUpdateAdapter<Pet>

  private val __updateAdapterOfPet: EntityDeleteOrUpdateAdapter<Pet>
  init {
    this.__db = __db
    this.__insertAdapterOfPet = object : EntityInsertAdapter<Pet>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `pets` (`petId`,`userId`,`name`,`species`,`breed`,`dateOfBirth`,`gender`,`weight`,`dietaryPreferences`,`allergies`,`vaccinationHistory`,`medicalNotes`,`favouriteToys`,`notes`,`imageUri`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Pet) {
        statement.bindLong(1, entity.petId)
        statement.bindLong(2, entity.userId)
        statement.bindText(3, entity.name)
        statement.bindText(4, entity.species)
        statement.bindText(5, entity.breed)
        statement.bindText(6, entity.dateOfBirth)
        statement.bindText(7, entity.gender)
        statement.bindDouble(8, entity.weight)
        statement.bindText(9, entity.dietaryPreferences)
        statement.bindText(10, entity.allergies)
        statement.bindText(11, entity.vaccinationHistory)
        statement.bindText(12, entity.medicalNotes)
        statement.bindText(13, entity.favouriteToys)
        statement.bindText(14, entity.notes)
        val _tmpImageUri: String? = entity.imageUri
        if (_tmpImageUri == null) {
          statement.bindNull(15)
        } else {
          statement.bindText(15, _tmpImageUri)
        }
        statement.bindLong(16, entity.createdAt)
        statement.bindLong(17, entity.updatedAt)
      }
    }
    this.__deleteAdapterOfPet = object : EntityDeleteOrUpdateAdapter<Pet>() {
      protected override fun createQuery(): String = "DELETE FROM `pets` WHERE `petId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Pet) {
        statement.bindLong(1, entity.petId)
      }
    }
    this.__updateAdapterOfPet = object : EntityDeleteOrUpdateAdapter<Pet>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `pets` SET `petId` = ?,`userId` = ?,`name` = ?,`species` = ?,`breed` = ?,`dateOfBirth` = ?,`gender` = ?,`weight` = ?,`dietaryPreferences` = ?,`allergies` = ?,`vaccinationHistory` = ?,`medicalNotes` = ?,`favouriteToys` = ?,`notes` = ?,`imageUri` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `petId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Pet) {
        statement.bindLong(1, entity.petId)
        statement.bindLong(2, entity.userId)
        statement.bindText(3, entity.name)
        statement.bindText(4, entity.species)
        statement.bindText(5, entity.breed)
        statement.bindText(6, entity.dateOfBirth)
        statement.bindText(7, entity.gender)
        statement.bindDouble(8, entity.weight)
        statement.bindText(9, entity.dietaryPreferences)
        statement.bindText(10, entity.allergies)
        statement.bindText(11, entity.vaccinationHistory)
        statement.bindText(12, entity.medicalNotes)
        statement.bindText(13, entity.favouriteToys)
        statement.bindText(14, entity.notes)
        val _tmpImageUri: String? = entity.imageUri
        if (_tmpImageUri == null) {
          statement.bindNull(15)
        } else {
          statement.bindText(15, _tmpImageUri)
        }
        statement.bindLong(16, entity.createdAt)
        statement.bindLong(17, entity.updatedAt)
        statement.bindLong(18, entity.petId)
      }
    }
  }

  public override suspend fun insertPet(pet: Pet): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfPet.insertAndReturnId(_connection, pet)
    _result
  }

  public override suspend fun deletePet(pet: Pet): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfPet.handle(_connection, pet)
  }

  public override suspend fun updatePet(pet: Pet): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfPet.handle(_connection, pet)
  }

  public override fun getPetsByUserId(userId: Long): Flow<List<Pet>> {
    val _sql: String = "SELECT * FROM pets WHERE userId = ? ORDER BY name ASC"
    return createFlow(__db, false, arrayOf("pets")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        val _columnIndexOfPetId: Int = getColumnIndexOrThrow(_stmt, "petId")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfSpecies: Int = getColumnIndexOrThrow(_stmt, "species")
        val _columnIndexOfBreed: Int = getColumnIndexOrThrow(_stmt, "breed")
        val _columnIndexOfDateOfBirth: Int = getColumnIndexOrThrow(_stmt, "dateOfBirth")
        val _columnIndexOfGender: Int = getColumnIndexOrThrow(_stmt, "gender")
        val _columnIndexOfWeight: Int = getColumnIndexOrThrow(_stmt, "weight")
        val _columnIndexOfDietaryPreferences: Int = getColumnIndexOrThrow(_stmt, "dietaryPreferences")
        val _columnIndexOfAllergies: Int = getColumnIndexOrThrow(_stmt, "allergies")
        val _columnIndexOfVaccinationHistory: Int = getColumnIndexOrThrow(_stmt, "vaccinationHistory")
        val _columnIndexOfMedicalNotes: Int = getColumnIndexOrThrow(_stmt, "medicalNotes")
        val _columnIndexOfFavouriteToys: Int = getColumnIndexOrThrow(_stmt, "favouriteToys")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfImageUri: Int = getColumnIndexOrThrow(_stmt, "imageUri")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _result: MutableList<Pet> = mutableListOf()
        while (_stmt.step()) {
          val _item: Pet
          val _tmpPetId: Long
          _tmpPetId = _stmt.getLong(_columnIndexOfPetId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpSpecies: String
          _tmpSpecies = _stmt.getText(_columnIndexOfSpecies)
          val _tmpBreed: String
          _tmpBreed = _stmt.getText(_columnIndexOfBreed)
          val _tmpDateOfBirth: String
          _tmpDateOfBirth = _stmt.getText(_columnIndexOfDateOfBirth)
          val _tmpGender: String
          _tmpGender = _stmt.getText(_columnIndexOfGender)
          val _tmpWeight: Double
          _tmpWeight = _stmt.getDouble(_columnIndexOfWeight)
          val _tmpDietaryPreferences: String
          _tmpDietaryPreferences = _stmt.getText(_columnIndexOfDietaryPreferences)
          val _tmpAllergies: String
          _tmpAllergies = _stmt.getText(_columnIndexOfAllergies)
          val _tmpVaccinationHistory: String
          _tmpVaccinationHistory = _stmt.getText(_columnIndexOfVaccinationHistory)
          val _tmpMedicalNotes: String
          _tmpMedicalNotes = _stmt.getText(_columnIndexOfMedicalNotes)
          val _tmpFavouriteToys: String
          _tmpFavouriteToys = _stmt.getText(_columnIndexOfFavouriteToys)
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
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          _item = Pet(_tmpPetId,_tmpUserId,_tmpName,_tmpSpecies,_tmpBreed,_tmpDateOfBirth,_tmpGender,_tmpWeight,_tmpDietaryPreferences,_tmpAllergies,_tmpVaccinationHistory,_tmpMedicalNotes,_tmpFavouriteToys,_tmpNotes,_tmpImageUri,_tmpCreatedAt,_tmpUpdatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getPetById(petId: Long): Flow<Pet?> {
    val _sql: String = "SELECT * FROM pets WHERE petId = ? LIMIT 1"
    return createFlow(__db, false, arrayOf("pets")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, petId)
        val _columnIndexOfPetId: Int = getColumnIndexOrThrow(_stmt, "petId")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfSpecies: Int = getColumnIndexOrThrow(_stmt, "species")
        val _columnIndexOfBreed: Int = getColumnIndexOrThrow(_stmt, "breed")
        val _columnIndexOfDateOfBirth: Int = getColumnIndexOrThrow(_stmt, "dateOfBirth")
        val _columnIndexOfGender: Int = getColumnIndexOrThrow(_stmt, "gender")
        val _columnIndexOfWeight: Int = getColumnIndexOrThrow(_stmt, "weight")
        val _columnIndexOfDietaryPreferences: Int = getColumnIndexOrThrow(_stmt, "dietaryPreferences")
        val _columnIndexOfAllergies: Int = getColumnIndexOrThrow(_stmt, "allergies")
        val _columnIndexOfVaccinationHistory: Int = getColumnIndexOrThrow(_stmt, "vaccinationHistory")
        val _columnIndexOfMedicalNotes: Int = getColumnIndexOrThrow(_stmt, "medicalNotes")
        val _columnIndexOfFavouriteToys: Int = getColumnIndexOrThrow(_stmt, "favouriteToys")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfImageUri: Int = getColumnIndexOrThrow(_stmt, "imageUri")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _result: Pet?
        if (_stmt.step()) {
          val _tmpPetId: Long
          _tmpPetId = _stmt.getLong(_columnIndexOfPetId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpSpecies: String
          _tmpSpecies = _stmt.getText(_columnIndexOfSpecies)
          val _tmpBreed: String
          _tmpBreed = _stmt.getText(_columnIndexOfBreed)
          val _tmpDateOfBirth: String
          _tmpDateOfBirth = _stmt.getText(_columnIndexOfDateOfBirth)
          val _tmpGender: String
          _tmpGender = _stmt.getText(_columnIndexOfGender)
          val _tmpWeight: Double
          _tmpWeight = _stmt.getDouble(_columnIndexOfWeight)
          val _tmpDietaryPreferences: String
          _tmpDietaryPreferences = _stmt.getText(_columnIndexOfDietaryPreferences)
          val _tmpAllergies: String
          _tmpAllergies = _stmt.getText(_columnIndexOfAllergies)
          val _tmpVaccinationHistory: String
          _tmpVaccinationHistory = _stmt.getText(_columnIndexOfVaccinationHistory)
          val _tmpMedicalNotes: String
          _tmpMedicalNotes = _stmt.getText(_columnIndexOfMedicalNotes)
          val _tmpFavouriteToys: String
          _tmpFavouriteToys = _stmt.getText(_columnIndexOfFavouriteToys)
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
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          _result = Pet(_tmpPetId,_tmpUserId,_tmpName,_tmpSpecies,_tmpBreed,_tmpDateOfBirth,_tmpGender,_tmpWeight,_tmpDietaryPreferences,_tmpAllergies,_tmpVaccinationHistory,_tmpMedicalNotes,_tmpFavouriteToys,_tmpNotes,_tmpImageUri,_tmpCreatedAt,_tmpUpdatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getPetByIdDirect(petId: Long): Pet? {
    val _sql: String = "SELECT * FROM pets WHERE petId = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, petId)
        val _columnIndexOfPetId: Int = getColumnIndexOrThrow(_stmt, "petId")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfSpecies: Int = getColumnIndexOrThrow(_stmt, "species")
        val _columnIndexOfBreed: Int = getColumnIndexOrThrow(_stmt, "breed")
        val _columnIndexOfDateOfBirth: Int = getColumnIndexOrThrow(_stmt, "dateOfBirth")
        val _columnIndexOfGender: Int = getColumnIndexOrThrow(_stmt, "gender")
        val _columnIndexOfWeight: Int = getColumnIndexOrThrow(_stmt, "weight")
        val _columnIndexOfDietaryPreferences: Int = getColumnIndexOrThrow(_stmt, "dietaryPreferences")
        val _columnIndexOfAllergies: Int = getColumnIndexOrThrow(_stmt, "allergies")
        val _columnIndexOfVaccinationHistory: Int = getColumnIndexOrThrow(_stmt, "vaccinationHistory")
        val _columnIndexOfMedicalNotes: Int = getColumnIndexOrThrow(_stmt, "medicalNotes")
        val _columnIndexOfFavouriteToys: Int = getColumnIndexOrThrow(_stmt, "favouriteToys")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfImageUri: Int = getColumnIndexOrThrow(_stmt, "imageUri")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _result: Pet?
        if (_stmt.step()) {
          val _tmpPetId: Long
          _tmpPetId = _stmt.getLong(_columnIndexOfPetId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpSpecies: String
          _tmpSpecies = _stmt.getText(_columnIndexOfSpecies)
          val _tmpBreed: String
          _tmpBreed = _stmt.getText(_columnIndexOfBreed)
          val _tmpDateOfBirth: String
          _tmpDateOfBirth = _stmt.getText(_columnIndexOfDateOfBirth)
          val _tmpGender: String
          _tmpGender = _stmt.getText(_columnIndexOfGender)
          val _tmpWeight: Double
          _tmpWeight = _stmt.getDouble(_columnIndexOfWeight)
          val _tmpDietaryPreferences: String
          _tmpDietaryPreferences = _stmt.getText(_columnIndexOfDietaryPreferences)
          val _tmpAllergies: String
          _tmpAllergies = _stmt.getText(_columnIndexOfAllergies)
          val _tmpVaccinationHistory: String
          _tmpVaccinationHistory = _stmt.getText(_columnIndexOfVaccinationHistory)
          val _tmpMedicalNotes: String
          _tmpMedicalNotes = _stmt.getText(_columnIndexOfMedicalNotes)
          val _tmpFavouriteToys: String
          _tmpFavouriteToys = _stmt.getText(_columnIndexOfFavouriteToys)
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
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          _result = Pet(_tmpPetId,_tmpUserId,_tmpName,_tmpSpecies,_tmpBreed,_tmpDateOfBirth,_tmpGender,_tmpWeight,_tmpDietaryPreferences,_tmpAllergies,_tmpVaccinationHistory,_tmpMedicalNotes,_tmpFavouriteToys,_tmpNotes,_tmpImageUri,_tmpCreatedAt,_tmpUpdatedAt)
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
