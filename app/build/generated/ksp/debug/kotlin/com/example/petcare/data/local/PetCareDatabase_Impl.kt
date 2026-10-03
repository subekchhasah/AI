package com.example.petcare.`data`.local

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.example.petcare.`data`.local.dao.CareRoutineDao
import com.example.petcare.`data`.local.dao.CareRoutineDao_Impl
import com.example.petcare.`data`.local.dao.CareTaskDao
import com.example.petcare.`data`.local.dao.CareTaskDao_Impl
import com.example.petcare.`data`.local.dao.ExpenseDao
import com.example.petcare.`data`.local.dao.ExpenseDao_Impl
import com.example.petcare.`data`.local.dao.MedicalRecordDao
import com.example.petcare.`data`.local.dao.MedicalRecordDao_Impl
import com.example.petcare.`data`.local.dao.PetDao
import com.example.petcare.`data`.local.dao.PetDao_Impl
import com.example.petcare.`data`.local.dao.PetLocationDao
import com.example.petcare.`data`.local.dao.PetLocationDao_Impl
import com.example.petcare.`data`.local.dao.UserDao
import com.example.petcare.`data`.local.dao.UserDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class PetCareDatabase_Impl : PetCareDatabase() {
  private val _userDao: Lazy<UserDao> = lazy {
    UserDao_Impl(this)
  }

  private val _petDao: Lazy<PetDao> = lazy {
    PetDao_Impl(this)
  }

  private val _careRoutineDao: Lazy<CareRoutineDao> = lazy {
    CareRoutineDao_Impl(this)
  }

  private val _careTaskDao: Lazy<CareTaskDao> = lazy {
    CareTaskDao_Impl(this)
  }

  private val _medicalRecordDao: Lazy<MedicalRecordDao> = lazy {
    MedicalRecordDao_Impl(this)
  }

  private val _expenseDao: Lazy<ExpenseDao> = lazy {
    ExpenseDao_Impl(this)
  }

  private val _petLocationDao: Lazy<PetLocationDao> = lazy {
    PetLocationDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(9, "ad4bbf62453a3f5c35ee014262284b13", "56e810520423d9124ad3304cef8c8100") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `users` (`userId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `email` TEXT NOT NULL, `passwordHash` TEXT NOT NULL, `createdAt` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `pets` (`petId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `name` TEXT NOT NULL, `species` TEXT NOT NULL, `breed` TEXT NOT NULL, `dateOfBirth` TEXT NOT NULL, `gender` TEXT NOT NULL, `weight` REAL NOT NULL, `dietaryPreferences` TEXT NOT NULL, `allergies` TEXT NOT NULL, `vaccinationHistory` TEXT NOT NULL, `medicalNotes` TEXT NOT NULL, `favouriteToys` TEXT NOT NULL, `notes` TEXT NOT NULL, `imageUri` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_pets_userId` ON `pets` (`userId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `care_routines` (`routineId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `petId` INTEGER NOT NULL, `routineName` TEXT NOT NULL, `description` TEXT NOT NULL, `frequency` TEXT NOT NULL, `startDate` TEXT NOT NULL, `endDate` TEXT NOT NULL, `notes` TEXT NOT NULL, `imageUri` TEXT, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`petId`) REFERENCES `pets`(`petId`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_care_routines_petId` ON `care_routines` (`petId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `care_tasks` (`taskId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `petId` INTEGER, `routineId` INTEGER, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `category` TEXT NOT NULL, `scheduledDate` TEXT NOT NULL, `scheduledTime` TEXT NOT NULL, `frequency` TEXT NOT NULL, `requiredSupplies` TEXT NOT NULL, `notes` TEXT NOT NULL, `priority` TEXT NOT NULL, `reminderEnabled` INTEGER NOT NULL, `isCompleted` INTEGER NOT NULL, `completedAt` INTEGER, FOREIGN KEY(`petId`) REFERENCES `pets`(`petId`) ON UPDATE NO ACTION ON DELETE SET NULL , FOREIGN KEY(`routineId`) REFERENCES `care_routines`(`routineId`) ON UPDATE NO ACTION ON DELETE SET NULL )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_care_tasks_petId` ON `care_tasks` (`petId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_care_tasks_routineId` ON `care_tasks` (`routineId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `medical_records` (`medicalRecordId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `petId` INTEGER, `recordType` TEXT NOT NULL, `title` TEXT NOT NULL, `date` TEXT NOT NULL, `veterinarian` TEXT NOT NULL, `clinic` TEXT NOT NULL, `notes` TEXT NOT NULL, `attachmentUri` TEXT)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_medical_records_petId` ON `medical_records` (`petId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `expenses` (`expenseId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `petId` INTEGER, `category` TEXT NOT NULL, `description` TEXT NOT NULL, `amount` REAL NOT NULL, `date` TEXT NOT NULL, `notes` TEXT NOT NULL, FOREIGN KEY(`petId`) REFERENCES `pets`(`petId`) ON UPDATE NO ACTION ON DELETE SET NULL )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_expenses_petId` ON `expenses` (`petId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `pet_locations` (`locationId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `petId` INTEGER, `name` TEXT NOT NULL, `type` TEXT NOT NULL, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `address` TEXT NOT NULL, `notes` TEXT NOT NULL, FOREIGN KEY(`petId`) REFERENCES `pets`(`petId`) ON UPDATE NO ACTION ON DELETE SET NULL )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_pet_locations_petId` ON `pet_locations` (`petId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'ad4bbf62453a3f5c35ee014262284b13')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `users`")
        connection.execSQL("DROP TABLE IF EXISTS `pets`")
        connection.execSQL("DROP TABLE IF EXISTS `care_routines`")
        connection.execSQL("DROP TABLE IF EXISTS `care_tasks`")
        connection.execSQL("DROP TABLE IF EXISTS `medical_records`")
        connection.execSQL("DROP TABLE IF EXISTS `expenses`")
        connection.execSQL("DROP TABLE IF EXISTS `pet_locations`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        connection.execSQL("PRAGMA foreign_keys = ON")
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsUsers: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsUsers.put("userId", TableInfo.Column("userId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("email", TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("passwordHash", TableInfo.Column("passwordHash", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysUsers: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesUsers: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoUsers: TableInfo = TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers)
        val _existingUsers: TableInfo = read(connection, "users")
        if (!_infoUsers.equals(_existingUsers)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |users(com.example.petcare.data.local.entities.User).
              | Expected:
              |""".trimMargin() + _infoUsers + """
              |
              | Found:
              |""".trimMargin() + _existingUsers)
        }
        val _columnsPets: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsPets.put("petId", TableInfo.Column("petId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPets.put("userId", TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPets.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPets.put("species", TableInfo.Column("species", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPets.put("breed", TableInfo.Column("breed", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPets.put("dateOfBirth", TableInfo.Column("dateOfBirth", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPets.put("gender", TableInfo.Column("gender", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPets.put("weight", TableInfo.Column("weight", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPets.put("dietaryPreferences", TableInfo.Column("dietaryPreferences", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPets.put("allergies", TableInfo.Column("allergies", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPets.put("vaccinationHistory", TableInfo.Column("vaccinationHistory", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPets.put("medicalNotes", TableInfo.Column("medicalNotes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPets.put("favouriteToys", TableInfo.Column("favouriteToys", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPets.put("notes", TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPets.put("imageUri", TableInfo.Column("imageUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPets.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPets.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysPets: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesPets: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesPets.add(TableInfo.Index("index_pets_userId", false, listOf("userId"), listOf("ASC")))
        val _infoPets: TableInfo = TableInfo("pets", _columnsPets, _foreignKeysPets, _indicesPets)
        val _existingPets: TableInfo = read(connection, "pets")
        if (!_infoPets.equals(_existingPets)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |pets(com.example.petcare.data.local.entities.Pet).
              | Expected:
              |""".trimMargin() + _infoPets + """
              |
              | Found:
              |""".trimMargin() + _existingPets)
        }
        val _columnsCareRoutines: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsCareRoutines.put("routineId", TableInfo.Column("routineId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCareRoutines.put("petId", TableInfo.Column("petId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCareRoutines.put("routineName", TableInfo.Column("routineName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCareRoutines.put("description", TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCareRoutines.put("frequency", TableInfo.Column("frequency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCareRoutines.put("startDate", TableInfo.Column("startDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCareRoutines.put("endDate", TableInfo.Column("endDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCareRoutines.put("notes", TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCareRoutines.put("imageUri", TableInfo.Column("imageUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCareRoutines.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysCareRoutines: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysCareRoutines.add(TableInfo.ForeignKey("pets", "CASCADE", "NO ACTION", listOf("petId"), listOf("petId")))
        val _indicesCareRoutines: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesCareRoutines.add(TableInfo.Index("index_care_routines_petId", false, listOf("petId"), listOf("ASC")))
        val _infoCareRoutines: TableInfo = TableInfo("care_routines", _columnsCareRoutines, _foreignKeysCareRoutines, _indicesCareRoutines)
        val _existingCareRoutines: TableInfo = read(connection, "care_routines")
        if (!_infoCareRoutines.equals(_existingCareRoutines)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |care_routines(com.example.petcare.data.local.entities.CareRoutine).
              | Expected:
              |""".trimMargin() + _infoCareRoutines + """
              |
              | Found:
              |""".trimMargin() + _existingCareRoutines)
        }
        val _columnsCareTasks: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsCareTasks.put("taskId", TableInfo.Column("taskId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCareTasks.put("petId", TableInfo.Column("petId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCareTasks.put("routineId", TableInfo.Column("routineId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCareTasks.put("title", TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCareTasks.put("description", TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCareTasks.put("category", TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCareTasks.put("scheduledDate", TableInfo.Column("scheduledDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCareTasks.put("scheduledTime", TableInfo.Column("scheduledTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCareTasks.put("frequency", TableInfo.Column("frequency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCareTasks.put("requiredSupplies", TableInfo.Column("requiredSupplies", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCareTasks.put("notes", TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCareTasks.put("priority", TableInfo.Column("priority", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCareTasks.put("reminderEnabled", TableInfo.Column("reminderEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCareTasks.put("isCompleted", TableInfo.Column("isCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCareTasks.put("completedAt", TableInfo.Column("completedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysCareTasks: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysCareTasks.add(TableInfo.ForeignKey("pets", "SET NULL", "NO ACTION", listOf("petId"), listOf("petId")))
        _foreignKeysCareTasks.add(TableInfo.ForeignKey("care_routines", "SET NULL", "NO ACTION", listOf("routineId"), listOf("routineId")))
        val _indicesCareTasks: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesCareTasks.add(TableInfo.Index("index_care_tasks_petId", false, listOf("petId"), listOf("ASC")))
        _indicesCareTasks.add(TableInfo.Index("index_care_tasks_routineId", false, listOf("routineId"), listOf("ASC")))
        val _infoCareTasks: TableInfo = TableInfo("care_tasks", _columnsCareTasks, _foreignKeysCareTasks, _indicesCareTasks)
        val _existingCareTasks: TableInfo = read(connection, "care_tasks")
        if (!_infoCareTasks.equals(_existingCareTasks)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |care_tasks(com.example.petcare.data.local.entities.CareTask).
              | Expected:
              |""".trimMargin() + _infoCareTasks + """
              |
              | Found:
              |""".trimMargin() + _existingCareTasks)
        }
        val _columnsMedicalRecords: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsMedicalRecords.put("medicalRecordId", TableInfo.Column("medicalRecordId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMedicalRecords.put("petId", TableInfo.Column("petId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMedicalRecords.put("recordType", TableInfo.Column("recordType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMedicalRecords.put("title", TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMedicalRecords.put("date", TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMedicalRecords.put("veterinarian", TableInfo.Column("veterinarian", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMedicalRecords.put("clinic", TableInfo.Column("clinic", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMedicalRecords.put("notes", TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMedicalRecords.put("attachmentUri", TableInfo.Column("attachmentUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysMedicalRecords: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesMedicalRecords: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesMedicalRecords.add(TableInfo.Index("index_medical_records_petId", false, listOf("petId"), listOf("ASC")))
        val _infoMedicalRecords: TableInfo = TableInfo("medical_records", _columnsMedicalRecords, _foreignKeysMedicalRecords, _indicesMedicalRecords)
        val _existingMedicalRecords: TableInfo = read(connection, "medical_records")
        if (!_infoMedicalRecords.equals(_existingMedicalRecords)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |medical_records(com.example.petcare.data.local.entities.MedicalRecord).
              | Expected:
              |""".trimMargin() + _infoMedicalRecords + """
              |
              | Found:
              |""".trimMargin() + _existingMedicalRecords)
        }
        val _columnsExpenses: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsExpenses.put("expenseId", TableInfo.Column("expenseId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsExpenses.put("petId", TableInfo.Column("petId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsExpenses.put("category", TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsExpenses.put("description", TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsExpenses.put("amount", TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsExpenses.put("date", TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsExpenses.put("notes", TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysExpenses: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysExpenses.add(TableInfo.ForeignKey("pets", "SET NULL", "NO ACTION", listOf("petId"), listOf("petId")))
        val _indicesExpenses: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesExpenses.add(TableInfo.Index("index_expenses_petId", false, listOf("petId"), listOf("ASC")))
        val _infoExpenses: TableInfo = TableInfo("expenses", _columnsExpenses, _foreignKeysExpenses, _indicesExpenses)
        val _existingExpenses: TableInfo = read(connection, "expenses")
        if (!_infoExpenses.equals(_existingExpenses)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |expenses(com.example.petcare.data.local.entities.Expense).
              | Expected:
              |""".trimMargin() + _infoExpenses + """
              |
              | Found:
              |""".trimMargin() + _existingExpenses)
        }
        val _columnsPetLocations: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsPetLocations.put("locationId", TableInfo.Column("locationId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPetLocations.put("petId", TableInfo.Column("petId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPetLocations.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPetLocations.put("type", TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPetLocations.put("latitude", TableInfo.Column("latitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPetLocations.put("longitude", TableInfo.Column("longitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPetLocations.put("address", TableInfo.Column("address", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPetLocations.put("notes", TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysPetLocations: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysPetLocations.add(TableInfo.ForeignKey("pets", "SET NULL", "NO ACTION", listOf("petId"), listOf("petId")))
        val _indicesPetLocations: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesPetLocations.add(TableInfo.Index("index_pet_locations_petId", false, listOf("petId"), listOf("ASC")))
        val _infoPetLocations: TableInfo = TableInfo("pet_locations", _columnsPetLocations, _foreignKeysPetLocations, _indicesPetLocations)
        val _existingPetLocations: TableInfo = read(connection, "pet_locations")
        if (!_infoPetLocations.equals(_existingPetLocations)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |pet_locations(com.example.petcare.data.local.entities.PetLocation).
              | Expected:
              |""".trimMargin() + _infoPetLocations + """
              |
              | Found:
              |""".trimMargin() + _existingPetLocations)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "users", "pets", "care_routines", "care_tasks", "medical_records", "expenses", "pet_locations")
  }

  public override fun clearAllTables() {
    super.performClear(true, "users", "pets", "care_routines", "care_tasks", "medical_records", "expenses", "pet_locations")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(UserDao::class, UserDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(PetDao::class, PetDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(CareRoutineDao::class, CareRoutineDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(CareTaskDao::class, CareTaskDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(MedicalRecordDao::class, MedicalRecordDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(ExpenseDao::class, ExpenseDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(PetLocationDao::class, PetLocationDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun userDao(): UserDao = _userDao.value

  public override fun petDao(): PetDao = _petDao.value

  public override fun careRoutineDao(): CareRoutineDao = _careRoutineDao.value

  public override fun careTaskDao(): CareTaskDao = _careTaskDao.value

  public override fun medicalRecordDao(): MedicalRecordDao = _medicalRecordDao.value

  public override fun expenseDao(): ExpenseDao = _expenseDao.value

  public override fun petLocationDao(): PetLocationDao = _petLocationDao.value
}
