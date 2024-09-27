package com.try1

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import android.util.Log

class CalendarModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    init {
        Log.d("CalendarModule", "CalendarModule initialized") // Initialization log
    }

    override fun getName() = "CalendarModule"

    @ReactMethod
    fun createCalendarEvent(name: String, location: String) {
        Log.d("CalendarModule", "Method called: createCalendarEvent") // Log method name
        Log.d("CalendarModule", "Create event called with name: $name and location: $location") // Existing log
        // Add more logs if necessary
        Log.d("CalendarModule", "Event creation logic would go here.") // Additional log
    }

    @ReactMethod
    fun deleteCalendarEvent(eventId: String) {
        Log.d("CalendarModule", "Delete event called with eventId: $eventId") // Log deletion
        // Add your event deletion logic here
        Log.d("CalendarModule", "Event deletion logic would go here.") // Additional log
    }
}