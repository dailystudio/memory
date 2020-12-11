package com.dailystudio.memory.loader;

import android.content.Context;

import com.dailystudio.dataobject.DatabaseObject;

public abstract class ProjectedPeroidDatabaseObjectsLoader<D extends DatabaseObject, P extends DatabaseObject> 
	extends ConvertedPeroidDatabaseObjectsLoader<D, P, P> {

	public ProjectedPeroidDatabaseObjectsLoader(Context context) {
		super(context);
	}

	public ProjectedPeroidDatabaseObjectsLoader(Context context, long start, long end) {
		super(context, start, end);
	}

}
