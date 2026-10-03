package com.example.petcare.data.repository

import com.example.petcare.data.local.dao.UserDao
import com.example.petcare.data.local.entities.User
import com.example.petcare.utils.PasswordHasher

class AuthRepository(private val userDao: UserDao) {
    suspend fun registerUser(name: String, email: String, password: String): Result<Long> {
        val cleanEmail = email.trim().lowercase()
        val existing = userDao.getUserByEmail(cleanEmail)
        if (existing != null) {
            return Result.failure(Exception("An account with $cleanEmail already exists. Please sign in!"))
        }
        val hashedPassword = PasswordHasher.hashPassword(password.trim())
        val user = User(name = name.trim(), email = cleanEmail, passwordHash = hashedPassword)
        val userId = userDao.insertUser(user)
        return Result.success(userId)
    }

    suspend fun loginUser(email: String, password: String): Result<User> {
        val cleanEmail = email.trim().lowercase()
        var user = userDao.getUserByEmail(cleanEmail)

        // Ensure default demo user exists even after DB migration
        if (user == null && cleanEmail == "emily@example.com") {
            val emily = User(
                userId = 1L,
                name = "Emily",
                email = "emily@example.com",
                passwordHash = PasswordHasher.hashPassword("Password123")
            )
            userDao.insertUser(emily)
            user = emily
        }

        if (user == null) {
            return Result.failure(Exception("No account found for $cleanEmail. Click 'Register Now' below to create one!"))
        }

        val hashedInput = PasswordHasher.hashPassword(password.trim())
        return if (user.passwordHash == hashedInput) {
            Result.success(user)
        } else {
            Result.failure(Exception("Incorrect password. Please try again or click 'Forgot Password?' to reset."))
        }
    }

    suspend fun resetPassword(email: String, newPassword: String): Result<Unit> {
        val cleanEmail = email.trim().lowercase()
        val user = userDao.getUserByEmail(cleanEmail)
            ?: return Result.failure(Exception("No account found with $cleanEmail."))
        val hashed = PasswordHasher.hashPassword(newPassword.trim())
        userDao.updatePasswordByEmail(cleanEmail, hashed)
        return Result.success(Unit)
    }
}
