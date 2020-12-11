package com.dailystudio.memory.ask;

import android.content.Context;

import com.dailystudio.app.widget.SimpleDatabaseObjectCursorAdapter;

public class MemoryAskCursorAdapter extends SimpleDatabaseObjectCursorAdapter<MemoryQuestion> {

	public MemoryAskCursorAdapter(Context context) {
		super(context, -1, MemoryQuestion.class);
	}
	
	@Override
	public long getItemId(int position) {
		if (position < 0 || position >= getCount()) {
			return -1;
		}
		
		MemoryQuestion q = dumpItem(position);
		
		return q.getId();
	}

}
