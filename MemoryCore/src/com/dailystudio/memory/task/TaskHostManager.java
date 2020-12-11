package com.dailystudio.memory.task;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.dailystudio.memory.Constants;
import com.dailystudio.memory.plugin.MemoryPluginInfo;
import com.dailystudio.memory.plugin.PluginManager;
import com.dailystudio.development.Logger;
import com.dailystudio.manager.GroupManager;
import com.dailystudio.manager.Manager;

public class TaskHostManager extends GroupManager<String, Integer, TaskHost> {
	
	private void registerResultReceiver() {
		final Context context = getContext();
		if (context == null) {
			return;
		}
		
		final IntentFilter filter = new IntentFilter(Constants.ACTION_RETURN_TASK_RESULT);
		
		try {
			context.registerReceiver(mResultReceiver, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void unregisterResultReceiver() {
		final Context context = getContext();
		if (context == null) {
			return;
		}
		
		try {
			context.unregisterReceiver(mResultReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void registerKeepAliveReceiver() {
		final Context context = getContext();
		if (context == null) {
			return;
		}
		
		final IntentFilter filter = new IntentFilter(Constants.ACTION_TASKS_BECOME_ALIVE);
		
		try {
			context.registerReceiver(mKeepAliveReceiver, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void unregisterKeepAliveReceiver() {
		final Context context = getContext();
		if (context == null) {
			return;
		}
		
		try {
			context.unregisterReceiver(mKeepAliveReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void registerRequestReceiver() {
		final Context context = getContext();
		if (context == null) {
			return;
		}
		
		final IntentFilter filter = new IntentFilter(Constants.ACTION_REQUEST_EXECUTE_TASK);
		
		filter.addDataScheme("memory-task");
		try {
			context.registerReceiver(mRequestExecuteReceiver, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void unregisterRequestReceiver() {
		final Context context = getContext();
		if (context == null) {
			return;
		}
		
		try {
			context.unregisterReceiver(mRequestExecuteReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void addObject(TaskHost object) {
		final int count = countTasks();
		
		super.addObject(object);

		if (object != null) {
    		object.createTask();
			object.startTask();
		}    				

		if (count == 0) {
			registerResultReceiver();
			registerKeepAliveReceiver();
			registerRequestReceiver();
		}
	}
	
	@Override
	public void removeObject(TaskHost object) {
		super.removeObject(object);
		
		if (object != null) {
			object.stopTask();
			object.destroyTask();
		}
		
		final int count = countTasks();

		if (count == 0) {
			unregisterKeepAliveReceiver();
			unregisterResultReceiver();
			unregisterRequestReceiver();
		}
	}
	
	@Override
	public TaskHost removeObjectByKey(Integer key) {
		TaskHost host = getObject(key);
		if (host != null) {
			host.stopTask();
			host.destroyTask();
		}
		
		TaskHost removed = super.removeObjectByKey(key);

		final int count = countTasks();

		if (count == 0) {
			unregisterKeepAliveReceiver();
			unregisterResultReceiver();
			unregisterRequestReceiver();
		}

		return removed;
	}
	
	@Override
	public void clearObjects() {
		List<TaskHost> hosts = listTasks();
		if (hosts != null && hosts.size() > 0) {
			for (TaskHost host: hosts) {
				host.stopTask();
				host.destroyTask();
			}
		}
		
		super.clearObjects();
	}
	
	public void recoveryObject(int taskId) {
		Logger.debug("id = %d", taskId);
		if (taskId == Constants.INVALID_ID) {
			return;
		}
		
		TaskHost host = getTask(taskId);
		Logger.debug("host = %s", host);
		if (host == null) {
			return;
		}
		
		host.createTask();
		host.startTask();
	}
	
	public static synchronized TaskHostManager getInstance() {
		return Manager.getInstance(TaskHostManager.class);
	}
	
	public static void registerTask(TaskHost host) {
		if (host == null) {
			return;
		}
		
		TaskHostManager dtmgr = TaskHostManager.getInstance();
		if (dtmgr == null) {
			return;
		}
		
		dtmgr.addObject(host);
	}
	
	public static void unregisterTask(TaskHost host) {
		Logger.debug("host = %s", host);
		if (host == null) {
			return;
		}
		
		TaskHostManager dtmgr = TaskHostManager.getInstance();
		if (dtmgr == null) {
			return;
		}
		
		dtmgr.removeObject(host);
	}
	
	public static void unregisterTask(int taskId) {
		TaskHostManager dtmgr = TaskHostManager.getInstance();
		if (dtmgr == null) {
			return;
		}
		
		dtmgr.removeObjectByKey(taskId);
	}
	
	public static void clearTasks() {
		TaskHostManager dtmgr = TaskHostManager.getInstance();
		if (dtmgr == null) {
			return;
		}
		
		dtmgr.clearObjects();
	}

	public static TaskHost getTask(int taskId) {
		TaskHostManager dtmgr = TaskHostManager.getInstance();
		if (dtmgr == null) {
			return null;
		}
		
		return dtmgr.getObject(taskId);
	}

	public static List<TaskHost> getTasks(ComponentName comp) {
		if (comp == null) {
			return null;
		}
		
		TaskHostManager dtmgr = TaskHostManager.getInstance();
		if (dtmgr == null) {
			return null;
		}
		
		return dtmgr.getObjectsInGroup(comp.flattenToShortString());
	}
	
	public static List<TaskHost> listTasks() {
		TaskHostManager dtmgr = TaskHostManager.getInstance();
		if (dtmgr == null) {
			return null;
		}
		
		return dtmgr.listObjects();
	}

	public static int countTasks() {
		TaskHostManager dtmgr = TaskHostManager.getInstance();
		if (dtmgr == null) {
			return 0;
		}
		
		return dtmgr.getCount();
	}
	
	public static void recoveryTask(int taskId) {
		TaskHostManager dtmgr = TaskHostManager.getInstance();
		if (dtmgr == null) {
			return;
		}
		
		TaskHost host = dtmgr.removeObjectByKey(taskId);
		Logger.debug("taskId = %d, host = %s", taskId, host);
		if (host == null) {
			return;
		}
		
		dtmgr.addObject(host);
	}


	private BroadcastReceiver mResultReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null) {
				return;
			}
			
			final String action = intent.getStringExtra(Constants.EXTRA_TASK_ACTION);			
			final int resultCode = intent.getIntExtra(
					Constants.EXTRA_TASK_RESULT, Constants.RESULT_FAILURE);
			final int taskId = 
				intent.getIntExtra(Constants.EXTRA_TASK_ID, Constants.INVALID_ID);
			
			Logger.debug("taskId = %d, action = %s, resultCode = %d", 
					taskId, action, resultCode);
			if (resultCode != Constants.RESULT_SUCCESS) {
				if (Constants.ACTION_EXECUTE_TASK.equals(action)) {
					TaskHost host = TaskHostManager.getTask(taskId);
					if (host != null) {
						host.execFailure();
					}
					
					TaskHostManager.recoveryTask(taskId);
				} else if (Constants.ACTION_KEEP_ALIVE_TASK.equals(action)) {
					TaskHostManager.recoveryTask(taskId);
				}
			} else {
				if (Constants.ACTION_EXECUTE_TASK.equals(action)) {
					TaskHost host = TaskHostManager.getTask(taskId);
					if (host != null) {
						host.execSuccess();
					}
				}
			}
		}
		
	};

	private BroadcastReceiver mKeepAliveReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null) {
				return;
			}
			
			final String packageName = intent.getStringExtra(Constants.EXTRA_ALIVE_TASKS_PACKAGE);			
			Logger.debug("packageName = %s", packageName);
			if (packageName == null) {
				return;
			}
			
			keepAliveHostsInPackage(packageName);
		}
		
		private void keepAliveHostsInPackage(String packageName) {
			if (packageName == null) {
				return;
			}
			
			final List<MemoryPluginInfo> plugins = 
				PluginManager.listPlugins(packageName);
			if (plugins == null) {
				return;
			}
			
			final int N = plugins.size();
			if (N <= 0) {
				return;
			}
			
			MemoryPluginInfo pInfo = null;
			for (int i = 0; i < N; i++) {
				pInfo = plugins.get(i);
				if (pInfo == null) {
					continue;
				}
				
				keepAliveHostsInPlugin(pInfo);
			}
		}

		private void keepAliveHostsInPlugin(MemoryPluginInfo pInfo) {
			if (pInfo == null) {
				return;
			}
			
			final ComponentName comp = pInfo.getComponent();
			if (comp == null) {
				return;
			}
			
			List<TaskHost> hosts = TaskHostManager.getTasks(comp);
			if (hosts == null) {
				return;
			}
			
			final int N = hosts.size();
			if (N <= 0) {
				return;
			}
			
			TaskHost host = null;
			for (int i = 0; i < N; i++) {
				host = hosts.get(i);
				if (host == null) {
					continue;
				}
				
				host.keepAliveTask();
			}
		}
		
	};

	private BroadcastReceiver mRequestExecuteReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null) {
				return;
			}
			
			final String holder = intent.getStringExtra(Constants.EXTRA_REQUEST_HOLDER);
			Logger.debug("REQEXEC: this = 0x%8x, holder = %s, comp = %s", 
					mRequestExecuteReceiver.hashCode(),
					holder,
					intent.getComponent());
			
			final int taskId = intent.getIntExtra(
					Constants.EXTRA_TASK_ID, Constants.INVALID_ID);
			
			final TaskHost task = getObject(taskId);
			Logger.debug("REQEXEC: this = 0x%8x, taskId = %d, task = %s", 
					mRequestExecuteReceiver.hashCode(),
					taskId, task);
			if (task != null) {
				task.executeTask();
			}
		}
		
	};

}
