package com.dailystudio.memory.loader;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.dailystudio.dataobject.query.OrderingToken;
import com.dailystudio.dataobject.query.Query;
import com.dailystudio.datetime.CalendarUtils;
import com.dailystudio.datetime.dataobject.TimeCapsule;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.dataobject.WeekdayObject;

public abstract class WeekdaysLoader<D extends TimeCapsule>
	extends AbsDateGroupLoader<D, WeekdayObject> {

	public WeekdaysLoader(Context context) {
		super(context);
	}

	public WeekdaysLoader(Context context,
			long start, long end) {
		super(context, start, end);
	}
	
	@Override
	protected Query getQuery(Class<D> klass) {
		Query query =  super.getQuery(klass);

		OrderingToken groupByToken = D.COLUMN_TIME.groupByWeekday();
		if (groupByToken != null) {
			query.setGroupBy(groupByToken);
		}
		
		return query;
	}
	
	@Override
	public List<WeekdayObject> loadInBackground() {
		List<WeekdayObject> weekdays = super.loadInBackground();
		if (weekdays == null || weekdays.size() <= 0) {
			return weekdays;
		}
		
		final long now = System.currentTimeMillis();
		
		final long start = getPeroidStart();
		final long end = getPeroidEnd();
		Logger.debug("[%s - %s]", 
				CalendarUtils.timeToReadableString(start),
				CalendarUtils.timeToReadableString(end));
				
		final boolean currentWeek = CalendarUtils.isCurrentWeek(start);
		final int currentWeekDay = CalendarUtils.getWeekDay(now);
		
		List<WeekdayObject> fWeekdays = new ArrayList<WeekdayObject>();
		for (WeekdayObject weekday: weekdays) {
			if (currentWeek && weekday.getWeekday() > currentWeekDay) {
				continue;
			}

			fWeekdays.add(weekday);
		}
		
		sortDates(fWeekdays, false);
		
		return fWeekdays;
	}

	@Override
	protected Class<WeekdayObject> getProjectionClass() {
		return WeekdayObject.class;
	}
	
}
