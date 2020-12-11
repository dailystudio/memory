package com.dailystudio.memory.notify;

import com.dailystudio.app.fragment.AbsLoaderFragment;
import com.dailystudio.memory.ui.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MemoryNotificationActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_notification);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		AbsLoaderFragment<?> fragment = getBaseFragment();
		if (fragment == null) {
			return;
		}
		
		fragment.onNewIntent(intent);
	}
	
	private AbsLoaderFragment<?> getBaseFragment() {
		return null;
	}
	
}
