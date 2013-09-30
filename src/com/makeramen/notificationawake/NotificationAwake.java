package com.makeramen.notificationawake;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationAwake extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, final Intent intent) {
		final Intent i = new Intent();
		i.setClass(context, NotificationAwakeService.class);
		context.startService(i);
	}

}
