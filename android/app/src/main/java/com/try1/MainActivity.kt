package com.try1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.app.ActivityManager
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate

class MainActivity : ReactActivity() {

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  override fun getMainComponentName(): String = "try1" // Ensure this matches your app name

  /**
   * Returns the instance of the [ReactActivityDelegate]. We use [DefaultReactActivityDelegate]
   * which allows you to enable New Architecture with a single boolean flags [fabricEnabled]
   */
  override fun createReactActivityDelegate(): ReactActivityDelegate =
      DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)

  override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      Log.d("MainActivity", "onCreate called")
      
      // Start the background service if it's not already running
      if (!isServiceRunning(YourBackgroundService::class.java)) {
          val serviceIntent = Intent(this, YourBackgroundService::class.java)
          startService(serviceIntent)
          Log.d("MainActivity", "Background service start attempted")
      }
  }

  private fun isServiceRunning(serviceClass: Class<*>): Boolean {
      val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
      for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
          if (serviceClass.name == service.service.className) {
              return true
          }
      }
      return false
  }
}