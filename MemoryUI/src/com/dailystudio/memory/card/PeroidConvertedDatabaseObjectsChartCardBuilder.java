package com.dailystudio.memory.card;

import android.content.Context;

import com.dailystudio.dataobject.DatabaseObject;
import com.dailystudio.dataobject.query.Query;
import com.dailystudio.datetime.dataobject.TimeCapsuleQueryBuilder;

public abstract class PeroidConvertedDatabaseObjectsChartCardBuilder<D extends DatabaseObject, P extends DatabaseObject, C extends DatabaseObject>  
	extends ConvertedDatabaseObjectsChartCardBuilder<D, P, C> {

	private long mPeroidStart;
	private long mPeroidEnd;
	
	public PeroidConvertedDatabaseObjectsChartCardBuilder(Context context,
			String templFile, String targetFile) {
		this(context, templFile, targetFile, -1, -1);
	}
	
	public PeroidConvertedDatabaseObjectsChartCardBuilder(Context context,
			String templFile, String targetFile,
			long start, long end) {
		super(context, templFile, targetFile);
		
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
