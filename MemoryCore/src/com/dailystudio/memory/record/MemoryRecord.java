package com.dailystudio.memory.record;

import android.content.Context;

import com.dailystudio.datetime.CalendarUtils;
import com.dailystudio.dataobject.Column;
import com.dailystudio.dataobject.IntegerColumn;
import com.dailystudio.dataobject.LongColumn;
import com.dailystudio.dataobject.Template;
import com.dailystudio.dataobject.TextColumn;
import com.dailystudio.datetime.dataobject.TimeCapsule;

public class MemoryRecord extends TimeCapsule {

	public static final Column COLUMN_NAME = new TextColumn("name", false);
	public static final Column COLUMN_SCORE = new LongColumn("score", false);
	public static final Column COLUMN_DELTA_SCORE = new LongColumn("delta_score", false);
	public static final Column COLUMN_REVIEWED = new IntegerColumn("reviewed", false);

	private final static Column[] sCloumns = {
		COLUMN_NAME,
		COLUMN_SCORE,
		COLUMN_DELTA_SCORE,
		COLUMN_REVIEWED,
	};

	public MemoryRecord(Context context) {
		super(context);
		
		final Template templ = getTemplate();
		
		templ.addColumns(sCloumns);
	}
	
	public void setRecordName(String name) {
		setValue(COLUMN_NAME, name);
	}
	
	public String getRecordName() {
		return getTextValue(COLUMN_NAME);
	}
	
	public void setRecordScore(long score) {
		setValue(COLUMN_SCORE, score);
	}

	public long getRecordScore() {
		return getLongValue(COLUMN_SCORE);
	}
	
	public void setDeltaScore(long delta) {
		setValue(COLUMN_DELTA_SCORE, delta);
	}

	public long getDeltaScore() {
		return getLongValue(COLUMN_DELTA_SCORE);
	}
	
	public void setReviewed(boolean reviewd) {
		setValue(COLUMN_REVIEWED, (reviewd ? 1: 0));
	}

	public boolean isReviewed() {
		return (getIntegerValue(COLUMN_REVIEWED) == 1);
	}
	
	@Override
	public String toString() {
		return String.format("%s, name(%s), score[%d, time: %s], delta[%d], reviewed(%s)",
				super.toString(),
				getRecordName(),
				getRecordScore(),
				CalendarUtils.durationToReadableString(getRecordScore()),
				getDeltaScore(),
				isReviewed());
	}

}
