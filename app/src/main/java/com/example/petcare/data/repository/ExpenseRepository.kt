package com.example.petcare.data.repository

import com.example.petcare.data.local.dao.ExpenseDao
import com.example.petcare.data.local.entities.Expense
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    fun getAllExpenses(): Flow<List<Expense>> = expenseDao.getAllExpenses()
    fun getExpensesByPetId(petId: Long): Flow<List<Expense>> = expenseDao.getExpensesByPetId(petId)
    fun getExpenseById(id: Long): Flow<Expense?> = expenseDao.getExpenseById(id)
    fun getTotalExpenseAmount(): Flow<Double?> = expenseDao.getTotalExpenseAmount()
    fun getTotalExpenseAmountByPet(petId: Long): Flow<Double?> = expenseDao.getTotalExpenseAmountByPet(petId)
    suspend fun insertExpense(expense: Expense): Long = expenseDao.insertExpense(expense)
    suspend fun updateExpense(expense: Expense) = expenseDao.updateExpense(expense)
    suspend fun deleteExpense(expense: Expense) = expenseDao.deleteExpense(expense)
}
