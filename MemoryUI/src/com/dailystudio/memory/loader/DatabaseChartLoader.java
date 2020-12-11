package com.dailystudio.memory.loader;

import com.dailystudio.dataobject.DatabaseObject;

import android.content.Context;

public abstract class DatabaseChartLoader<D extends DatabaseObject> 
	extends ProjectedDatabaseChartLoader<D, D> {

	public DatabaseChartLoader(Context context) {
		super(context);
	}
	
	public DatabaseChartLoader(Context context, long start, long end) {
		super(context, start, end);
	}

}
