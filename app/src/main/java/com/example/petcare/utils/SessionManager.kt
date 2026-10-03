package com.example.petcare.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveAuthSession(userId: Long, email: String, name: String) {
        prefs.edit().apply {
            putLong(KEY_USER_ID, userId)
            putString(KEY_EMAIL, email)
            putString(KEY_NAME, name)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun getUserId(): Long = prefs.getLong(KEY_USER_ID, 1L) // Defaults to Emily (1L) for prototype ease

    fun getUserName(): String = prefs.getString(KEY_NAME, "Emily") ?: "Emily"

    fun getUserEmail(): String = prefs.getString(KEY_EMAIL, "emily@example.com") ?: "emily@example.com"

    fun updateProfile(name: String, email: String) {
        prefs.edit().apply {
            putString(KEY_NAME, name)
            putString(KEY_EMAIL, email)
            apply()
        }
    }

    fun saveProfilePhotoUri(uri: String) {
        prefs.edit().putString(KEY_PHOTO_URI, uri).apply()
    }

    fun getProfilePhotoUri(): String? = prefs.getString(KEY_PHOTO_URI, null)

    fun savePassword(password: String) {
        prefs.edit().putString(KEY_PASSWORD, password).apply()
    }

    fun getPhoneNumber(): String = prefs.getString(KEY_PHONE, "+1 555-0199") ?: "+1 555-0199"
    fun setPhoneNumber(phone: String) = prefs.edit().putString(KEY_PHONE, phone).apply()

    fun isPushNotificationsEnabled(): Boolean = prefs.getBoolean(KEY_PUSH_NOTIFS, true)
    fun setPushNotificationsEnabled(enabled: Boolean) = prefs.edit().putBoolean(KEY_PUSH_NOTIFS, enabled).apply()

    fun isDailyDigestEnabled(): Boolean = prefs.getBoolean(KEY_DAILY_DIGEST, true)
    fun setDailyDigestEnabled(enabled: Boolean) = prefs.edit().putBoolean(KEY_DAILY_DIGEST, enabled).apply()

    fun isSoundVibrationEnabled(): Boolean = prefs.getBoolean(KEY_SOUND_VIBE, true)
    fun setSoundVibrationEnabled(enabled: Boolean) = prefs.edit().putBoolean(KEY_SOUND_VIBE, enabled).apply()

    fun getWeightUnit(): String = prefs.getString(KEY_WEIGHT_UNIT, "kg") ?: "kg"
    fun setWeightUnit(unit: String) = prefs.edit().putString(KEY_WEIGHT_UNIT, unit).apply()

    fun getTempUnit(): String = prefs.getString(KEY_TEMP_UNIT, "°C") ?: "°C"
    fun setTempUnit(unit: String) = prefs.edit().putString(KEY_TEMP_UNIT, unit).apply()

    fun getCurrency(): String = prefs.getString(KEY_CURRENCY, "$ (USD)") ?: "$ (USD)"
    fun setCurrency(currency: String) = prefs.edit().putString(KEY_CURRENCY, currency).apply()

    fun isDarkModeEnabled(): Boolean = prefs.getBoolean(KEY_DARK_MODE, false)
    fun setDarkModeEnabled(enabled: Boolean) = prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply()

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun logout() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREF_NAME = "petcare_user_session"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_EMAIL = "email"
        private const val KEY_NAME = "name"
        private const val KEY_PHONE = "phone"
        private const val KEY_PASSWORD = "password"
        private const val KEY_PHOTO_URI = "photo_uri"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"

        private const val KEY_PUSH_NOTIFS = "push_notifications"
        private const val KEY_DAILY_DIGEST = "daily_digest"
        private const val KEY_SOUND_VIBE = "sound_vibration"
        private const val KEY_WEIGHT_UNIT = "weight_unit"
        private const val KEY_TEMP_UNIT = "temp_unit"
        private const val KEY_CURRENCY = "currency"
        private const val KEY_DARK_MODE = "dark_mode"
    }
}
