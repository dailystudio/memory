package com.dailystudio.memory.notify;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dailystudio.app.fragment.AbsLoaderFragment;
import com.dailystudio.memory.loader.CoreLoaderIds;
import com.dailystudio.memory.ui.R;

public class MemoryNotifyFragment extends AbsLoaderFragment<Cursor>{
	
	private CursorAdapter mAdapter;
	private MemoryNotifyHostView mNotifyHostView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_memory_notify, null);
		
		setupViews(view);
		
		return view;
	}

	private void setupViews(View fragmentView) {
		if (fragmentView == null) {
			return;
		}
		
		mNotifyHostView = (MemoryNotifyHostView)fragmentView.findViewById(
				R.id.notify_view);
		if (mNotifyHostView != null) {
			mAdapter = new MemoryNotifyCursorAdapter(getActivity());
			
			mNotifyHostView.setAdapter(mAdapter);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
		return new MemoryNotificationsLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader,
			Cursor data) {
		DatabaseUtils.dumpCursor(data);
		
		swapCursor(data);
	}
	
	protected void swapCursor(Cursor c) {
		if (mAdapter != null) {
			Cursor oldCursor = mAdapter.swapCursor(c);
			
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
		return CoreLoaderIds.LOADER_ID_NOTIFICATIONS;
	}

	@Override
	protected Bundle createLoaderArguments() {
		return null;
	}
	
}
