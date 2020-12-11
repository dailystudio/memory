package com.dailystudio.memory.dataobject;

import android.content.Context;

import com.dailystudio.dataobject.Column;
import com.dailystudio.dataobject.Template;
import com.dailystudio.datetime.dataobject.TimeCapsule;

public class WeekObject extends DateGroupObject {

	private Column COLUMN_WEEK_NUM = TimeCapsule.COLUMN_TIME.WEEK();
	
	public WeekObject(Context context) {
		super(context);

		final Template tmpl = getTemplate();
		
		tmpl.addColumn(COLUMN_WEEK_NUM);
	}
	
	public int getWeekNumber() {
		return getIntegerValue(COLUMN_WEEK_NUM);
	}
	
	@Override
	public String toString() {
		return String.format("%s, week(%d)",
				super.toString(),
				getWeekNumber());
	}

}
