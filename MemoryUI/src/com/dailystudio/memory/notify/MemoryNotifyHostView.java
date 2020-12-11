package com.dailystudio.memory.notify;

import java.util.HashSet;
import java.util.Set;

import com.dailystudio.app.utils.ActivityLauncher;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;
import com.dailystudio.memory.ui.R;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.FrameLayout;

public class MemoryNotifyHostView extends FrameLayout {

	private final static int ROOT_ANIM_DELAY = 200;
	private final static int CHILD_ANIM_DELAY = 500;
	
	private class NotifyDataSetObserver extends DataSetObserver {
		
		@Override
		public void onChanged() {
			super.onChanged();
			
			bindViews();
		}
		
		@Override
		public void onInvalidated() {
			super.onInvalidated();
		}

	}
	
	private class NotifyChildDisplayRunnable implements Runnable {

		private int mViewIndex;
		
		private NotifyChildDisplayRunnable(int viewIndex) {
			mViewIndex = viewIndex;
		}
		
		@Override
		public void run() {
			showChild(mViewIndex);
		}
		
	}
	
	private class NotifyChildDisappearRunnable implements Runnable {

		private int mViewIndex;
		
		private NotifyChildDisappearRunnable(int viewIndex) {
			mViewIndex = viewIndex;
		}
		
		@Override
		public void run() {
			hideChild(mViewIndex);
		}
		
	}
	
	protected Context mContext;
	
	private Adapter mAdapter;

	private NotifyDataSetObserver mDataSetObserver;
	
	private ViewGroup mNotifySlotsRoot;
	
	private Set<Integer> mReservedViews;
	private SparseIntArray mActivedViews;
	
	private Animation mNotifyInAnim;
	
	public MemoryNotifyHostView(Context context) {
        this(context, null);
    }

    public MemoryNotifyHostView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MemoryNotifyHostView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
		mContext = context;
		
		initMembers();
    }

	private void initMembers() {
		LayoutInflater.from(mContext).inflate(
				R.layout.notify_host_view, this);

		mNotifySlotsRoot = (ViewGroup) findViewById(R.id.notify_slots_root);
		if (mNotifySlotsRoot != null) {
			final int nSlots = mNotifySlotsRoot.getChildCount();
			
			View child = null;
			for (int i = 0; i < nSlots; i++) {
				child = mNotifySlotsRoot.getChildAt(i);
				
				child.setTag(i);
				child.setOnClickListener(mNotifyChildOnClickListener);
			}
		}
		
		mNotifyInAnim = AnimationUtils.loadAnimation(
				mContext, R.anim.notify_view_in);
	}	
	
	public void setAdapter(Adapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        mAdapter = adapter;
        
        if (mAdapter != null) {
            mDataSetObserver = new NotifyDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);
        }

        bindViews();
	}
	
	private void bindViews() {
		int nItems = 0;
		if (mAdapter instanceof MemoryNotifyCursorAdapter) {
			nItems = mAdapter.getCount();
		}   
		
        if (nItems > 0) {
        	postDelayed(mRootDisplayRunnable, ROOT_ANIM_DELAY);
        } else {
        	setVisibility(INVISIBLE);
        }
		
		int nViews = 0;
		if (mNotifySlotsRoot != null) {
			nViews = mNotifySlotsRoot.getChildCount();
		}
			
		if (nViews <= 0) {
			return;
		}
		
		if (mActivedViews == null) {
			mActivedViews = new SparseIntArray(nViews);
		}
		
		if (mReservedViews == null) {		
			mReservedViews = new HashSet<Integer>(nViews);
		}
		
		final int nActives = mActivedViews.size();
		final int nReserves= mReservedViews.size();
		final int nAvailables = (nViews - nActives);

		Logger.debug("nItems = %d, nViews = %d",
				nItems, nViews);
		Logger.debug("nActived = %d, nReserves = %d, nAvailables = %d",
				nActives, nReserves, nAvailables);
		
		MemoryNotification notification = null;
		int i, v;
		int dbId;
		int delayUnit;
		
		Set<Integer> occupiedViews = new HashSet<Integer>();
		
		for (i = 0, delayUnit = 0; i < nItems; i++) {
			notification = 
				((MemoryNotifyCursorAdapter)mAdapter).dumpItem(i);
			if (notification == null) {
				continue;
			}
			
			dbId = notification.getId();
			
			final int viewIndex = 
				mActivedViews.indexOfValue(dbId);
			if (viewIndex >= 0) {
				occupiedViews.add(viewIndex);
				continue;
			}
			
			for (v = 0; v < nViews; v++) {
				if (mActivedViews.indexOfKey(v) >= 0
						|| mReservedViews.contains(v)) {
					occupiedViews.add(v);
					continue;
				}
				
				mActivedViews.put(v, dbId);
				
				occupiedViews.add(v);
				
				postDelayed(new NotifyChildDisplayRunnable(v),
						CHILD_ANIM_DELAY * delayUnit);

				delayUnit++;
				
				break;
			}
		}
		
		for (v = 0, delayUnit = 0; v < nViews; v++) {
			if (occupiedViews.contains(v)) {
				continue;
			}
			
			if (mActivedViews != null) {
				mActivedViews.delete(v);
			}

			postDelayed(new NotifyChildDisappearRunnable(v),
					CHILD_ANIM_DELAY * delayUnit);
		}
	}
	
	private void showChild(int viewIndex) {
		Logger.debug("childIndex = %d", viewIndex);
		if (viewIndex < 0) {
			return;
		}
		
		if (mNotifySlotsRoot == null) {
			return;
		}
		
		if (viewIndex >= mNotifySlotsRoot.getChildCount()) {
			return;
		}
		
		View childView = 
			mNotifySlotsRoot.getChildAt(viewIndex);
		
		if (childView.getVisibility() == VISIBLE) {
			return;
		}
		
		childView.setVisibility(VISIBLE);
		childView.startAnimation(AnimationUtils.loadAnimation(
				mContext, R.anim.notify_view_in));
	}

	private void hideChild(int viewIndex) {
		Logger.debug("childIndex = %d", viewIndex);
		if (viewIndex < 0) {
			return;
		}
		
		if (mNotifySlotsRoot == null) {
			return;
		}
		
		if (viewIndex >= mNotifySlotsRoot.getChildCount()) {
			return;
		}
		
		View childView = 
			mNotifySlotsRoot.getChildAt(viewIndex);
		
		if (childView.getVisibility() != VISIBLE) {
			return;
		}
		
		childView.setVisibility(INVISIBLE);
		
		if (mActivedViews != null) {
			mActivedViews.delete(viewIndex);
		}
		
		if (mReservedViews != null) {
			mReservedViews.add(viewIndex);
			
			if (mActivedViews.size() <= 0) {
				mReservedViews.clear();
				
				setVisibility(INVISIBLE);
			}
		}
	}

	private Runnable mRootDisplayRunnable = new Runnable() {
		
		@Override
		public void run() {
			if (getVisibility() == VISIBLE) {
				return;
			}
			
			setVisibility(VISIBLE);
			
			if (mNotifyInAnim != null) {
				startAnimation(mNotifyInAnim);
			}
		}
		
	};
	
	private OnClickListener mNotifyChildOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (v == null) {
				return;
			}
			
			Object o = v.getTag();
			if (o instanceof Integer == false) {
				return;
			}
			
			int indexTag = ((Integer)o).intValue();
			
			if (mActivedViews != null) {
				int dbId = mActivedViews.get(indexTag);
				
				MemoryNotification notification = 
					MemoryNotificationDatabaseModal.findNotify(mContext, dbId);
				if (notification != null) {
					Intent i = new Intent();
					
					i.setClass(mContext, MemoryNotificationActivity.class);
					i.putExtra(Constants.EXTRA_NOTIFY_DATABASE_ID,
							dbId);
					i.putExtra(Constants.EXTRA_NOTIFY_ID, 
							notification.getNotifyId());
					i.putExtra(Constants.EXTRA_SOURCE_PACKAGE, 
							notification.getSourcePackage());
					
					ActivityLauncher.launchActivity(mContext, i);
				}
				
			}
			
			hideChild(indexTag);
		}
		
	};
	
}
