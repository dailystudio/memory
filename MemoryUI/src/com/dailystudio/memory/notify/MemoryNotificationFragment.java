package com.dailystudio.memory.notify;

import java.net.URISyntaxException;

import com.dailystudio.app.fragment.AbsLoaderFragment;
import com.dailystudio.app.utils.ActivityLauncher;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;
import com.dailystudio.memory.loader.CoreLoaderIds;
import com.dailystudio.memory.ui.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class MemoryNotificationFragment extends AbsLoaderFragment<Cursor> {

	private int mDatabaseId;
	private int mNid;
	private String mSourcePackage;
	private Intent mNotifyIntent;
	
	private TextView mNotificationContent;
	private TextView mNotificationTitle;
	private View mCloseButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_memory_notification, null);
		
		setupViews(view);
		
		return view;
	}
	
	private void setupViews(View fragmentView) {
		if (fragmentView == null) {
			return;
		}
		
		mNotificationContent = (TextView) fragmentView.findViewById(
				R.id.notification_content);
		if (mNotificationContent != null) {
			mNotificationContent.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					final Activity hostActivity = getActivity();
					if (hostActivity == null) {
						return;
					}
					
					Logger.debug("launch notify intent: %s", 
							mNotifyIntent);
					if (mNotifyIntent == null) {
						return;
					}
					
					ActivityLauncher.launchActivity(
							hostActivity, mNotifyIntent);
							
					setNotifyViewed(true);
				}
				
			});
			
		}
		
		mNotificationTitle = (TextView) fragmentView.findViewById(
				R.id.notification_title);
		
		mCloseButton = fragmentView.findViewById(R.id.notification_close);
		if (mCloseButton != null) {
			mCloseButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					setNotifyViewed(true);
				}

			});
		}
	}

	@Override
	public void bindIntent(Intent intent) {
		super.bindIntent(intent);
		
		if (intent == null) {
			return;
		}
		
		mDatabaseId = intent.getIntExtra(Constants.EXTRA_NOTIFY_DATABASE_ID, 
				Constants.INVALID_ID);
		mNid = intent.getIntExtra(Constants.EXTRA_NOTIFY_ID,
				Constants.INVALID_ID);
		mSourcePackage = intent.getStringExtra(Constants.EXTRA_SOURCE_PACKAGE);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
		return new MemoryNotificationLoader(getActivity(), mNid, mSourcePackage);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader,
			Cursor data) {
		if (data == null) {
			return;
		}
		
		if (data.moveToFirst() == false) {
			return;
		}
		
		MemoryNotification notification = new MemoryNotification(getActivity());
		notification.fillValuesFromCursor(data);
		
		Logger.debug("notification = %s", notification);
		
		updateContent(notification);
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}

	@Override
	protected int getLoaderId() {
		return CoreLoaderIds.LOADER_ID_QUESTION;
	}

	@Override
	protected Bundle createLoaderArguments() {
		return null;
	}
	
	private void setNotifyViewed(boolean viewed) {
		final Activity hostActivity = getActivity();
		if (hostActivity == null) {
			return;
		}
		
		if (viewed) {
			MemoryNotificationDatabaseModal.deleteNotify(
					hostActivity, mDatabaseId);
		}
		
		hostActivity.finish();
	}

	private void updateContent(MemoryNotification notification) {
		if (notification == null) {
			return;
		}
		
		if (mNotificationContent != null) {
			final String content = notification.dumpNotifyText();

			mNotificationContent.setText(content);
		}
		
		if (mNotificationTitle != null) {
			final String title = notification.dumpNotifyTitle();

			mNotificationTitle.setText(title);
		}
		
		final String notifyIntentString = notification.getNotifyIntent();
		if (notifyIntentString != null) {
			try {
				mNotifyIntent = Intent.getIntent(notifyIntentString);
			} catch (URISyntaxException e) {
				Logger.warnning("parse notify intent failure: %s", 
						notifyIntentString);
				
				mNotifyIntent = null;
			}
		}
	}

}
