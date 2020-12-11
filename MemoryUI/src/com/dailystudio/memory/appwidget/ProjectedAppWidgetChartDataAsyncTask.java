package com.dailystudio.memory.appwidget;

import android.content.Context;

import com.dailystudio.dataobject.DatabaseObject;

public abstract class ProjectedAppWidgetChartDataAsyncTask<D extends DatabaseObject, P extends DatabaseObject> 
	extends ConvertedAppWidgetChartDataAsyncTask<D, P, P> {

	public ProjectedAppWidgetChartDataAsyncTask(Context context) {
		super(context);
	}

	public ProjectedAppWidgetChartDataAsyncTask(Context context, long start, long end) {
		super(context, start, end);
	}
	
}
