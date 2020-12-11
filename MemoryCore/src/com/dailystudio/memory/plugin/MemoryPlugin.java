package com.dailystudio.memory.plugin;

import java.util.List;

import com.dailystudio.GlobalContextWrapper;
import com.dailystudio.datetime.CalendarUtils;
import com.dailystudio.memory.Constants;
import com.dailystudio.memory.plugin.actions.ClearTasksAction;
import com.dailystudio.memory.plugin.actions.CreateTaskAction;
import com.dailystudio.memory.plugin.actions.DestroyTaskAction;
import com.dailystudio.memory.plugin.actions.DestroyTaskObservablesAction;
import com.dailystudio.memory.plugin.actions.ExecuteTaskAction;
import com.dailystudio.memory.plugin.actions.KeepAliveTaskAction;
import com.dailystudio.memory.plugin.actions.PauseTaskAction;
import com.dailystudio.memory.plugin.actions.PrepareTaskObservablesAction;
import com.dailystudio.memory.plugin.actions.ResumeTaskAction;
import com.dailystudio.memory.task.Task;
import com.dailystudio.memory.task.TaskFactory;
import com.dailystudio.memory.task.TaskManager;
import com.dailystudio.development.Logger;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class MemoryPlugin extends BroadcastReceiver {
	
	@Override
	final public void onReceive(Context context, Intent intent) {
		Context appContext = context.getApplicationContext();

		GlobalContextWrapper.bindContext(appContext);
		
		if (intent == null) {
			return;
		}

		final String action = intent.getAction();
		if (action == null) {
			return;
		}
		
		TaskActionExecutor tae = TaskActionExecutor.getInstance();
		if (tae == null) {
			return;
		}

		long now = System.currentTimeMillis();
		
		startTaskActionExecutor(tae);

		if (Constants.ACTION_REGISTER_PLUGIN.equals(action)) {
			onRegister(appContext, now);
		} else if (Constants.ACTION_UNREGISTER_PLUGIN.equals(action)) {
			onUnregister(appContext, now);
		} else if (Constants.ACTION_CREATE_TASK.equals(action)) {
			tae.tryToExecuteAction(new CreateTaskAction(appContext, this, intent));
			tae.tryToExecuteAction(new PrepareTaskObservablesAction(appContext, this, intent));
		} else if (Constants.ACTION_DESTROY_TASK.equals(action)) {
			tae.tryToExecuteAction(new DestroyTaskObservablesAction(appContext, this, intent));
			tae.tryToExecuteAction(new DestroyTaskAction(appContext, this, intent));
		} else if (Constants.ACTION_EXECUTE_TASK.equals(action)) {
			tae.tryToExecuteAction(new ExecuteTaskAction(appContext, this, intent));
		} else if (Constants.ACTION_RESUME_TASK.equals(action)) {
			tae.tryToExecuteAction(new ResumeTaskAction(appContext, this, intent));
		} else if (Constants.ACTION_PAUSE_TASK.equals(action)) {
			tae.tryToExecuteAction(new PauseTaskAction(appContext, this, intent));
		} else if (Constants.ACTION_KEEP_ALIVE_TASK.equals(action)) {
			startTaskActionExecutor(tae);
			
			tae.tryToExecuteAction(new KeepAliveTaskAction(appContext, this, intent));
		} else if (Constants.ACTION_CLEAR_TASKS.equals(action)) {
			tae.tryToExecuteAction(new ClearTasksAction(appContext, this, intent));
		}
	}

	private void startTaskActionExecutor(TaskActionExecutor tae) {
		if (tae == null) {
			return;
		}
		
		AsyncTask.Status status = tae.getStatus();
		
		if (status == AsyncTask.Status.RUNNING) {
			return;
		}
		
		tae.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void)null);
	}
	
	protected boolean onRegister(Context context, long now) {
		final Intent katIntent = getKeepAliveTaskServiceIntent(context);
		if (katIntent != null) {
			final ComponentName comp = context.startService(katIntent);
			Logger.debug("START KAT Service: intent = %s, comp = %s", 
					katIntent, comp);
		}
		
		final Intent awIntent = getAppWidgetDataServiceIntent(context);
		if (awIntent != null) {
			final ComponentName comp = context.startService(awIntent);
			Logger.debug("START AWD Service: intent = %s, comp = %s", 
					awIntent, comp);
		}
		
		return true;
	}

	protected boolean onUnregister(Context context, long now) {
		final Intent katIntent = getKeepAliveTaskServiceIntent(context);
		if (katIntent != null) {
			boolean ret = context.stopService(katIntent);
			Logger.debug("STOP KAT Service: intent = %s, ret = %s",
					katIntent, ret);
		}
		
		final Intent awIntent = getAppWidgetDataServiceIntent(context);
		if (awIntent != null) {
			boolean ret = context.stopService(awIntent);
			Logger.debug("STOP AWD Service: intent = %s, ret = %s",
					awIntent, ret);
		}
		
		return true;
	}
	
	public boolean onCreateTask(Context context, int taskId, String klass, long now) {
		if (klass == null || taskId == Constants.INVALID_ID) {
			return false;
		}
		
		Task task = TaskFactory.createTask(klass);
		if (task == null) {
			return false;
		}
		
		task.setTaskId(taskId);
		Logger.debug("NOW(%s): id = %d, task = %s",
				CalendarUtils.timeToReadableString(now),
				taskId,
				task.getClass().getSimpleName());
		
		task.onCreate(context, now);
		
		TaskManager.registerTask(task);
		
		return true;
	}
	
	public boolean onDestoryTask(Context context, int taskId, long now) {
		if (taskId == Constants.INVALID_ID) {
			return false;
		}
		
		Task task = TaskManager.getTask(taskId);
		if (task == null) {
			return false;
		}		

		task.onDestroy(context, now);
		
		TaskManager.unregisterTask(task);

		return true;
	}
	
	public boolean onExecuteTask(Context context, int taskId, long now) {
		if (taskId == Constants.INVALID_ID) {
			return false;
		}

		Task task = TaskManager.getTask(taskId); 
		if (task == null) {
			return false;
		}
		
		final boolean inDBUpdates = task.isInDatabaseUpdates();

		Logger.debug("NOW(%s): id = %d, task = %s (dbUpdates: %s)",
				CalendarUtils.timeToReadableString(now),
				taskId,
				task.getClass().getSimpleName(),
				inDBUpdates);

		if (inDBUpdates) {
			return true;
		}
		
		task.onExecute(context, now);
		
		return true;
	}
	
	public boolean onPauseTask(Context context, int taskId, long now) {
		if (taskId == Constants.INVALID_ID) {
			return false;
		}
		
		Task task = TaskManager.getTask(taskId);
		if (task == null) {
			return false;
		}		

		task.onPause(context, now);

		return true;
	}

	public boolean onResumeTask(Context context, int taskId, long now) {
		if (taskId == Constants.INVALID_ID) {
			return false;
		}
		
		Task task = TaskManager.getTask(taskId);
		if (task == null) {
			return false;
		}		

		task.onResume(context, now);
		
		return true;
	}
	
	public boolean onPrepareTaskObservables(Context context, int taskId, long now) {
		if (taskId == Constants.INVALID_ID) {
			return false;
		}
		
		Task task = TaskManager.getTask(taskId);
		if (task == null) {
			return false;
		}		

		task.onPrepareObservables(context, now);
		
		return true;
	}
	
	public boolean onDestoryTaskObservables(Context context, int taskId, long now) {
		if (taskId == Constants.INVALID_ID) {
			return false;
		}
		
		Task task = TaskManager.getTask(taskId);
		if (task == null) {
			return false;
		}		

		task.onDestoryObservables(context, now);
		
		return true;
	}
	
	public boolean onKeepAliveTask(Context context, int taskId, long now) {
		if (taskId == Constants.INVALID_ID) {
			return false;
		}
		
		Task task = TaskManager.getTask(taskId);
		if (task == null) {
			return false;
		}		
		
		return true;
	}
	
	public boolean onClearTasks(Context context, long now) {
		List<Task> tasks = TaskManager.listTasks();
		
		if (tasks == null) {
			return true;
		}
		
		for (Task task: tasks) {
			task.onDestoryObservables(context, now);
			task.onDestroy(context, now);
			
			TaskManager.unregisterTask(task);
		}
		
		return true;
	}
	
	protected Intent getKeepAliveTaskServiceIntent(Context context) {
		return null;
	}
	
	protected Intent getAppWidgetDataServiceIntent(Context context) {
		return null;
	}
	
}
