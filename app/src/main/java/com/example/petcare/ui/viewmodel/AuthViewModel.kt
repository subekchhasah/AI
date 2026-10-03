package com.example.petcare.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.petcare.data.local.PetCareDatabase
import com.example.petcare.data.repository.AuthRepository
import com.example.petcare.utils.SessionManager
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AuthRepository
    private val sessionManager: SessionManager

    private val _authResult = MutableLiveData<Result<String>>()
    val authResult: LiveData<Result<String>> = _authResult

    private val _resetResult = MutableLiveData<Result<String>>()
    val resetResult: LiveData<Result<String>> = _resetResult

    fun resetPassword(email: String, newPassword: String) {
        viewModelScope.launch {
            val result = repository.resetPassword(email, newPassword)
            if (result.isSuccess) {
                _resetResult.value = Result.success("Password reset successfully! You can now log in.")
            } else {
                _resetResult.value = Result.failure(result.exceptionOrNull() ?: Exception("Failed to reset password."))
            }
        }
    }

    init {
        val userDao = PetCareDatabase.getDatabase(application).userDao()
        repository = AuthRepository(userDao)
        sessionManager = SessionManager(application)
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            val result = repository.registerUser(name, email, password)
            if (result.isSuccess) {
                val userId = result.getOrNull() ?: 1L
                sessionManager.saveAuthSession(userId, email, name)
                _authResult.value = Result.success("Registration successful!")
            } else {
                _authResult.value = Result.failure(result.exceptionOrNull() ?: Exception("Registration failed."))
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = repository.loginUser(email, password)
            if (result.isSuccess) {
                val user = result.getOrNull()!!
                sessionManager.saveAuthSession(user.userId, user.email, user.name)
                _authResult.value = Result.success("Login successful!")
            } else {
                _authResult.value = Result.failure(result.exceptionOrNull() ?: Exception("Invalid email or password."))
            }
        }
    }
}
