package com.dailystudio.memory.plugin.actions;

import android.content.Context;
import android.content.Intent;

import com.dailystudio.memory.plugin.MemoryPlugin;

public class PrepareTaskObservablesAction extends TaskAction {

	public PrepareTaskObservablesAction(Context context, MemoryPlugin plugin,
			Intent i) {
		super(context, plugin, i, true);
	}

	@Override
	protected boolean doTaskAction(int taskId, long now) {
		return mPlugin.onPrepareTaskObservables(mContext, taskId, now);
	}

}
