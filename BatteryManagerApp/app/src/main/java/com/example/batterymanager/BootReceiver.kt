package com.example.batterymanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            // Restart the battery manager service after reboot
            val serviceIntent = Intent(context, BatteryMonitorService::class.java)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }

            Toast.makeText(context, context.getString(R.string.boot_completed_toast), Toast.LENGTH_SHORT).show()
        }
    }
}