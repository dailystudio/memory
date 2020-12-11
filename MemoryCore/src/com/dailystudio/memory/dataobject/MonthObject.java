package com.dailystudio.memory.dataobject;

import android.content.Context;

import com.dailystudio.dataobject.Column;
import com.dailystudio.dataobject.Template;
import com.dailystudio.datetime.dataobject.TimeCapsule;

public class MonthObject extends DateGroupObject {

	private Column COLUMN_MONTH = TimeCapsule.COLUMN_TIME.MONTH();
	
	public MonthObject(Context context) {
		super(context);

		final Template tmpl = getTemplate();
		
		tmpl.addColumn(COLUMN_MONTH);
	}
	
	public int getMonth() {
		return getIntegerValue(COLUMN_MONTH);
	}
	
	@Override
	public String toString() {
		return String.format("%s, month(%d)",
				super.toString(),
				getMonth());
	}

}
