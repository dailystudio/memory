package com.dailystudio.memory.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.dailystudio.datetime.CalendarUtils;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.dataobject.WeekdayObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WeekdaysAdatper extends ArrayAdapter<WeekdayObject> {

	public WeekdaysAdatper(Context context) {
		super(context, 0, new ArrayList<WeekdayObject>());
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)  {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.weekday_item, null);
		}
		
		WeekdayObject weekday = getItem(position);
		Logger.debug("weekday = %s", weekday);
		if (weekday != null && convertView != null) {
			final long time = weekday.getTime();
			
			TextView dayNameView = (TextView) convertView.findViewById(R.id.weekday_dayname);
			if (dayNameView != null) {
				final SimpleDateFormat formater = new SimpleDateFormat("EEEE");
				dayNameView.setText(formater.format(time));
			}
			
			TextView timeView = (TextView) convertView.findViewById(R.id.weekay_time);
			if (timeView != null) {
				timeView.setText(CalendarUtils.timeToReadableStringWithoutTime(time));
			}
		}
		
		return convertView;
	}
	
}

