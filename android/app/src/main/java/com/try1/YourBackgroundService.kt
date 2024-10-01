package com.try1

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class YourBackgroundService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("YourBackgroundService", "Service started")
        
        // Start the MainActivity (React Native app)
        val launchIntent = Intent(this, MainActivity::class.java)
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(launchIntent)
        
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("YourBackgroundService", "Service destroyed")
    }
}