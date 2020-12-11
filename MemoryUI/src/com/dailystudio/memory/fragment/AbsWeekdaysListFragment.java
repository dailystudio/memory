package com.dailystudio.memory.fragment;

import android.widget.BaseAdapter;

import com.dailystudio.dataobject.DatabaseObject;
import com.dailystudio.memory.dataobject.WeekdayObject;
import com.dailystudio.memory.ui.WeekdaysAdatper;

public abstract class AbsWeekdaysListFragment<D extends DatabaseObject>
	extends AbsDateGroupListFragment<D, WeekdayObject> {

/*	@Override
	public Loader<List<WeekdayObject>> onCreateLoader(int arg0, Bundle arg1) {
		final long now = System.currentTimeMillis();
		
		long start = getPeroidStart();
		if (start <= 0) {
			start = CalendarUtils.getStartOfWeek(now);
		}
		
		long end = getPeroidEnd();
		if (end <= 0) {
			end = CalendarUtils.getEndOfWeek(now);
		}
		
		return new WeekdaysLoader(getActivity(), start, end, getDataObjectClass());
	}
*/	
	@Override
	protected BaseAdapter onCreateAdapter() {
		return new WeekdaysAdatper(getActivity());
	}

}
