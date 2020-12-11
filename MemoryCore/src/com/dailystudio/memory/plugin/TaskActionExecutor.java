package com.dailystudio.memory.plugin;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.dailystudio.memory.plugin.actions.TaskAction;

import android.os.AsyncTask;
import android.os.Handler;

class TaskActionExecutor extends
		AsyncTask<Void, Void, Void> {
	
	private BlockingQueue<TaskAction> mPendingQueue;
	
	private volatile boolean mEndFlag = false;
	private Object mWaitObject = new Object();
	
	private static TaskActionExecutor sInstance = null;
	
	private TaskActionExecutor() {
		mPendingQueue = new LinkedBlockingQueue<TaskAction>();
	}
	
	static final synchronized TaskActionExecutor getInstance() {
		if (sInstance == null) {
			sInstance = new TaskActionExecutor();
		}
		
		return sInstance;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		if (mPendingQueue == null) {
			return null;
		}
		
		TaskAction action = null;
		for ( ; !mEndFlag; ) {
			try {
				action = mPendingQueue.poll(1000, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
				
				action = null;
			}

			if (action == null) {
				synchronized (mWaitObject) {
					try {
						mWaitObject.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				continue;
			}
			
			if (action.isRunInMainThread() == false) {
				action.run();
			} else {
				synchronized (action) {
					mHandler.post(action);

					try {
						action.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return null;
	}

	public void tryToExecuteAction(TaskAction action) {
		if (action == null) {
			return;
		}
		
		mPendingQueue.add(action);
		
		synchronized (mWaitObject) {
			mWaitObject.notifyAll();
		}
	}
	
	public void clearActions() {
		mPendingQueue.clear();
		
		synchronized (mWaitObject) {
			mWaitObject.notifyAll();
		}
	}
	
	@Override
	protected void onCancelled() {
		mEndFlag = true;
		synchronized (mWaitObject) {
			mWaitObject.notifyAll();
		}

		super.onCancelled();
	}
	
	private Handler mHandler = new Handler();
	
}
