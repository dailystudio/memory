package com.dailystudio.memory.plugin.actions;

import android.content.Context;
import android.content.Intent;

import com.dailystudio.memory.plugin.MemoryPlugin;

public class DestroyTaskObservablesAction extends TaskAction {

	public DestroyTaskObservablesAction(Context context, MemoryPlugin plugin,
			Intent i) {
		super(context, plugin, i, true);
	}

	@Override
	protected boolean doTaskAction(int taskId, long now) {
		return mPlugin.onDestoryTaskObservables(mContext, taskId, now);
	}

}
