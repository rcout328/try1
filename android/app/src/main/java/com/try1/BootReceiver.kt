package com.try1 // Ensure this matches your package declaration

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Handle the boot completed event
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            Log.d("BootReceiver", "Boot completed detected!")
            // You can add more logging or perform minimal actions here
            // Do NOT start activities or heavy operations here
        }
    }
}