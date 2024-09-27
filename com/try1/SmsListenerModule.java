package com.try1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SmsListenerModule extends BroadcastReceiver {
    // Default constructor
    public SmsListenerModule() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Handle the received SMS here
        Log.i("SmsListenerModule", "SMS received");
        // You can add your logic to process the SMS here
    }
}