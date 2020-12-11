package com.dailystudio.memory.task;

import android.content.Context;

import com.dailystudio.dataobject.Column;
import com.dailystudio.dataobject.IntegerColumn;
import com.dailystudio.dataobject.Template;
import com.dailystudio.dataobject.TextColumn;
import com.dailystudio.datetime.dataobject.TimeCapsule;

public class TaskHostAction extends TimeCapsule {

	public static Column COLUMN_TASK_ID = new IntegerColumn("task_id", false);
	public static Column COLUMN_TASK_CLASS = new TextColumn("task_class", false);
	public static Column COLUMN_TASK_ACTION = new TextColumn("task_action", false);
	
	private static final Column[] sColumns = {
		COLUMN_TASK_ID,
		COLUMN_TASK_CLASS,
		COLUMN_TASK_ACTION,
	};
	
	public TaskHostAction(Context context) {
		this(context, VERSION_START);
	}

	public TaskHostAction(Context context, int version) {
		super(context, version);
		
		
		final Template templ = getTemplate();
		
		templ.addColumns(sColumns);
	}
	
	public void setTaskId(int taskId) {
		setValue(COLUMN_TASK_ID, taskId);
	}
	
	public int getTaskId() {
		return getIntegerValue(COLUMN_TASK_ID);
	}

	public void setTaskClass(String taskClass) {
		setValue(COLUMN_TASK_CLASS, taskClass);
	}
	
	public String getTaskClass() {
		return getTextValue(COLUMN_TASK_CLASS);
	}

	public void setTaskAction(String action) {
		setValue(COLUMN_TASK_ACTION, action);
	}
	
	public String getTaskAction() {
		return getTextValue(COLUMN_TASK_ACTION);
	}

}
