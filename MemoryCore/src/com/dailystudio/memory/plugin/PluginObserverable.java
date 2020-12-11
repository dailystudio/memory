package com.dailystudio.memory.plugin;

import com.dailystudio.nativelib.observable.NativeObservable;

import android.content.Context;

public class PluginObserverable extends NativeObservable {

	public PluginObserverable(Context context) {
		super(context);
	}

	@Override
	protected void onCreate() {
	}

	@Override
	protected void onDestroy() {
	}

	@Override
	protected void onPause() {
	}

	@Override
	protected void onResume() {
	}

}
