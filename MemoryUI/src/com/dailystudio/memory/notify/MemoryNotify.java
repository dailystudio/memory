package com.dailystudio.memory.notify;

import com.dailystudio.memory.Constants;
import com.dailystudio.memory.ui.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class MemoryNotify {
	
	public static void notifySystemInfo(Context context, 
			int notifyId,
			CharSequence info) {
		notifySystemInfo(context, notifyId, info, null, 0);
	}
	
	public static void notifySystemInfo(Context context, 
			int notifyId,
			CharSequence info,
			Intent notifyIntent) {
		notifySystemInfo(context, notifyId, info, null, 0);
	}
	
	public static void notifySystemInfo(Context context, 
			int notifyId,
			CharSequence info,
			Intent notifyIntent,
			int notifyFlag) {
		notifySystemInfo(context, notifyId, -1, 
				null, info, notifyIntent, notifyFlag);
	}
	
	public static void notifySystemInfo(Context context, 
			int notifyId,
			int notifyIcon,
			CharSequence title, 
			CharSequence info,
			Intent notifyIntent,
			int notifyFlag) {
		if (context == null) {
			return;
		}
		
		if (notifyIcon <= 0) {
			notifyIcon = R.drawable.ic_nofity;
		}
		
		if (title == null) {
			title = context.getString(R.string.default_notification_title);
		}
		
		if (notifyIntent == null) {
			notifyIntent = new Intent();
		}
		
//		Notification notification = new Notification(
//				notifyIcon, null, System.currentTimeMillis());
//
//		notification.flags = notifyFlag;
		
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notifyIntent, 0);
//
//		notification.setLatestEventInfo(context, title,
//				info, contentIntent);

		NotificationManager nmgr = 
			(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		if (nmgr == null) {
			return;
		}

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context);

        builder.setContentTitle(title);
        builder.setContentText(info);
//        builder.setContentInfo(info);
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(notifyIcon);
        builder.setOngoing(notifyFlag == Notification.FLAG_ONGOING_EVENT);
        builder.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);
        builder.setWhen(System.currentTimeMillis());

        Notification notification = builder.build();

        nmgr.notify(notifyId, notification);
	}

    public static void beginForegroundService(Service service,
                                              int notifyId,
                                              CharSequence info) {
        beginForegroundService(service, notifyId, info, null);
    }

    public static void beginForegroundService(Service service,
                                              int notifyId,
                                              CharSequence info,
                                              Intent notifyIntent) {
        beginForegroundService(service, notifyId, -1, null,info, null);
    }

    public static void beginForegroundService(Service service,
                                        int notifyId,
                                        int notifyIcon,
                                        CharSequence title,
                                        CharSequence info,
                                        Intent notifyIntent) {
        if (service == null) {
            return;
        }

        if (notifyIcon <= 0) {
            notifyIcon = R.drawable.ic_nofity;
        }

        if (title == null) {
            title = service.getString(R.string.default_notification_title);
        }

        if (notifyIntent == null) {
            notifyIntent = new Intent();
        }

        PendingIntent contentIntent = PendingIntent.getActivity(service, 0,
                notifyIntent, 0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(service);

        builder.setContentTitle(title);
        builder.setContentInfo(info);
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(notifyIcon);
        builder.setWhen(System.currentTimeMillis());
        builder.setOngoing(true);
        builder.setPriority(NotificationCompat.PRIORITY_MIN);
        if (Build.VERSION.SDK_INT >= 21) {
            builder.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);
        }

        Notification notification = builder.build();

        service.startForeground(notifyId, notification);
    }

    public static void cancelNotifySystemInfo(Context context,
			int notifyId) {
		if (context == null) {
			return;
		}
		
		NotificationManager nmgr = 
			(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		if (nmgr == null) {
			return;
		}
	        
		nmgr.cancel(notifyId);
	}
	
	public static void notifyInfo(Context context,
			int nid,
			String titleText,
			String contentText) {
		notifyInfo(context, nid, titleText, -1, contentText, -1, null);
	}
	
	public static void notifyInfo(Context context,
			int nid,
			String titleText,
			String contentText,
			Intent notifyIntent) {
		notifyInfo(context, nid, titleText, -1, contentText, -1, notifyIntent);
	}
	
	public static void notifyInfo(Context context,
			int nid,
			int titleResId,
			int contentResId) {
		notifyInfo(context, nid, null, titleResId, null, contentResId, null);
	}
	
	public static void notifyInfo(Context context,
			int nid,
			int titleResId,
			int contentResId,
			Intent notifyIntent) {
		notifyInfo(context, nid, null, titleResId, null, contentResId, notifyIntent);
	}
	
	public static void notifyInfo(Context context,
			int nid,
			String titleText,
			int titleResId,
			String contentText,
			int contentResId,
			Intent notifyIntent) {
		if (context == null || nid == Constants.INVALID_ID) {
			return;
		}
		
		Intent i = new Intent(Constants.ACTION_SHOW_NOTIFY);
		
		i.putExtra(Constants.EXTRA_NOTIFY_ID, nid);
		i.putExtra(Constants.EXTRA_NOTIFY_SOURCE_PACKAGE, context.getPackageName());
		
		if (titleText != null) {
			i.putExtra(Constants.EXTRA_NOTIFY_TITLE_TEXT, titleText);
		}
		
		if (titleResId > 0) {
			i.putExtra(Constants.EXTRA_NOTIFY_TITLE_RESOURCE, titleResId);
		}
		
		if (contentText != null) {
			i.putExtra(Constants.EXTRA_NOTIFY_CONTENT_TEXT, contentText);
		}
		
		if (contentResId > 0) {
			i.putExtra(Constants.EXTRA_NOTIFY_CONTENT_RESOURCE, contentResId);
		}
		
		if (notifyIntent != null) {
			i.putExtra(Constants.EXTRA_NOTIFY_INTENT,
					notifyIntent.toURI());
		}
		
		context.sendBroadcast(i);
	}

}
