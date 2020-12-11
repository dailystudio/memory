package com.dailystudio.memory.loader;

import android.content.Context;

import com.dailystudio.dataobject.DatabaseObject;

public abstract class PeroidDatabaseObjectsLoader<D extends DatabaseObject> 
	extends ProjectedPeroidDatabaseObjectsLoader<D, D> {

	public PeroidDatabaseObjectsLoader(Context context) {
		super(context);
	}

	public PeroidDatabaseObjectsLoader(Context context, long start, long end) {
		super(context, start, end);
	}

}
