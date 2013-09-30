package com.makeramen.notificationawake;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class NotificationAwakeSettings extends PreferenceActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.addPreferencesFromResource(R.xml.settings);

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        startService(new Intent(this, NotificationListener.class));
    }
}
