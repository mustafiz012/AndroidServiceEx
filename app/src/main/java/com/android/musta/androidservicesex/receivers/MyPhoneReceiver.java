package com.android.musta.androidservicesex.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.musta.androidservicesex.interfaces.OnCustomBroadcastTriggeredAction;

/**
 * Created by musta on 12/2/17.
 */

public class MyPhoneReceiver extends BroadcastReceiver {
    private static final String TAG = "MyPhoneReceiver";
    private OnCustomBroadcastTriggeredAction mTriggeredAction;

    public MyPhoneReceiver(OnCustomBroadcastTriggeredAction triggeredAction) {
        this.mTriggeredAction = triggeredAction;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction().toString();
        Log.i(TAG, "onReceive: " + action);
        if (action.equals("android.intent.action.ACTION_POWER_CONNECTED")) {
            Log.i(TAG, "onReceive: Charger connected");
            mTriggeredAction.onPowerConnected();
        } else if (action.equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
            Log.i(TAG, "onReceive: Charger disconnected");
            mTriggeredAction.onPowerDisconnected();
        }
    }
}
