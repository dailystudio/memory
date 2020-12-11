package com.dailystudio.memory.appwidget;

import java.util.List;

import android.content.Context;

import com.dailystudio.dataobject.DatabaseObject;
import com.dailystudio.dataobject.query.Query;
import com.dailystudio.datetime.dataobject.TimeCapsuleQueryBuilder;
import com.dailystudio.memory.chart.ChartBuilder;

public abstract class ConvertedAppWidgetChartDataAsyncTask<D extends DatabaseObject, P extends DatabaseObject, C extends DatabaseObject> 
	extends ConvertedAppWidgetDataAsyncTask<D, P, C> {
	
	private Object mDataSet = null;
	private Object mRenderer = null;

	public ConvertedAppWidgetChartDataAsyncTask(Context context) {
		super(context);
	}

	public ConvertedAppWidgetChartDataAsyncTask(Context context, long start, long end) {
		super(context, start, end);
		
		setPeroid(start, end);
	}

	@Override
	protected List<C> doInBackground(Void... params) {
		mDataSet = null;
		mRenderer = null;
		
		List<C> objects = super.doInBackground(params);
		
		Object arguments = createShareArguments();

		ChartBuilder<C> builder = createChartBuilder();
		if (builder != null) {
			mDataSet = builder.buildDataset(objects, arguments);
			mRenderer = builder.buildRenderer(objects, arguments);
		}
		
		return objects;
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

	protected abstract ChartBuilder<C> createChartBuilder();

}
