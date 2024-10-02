package com.try1

import android.app.Application
import android.util.Log
import com.facebook.react.PackageList
import com.facebook.react.ReactApplication
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.load
import com.facebook.react.defaults.DefaultReactNativeHost
import com.facebook.soloader.SoLoader

class MainApplication : Application(), ReactApplication {

  private val mReactNativeHost = object : DefaultReactNativeHost(this) {
    override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG

    override fun getPackages(): List<ReactPackage> = PackageList(this).packages.apply {
      // Packages that cannot be autolinked yet can be added manually here
      add(SmsListenerPackage())
      Log.d("MainApplication", "Added SmsListenerPackage")
    }

    override fun getJSMainModuleName(): String = "index"
  }

  override val reactNativeHost: ReactNativeHost
    get() = mReactNativeHost

  override fun onCreate() {
    super.onCreate()
    SoLoader.init(this, false)
    if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
      load()
    }
    Log.d("MainApplication", "onCreate called")
  }
}