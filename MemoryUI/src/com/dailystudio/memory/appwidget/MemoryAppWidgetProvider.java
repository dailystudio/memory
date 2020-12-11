package com.dailystudio.memory.appwidget;

import com.dailystudio.dataobject.DatabaseObject;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class MemoryAppWidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		dumpAppWidgetIds(appWidgetIds);
	}
	
	protected void updateWidgetWithDataObjects(Context context,  
			Class<? extends DatabaseObject> objectClass,
			int[] appWidgetIds) {
		if (context == null || objectClass == null) {
			return;
		}
		
		final Intent srvIntent = getDataServiceIntent(context);
		if (srvIntent == null) {
			return;
		}
		
		srvIntent.putExtra(Constants.EXTRA_APP_WIDGET_DATA_OBJECT_CLASS,
				objectClass.getName());
		if (appWidgetIds != null && appWidgetIds.length > 0) {
			srvIntent.putExtra(Constants.EXTRA_APP_WIDGET_IDS, appWidgetIds);
		}
		
		context.startService(srvIntent);
	}
	
	protected Intent getDataServiceIntent(Context context) {
		return null;
	}
	
	protected void dumpAppWidgetIds(int[] appWidgetIds) {
		if (appWidgetIds == null || appWidgetIds.length <= 0) {
			Logger.debug("appWidgetIds is EMPTY");
		}
		
		for (int i = 0; i < appWidgetIds.length; i++) {
			Logger.debug("appWidgetIds[%d]: 0x%08x", i, appWidgetIds[i]);
		}
	}
	
}
