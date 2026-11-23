package com.example.batterymanager

import android.app.Notification
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class BatteryMonitorService : Service() {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "battery_management_channel"
        const val NOTIFICATION_ID = 1001
        private const val TAG = "BatteryMonitorService"
    }

    private var startThreshold = 30
    private var stopThreshold = 80
    private var job: Job? = null
    private lateinit var powerManager: PowerManager

    override fun onCreate() {
        super.onCreate()
        powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startThreshold = intent?.getIntExtra("start_threshold", 30) ?: 30
        stopThreshold = intent?.getIntExtra("stop_threshold", 80) ?: 80

        Log.d(TAG, "Service started with thresholds - Start: $startThreshold%, Stop: $stopThreshold%")

        // Start as foreground service
        startForeground(NOTIFICATION_ID, createNotification())

        // Start monitoring in coroutine
        job = CoroutineScope(Dispatchers.Default).launch {
            monitorBattery()
        }

        return START_STICKY
    }

    private fun monitorBattery() {
        while (true) {
            try {
                val batteryLevel = getBatteryLevel()
                val isCharging = isDeviceCharging()
                val isPluggedIn = isPluggedIn()

                Log.d(TAG, "Battery: $batteryLevel%, Charging: $isCharging, Plugged: $isPluggedIn")

                // Logic to control charging (conceptual - actual control requires OEM-specific APIs)
                if (isCharging && batteryLevel >= stopThreshold) {
                    Log.i(TAG, "Stop charging recommended at $stopThreshold%")
                    // Note: Actual charging control requires system-level permissions or manufacturer APIs
                } else if (!isCharging && batteryLevel <= startThreshold) {
                    // Device is not charging but below threshold
                }

                // Update notification
                updateNotification(batteryLevel, isCharging)

                Thread.sleep(TimeUnit.SECONDS.toMillis(30))
            } catch (e: Exception) {
                Log.e(TAG, "Error monitoring battery", e)
                Thread.sleep(TimeUnit.SECONDS.toMillis(60))
            }
        }
    }

    private fun getBatteryLevel(): Int {
        val batteryManager = getSystemService(Context.BATTERY_SERVICE) as android.os.BatteryManager
        return batteryManager.getIntProperty(android.os.BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }

    private fun isDeviceCharging(): Boolean {
        val batteryManager = getSystemService(Context.BATTERY_SERVICE) as android.os.BatteryManager
        val status = batteryManager.getIntProperty(android.os.BatteryManager.BATTERY_PROPERTY_STATUS)
        return status == android.os.BatteryManager.BATTERY_STATUS_CHARGING ||
                status == android.os.BatteryManager.BATTERY_STATUS_FULL
    }

    private fun isPluggedIn(): Boolean {
        val batteryManager = getSystemService(Context.BATTERY_SERVICE) as android.os.BatteryManager
        val status = batteryManager.getIntProperty(android.os.BatteryManager.BATTERY_PROPERTY_STATUS)
        return status == android.os.BatteryManager.BATTERY_STATUS_CHARGING ||
                status == android.os.BatteryManager.BATTERY_STATUS_FULL ||
                status == android.os.BatteryManager.BATTERY_STATUS_DISCHARGING
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_name)
            val descriptionText = getString(R.string.notification_channel_desc)
            val importance = android.app.NotificationManager.IMPORTANCE_LOW
            val channel = android.app.NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
            channel.description = descriptionText
            val notificationManager: android.app.NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Battery Manager Active")
            .setContentText(String.format(
                getString(R.string.service_notification),
                startThreshold,
                stopThreshold
            ))
            .setSmallIcon(R.drawable.ic_battery_24)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    private fun updateNotification(batteryLevel: Int, isCharging: Boolean) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        notificationManager.notify(NOTIFICATION_ID, createNotification())
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}