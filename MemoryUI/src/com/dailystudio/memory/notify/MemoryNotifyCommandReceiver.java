package com.dailystudio.memory.notify;

import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MemoryNotifyCommandReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent == null) {
			return;
		}
		
		final Context appContext = context.getApplicationContext();
		
		final String action = intent.getAction();
		if (Constants.ACTION_SHOW_NOTIFY.equals(action)) {
			final int nid = intent.getIntExtra(
					Constants.EXTRA_NOTIFY_ID, Constants.INVALID_ID);
			final String srcPkg = intent.getStringExtra(Constants.EXTRA_NOTIFY_SOURCE_PACKAGE);
			final int titleResId = intent.getIntExtra(
					Constants.EXTRA_NOTIFY_TITLE_RESOURCE, Constants.INVALID_RESOURCE_ID);
			final String titleText = intent.getStringExtra(Constants.EXTRA_NOTIFY_TITLE_TEXT);
			final int cntResId = intent.getIntExtra(
					Constants.EXTRA_NOTIFY_CONTENT_RESOURCE, Constants.INVALID_RESOURCE_ID);
			final String cntText = intent.getStringExtra(Constants.EXTRA_NOTIFY_CONTENT_TEXT);
			final String notifyIntent = intent.getStringExtra(Constants.EXTRA_NOTIFY_INTENT);
			
			Logger.debug("nid = %d, srcPkg = %s, title = [%d / %s], cnt = [%d / %s], notifyIntent = %s", 
					nid, srcPkg, titleResId, titleText, cntResId, cntText, notifyIntent);
			
			MemoryNotification notification = MemoryNotificationDatabaseModal.findNotify(
					appContext, nid, srcPkg);
			
			if (notification != null) {
				MemoryNotificationDatabaseModal.updateNotify(appContext, notification, 
						nid, srcPkg, titleResId, titleText, 
						cntResId, cntText, notifyIntent);
			} else {
				MemoryNotificationDatabaseModal.addNotify(appContext, 
						nid, srcPkg, titleResId, titleText, 
						cntResId, cntText, notifyIntent);
			}
		} else if (Constants.ACTION_CANCEL_NOTIFY.equals(action)) {
			final int qid = intent.getIntExtra(
					Constants.EXTRA_NOTIFY_ID, Constants.INVALID_ID);
			final String srcPkg = intent.getStringExtra(Constants.EXTRA_NOTIFY_SOURCE_PACKAGE);
			
			MemoryNotification notification = MemoryNotificationDatabaseModal.findNotify(
					appContext, qid, srcPkg);
			
			if (notification != null) {
				MemoryNotificationDatabaseModal.deleteNotify(
						appContext, notification);
			} 
		} 
	}

}
