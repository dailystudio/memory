package com.dailystudio.memory.loader;

import java.util.List;

import com.dailystudio.dataobject.DatabaseObject;
import com.dailystudio.dataobject.query.Query;
import com.dailystudio.datetime.dataobject.TimeCapsuleQueryBuilder;

import android.content.Context;

public abstract class ConvertedDatabaseChartLoader<D extends DatabaseObject, P extends DatabaseObject, C extends DatabaseObject> 
	extends ConvertedPeroidDatabaseObjectsLoader<D, P, C> {

	private Object mDataSet = null;
	private Object mRenderer = null;

	public ConvertedDatabaseChartLoader(Context context) {
		super(context);
	}
	
	public ConvertedDatabaseChartLoader(Context context, long start, long end) {
		super(context, start, end);
	}

	@Override
	public List<C> loadInBackground() {
		mDataSet = null;
		mRenderer = null;
		
		List<C> objects = super.loadInBackground();
		
		Object arguments = createShareArguments();
		
		mDataSet = buildDataset(objects, arguments);
		mRenderer = buildRenderer(objects, arguments);

		return objects;
	}
	
	public boolean isEmpty() {
		return (mDataSet == null || mRenderer == null);
	}
	
	@Override
	protected Query getQuery(Class<D> klass) {
		final long start = getPeroidStart();
		final long end = getPeroidEnd();
		
		if (end <= start) {
			return super.getQuery(getObjectClass());
		}

		TimeCapsuleQueryBuilder builer =
			new TimeCapsuleQueryBuilder(getObjectClass());
		
		return builer.getQuery(start, end, true);		
	}
	
	public Object createShareArguments() {
		return null;
	}
	
	public Object getDataSet() {
		return mDataSet;
	}
	
	public Object getChartRenderer() {
		return mRenderer;
	}

	abstract protected Object buildDataset(List<C> objects, Object sharedArguments);

	abstract protected Object buildRenderer(List<C> objects, Object sharedArguments);

}
