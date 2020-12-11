package com.dailystudio.memory.task;

import java.util.Calendar;

import com.dailystudio.datetime.CalendarUtils;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;
import com.dailystudio.manager.ISingletonObject;
import com.dailystudio.utils.ClassNameUtils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;

public abstract class Task implements ISingletonObject<Integer> {
	
	protected Context mContext;
	
	private int mTaskId;
	
	private volatile boolean mInDBUpdates = false;
	
	private PendingIntent mAlarmSender;

	public Task(Context context) {
		mContext = context;
		
		initMembers();
	}
	
	private void initMembers() {
		mTaskId = Constants.INVALID_ID;
	}
	
	public int getTaskId() {
		return mTaskId;
	}

	public void setTaskId(int tid) {
		mTaskId = tid;
	}

	@Override
	public Integer getSingletonKey() {
		return getTaskId();
	}
	
	public void checkAndUpdateDatabase(Context context, long time) {
		mInDBUpdates = true;
		onCheckAndUpdate(context, time);
		mInDBUpdates = false;
	}	
	
	public boolean isInDatabaseUpdates() {
		return mInDBUpdates;
	}
	
	private ComponentName composeTaskComponent() {
		final String pkgName = mContext.getPackageName();
		final String clsName = getClass().getName();
		
		if (pkgName == null || clsName == null) {
			return null;
		}
		
		return new ComponentName(pkgName, clsName);
	}

	protected void requestExecute() {
		if (mContext == null) {
			return;
		}
		
		final ComponentName taskComp = composeTaskComponent();
		final String data = String.format(Constants.DATA_TEMPL_TASK_REQUEST,
				taskComp.flattenToShortString());
		
		Intent i = new Intent(Constants.ACTION_REQUEST_EXECUTE_TASK);
		
		i.setData(Uri.parse(data));
		i.putExtra(Constants.EXTRA_REQUEST_HOLDER, 
				String.format("CONDITIONAL HOLDER [%s]", this));
		i.putExtra(Constants.EXTRA_TASK_ID, getTaskId());
		
		Logger.debug("taskId = %d, task = %s", 
				getTaskId(),
				this);
		
		mContext.sendBroadcast(i);
	}
	
	protected void scheduleExecuteDelayed(long delay) {
		long now = Calendar.getInstance().getTimeInMillis();

		scheduleExecuteAt(now + delay);
	}
	
	protected void scheduleExecuteAt(long millis) {
		if (millis <= 0) {
			return;
		}
		
		AlarmManager alarmmgr = 
			(AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		if (alarmmgr == null) {
			return;
		}
		
		Intent i = new Intent(Constants.ACTION_REQUEST_EXECUTE_TASK);
		
		i.putExtra(Constants.EXTRA_REQUEST_HOLDER, 
				String.format("CONDITIONAL HOLDER [%s]", this));
		i.putExtra(Constants.EXTRA_TASK_ID, getTaskId());
		
		Logger.debug("NEXT Execution: task[id = %d, instance = %s], on time = %s", 
				getTaskId(),
				this,
				CalendarUtils.timeToReadableString(millis));
		
		mAlarmSender = PendingIntent.getBroadcast(
				mContext, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
		
		alarmmgr.set(AlarmManager.RTC_WAKEUP, millis, mAlarmSender);
	}

	
	public void onCreate(Context context, long time) {
	}
	
	public void onDestroy(Context context, long time) {
	}
	
	public void onPause(Context context, long time) {
	}
	
	public void onResume(Context context, long time) {
	}
	
	public void onCheckAndUpdate(Context context, long time) {
	}
	
	public void onPrepareObservables(Context context, long time) {
	}
	
	public void onDestoryObservables(Context context, long time) {
	}
	
	abstract public void onExecute(Context context, long time);

	private String getPrefName() {
		final Class<? extends Task> klass = getClass();
		
		String pkgName = ClassNameUtils.getPackageName(klass);
		if (pkgName == null) {
			return null;
		}
			
		String className = ClassNameUtils.getClassName(klass);
		if (className == null) {
			return null;
		}
			
		return String.format("%s.%s", 
				pkgName, 
				className.replace('$', '_'));
	}
	
	public void setStateValue(String state, boolean value) {
		SharedPreferences settings = 
			mContext.getSharedPreferences(getPrefName(), 0);
		if (settings == null) {
			return;
		}
		
		SharedPreferences.Editor editor = settings.edit();
		if (editor == null) {
			return;
		}
		
		editor.putBoolean(state, value);
		editor.commit();
	}
	
	public void setStateValue(String state, float value) {
		SharedPreferences settings = 
			mContext.getSharedPreferences(getPrefName(), 0);
		if (settings == null) {
			return;
		}
		
		SharedPreferences.Editor editor = settings.edit();
		if (editor == null) {
			return;
		}
		
		editor.putFloat(state, value);
		editor.commit();
	}
	
	public void setStateValue(String state, int value) {
		SharedPreferences settings = 
			mContext.getSharedPreferences(getPrefName(), 0);
		if (settings == null) {
			return;
		}
		
		SharedPreferences.Editor editor = settings.edit();
		if (editor == null) {
			return;
		}
		
		editor.putInt(state, value);
		editor.commit();
	}
	
	public void setStateValue(String state, long value) {
		SharedPreferences settings = 
			mContext.getSharedPreferences(getPrefName(), 0);
		if (settings == null) {
			return;
		}
		
		SharedPreferences.Editor editor = settings.edit();
		if (editor == null) {
			return;
		}
		
		editor.putLong(state, value);
		editor.commit();
	}
	
	public void setStateValue(String state, String value) {
		SharedPreferences settings = 
			mContext.getSharedPreferences(getPrefName(), 0);
		if (settings == null) {
			return;
		}
		
		SharedPreferences.Editor editor = settings.edit();
		if (editor == null) {
			return;
		}
		
		editor.putString(state, value);
		editor.commit();
	}

	public boolean getStateBooleanValue(String key, boolean defValue) {
		SharedPreferences settings = 
			mContext.getSharedPreferences(getPrefName(), 0);
		if (settings == null) {
			return defValue; 
		}
		
		return settings.getBoolean(key, defValue);
	}
	
	public float getStateFloatValue(String key, float defValue) {
		SharedPreferences settings = 
			mContext.getSharedPreferences(getPrefName(), 0);
		if (settings == null) {
			return defValue; 
		}
		
		return settings.getFloat(key, defValue);
	}
	
	public int getStateIntValue(String key, int defValue) {
		SharedPreferences settings = 
			mContext.getSharedPreferences(getPrefName(), 0);
		if (settings == null) {
			return defValue; 
		}
		
		return settings.getInt(key, defValue);
	}
	
	public long getStateLongValue(String key, long defValue) {
		SharedPreferences settings = 
			mContext.getSharedPreferences(getPrefName(), 0);
		if (settings == null) {
			return defValue; 
		}
		
		return settings.getLong(key, defValue);
	}
	
	public String getStateStringValue(String key, String defValue) {
		SharedPreferences settings = 
			mContext.getSharedPreferences(getPrefName(), 0);
		if (settings == null) {
			return defValue; 
		}
		
		return settings.getString(key, defValue);
	}
	
	protected void runInUiThread(Runnable r) {
		if (r == null) {
			return;
		}
		
		mHandler.post(r);
	}
	
	protected void runInUiThreadDelayed(Runnable r, long delayMillis) {
		if (r == null) {
			return;
		}
		
		mHandler.postDelayed(r, delayMillis);
	}
	
	private Handler mHandler = new Handler();
	
}
