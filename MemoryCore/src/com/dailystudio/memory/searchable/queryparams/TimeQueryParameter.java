package com.dailystudio.memory.searchable.queryparams;

import com.dailystudio.datetime.CalendarUtils;

public class TimeQueryParameter extends QueryParameter {

	public long timeBegin = -1;
	public long timeEnd = -1;
	
	@Override
	public boolean isValid() {
		return (timeEnd > timeBegin);
	}

	@Override
	public String toString() {
		return String.format("%s, time[%s - %s]",
				super.toString(),
				CalendarUtils.timeToReadableString(timeBegin),
				CalendarUtils.timeToReadableString(timeEnd));
	}

}
