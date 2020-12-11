package com.dailystudio.memory.card;

import android.content.Context;

import com.dailystudio.dataobject.DatabaseObject;

public abstract class PeroidProjectedDatabaseObjectsChartCardBuilder<D extends DatabaseObject, P extends DatabaseObject>  
	extends PeroidConvertedDatabaseObjectsChartCardBuilder<D, P, P> {
	
	public PeroidProjectedDatabaseObjectsChartCardBuilder(Context context,
			String templFile, String targetFile,
			long start, long end) {
		super(context, templFile, targetFile, start, end);
	}

	public PeroidProjectedDatabaseObjectsChartCardBuilder(Context context,
			String templFile, String targetFile) {
		super(context, templFile, targetFile);
	}
	
}
