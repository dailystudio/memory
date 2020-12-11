package com.dailystudio.memory.fragment;

import com.dailystudio.app.fragment.AbsCursorAdapterFragment;
import com.dailystudio.memory.Constants;

import android.content.Intent;

public abstract class MemoryPeroidCursorListFragment extends AbsCursorAdapterFragment {
	
	private long mPeroidStart;
	private long mPeroidEnd;
	
	@Override
	public void bindIntent(Intent intent) {
		super.bindIntent(intent);
		
		if (intent == null) {
			return;
		}
		
		mPeroidStart = intent.getLongExtra(Constants.EXTRA_PEROID_START, -1);
		mPeroidEnd = intent.getLongExtra(Constants.EXTRA_PEROID_END, -1);
	}

	public long getPeroidStart() {
		return mPeroidStart;
	}
	
	public long getPeroidEnd() {
		return mPeroidEnd;
	}

}
