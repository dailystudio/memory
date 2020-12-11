package com.dailystudio.memory.plugin.actions;

import com.dailystudio.memory.Constants;
import com.dailystudio.memory.plugin.MemoryPlugin;
import com.dailystudio.development.Logger;

import android.content.Context;
import android.content.Intent;

public abstract class TaskAction implements Runnable {

	protected Context mContext;
	protected MemoryPlugin mPlugin;
	protected Intent mIntent;
	
	private boolean mRunInMainThread = false;

	public TaskAction(Context context, MemoryPlugin plugin, 
			Intent i) {
		this(context, plugin, i, false);
	}
	
	public TaskAction(Context context, MemoryPlugin plugin,
			Intent i, boolean runInMainThread) {
		mContext = context;
		mPlugin = plugin;
		mIntent = i;
		mRunInMainThread = runInMainThread;
	}
	
	@Override
	public void run() {
		if (mContext == null || mIntent == null || mPlugin == null) {
			return;
		}
		
		long now = System.currentTimeMillis();

		final String action = mIntent.getAction();

		int taskId = mIntent.getIntExtra(Constants.EXTRA_TASK_ID, Constants.INVALID_ID);

		boolean successful = doTaskAction(taskId, now);
		
		Logger.debug("action = %s, successful = %s", action, successful);
		
		Intent i = new Intent(Constants.ACTION_RETURN_TASK_RESULT);
		
		i.putExtra(Constants.EXTRA_TASK_ACTION, action);
		i.putExtra(Constants.EXTRA_TASK_RESULT, 
				(successful ? Constants.RESULT_SUCCESS : Constants.RESULT_FAILURE));
		i.putExtra(Constants.EXTRA_TASK_ID, taskId);
		
		mContext.sendBroadcast(i);
		
		synchronized (this) {
			this.notifyAll();
		}
	}

	public boolean isRunInMainThread() {
		return mRunInMainThread;
	}
	
	@Override
	public String toString() {
		return String.format("%s(0x%08x): runInMainThread(%s)",
				getClass().getSimpleName(),
				hashCode(),
				isRunInMainThread());
	}

	abstract protected boolean doTaskAction(int taskId, long now);

}
