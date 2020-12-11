package com.dailystudio.memory.loader;

import com.dailystudio.dataobject.DatabaseObject;

import android.content.Context;

public abstract class ProjectedDatabaseChartLoader<D extends DatabaseObject, P extends DatabaseObject> 
	extends ConvertedDatabaseChartLoader<D, P, P> {

	public ProjectedDatabaseChartLoader(Context context) {
		super(context);
	}
	
	public ProjectedDatabaseChartLoader(Context context, long start, long end) {
		super(context, start, end);
	}

}
