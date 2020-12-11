package com.dailystudio.memory.lifestyle;

import com.dailystudio.datetime.CalendarUtils;

public class TimeSpan {
	
	public long start;
	public long end;
	
	public TimeSpan() {
		start = 0;
		end = 0;
	}

	public TimeSpan(long start, long end) {
		this.start = start;
		this.end = end;
	}
	
	@Override
	public String toString() {
		return String.format("%s(0x%08x): [%d(%s) - %d(%s)]",
				getClass().getSimpleName(),
				hashCode(),
				start,
				CalendarUtils.timeToReadableStringWithoutDate(start),
				end,
				CalendarUtils.timeToReadableStringWithoutDate(end));
	}

}
