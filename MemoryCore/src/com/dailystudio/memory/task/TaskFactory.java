package com.dailystudio.memory.task;

import com.dailystudio.factory.ClassNameFactory;
import com.dailystudio.factory.Factory;

public class TaskFactory extends ClassNameFactory<Task> {
	
	private static volatile int sTaskIdSeed = 0;
	
	public synchronized static final TaskFactory getInstance() {
		return Factory.getInstance(TaskFactory.class);
	}

	public synchronized static final Task createTask(String params) {
		final TaskFactory factory = TaskFactory.getInstance();
		if (factory == null) {
			return null;
		}
	
		Task task = factory.createObject(params);
		
		return task;
	}
	
	@Override
	protected Task newObject(String params) {
		Task task = super.newObject(params);
		if (task == null) {
			return null;
		}
		
//		Logger.debug("sTaskIdSeed(%d)", sTaskIdSeed);
		task.setTaskId(sTaskIdSeed++);
		
		return task;
	}
	
	@Override
	public void reset() {
		super.reset();
		sTaskIdSeed = 0;
	}

}
