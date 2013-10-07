package com.makeramen.notificationawake;

import android.app.Notification;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotificationListener extends NotificationListenerService implements SensorEventListener{

    private Handler mHandler;

    private SensorManager mSensorManager;
    private Sensor mProximity;

    private int NOTIFICATION_TIMEOUT = 10 * 1000;

    private boolean mCovered = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onDestroy() {
        mSensorManager.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if ((sbn.getNotification().flags & Notification.FLAG_SHOW_LIGHTS) != 0 && !mCovered) {

            final int origTimeout = android.provider.Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 30 * 1000);
            android.provider.Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, NOTIFICATION_TIMEOUT);

            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "NotificationAwake");
            wakeLock.acquire();
            wakeLock.release();

            mHandler.postDelayed(new Runnable() {
                @Override public void run() {
                    android.provider.Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, origTimeout);
                }
            }, NOTIFICATION_TIMEOUT + 100);
        }
    }

    @Override public void onNotificationRemoved(StatusBarNotification sbn) {
        // meh
    }

    @Override public void onSensorChanged(SensorEvent event) {
        mCovered = (event.values[0] < 3);
    }

    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // meh
    }
}