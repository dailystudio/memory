package com.dailystudio.memory.task;

import java.util.List;

import com.dailystudio.manager.Manager;
import com.dailystudio.manager.SingletonManager;

public class TaskManager extends SingletonManager<Integer, Task> {
	
	public static synchronized TaskManager getInstance() {
		return Manager.getInstance(TaskManager.class);
	}
	
	public static void registerTask(Task task) {
		if (task == null) {
			return;
		}
		
		TaskManager dtmgr = TaskManager.getInstance();
		if (dtmgr == null) {
			return;
		}
		
		dtmgr.addObject(task);
	}
	
	public static void unregisterTask(Task task) {
		if (task == null) {
			return;
		}
		
		TaskManager dtmgr = TaskManager.getInstance();
		if (dtmgr == null) {
			return;
		}
		
		dtmgr.removeObject(task);
	}
	
	public static void unregisterTask(int taskId) {
		TaskManager dtmgr = TaskManager.getInstance();
		if (dtmgr == null) {
			return;
		}
		
		dtmgr.removeObjectByKey(taskId);
	}
	
	public static void clearTasks() {
		TaskManager dtmgr = TaskManager.getInstance();
		if (dtmgr == null) {
			return;
		}
		
		dtmgr.clearObjects();
	}

	public static Task getTask(int taskId) {
		TaskManager dtmgr = TaskManager.getInstance();
		if (dtmgr == null) {
			return null;
		}
		
		return dtmgr.getObject(taskId);
	}

	public static List<Task> listTasks() {
		TaskManager dtmgr = TaskManager.getInstance();
		if (dtmgr == null) {
			return null;
		}
		
		return dtmgr.listObjects();
	}

	public static int countTasks() {
		TaskManager dtmgr = TaskManager.getInstance();
		if (dtmgr == null) {
			return 0;
		}
		
		return dtmgr.getCount();
	}

}
