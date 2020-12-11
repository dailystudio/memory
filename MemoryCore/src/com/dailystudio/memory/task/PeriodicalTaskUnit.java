package com.dailystudio.memory.task;

import java.util.concurrent.ScheduledFuture;

import com.dailystudio.manager.ISingletonObject;

class PeriodicalTaskUnit implements ISingletonObject<Integer> {
	
	int mTaskId;
	ScheduledFuture<?> mFuture = null;
		
	public PeriodicalTaskUnit(int taskId, ScheduledFuture<?> future) {
		mTaskId = taskId;
		mFuture = future;
	}
	
	@Override
	public Integer getSingletonKey() {
		return mTaskId;
	}
	
}
