package com.example.petcare.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.petcare.PetCareApplication

class TaskReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(EXTRA_TASK_TITLE) ?: "PetCare Reminder"
        val petName = intent.getStringExtra(EXTRA_PET_NAME) ?: "Your Pet"
        val description = intent.getStringExtra(EXTRA_TASK_DESC) ?: "Task is due now."
        val taskId = intent.getLongExtra(EXTRA_TASK_ID, System.currentTimeMillis()).toInt()

        val fullScreenIntent = Intent(context, ReminderAlertActivity::class.java).apply {
            putExtra("EXTRA_TASK_ID", taskId)
            putExtra("EXTRA_TITLE", "$petName: $title")
            putExtra("EXTRA_DESC", description)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val fullScreenPendingIntent = android.app.PendingIntent.getActivity(
            context,
            taskId,
            fullScreenIntent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )

        val contentIntent = Intent(context, com.example.petcare.ui.MainActivity::class.java).apply {
            putExtra("EXTRA_NAVIGATE_TO", com.example.petcare.R.id.checklistFragment)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val contentPendingIntent = android.app.PendingIntent.getActivity(
            context,
            taskId + 10000,
            contentIntent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, PetCareApplication.CHANNEL_ID_CARE_REMINDERS)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("$petName: $title")
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setContentIntent(contentPendingIntent)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setAutoCancel(true)

        try {
            context.startActivity(fullScreenIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            with(NotificationManagerCompat.from(context)) {
                notify(taskId, builder.build())
            }
        } catch (e: SecurityException) {
            // Permission POST_NOTIFICATIONS missing on Android 13+
            e.printStackTrace()
        }
    }

    companion object {
        const val EXTRA_TASK_ID = "extra_task_id"
        const val EXTRA_TASK_TITLE = "extra_task_title"
        const val EXTRA_PET_NAME = "extra_pet_name"
        const val EXTRA_TASK_DESC = "extra_task_desc"
    }
}
