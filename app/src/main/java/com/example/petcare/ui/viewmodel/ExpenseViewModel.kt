package com.example.petcare.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.petcare.data.local.PetCareDatabase
import com.example.petcare.data.local.entities.Expense
import com.example.petcare.data.repository.ExpenseRepository
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ExpenseRepository
    val allExpenses: LiveData<List<Expense>>
    val totalExpenseAmount: LiveData<Double?>

    init {
        val dao = PetCareDatabase.getDatabase(application).expenseDao()
        repository = ExpenseRepository(dao)
        allExpenses = repository.getAllExpenses().asLiveData()
        totalExpenseAmount = repository.getTotalExpenseAmount().asLiveData()
    }

    fun getExpensesForPet(petId: Long): LiveData<List<Expense>> {
        return repository.getExpensesByPetId(petId).asLiveData()
    }

    fun getExpenseById(id: Long): LiveData<Expense?> {
        return repository.getExpenseById(id).asLiveData()
    }

    fun getTotalForPet(petId: Long): LiveData<Double?> {
        return repository.getTotalExpenseAmountByPet(petId).asLiveData()
    }

    fun saveExpense(
        id: Long,
        petId: Long?,
        category: String,
        description: String,
        amount: Double,
        date: String,
        notes: String,
        onComplete: (Long) -> Unit,
    ) {
        viewModelScope.launch {
            val expense = Expense(
                expenseId = if (id > 0) id else 0,
                petId = petId,
                category = category,
                description = description,
                amount = amount,
                date = date,
                notes = notes
            )
            val resultId = if (id > 0) {
                repository.updateExpense(expense)
                id
            } else {
                repository.insertExpense(expense)
            }
            onComplete(resultId)
        }
    }

    fun deleteExpense(expense: Expense, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
            onComplete()
        }
    }
}
