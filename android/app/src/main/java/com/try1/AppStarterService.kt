package com.try1

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.facebook.react.ReactInstanceManager
import com.facebook.react.bridge.ReactContext
import com.facebook.react.modules.core.DeviceEventManagerModule

class AppStarterService : Service() {

    private val CHANNEL_ID = "AppStarterServiceChannel"

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("AppStarterService", "Service started")
        createNotificationChannel()
        startForeground(1, createNotification()) // Start the service in the foreground
        startReactNativeApp()
        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "App Starter Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        return Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("App Started")
            .setContentText("Click to open the app")
            .setSmallIcon(R.drawable.ic_notification) // Replace with your app's notification icon
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun startReactNativeApp() {
        val application = application as MainApplication
        val reactNativeHost = application.reactNativeHost

        if (!reactNativeHost.hasInstance()) {
            Log.d("AppStarterService", "Creating React Native instance")
            reactNativeHost.reactInstanceManager.createReactContextInBackground()
        }

        reactNativeHost.reactInstanceManager.addReactInstanceEventListener(object : ReactInstanceManager.ReactInstanceEventListener {
            override fun onReactContextInitialized(context: ReactContext) {
                Log.d("AppStarterService", "React context initialized")
                context.runOnJSQueueThread {
                    context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                        .emit("BootCompleted", null)
                }
                reactNativeHost.reactInstanceManager.removeReactInstanceEventListener(this)

                // Start MainActivity
                val mainActivityIntent = Intent(this@AppStarterService, MainActivity::class.java)
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(mainActivityIntent)
            }
        })
    }
}