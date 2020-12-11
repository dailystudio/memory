package com.dailystudio.memory.loader;

import java.util.List;

import android.content.Context;

import com.dailystudio.app.loader.AbsAsyncDataLoader;
import com.dailystudio.dataobject.DatabaseObject;

public abstract class PeroidAsyncDataLoader<T extends DatabaseObject> 
	extends AbsAsyncDataLoader<List<T>> {

	private long mPeroidStart;
	private long mPeroidEnd;
	
	public PeroidAsyncDataLoader(Context context) {
		super(context);
		
		mPeroidStart = -1;
		mPeroidEnd = -1;
	}

	public PeroidAsyncDataLoader(Context context, long start, long end) {
		super(context);
		
		setPeroid(start, end);
	}

	public void setPeroid(long start, long end) {
		mPeroidStart = start;
		mPeroidEnd = end;
		
		if (mPeroidStart < 0) {
			mPeroidStart = System.currentTimeMillis();
		}
		
		if (mPeroidEnd < mPeroidStart) {
			mPeroidEnd = mPeroidStart;
		}
	}
	
	public long getPeroidStart() {
		return mPeroidStart;
	}
	
	public long getPeroidEnd() {
		return mPeroidEnd;
	}
	
}
