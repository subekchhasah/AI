package com.example.petcare

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class PetCareApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channelId = CHANNEL_ID_CARE_REMINDERS
        val name = getString(R.string.app_name) + " Care Reminders"
        val descriptionText = "Notifications for pet feeding, medication, walks, and vet appointments."
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_ID_CARE_REMINDERS = "petcare_reminders_channel"
    }
}
