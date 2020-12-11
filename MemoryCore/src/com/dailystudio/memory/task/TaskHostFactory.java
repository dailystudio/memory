package com.dailystudio.memory.task;

import java.util.HashMap;
import java.util.Map;

import com.dailystudio.memory.Constants;
import com.dailystudio.factory.ClassNameFactory;
import com.dailystudio.factory.Factory;

public class TaskHostFactory extends ClassNameFactory<TaskHost> {
	
	private static volatile int sTaskIdSeed = 0;
	
	private static Map<String, String> sTaskHostClassesMap = new HashMap<String, String>();
	
	static {
		if (sTaskHostClassesMap != null) {
			sTaskHostClassesMap.put(Constants.TASK_TYPE_PERIODICAL, PeriodicalTaskHost.class.getName());
			sTaskHostClassesMap.put(Constants.TASK_TYPE_CONDITIONAL, ConditionalTaskHost.class.getName());
		}
	}
	
	public synchronized static final TaskHostFactory getInstance() {
		return Factory.getInstance(TaskHostFactory.class);
	}

	public synchronized static final TaskHost allocateHost(String taskType) {
		final TaskHostFactory factory = TaskHostFactory.getInstance();
		if (factory == null) {
			return null;
		}
	
		if (sTaskHostClassesMap == null) {
			return null;
		}
		
		if (taskType == null) {
			return null;
		}
		
		String klassName = sTaskHostClassesMap.get(taskType);
		if (klassName == null) {
			return null;
		}
		
		TaskHost host = factory.createObject(klassName);
		
		return host;
	}
	
	@Override
	protected TaskHost newObject(String taskType) {
		if (taskType == null) {
			taskType = Constants.TASK_TYPE_PERIODICAL;
		}
		
		TaskHost host = super.newObject(taskType);
		if (host == null) {
			return null;
		}
		
		host.setTaskId(sTaskIdSeed++);
		
		return host;
	}
	
	@Override
	public void reset() {
		super.reset();
		
		sTaskIdSeed = 0;
	}

}
