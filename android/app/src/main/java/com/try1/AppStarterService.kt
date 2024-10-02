package com.try1

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.facebook.react.ReactInstanceManager
import com.facebook.react.bridge.ReactContext
import com.facebook.react.modules.core.DeviceEventManagerModule

class AppStarterService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("AppStarterService", "Service started") // Logging added
        startReactNativeApp() // Start the React Native app
        return START_STICKY
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
                val mainActivityIntent = Intent(this, MainActivity::class.java)
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(mainActivityIntent)
            }
        })
    }
}