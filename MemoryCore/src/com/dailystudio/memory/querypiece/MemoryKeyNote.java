package com.dailystudio.memory.querypiece;

import java.util.Comparator;

import com.dailystudio.datetime.CalendarUtils;

public class MemoryKeyNote {

	public static class MemoryKeyNoteComparator implements Comparator<MemoryKeyNote> {

		@Override
		public int compare(MemoryKeyNote lhs, MemoryKeyNote rhs) {
			if (lhs == null) {
				return 1;
			} else if (rhs == null) {
				return -1;
			}
			
			if (lhs.mOverTheDayEnd == true
					&& rhs.mOverTheDayEnd == false) {
				return -1;
			} else if (lhs.mOverTheDayEnd == false
					&& rhs.mOverTheDayEnd == true) {
				return 1;
			}
			
			if (lhs.mTimestamp == rhs.mTimestamp) {
				return 0;
			}
			
			return (CalendarUtils.getTimeOfDay(lhs.mTimestamp) < 
					CalendarUtils.getTimeOfDay(rhs.mTimestamp) ? 1 : -1);
		}
		
	}
	
	private String mContent;
	private long mTimestamp;
	private boolean mOverTheDayEnd;

	public MemoryKeyNote(long timestamp) {
		this (timestamp, null);
	}
	
	public MemoryKeyNote(long timestamp, String content) {
		this (timestamp, content, false);
	}
	
	public MemoryKeyNote(long timestamp, String content, boolean overDayEnd) {
		mTimestamp = timestamp;
		mContent = content;
		
		mOverTheDayEnd = overDayEnd;
	}
	
	public long getTimestamp() {
		return mTimestamp;
	}
	
	public String getContent() {
		return mContent;
	}
	
	public void setOverTheDayEnd(boolean overTheDayEnd) {
		mOverTheDayEnd = overTheDayEnd;
	}
	
	public boolean isOverTheDayEnd() {
		return mOverTheDayEnd;
	}
	
	public void setContent(String content) {
		mContent = content;
	}
	
	@Override
	public String toString() {
		return String.format("%s(0x%08x): [%s]: content = [%s], overTheDay = %s",
				getClass().getSimpleName(),
				hashCode(),
				CalendarUtils.timeToReadableString(mTimestamp),
				mContent,
				mOverTheDayEnd);
	}
	
}
