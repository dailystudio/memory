package com.dailystudio.memory.card;

import android.content.Context;

import com.dailystudio.dataobject.DatabaseObject;

public abstract class DatabaseObjectsChartCardBuilder<D extends DatabaseObject>  
	extends ProjectedDatabaseObjectsChartCardBuilder<D, D> {
	
	public DatabaseObjectsChartCardBuilder(Context context,
			String templFile, String targetFile) {
		super(context, templFile, targetFile);
	}

}
