package com.makeramen.notificationawake;

import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotificationListener extends NotificationListenerService {

    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        final PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "NotificationAwake");
        wakeLock.acquire();

        int delay = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getString("timeout", "5")) * 1000;

        mHandler.postDelayed(new Runnable() {
            @Override public void run() {
                wakeLock.release();
            }
        }, delay);
    }

    @Override public void onNotificationRemoved(StatusBarNotification sbn) {
        // meh
    }
}