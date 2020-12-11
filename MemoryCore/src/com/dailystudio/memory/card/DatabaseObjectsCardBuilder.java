package com.dailystudio.memory.card;

import android.content.Context;

import com.dailystudio.dataobject.DatabaseObject;

public abstract class DatabaseObjectsCardBuilder<D extends DatabaseObject>  
	extends ProjectedDatabaseObjectsCardBuilder<D, D> {

	public DatabaseObjectsCardBuilder(Context context,
			String templFile, String targetFile) {
		super(context, templFile, targetFile);
	}

}
