package com.dailystudio.memory.loader;

import android.content.Context;

import com.dailystudio.dataobject.query.OrderingToken;
import com.dailystudio.dataobject.query.Query;
import com.dailystudio.datetime.dataobject.TimeCapsule;
import com.dailystudio.memory.dataobject.WeekObject;

public abstract class WeeksLoader<D extends TimeCapsule>
	extends AbsDateGroupLoader<D, WeekObject> {

	public WeeksLoader(Context context) {
		super(context);
	}

	public WeeksLoader(Context context,
			long start, long end) {
		super(context, start, end);
	}
	
	@Override
	protected Query getQuery(Class<D> klass) {
		Query query =  super.getQuery(klass);

		OrderingToken groupByToken = D.COLUMN_TIME.groupByWeek();
		if (groupByToken != null) {
			query.setGroupBy(groupByToken);
		}
		
		return query;
	}

	@Override
	protected Class<WeekObject> getProjectionClass() {
		return WeekObject.class;
	}
	
}
