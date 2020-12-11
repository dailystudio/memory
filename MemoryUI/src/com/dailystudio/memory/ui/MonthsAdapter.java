package com.dailystudio.memory.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.dailystudio.memory.dataobject.MonthObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MonthsAdapter extends ArrayAdapter<MonthObject> {

	private final static String DEFAULT_MONTH_PATTERN = "MM/yyyy";
	
	private SimpleDateFormat mFormater;
	
	public MonthsAdapter(Context context) {
		super(context, 0, new ArrayList<MonthObject>());
		
		String month_pattern = context.getString(R.string.month_pattern);
		
		if (month_pattern == null) {
			month_pattern = DEFAULT_MONTH_PATTERN;
		}
		
		mFormater = new SimpleDateFormat(month_pattern);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Context context = getContext();
		
		if (convertView == null)  {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.month_item, null);
		}
		
		MonthObject month = getItem(position);

		if (month != null && convertView != null) {
			TextView nameView = (TextView) convertView.findViewById(R.id.month_name);
			if (nameView != null) {
				nameView.setText(mFormater.format(month.getTime()));
			}
		}
		
		return convertView;
	}

}

