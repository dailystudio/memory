package com.dailystudio.memory.task;

import java.util.Calendar;

import com.dailystudio.memory.Constants;
import com.dailystudio.datetime.CalendarUtils;
import com.dailystudio.development.Logger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class PeriodicalTaskHost extends TaskHost {
	
	protected int mExecPeroidMillis;
	
	private PendingIntent mAlarmSender;
	
	
	public PeriodicalTaskHost(Context context) {
		super(context);
		
		initMembers();
	}

	private void initMembers() {
		mExecPeroidMillis = 0;
		mAlarmSender = null;
	}
	
	public void setExecPeroid(int millis) {
		mExecPeroidMillis = millis;
	}
	
	public int getExecPeroid() {
		return mExecPeroidMillis;
	}
	
	@Override
	protected long predictNextExecTime() {
		return (mLastExecTime + mExecPeroidMillis);
	}
	
	@Override
	public boolean equals(Object o) {
		boolean equals = super.equals(o);
		if (equals == false) {
			return false;
		}
		
		PeriodicalTaskHost task = (PeriodicalTaskHost)o;
		
		if (mExecPeroidMillis != task.getExecPeroid()) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("%s, execPeroid(%s)", 
				super.toString(),
				CalendarUtils.durationToReadableString(mExecPeroidMillis));
	}
	
	@Override
	public void startTask() {
		super.startTask();	

		final boolean oneshot = isOneshot();
		final int execPeroidMillis = getExecPeroid();
		if (oneshot == false && execPeroidMillis <= 0) {
			return;
		}
		
		long now = Calendar.getInstance().getTimeInMillis();
		long initialDelay = (oneshot ?
				execPeroidMillis : (execPeroidMillis - now % execPeroidMillis));
		
		AlarmManager alarmmgr = 
			(AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		if (alarmmgr == null) {
			return;
		}
		
		final ComponentName taskComp = composeTaskComponent();
		final String data = String.format(Constants.DATA_TEMPL_TASK_REQUEST,
				taskComp.flattenToShortString());
		
		Intent i = new Intent(Constants.ACTION_REQUEST_EXECUTE_TASK);
		
		i.setData(Uri.parse(data));
		i.putExtra(Constants.EXTRA_REQUEST_HOLDER, 
				String.format("PERIODICAL HOLDER [%s]", this));
		i.putExtra(Constants.EXTRA_TASK_ID, getTaskId());
		
		mAlarmSender = PendingIntent.getBroadcast(
				mContext, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
		
		Logger.debug("[ID: %d, DATA: %s, SENDER: %s, FISRT: %s, INTERVAL: %S", 
				getTaskId(),
				data, mAlarmSender, 
				CalendarUtils.timeToReadableString(now + initialDelay), 
				CalendarUtils.durationToReadableString(execPeroidMillis));
		
		if (oneshot) {
			alarmmgr.set(AlarmManager.RTC_WAKEUP, 
					(now + initialDelay), mAlarmSender);
		} else {
			alarmmgr.setRepeating(AlarmManager.RTC_WAKEUP, 
					(now + initialDelay), execPeroidMillis, mAlarmSender);
		}
	}
	
	private ComponentName composeTaskComponent() {
		final ComponentName pluginComp = getComponent();
		if (pluginComp == null) {
			return null;
		}
		
		final String pkgName = pluginComp.getPackageName();
		final String clsName = mTaskClass;
		
		if (pkgName == null || clsName == null) {
			return null;
		}
		
		return new ComponentName(pkgName, clsName);
	}

	@Override
	public void stopTask() {
		super.stopTask();
		
		if (mAlarmSender == null) {
			return;
		}
		
		AlarmManager alarmmgr = 
			(AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		if (alarmmgr == null) {
			return;
		}
		
		alarmmgr.cancel(mAlarmSender);
		
		mAlarmSender = null;
	}

}
