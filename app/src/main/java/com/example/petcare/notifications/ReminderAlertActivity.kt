package com.example.petcare.notifications

import android.app.AlarmManager
import android.app.KeyguardManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.petcare.R
import com.example.petcare.data.local.PetCareDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReminderAlertActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Show over lockscreen and wake up screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            )
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_alert)

        val title = intent.getStringExtra("EXTRA_TITLE") ?: "Doggy Task Reminder 🐶"
        val desc = intent.getStringExtra("EXTRA_DESC") ?: "It's time to take care of your pet!"
        val taskId = intent.getIntExtra("EXTRA_TASK_ID", -1)

        findViewById<TextView>(R.id.tv_alert_title).text = title
        findViewById<TextView>(R.id.tv_alert_desc).text = desc

        // Clear status bar notification
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (taskId != -1) {
            notificationManager.cancel(taskId)
        }

        val triggerSnooze = {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val snoozeIntent = Intent(this, TaskReminderReceiver::class.java).apply {
                putExtra(TaskReminderReceiver.EXTRA_TASK_ID, taskId.toLong())
                putExtra(TaskReminderReceiver.EXTRA_TASK_TITLE, title)
                putExtra(TaskReminderReceiver.EXTRA_TASK_DESC, desc)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                (taskId + 5000),
                snoozeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val triggerTime = System.currentTimeMillis() + (5 * 60 * 1000) // 5 minutes
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            }
            Toast.makeText(this, "Reminder repeated in 5 minutes 🐾", Toast.LENGTH_SHORT).show()
            finish()
        }

        // NO Button -> Snooze for 5 minutes
        findViewById<Button>(R.id.btn_alert_no).setOnClickListener {
            triggerSnooze()
        }

        // Snooze Button -> Snooze for 5 minutes
        findViewById<Button>(R.id.btn_alert_snooze).setOnClickListener {
            triggerSnooze()
        }

        // YES Button -> Mark Task Completed in DB
        findViewById<Button>(R.id.btn_alert_yes).setOnClickListener {
            if (taskId != -1) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val db = PetCareDatabase.getDatabase(applicationContext)
                    val taskDao = db.careTaskDao()
                    val task = taskDao.getTaskByIdDirect(taskId.toLong())
                    if (task != null) {
                        taskDao.updateTask(task.copy(isCompleted = true))
                    }
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ReminderAlertActivity, "Woof! Great job! Task marked complete! 🐶🎉", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Woof! Great job! Task marked complete! 🐶🎉", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
}
