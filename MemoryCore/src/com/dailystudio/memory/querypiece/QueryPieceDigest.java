package com.dailystudio.memory.querypiece;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;

public abstract class QueryPieceDigest<T> {
	
	private class QueryPieceResultBroadcastReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			Logger.debug("intent = %s", intent);
			if (intent == null) {
				releaseLock();
				return;
			}
			
			final String action = intent.getAction();
			if (Constants.ACTION_QUERY_MEMORY_PIECE.equals(action)) {
				final String type = intent.getStringExtra(
						Constants.EXTRA_QUERY_CONTENT_TYPE);
				Logger.debug("type = %s", type);
				if (Constants.QUERY_CONTENT_TYPE_DIGEST.equals(type)) {
					Bundle resultExtras = getResultExtras(true);
					Logger.debug("resultExtras = %s", resultExtras);

					if (resultExtras != null) {
						Parcelable[] data = 
								resultExtras.getParcelableArray(
										Constants.EXTRA_QUERY_DIGEST);
						
						MemoryPieceDigest[] digests = 
								convertToDigests(data);
						
						mResultData = parseResult(context, digests); 
					}
				}
			}
			
			releaseLock();
		}
		
		private MemoryPieceDigest[] convertToDigests(Parcelable[] data) {
			if (data == null || data.length < 0) {
				return null;
			}
			
			List<MemoryPieceDigest> digests = 
					new ArrayList<MemoryPieceDigest>();
			
			for (Parcelable p: data) {
				if (p instanceof MemoryPieceDigest) {
					digests.add((MemoryPieceDigest)p);
				}
			}
			
			
			return digests.toArray(new MemoryPieceDigest[0]);
		}
		
		private void releaseLock() {
			synchronized (this) {
				notifyAll();
			}
		}
		
	};

	private T mResultData;
	
	public T doQuery(Context context) {
		return doQuery(context, null);
	}
	
	public T doQuery(Context context, String queryArgs) {
		Intent i = new Intent(Constants.ACTION_QUERY_MEMORY_PIECE);
		
		i.putExtra(Constants.EXTRA_QUERY_CONTENT_TYPE, 
				Constants.QUERY_CONTENT_TYPE_DIGEST);
		
		if (queryArgs != null) {
			i.putExtra(Constants.EXTRA_QUERY_CONTENT_ARGS, 
					queryArgs);
		}
		
		QueryPieceResultBroadcastReceiver resultReceiver = 
			new QueryPieceResultBroadcastReceiver();
		
		synchronized (resultReceiver) {
			context.sendOrderedBroadcast(i, null, resultReceiver, 
					null, Activity.RESULT_OK, null, null);
			
			try {
				Logger.debug("WAIT() for result");
				resultReceiver.wait();
			} catch (InterruptedException e) {
				Logger.warnning("WAIT() release: %s",
						e.toString());
			}
		}
		
		Logger.debug("result data = %s", mResultData);
		
		return mResultData;
	}

	abstract protected T parseResult(Context context, MemoryPieceDigest[] digests);
	

}
