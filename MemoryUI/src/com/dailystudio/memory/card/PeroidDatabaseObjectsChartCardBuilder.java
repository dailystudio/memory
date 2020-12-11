package com.dailystudio.memory.card;

import android.content.Context;

import com.dailystudio.dataobject.DatabaseObject;

public abstract class PeroidDatabaseObjectsChartCardBuilder<D extends DatabaseObject>  
	extends PeroidProjectedDatabaseObjectsChartCardBuilder<D, D> {
	
	public PeroidDatabaseObjectsChartCardBuilder(Context context,
			String templFile, String targetFile,
			long start, long end) {
		super(context, templFile, targetFile, start, end);
	}

	public PeroidDatabaseObjectsChartCardBuilder(Context context,
			String templFile, String targetFile) {
		super(context, templFile, targetFile);
	}

}
