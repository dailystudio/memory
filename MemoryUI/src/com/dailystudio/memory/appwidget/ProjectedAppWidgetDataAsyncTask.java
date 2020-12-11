package com.dailystudio.memory.appwidget;

import android.content.Context;

import com.dailystudio.dataobject.DatabaseObject;

public abstract class ProjectedAppWidgetDataAsyncTask<D extends DatabaseObject, P extends DatabaseObject> 
	extends ConvertedAppWidgetDataAsyncTask<D, P, P> {

	public ProjectedAppWidgetDataAsyncTask(Context context) {
		super(context);
	}

	public ProjectedAppWidgetDataAsyncTask(Context context, long start, long end) {
		super(context, start, end);
	}
	
}
