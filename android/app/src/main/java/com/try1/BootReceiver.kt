package com.try1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootReceiver", "Boot completed intent received")
            val serviceIntent = Intent(context, YourBackgroundService::class.java)
            context.startService(serviceIntent)
            Log.d("BootReceiver", "YourBackgroundService start intent sent")
        }
    }
}