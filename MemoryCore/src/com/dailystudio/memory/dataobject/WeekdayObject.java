package com.dailystudio.memory.dataobject;

import android.content.Context;

import com.dailystudio.dataobject.Column;
import com.dailystudio.dataobject.Template;
import com.dailystudio.datetime.dataobject.TimeCapsule;

public class WeekdayObject extends DateGroupObject {

	private Column COLUMN_WEEKDAY = TimeCapsule.COLUMN_TIME.WEEKDAY();
	
	public WeekdayObject(Context context) {
		super(context);

		final Template tmpl = getTemplate();
		
		tmpl.addColumn(COLUMN_WEEKDAY);
	}
	
	public int getWeekday() {
		return getIntegerValue(COLUMN_WEEKDAY);
	}
	
	@Override
	public String toString() {
		return String.format("%s, weekday(%d)",
				super.toString(),
				getWeekday());
	}

}
