package com.dailystudio.memory.plugin.actions;

import android.content.Context;
import android.content.Intent;

import com.dailystudio.memory.Constants;
import com.dailystudio.memory.plugin.MemoryPlugin;

public class CreateTaskAction extends TaskAction {

	public CreateTaskAction(Context context, MemoryPlugin plugin, Intent i) {
		super(context, plugin, i, true);
	}

	@Override
	protected boolean doTaskAction(int taskId, long now) {
		String klass = mIntent.getStringExtra(Constants.EXTRA_TASK_CLASS);
		
		return mPlugin.onCreateTask(mContext, taskId, klass, now);
	}

}
