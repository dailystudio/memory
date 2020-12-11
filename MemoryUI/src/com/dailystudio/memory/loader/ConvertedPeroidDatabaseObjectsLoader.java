package com.dailystudio.memory.loader;

import android.content.Context;

import com.dailystudio.app.dataobject.loader.ConvertedDatabaseObjectsLoader;
import com.dailystudio.dataobject.DatabaseObject;
import com.dailystudio.dataobject.query.Query;
import com.dailystudio.datetime.dataobject.TimeCapsuleQueryBuilder;

public abstract class ConvertedPeroidDatabaseObjectsLoader<D extends DatabaseObject, P extends DatabaseObject, C extends DatabaseObject> 
	extends ConvertedDatabaseObjectsLoader<D, P, C> {

	private long mPeroidStart;
	private long mPeroidEnd;
	
	public ConvertedPeroidDatabaseObjectsLoader(Context context) {
		super(context);
		
		mPeroidStart = -1;
		mPeroidEnd = -1;
	}

	public ConvertedPeroidDatabaseObjectsLoader(Context context, long start, long end) {
		super(context);
		
		setPeroid(start, end);
	}

	public void setPeroid(long start, long end) {
		mPeroidStart = start;
		mPeroidEnd = end;
		
		if (mPeroidStart < 0) {
			mPeroidStart = System.currentTimeMillis();
		}
		
		if (mPeroidEnd < mPeroidStart) {
			mPeroidEnd = mPeroidStart;
		}
	}
	
	public long getPeroidStart() {
		return mPeroidStart;
	}
	
	public long getPeroidEnd() {
		return mPeroidEnd;
	}
	
	@Override
	protected Query getQuery(Class<D> klass) {
		if (mPeroidEnd <= mPeroidStart) {
			return super.getQuery(getObjectClass());
		}
		
		TimeCapsuleQueryBuilder builer =
			new TimeCapsuleQueryBuilder(getObjectClass());
		
		return builer.getQuery(mPeroidStart, mPeroidEnd);		
	}
	
}
