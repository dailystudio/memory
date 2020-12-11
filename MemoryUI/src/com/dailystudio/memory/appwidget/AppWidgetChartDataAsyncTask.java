package com.dailystudio.memory.appwidget;

import android.content.Context;

import com.dailystudio.dataobject.DatabaseObject;

public abstract class AppWidgetChartDataAsyncTask<D extends DatabaseObject>
	extends ProjectedAppWidgetChartDataAsyncTask<D, D> {

	public AppWidgetChartDataAsyncTask(Context context) {
		super(context);
	}

	public AppWidgetChartDataAsyncTask(Context context, long start, long end) {
		super(context, start, end);
	}
	
}
