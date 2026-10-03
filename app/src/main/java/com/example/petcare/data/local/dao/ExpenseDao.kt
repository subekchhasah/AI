package com.example.petcare.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.petcare.data.local.entities.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense): Long

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE petId = :petId ORDER BY date DESC")
    fun getExpensesByPetId(petId: Long): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE expenseId = :id LIMIT 1")
    fun getExpenseById(id: Long): Flow<Expense?>

    @Query("SELECT SUM(amount) FROM expenses")
    fun getTotalExpenseAmount(): Flow<Double?>

    @Query("SELECT SUM(amount) FROM expenses WHERE petId = :petId")
    fun getTotalExpenseAmountByPet(petId: Long): Flow<Double?>
}
