package com.try1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            Log.d("BootReceiver", "Boot completed detected!")
            val serviceIntent = Intent(context, AppStarterService::class.java)
            context.startForegroundService(serviceIntent) // Use startForegroundService for Android O and above
        }
    }
}