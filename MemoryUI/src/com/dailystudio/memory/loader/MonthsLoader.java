package com.dailystudio.memory.loader;

import android.content.Context;

import com.dailystudio.dataobject.query.OrderingToken;
import com.dailystudio.dataobject.query.Query;
import com.dailystudio.datetime.dataobject.TimeCapsule;
import com.dailystudio.memory.dataobject.MonthObject;

public abstract class MonthsLoader<D extends TimeCapsule>
	extends AbsDateGroupLoader<D, MonthObject> {

	public MonthsLoader(Context context) {
		super(context);
	}

	public MonthsLoader(Context context, 
			long start, long end) {
		super(context, start, end);
	}

	@Override
	protected Query getQuery(Class<D> klass) {
		Query query =  super.getQuery(klass);

		OrderingToken groupByToken = D.COLUMN_TIME.groupByMonth();
		if (groupByToken != null) {
			query.setGroupBy(groupByToken);
		}
		
		return query;
	}
	
	@Override
	protected Class<MonthObject> getProjectionClass() {
		return MonthObject.class;
	}

}
