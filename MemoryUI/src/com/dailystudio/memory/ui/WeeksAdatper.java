package com.dailystudio.memory.ui;

import java.util.ArrayList;

import com.dailystudio.datetime.CalendarUtils;
import com.dailystudio.memory.dataobject.WeekObject;
import com.dailystudio.memory.ui.utils.DateTimePrintUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WeeksAdatper extends ArrayAdapter<WeekObject> {

	public WeeksAdatper(Context context) {
		super(context, 0, new ArrayList<WeekObject>());
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Context context = getContext();
		
		if (convertView == null)  {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.week_item, null);
		}
		
		WeekObject week = getItem(position);

		if (week != null && convertView != null) {
			TextView numView = (TextView) convertView.findViewById(R.id.week_num);
			if (numView != null) {
				numView.setText(String.format("%s - Week %02d",
						CalendarUtils.getYear(week.getTime()),
						week.getWeekNumber()));
			}
			
			TextView rangeView = (TextView) convertView.findViewById(R.id.week_range);
			if (rangeView != null) {
				final long weekStart = CalendarUtils.getStartOfWeek(week.getTime());
				final long weekEnd = CalendarUtils.getEndOfWeek(week.getTime());
				rangeView.setText(String.format("%s - %s", 
						DateTimePrintUtils.printTimeStringWithoutTime(context, weekStart),
						DateTimePrintUtils.printTimeStringWithoutTime(context, weekEnd)));
			}
		}
		
		return convertView;
	}

}

