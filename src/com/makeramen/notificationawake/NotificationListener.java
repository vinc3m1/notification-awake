package com.makeramen.notificationawake;

import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationListener extends NotificationListenerService {

    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("vmi", "onCreate Service");
        mHandler = new Handler();
    }

    @Override
    public void onDestroy() {
        Log.d("vmi", "onDestroy Service");
        super.onDestroy();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.d("vmi", "onNotificationPosted");

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        final PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "NotificationAwake");
        Log.d("vmi", "acquire wakeLock");
        wakeLock.acquire();

        mHandler.postDelayed(new Runnable() {
            @Override public void run() {
                Log.d("vmi", "release wakeLock");
                wakeLock.release();
            }
        }, PreferenceManager.getDefaultSharedPreferences(this).getInt("timeout", 5) * 1000);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // meh
    }
}