package com.dailystudio.memory.chart;

import java.util.List;

import android.content.Context;
import android.view.View;

public abstract class ChartBuilder<T> {

	private Context mContext;
	
	public ChartBuilder(Context context) {
		mContext = context;
	}
	
	public Context getContext() {
		return mContext;
	}

	public Object createShareArguments() {
		return null;
	}

	abstract public Object buildDataset(List<T> objects, Object sharedArguments);
	abstract public Object buildRenderer(List<T> objects, Object sharedArguments);

	abstract public View getChart(Object dataset, Object renderer);

}
