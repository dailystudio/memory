package com.dailystudio.memory.fragment;

import com.dailystudio.dataobject.DatabaseObject;
import com.dailystudio.memory.Constants;

import android.content.Intent;

public abstract class MemoryPeroidChartFragment<C extends DatabaseObject>
	extends MemoryChartFragment<C> {
	
	private long mPeroidStart = -1;
	private long mPeroidEnd = -1;
	
	@Override
	public void bindIntent(Intent intent) {
		super.bindIntent(intent);
		
		if (intent == null) {
			return;
		}
		
		mPeroidStart = intent.getLongExtra(Constants.EXTRA_PEROID_START, 
				getPeroidStart());
		mPeroidEnd = intent.getLongExtra(Constants.EXTRA_PEROID_END, 
				getPeroidEnd());
	}

	public long getPeroidStart() {
		return mPeroidStart;
	}
	
	public long getPeroidEnd() {
		return mPeroidEnd;
	}
	
	public void setPeroidStart(long start) {
		mPeroidStart = start;
	}
	
	public void setPeroidEnd(long end) {
		mPeroidEnd = end;
	}

}
