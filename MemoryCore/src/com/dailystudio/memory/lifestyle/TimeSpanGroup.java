package com.dailystudio.memory.lifestyle;

import java.util.ArrayList;
import java.util.List;

import com.dailystudio.datetime.CalendarUtils;

public class TimeSpanGroup {
	
	private List<TimeSpan> mTimeSpans;
	
	private long mStartMin = 0;
	private long mStartMax = 0;
	private long mEndMin = 0;
	private long mEndMax = 0;
	private long mStartAvg = 0;
	private long mEndAvg = 0;

	public TimeSpanGroup() {
		this(null);
	}
	
	public TimeSpanGroup(List<TimeSpan> timespans) {
		setTimeSpans(timespans);
	}
	
	public void setTimeSpans(List<TimeSpan> timespans) {
		mTimeSpans = timespans;
		
		calAverageAndTopTimeSpan();
	}
	
	public List<TimeSpan> getTimeSpans() {
		return new ArrayList<TimeSpan>(mTimeSpans);
	}
	
	private void resetCalculation() {
		mStartMin = 0l;
		mStartMax = 0l;
		mEndMin = 0l;
		mEndMax = 0l;
		mStartAvg = 0l;
		mEndAvg = 0l;
	}
	
	private void calAverageAndTopTimeSpan() {
		if (mTimeSpans == null) {
			resetCalculation();
			
			return;
		}
		
		final int N = mTimeSpans.size();
		if (N <= 0) {
			resetCalculation();
			
			return;
		}
		
		long start = 0;
		long end = 0;
		long startsum = 0;
		long endsum = 0;
		for (TimeSpan gs: mTimeSpans) {
			if (gs == null) {
				continue;
			}
			
			start = CalendarUtils.getTimeOfDay(gs.start);
			end = CalendarUtils.getTimeOfDay(gs.end);
			
			if (end < start) {
				end += CalendarUtils.DAY_IN_MILLIS;
			}
			
			startsum += start;
			endsum += end;
			
			if (mStartMin == 0) {
				mStartMin = start;
			} else if (start < mStartMin) {
				mStartMin = start;
			} 
			
			if (mStartMax == 0) {
				mStartMax = start;
			} else if (start > mStartMax) {
				mStartMax = start;
			}
			
			if (mEndMin == 0) {
				mEndMin = end;
			} else if (end < mEndMin) {
				mEndMin = end;
			}
			
			if (mEndMax == 0) {
				mEndMax = end;
			} else if (end > mEndMax) {
				mEndMax = end;
			}
		}
		
		mEndAvg = (endsum / mTimeSpans.size());
		mStartAvg = (startsum / mTimeSpans.size());
	}
	
	public long getStartAverage() {
		return mStartAvg;
	}
	
	public long getEndAverage() {
		return mEndAvg;
	}

	public long getStartMinimum() {
		return mStartMin;
	}

	public long getStartMaximum() {
		return mStartMax;
	}

	public long getEndMinimum() {
		return mEndMin;
	}

	public long getEndMaximum() {
		return mEndMax;
	}
	
	public TimeSpan getAverageSpan() {
		return new TimeSpan(mStartAvg, mEndAvg);
	}
	
	public TimeSpan getShortestSpan() {
		return new TimeSpan(mStartMax, mEndMin);
	}
	
	public TimeSpan getLongestSpan() {
		return new TimeSpan(mStartMin, mEndMax);
	}
	
	@Override
	public String toString() {
		return String.format("%s(0x%08x): avg[%s], shortest[%s], longest[%s], data[%s]",
				getClass().getSimpleName(),
				hashCode(),
				getAverageSpan(),
				getShortestSpan(),
				getLongestSpan(),
				mTimeSpans);
	}

}
