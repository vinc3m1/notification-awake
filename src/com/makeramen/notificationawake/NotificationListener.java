package com.makeramen.notificationawake;

import android.app.Notification;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotificationListener extends NotificationListenerService implements SensorEventListener{

    private Handler mHandler;

    private SensorManager mSensorManager;
    private Sensor mProximity;

    private float mDistance = 10;

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
        super.onDestroy();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        if ((sbn.getNotification().flags & Notification.FLAG_SHOW_LIGHTS) != 0 && mDistance > 1) {
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

    }

    @Override public void onNotificationRemoved(StatusBarNotification sbn) {
        // meh
    }

    @Override public void onSensorChanged(SensorEvent event) {
        mDistance = event.values[0];
    }

    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // meh
    }
}