package com.dailystudio.memory.task;

import com.dailystudio.memory.Constants;
import com.dailystudio.memory.plugin.MemoryPluginInfo;
import com.dailystudio.memory.plugin.PluginManager;
import com.dailystudio.dataobject.database.DatabaseConnectivity;
import com.dailystudio.development.Logger;
import com.dailystudio.manager.IGroupObject;

import android.content.ComponentName;
import android.content.Context;

public class TaskHost implements IGroupObject<String, Integer>, Runnable {
	
	protected Context mContext;
	
	protected int mTaskId;
	
	protected String mLabel;
	
	protected ComponentName mComponent;
	
	protected String mTaskClass;
	
	protected int mSuccessTimes;
	protected int mFailureTimes;
	
	protected long mLastExecTime;
	protected long mNextExecTime;
	
	private boolean mIsOneshot;
	
	private TaskStatus mStatus;
	
	public TaskHost(Context context) {
		mContext = context;
		
		initMembers();
	}
	
	private void initMembers() {
		mLabel = null;
		mTaskId = Constants.INVALID_ID;
		
		mSuccessTimes = 0;
		mFailureTimes = 0;
		mLastExecTime = -1;
		mNextExecTime = -1;
		
		mIsOneshot = false;
		
		mStatus = TaskStatus.IDLE;
	}
	
	public int getTaskId() {
		return mTaskId;
	}

	public void setTaskId(int tid) {
		mTaskId = tid;
	}

	public void setLabel(String label) {
		mLabel = label;
	}
	
	public String getLabel() {
		return mLabel;
	}
	
	void execSuccess() {
		mSuccessTimes++;
	}
	
	void execFailure() {
		mFailureTimes++;
	}
	
	public int getSuccessTimes() {
		return mSuccessTimes;
	}
	
	public int getFailureTimes() {
		return mFailureTimes;
	}
	
	public long getLastExecTime() {
		return mLastExecTime;
	}
	
	protected long predictNextExecTime() {
		return -1;
	}

	public long getNextExecTime() {
		return mNextExecTime;
	}
	
	public boolean isOneshot() {
		return mIsOneshot;
	}
	
	public void setOneShot(boolean oneshot) {
		mIsOneshot = oneshot;
	}
	
	public ComponentName getComponent() {
		return mComponent;
	}

	public void setComponent(ComponentName comp) {
		mComponent = comp;
	}

	public void setTaskClass(String mTaskClass) {
		this.mTaskClass = mTaskClass;
	}

	public String getTaskClass() {
		return mTaskClass;
	}
	
	public TaskStatus getStatus() {
		return mStatus;
	}
	
	public MemoryPluginInfo getPlugin() {
		if (mComponent == null) {
			return null;
		}
		
		return PluginManager.getPlugin(mComponent.flattenToShortString());
	}
	
	public void createTask() {
		MemoryPluginInfo pInfo = getPlugin();
		if (pInfo == null) {
			return;
		}
		
		pInfo.createTask(mTaskClass, mTaskId);
		
		saveIntoDatabase("create");
	}
	
	public void destroyTask() {
		MemoryPluginInfo pInfo = getPlugin();
		Logger.debug("mTaskId = %d, pInfo = %s", mTaskId, pInfo);
		if (pInfo == null) {
			return;
		}
		
		pInfo.destroyTask(mTaskId);
		
		saveIntoDatabase("destroy");
	}
	
	public void startTask() {
		MemoryPluginInfo pInfo = getPlugin();
		if (pInfo == null) {
			return;
		}

		saveIntoDatabase("start");
		
		mStatus = TaskStatus.RUNNING;
	}

	public void stopTask() {
		MemoryPluginInfo pInfo = getPlugin();
		if (pInfo != null) {
			pInfo.pauseTask(mTaskId);
		}
		
		saveIntoDatabase("stop");

		mStatus = TaskStatus.DONE;
	}

	public void executeTask() {
		if (mComponent == null) {
			return;
		}
		
		if (mTaskId == Constants.INVALID_ID) {
			return;
		}
		
		MemoryPluginInfo pInfo = getPlugin();
		if (pInfo == null) {
			return;
		}
		
		mLastExecTime = System.currentTimeMillis();
		mNextExecTime = predictNextExecTime();
		
		pInfo.executeTask(mTaskId);
		
		saveIntoDatabase("execute");
	}
	
	public void keepAliveTask() {
		MemoryPluginInfo pInfo = getPlugin();
		if (pInfo != null) {
			pInfo.keepAliveTask(mTaskId);
		}
		
		saveIntoDatabase("keepAlive");
	}

	@Override
	public String getGroup() {
		if (mComponent == null) {
			return null;
		}
		
		return mComponent.flattenToShortString();
	}
	
	@Override
	public Integer getSingletonKey() {
		return getTaskId();
	}
	
	@Override
	public String toString() {
		return String.format("%s(0x%08x): id(%d), label(%s)",
				getClass().getSimpleName(),
				hashCode(),
				mTaskId,
				mLabel);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof TaskHost == false) {
			return false;
		}
		
		TaskHost task = (TaskHost)o;
		
		if (mTaskId != task.getTaskId()) {
			return false;
		}
		
		if ((mLabel == null && task.getLabel() != null)
				|| (mLabel != null && task.getLabel() == null)) {
			return false;
		} else if (mLabel == null && task.getLabel() == null) { 
		} else if (mLabel.equals(task.getLabel()) == false) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public void run() {
		executeTask();
	}
	
	private void saveIntoDatabase(String action) {
//		realSaveIntoDatabase(action);
	}
	
	@SuppressWarnings("unused")
	private void realSaveIntoDatabase(String action) {
		if (action == null) {
//		if (true) {
			return;
		}
		
		DatabaseConnectivity connectivity = 
			new DatabaseConnectivity(mContext, TaskHostAction.class);
		if (connectivity == null) {
			return;
		}
		 
		TaskHostAction tha = new TaskHostAction(mContext);
		if (tha == null) {
			return;
		}

		final long now = System.currentTimeMillis();
		
		tha.setTime(now);
		tha.setTaskId(mTaskId);
		tha.setTaskAction(action);
		tha.setTaskClass(mTaskClass);
		
		connectivity.insert(tha);
	}
	
}
