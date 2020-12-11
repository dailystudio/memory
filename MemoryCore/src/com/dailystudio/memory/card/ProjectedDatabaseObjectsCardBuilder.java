package com.dailystudio.memory.card;

import android.content.Context;

import com.dailystudio.dataobject.DatabaseObject;

public abstract class ProjectedDatabaseObjectsCardBuilder<D extends DatabaseObject, P extends DatabaseObject>  
	extends ConvertedDatabaseObjectsCardBuilder<D, P, P> {

	public ProjectedDatabaseObjectsCardBuilder(Context context,
			String templFile, String targetFile) {
		super(context, templFile, targetFile);
	}

}
