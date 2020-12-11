package com.dailystudio.memory.task;

import com.dailystudio.memory.Constants;
import com.dailystudio.development.Logger;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class TasksKeepAliveService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		final Context context = getApplicationContext();
		if (context == null) {
			return;
		}

		final String packageName = context.getPackageName();
		Logger.debug("packageName = %s", packageName);
		if (packageName == null) {
			return;
		}
		
		Intent i = new Intent(Constants.ACTION_TASKS_BECOME_ALIVE);
		
		i.putExtra(Constants.EXTRA_ALIVE_TASKS_PACKAGE, packageName);
		
		context.sendBroadcast(i);
	}


}
