package com.dailystudio.memory.ask;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dailystudio.app.fragment.AbsLoaderFragment;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.loader.CoreLoaderIds;
import com.dailystudio.memory.ui.BuildConfig;
import com.dailystudio.memory.ui.R;

public class MemoryAskFragment extends AbsLoaderFragment<Cursor>{

	private MemoryAskHostView mAskHostView;
	
	private CursorAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_memory_ask, null);
		
		setupViews(view);
		
		return view;
	}

	private void setupViews(View fragmentView) {
		if (fragmentView == null) {
			return;
		}
		
		mAskHostView = (MemoryAskHostView)fragmentView.findViewById(
				R.id.ask_view);
		if (mAskHostView != null) {
			mAdapter = new MemoryAskCursorAdapter(getActivity());
			
			mAskHostView.scheduleQuestions(mAdapter);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		if (mAskHostView != null) {
			mAskHostView.resumeAskQuestions();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();

		if (mAskHostView != null) {
			mAskHostView.pasueAskQuestions();
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
		return new MemoryQuestionsLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader,
			Cursor data) {
		swapCursor(data);
	}
	
	protected void swapCursor(Cursor c) {
		if (mAdapter != null) {
			Cursor oldCursor = mAdapter.swapCursor(c);
			
			if (BuildConfig.DEBUG) {
				Logger.debug("DUMPING CURSOR: oldCursor = %s", oldCursor);
				DatabaseUtils.dumpCursor(oldCursor);
				Logger.debug("DUMPING CURSOR: newCursor = %s", c);
				DatabaseUtils.dumpCursor(c);
			}
			
			if (oldCursor != null && oldCursor != c) {
				oldCursor.close();
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}

	@Override
	protected int getLoaderId() {
		return CoreLoaderIds.LOADER_ID_QUESTIONS;
	}

	@Override
	protected Bundle createLoaderArguments() {
		return null;
	}
	
}
