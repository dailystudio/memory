package com.dailystudio.memory.notify;

import android.content.Context;

import com.dailystudio.app.widget.SimpleDatabaseObjectCursorAdapter;

public class MemoryNotifyCursorAdapter extends SimpleDatabaseObjectCursorAdapter<MemoryNotification> {

	public MemoryNotifyCursorAdapter(Context context) {
		super(context, -1, MemoryNotification.class);
	}
	
	@Override
	public long getItemId(int position) {
		if (position < 0 || position >= getCount()) {
			return -1;
		}
		
		MemoryNotification q = dumpItem(position);
		
		return q.getId();
	}

}
