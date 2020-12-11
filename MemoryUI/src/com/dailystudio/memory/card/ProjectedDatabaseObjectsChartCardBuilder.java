package com.dailystudio.memory.card;

import android.content.Context;

import com.dailystudio.dataobject.DatabaseObject;

public abstract class ProjectedDatabaseObjectsChartCardBuilder<D extends DatabaseObject, P extends DatabaseObject>  
	extends ConvertedDatabaseObjectsChartCardBuilder<D, P, P> {
	
	public ProjectedDatabaseObjectsChartCardBuilder(Context context,
			String templFile, String targetFile) {
		super(context, templFile, targetFile);
	}

}
