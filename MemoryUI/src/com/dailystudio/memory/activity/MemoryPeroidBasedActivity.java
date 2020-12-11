package com.dailystudio.memory.activity;

import com.dailystudio.app.fragment.AbsLoaderFragment;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class MemoryPeroidBasedActivity extends ActionBarActivity {

	protected long mPeroidStart;
	protected long mPeroidEnd;
	
	protected void bindIntentOnFragments(Intent intent) {
		FragmentManager fragmgr = getSupportFragmentManager();
		if (fragmgr == null) {
			return;
		}
		
		int[] fragIds = listPeroidBaseFragmentIds();
		if (fragIds == null) {
			return;
		}
		
		Fragment fragment = null;
		for (int id: fragIds) {
			fragment = fragmgr.findFragmentById(id);
			if (fragment instanceof AbsLoaderFragment<?>) {
				Logger.debug("bindIntent: id = %d, fragment = %s", 
						id, fragment);
				((AbsLoaderFragment<?>) fragment).onNewIntent(intent);
			}
		}
	}

	@Override
	protected void bindIntent(Intent intent) {
		super.bindIntent(intent);
		
		if (intent == null) {
			return;
		}
		
		mPeroidStart = intent.getLongExtra(Constants.EXTRA_PEROID_START, -1);
		mPeroidEnd = intent.getLongExtra(Constants.EXTRA_PEROID_END, -1);
	}
	
	public long getPeroidStart() {
		return mPeroidStart;
	}
	
	public long getPeroidEnd() {
		return mPeroidEnd;
	}
	
	protected int[] listPeroidBaseFragmentIds() {
		return null;
	}

}
