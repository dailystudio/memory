package com.dailystudio.memory.plugin.actions;

import android.content.Context;
import android.content.Intent;

import com.dailystudio.memory.plugin.MemoryPlugin;

public class DestroyTaskAction extends TaskAction {

	public DestroyTaskAction(Context context, MemoryPlugin plugin, Intent i) {
		super(context, plugin, i);
	}

	@Override
	protected boolean doTaskAction(int taskId, long now) {
		return mPlugin.onDestoryTask(mContext, taskId, now);
	}

}
