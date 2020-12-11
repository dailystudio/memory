package com.dailystudio.memory.appwidget;

import java.util.List;

import com.dailystudio.dataobject.DatabaseObject;
import com.dailystudio.dataobject.database.DatabaseObserver;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;
import com.dailystudio.memory.appwidget.ConvertedAppWidgetDataAsyncTask.AsyncTaskCallbacks;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public abstract class MemoryAppWidgetDataService extends Service {

	private class AppWidgetDatabaseObserver extends DatabaseObserver {

		public AppWidgetDatabaseObserver(Context context,
				Class<? extends DatabaseObject> klass) {
			super(context, klass);
		}

		@Override
		protected void onDatabaseChanged(Context context,
				Class<? extends DatabaseObject> objectClass) {
			Logger.debug("objectClass = %s",
					objectClass);
			
			reloadData(objectClass);
		}
		
	}
	
	private DatabaseObserver[] mDbObservers;
	
	@Override
	public void onCreate() {
		super.onCreate();

		final Class<? extends DatabaseObject>[] dbObjClasses =
				listObservedDatabaseObject();
		if (dbObjClasses == null) {
			return;
		}
		
		final int N = dbObjClasses.length;
		if (N <= 0) { 
			return;
		}
		
		mDbObservers = new DatabaseObserver[dbObjClasses.length];
		for (int i = 0; i < N; i++) {
			mDbObservers[i] = new AppWidgetDatabaseObserver(
					this, dbObjClasses[i]);
		}
		
		if (isObserveDatabaseRequired()) {
			startObservers(mDbObservers);
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		handleIntent(intent);
		
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		if (isObserveDatabaseRequired()) {
			stopObservers(mDbObservers);
		}
		
		super.onDestroy();
	}
	
	private void startObservers(DatabaseObserver[] observers) {
		if (observers == null) {
			return;
		}
		
		for (DatabaseObserver o: observers) {
			Logger.debug("o = %s", o);
			reloadData(o.getObserverdObjectClass());
			o.register();
		}
	}

	private void stopObservers(DatabaseObserver[] observers) {
		if (observers == null) {
			return;
		}
		
		for (DatabaseObserver o: observers) {
			o.unregister();
		}
	}

	protected DatabaseObserver[] getDatabaseObserver() {
		return mDbObservers;
	}
	
	protected boolean isObserveDatabaseRequired() {
		return (mDbObservers != null 
				&& mDbObservers.length > 0);
	}
	
	@SuppressWarnings("unchecked")
	private void handleIntent(Intent intent) {
		if (intent == null) {
			return;
		}
		
		final String objectClassName = 
				intent.getStringExtra(Constants.EXTRA_APP_WIDGET_DATA_OBJECT_CLASS);
		if (objectClassName == null) {
			return;
		}
		
		Class<?> objectClass = null;
		try {
			objectClass = Class.forName(objectClassName);
		} catch (ClassNotFoundException  e) {
			Logger.debug("create DatabaseObject class failure: %s",
					e.toString());
			
			objectClass = null;
		}
		
		if (objectClass == null) {
			return;
		}
		
		reloadData((Class<? extends DatabaseObject>)objectClass);
	}

	protected void reloadData(Class<? extends DatabaseObject> objectClass) {
		if (objectClass == null) {
			return;
		}
		
		final List<ConvertedAppWidgetDataAsyncTask<?, ?, ?>> asyncTasks = 
				createAsyncTasks(objectClass);
		Logger.debug("DATA AsyncTask(s)[%s]: %s",
				objectClass.getSimpleName(),
				asyncTasks);
		if (asyncTasks == null) {
			return;
		}
		
		for (ConvertedAppWidgetDataAsyncTask<?, ?, ?> task: asyncTasks) {
			task.execute((Void)null);
			task.setCallbacks(mAsyncTaskCallbacks);
		}
	}
	
	abstract protected List<ConvertedAppWidgetDataAsyncTask<?, ?, ?>> createAsyncTasks(
			Class<? extends DatabaseObject> objectClass);
	abstract protected Class<? extends DatabaseObject>[] listObservedDatabaseObject();
	
	private AsyncTaskCallbacks mAsyncTaskCallbacks = new AsyncTaskCallbacks() {

		@Override
		public void onAsyncTaskFinished(
				ConvertedAppWidgetDataAsyncTask<?, ?, ?> asyncTask,
				List<?> result, RemoteViews remoteViews) {
			if (asyncTask == null) {
				return;
			}
			
			AppWidgetManager widmgr = AppWidgetManager.getInstance(getApplicationContext());
			if (widmgr == null) {
				return;
			}
			
			final ComponentName widgetProvider = asyncTask.getAppWidgetProvider();
			if (widgetProvider == null) {
				return;
			}
			
			widmgr.updateAppWidget(widgetProvider, remoteViews);
		}

	};

}
