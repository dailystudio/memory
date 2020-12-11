package com.dailystudio.memory.fragment;

import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Process;
import android.support.v4.content.Loader;

import com.dailystudio.development.Logger;
import com.dailystudio.memory.activity.ActionBarActivity;
import com.dailystudio.memory.fragment.MemoryPeroidObjectsListFragment;
import com.dailystudio.nativelib.application.IResourceObject;
import com.dailystudio.nativelib.application.IResourceObject.ResourceObjectComparator;

public abstract class AbsResObjectListFragment<T extends IResourceObject>
	extends MemoryPeroidObjectsListFragment<T> {

	private final static int DEFAULT_BATCH_SIZE = 4;
	
	private class ResolveResourcesAsyncTask extends AsyncTask<List<T>, Void, Integer> {

		private volatile boolean mEndFlags = false;
		
		@Override
		protected Integer doInBackground(List<T>... params) {
			if (params == null || params.length <= 0) {
				return 0;
			}
			
			final Context context = getActivity();
			if (context == null) {
				return 0;
			}
			
			final PackageManager pkgmgr = context.getPackageManager();
			if (pkgmgr == null) {
				return 0;
			}
			
			final List<T> objects = params[0];
			if (objects == null) {
				return 0;
			}
			
			final int N = objects.size();
			if (N <= 0) {
				return N;
			}
			
	        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

	        T object;
	        int i, resolvedCount = 0;
			for (i = 0; i < N && !mEndFlags; i++) {
				object = objects.get(i);
				if (object == null){
					continue;
				}
				
				object.resolveResources(context);
	        	
				resolvedCount++;
	        	
	        	if (mNotifyBatchSize > 0 && resolvedCount >= mNotifyBatchSize) {
	        		Logger.debug("resolved (%d/%d)", i, N);

	        		updateProgress(i, N);
	        		notifyAdapterChangedOnIdle();
	        		
	        		resolvedCount = 0;
	        	}
			}
			
			if (!mEndFlags) {
				updateProgress(N, N);
			}
			
			notifyAdapterChangedOnIdle();
			
			sortAdapterIfPossible();
			
			return N;
		}
		
		private void updateProgress(int resolved, int total) {
			if (isResolveProgressEnabled() == false) {
				return;
			}
			
			final Activity activity = getActivity();
			
			if (activity instanceof ActionBarActivity == false) {
				return;
			}
			
			int startProgress = getResolveStartProgress();
			if (startProgress > 100) {
				startProgress = 100;
			}
			
			int progress = startProgress + resolved * (100 - startProgress) / total;
			
			((ActionBarActivity)activity).setActionBarProgress(progress);
			
			if (progress >= 100) {
				((ActionBarActivity)activity).hidePrompt();
			}
		}
		
		private void resetProgress() {
			if (isResolveProgressEnabled() == false) {
				return;
			}
			
			final Activity activity = getActivity();
			
			if (activity instanceof ActionBarActivity == false) {
				return;
			}
			
			((ActionBarActivity)activity).clearActionBarProgress();
			((ActionBarActivity)activity).hidePrompt();
		}
		
		public void cancel() {
			Logger.debug("CANCELLING task: %s", this);

			mEndFlags = true;
			
			cancel(true);
			
			resetProgress();
		}
		
	}

	private ResolveResourcesAsyncTask mResolveAsyncTask = null;
	
	private int mNotifyBatchSize = DEFAULT_BATCH_SIZE;
	
	private boolean mResolveProgressEnabled = true;
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if (mResolveAsyncTask == null) {
			return;
		}
		
		mResolveAsyncTask.cancel();
		
		mResolveAsyncTask = null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoadFinished(Loader<List<T>> loader, List<T> data) {
		super.onLoadFinished(loader, data);
		
		if (mResolveAsyncTask != null) {
			AsyncTask.Status status = mResolveAsyncTask.getStatus();
			if (status != AsyncTask.Status.FINISHED) {
				mResolveAsyncTask.cancel();
			}
		}
		
		mResolveAsyncTask = new ResolveResourcesAsyncTask();
		if (mResolveAsyncTask == null) {
			return;
		}
		
		mResolveAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
	}
	
	public synchronized void setResolveProgressEnabled(boolean enabled) {
		mResolveProgressEnabled = enabled;
	}
	
	public synchronized boolean isResolveProgressEnabled() {
		return mResolveProgressEnabled;
	}
	
	protected void setNotifyBatchSize(int batchSize) {
		mNotifyBatchSize = batchSize;
	}
	
	protected int getResolveStartProgress() {
		return 0;
	}
	
	protected int getNotifyBatchSize() {
		return mNotifyBatchSize;
	}
	
	@Override
	protected Comparator<T> getComparator() {
	    return sDefaultResComparator;
	}
	
	private Comparator<T> sDefaultResComparator = new ResourceObjectComparator<T>();
	
}
