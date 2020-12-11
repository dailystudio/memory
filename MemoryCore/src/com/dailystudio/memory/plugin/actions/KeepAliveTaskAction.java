package com.dailystudio.memory.plugin.actions;

import android.content.Context;
import android.content.Intent;

import com.dailystudio.memory.plugin.MemoryPlugin;

public class KeepAliveTaskAction extends TaskAction {

	public KeepAliveTaskAction(Context context, MemoryPlugin plugin, Intent i) {
		super(context, plugin, i);
	}

	@Override
	protected boolean doTaskAction(int taskId, long now) {
		return mPlugin.onKeepAliveTask(mContext, taskId, now);
	}

}
