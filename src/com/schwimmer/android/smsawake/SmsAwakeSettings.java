package com.schwimmer.android.smsawake;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SmsAwakeSettings extends PreferenceActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.addPreferencesFromResource(R.layout.settings);

		PreferenceManager.setDefaultValues(this, R.layout.settings, false);
	}

}
