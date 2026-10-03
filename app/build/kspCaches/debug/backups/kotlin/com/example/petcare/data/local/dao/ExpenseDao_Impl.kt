package com.example.petcare.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.petcare.`data`.local.entities.Expense
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
public class ExpenseDao_Impl(
  __db: RoomDatabase,
) : ExpenseDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfExpense: EntityInsertAdapter<Expense>

  private val __deleteAdapterOfExpense: EntityDeleteOrUpdateAdapter<Expense>

  private val __updateAdapterOfExpense: EntityDeleteOrUpdateAdapter<Expense>
  init {
    this.__db = __db
    this.__insertAdapterOfExpense = object : EntityInsertAdapter<Expense>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `expenses` (`expenseId`,`petId`,`category`,`description`,`amount`,`date`,`notes`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Expense) {
        statement.bindLong(1, entity.expenseId)
        val _tmpPetId: Long? = entity.petId
        if (_tmpPetId == null) {
          statement.bindNull(2)
        } else {
          statement.bindLong(2, _tmpPetId)
        }
        statement.bindText(3, entity.category)
        statement.bindText(4, entity.description)
        statement.bindDouble(5, entity.amount)
        statement.bindText(6, entity.date)
        statement.bindText(7, entity.notes)
      }
    }
    this.__deleteAdapterOfExpense = object : EntityDeleteOrUpdateAdapter<Expense>() {
      protected override fun createQuery(): String = "DELETE FROM `expenses` WHERE `expenseId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Expense) {
        statement.bindLong(1, entity.expenseId)
      }
    }
    this.__updateAdapterOfExpense = object : EntityDeleteOrUpdateAdapter<Expense>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `expenses` SET `expenseId` = ?,`petId` = ?,`category` = ?,`description` = ?,`amount` = ?,`date` = ?,`notes` = ? WHERE `expenseId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Expense) {
        statement.bindLong(1, entity.expenseId)
        val _tmpPetId: Long? = entity.petId
        if (_tmpPetId == null) {
          statement.bindNull(2)
        } else {
          statement.bindLong(2, _tmpPetId)
        }
        statement.bindText(3, entity.category)
        statement.bindText(4, entity.description)
        statement.bindDouble(5, entity.amount)
        statement.bindText(6, entity.date)
        statement.bindText(7, entity.notes)
        statement.bindLong(8, entity.expenseId)
      }
    }
  }

  public override suspend fun insertExpense(expense: Expense): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfExpense.insertAndReturnId(_connection, expense)
    _result
  }

  public override suspend fun deleteExpense(expense: Expense): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfExpense.handle(_connection, expense)
  }

  public override suspend fun updateExpense(expense: Expense): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfExpense.handle(_connection, expense)
  }

  public override fun getAllExpenses(): Flow<List<Expense>> {
    val _sql: String = "SELECT * FROM expenses ORDER BY date DESC"
    return createFlow(__db, false, arrayOf("expenses")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfExpenseId: Int = getColumnIndexOrThrow(_stmt, "expenseId")
        val _columnIndexOfPetId: Int = getColumnIndexOrThrow(_stmt, "petId")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _result: MutableList<Expense> = mutableListOf()
        while (_stmt.step()) {
          val _item: Expense
          val _tmpExpenseId: Long
          _tmpExpenseId = _stmt.getLong(_columnIndexOfExpenseId)
          val _tmpPetId: Long?
          if (_stmt.isNull(_columnIndexOfPetId)) {
            _tmpPetId = null
          } else {
            _tmpPetId = _stmt.getLong(_columnIndexOfPetId)
          }
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpAmount: Double
          _tmpAmount = _stmt.getDouble(_columnIndexOfAmount)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          _item = Expense(_tmpExpenseId,_tmpPetId,_tmpCategory,_tmpDescription,_tmpAmount,_tmpDate,_tmpNotes)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getExpensesByPetId(petId: Long): Flow<List<Expense>> {
    val _sql: String = "SELECT * FROM expenses WHERE petId = ? ORDER BY date DESC"
    return createFlow(__db, false, arrayOf("expenses")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, petId)
        val _columnIndexOfExpenseId: Int = getColumnIndexOrThrow(_stmt, "expenseId")
        val _columnIndexOfPetId: Int = getColumnIndexOrThrow(_stmt, "petId")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _result: MutableList<Expense> = mutableListOf()
        while (_stmt.step()) {
          val _item: Expense
          val _tmpExpenseId: Long
          _tmpExpenseId = _stmt.getLong(_columnIndexOfExpenseId)
          val _tmpPetId: Long?
          if (_stmt.isNull(_columnIndexOfPetId)) {
            _tmpPetId = null
          } else {
            _tmpPetId = _stmt.getLong(_columnIndexOfPetId)
          }
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpAmount: Double
          _tmpAmount = _stmt.getDouble(_columnIndexOfAmount)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          _item = Expense(_tmpExpenseId,_tmpPetId,_tmpCategory,_tmpDescription,_tmpAmount,_tmpDate,_tmpNotes)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getExpenseById(id: Long): Flow<Expense?> {
    val _sql: String = "SELECT * FROM expenses WHERE expenseId = ? LIMIT 1"
    return createFlow(__db, false, arrayOf("expenses")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfExpenseId: Int = getColumnIndexOrThrow(_stmt, "expenseId")
        val _columnIndexOfPetId: Int = getColumnIndexOrThrow(_stmt, "petId")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _result: Expense?
        if (_stmt.step()) {
          val _tmpExpenseId: Long
          _tmpExpenseId = _stmt.getLong(_columnIndexOfExpenseId)
          val _tmpPetId: Long?
          if (_stmt.isNull(_columnIndexOfPetId)) {
            _tmpPetId = null
          } else {
            _tmpPetId = _stmt.getLong(_columnIndexOfPetId)
          }
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpAmount: Double
          _tmpAmount = _stmt.getDouble(_columnIndexOfAmount)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          _result = Expense(_tmpExpenseId,_tmpPetId,_tmpCategory,_tmpDescription,_tmpAmount,_tmpDate,_tmpNotes)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getTotalExpenseAmount(): Flow<Double?> {
    val _sql: String = "SELECT SUM(amount) FROM expenses"
    return createFlow(__db, false, arrayOf("expenses")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Double?
        if (_stmt.step()) {
          val _tmp: Double?
          if (_stmt.isNull(0)) {
            _tmp = null
          } else {
            _tmp = _stmt.getDouble(0)
          }
          _result = _tmp
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getTotalExpenseAmountByPet(petId: Long): Flow<Double?> {
    val _sql: String = "SELECT SUM(amount) FROM expenses WHERE petId = ?"
    return createFlow(__db, false, arrayOf("expenses")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, petId)
        val _result: Double?
        if (_stmt.step()) {
          val _tmp: Double?
          if (_stmt.isNull(0)) {
            _tmp = null
          } else {
            _tmp = _stmt.getDouble(0)
          }
          _result = _tmp
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
