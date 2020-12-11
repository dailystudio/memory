package com.dailystudio.memory.appwidget;

import android.content.Context;

import com.dailystudio.dataobject.DatabaseObject;

public abstract class AppWidgetDataAsyncTask<D extends DatabaseObject>
	extends ProjectedAppWidgetDataAsyncTask<D, D> {

	public AppWidgetDataAsyncTask(Context context) {
		super(context);
	}

	public AppWidgetDataAsyncTask(Context context, long start, long end) {
		super(context, start, end);
	}
	
}
