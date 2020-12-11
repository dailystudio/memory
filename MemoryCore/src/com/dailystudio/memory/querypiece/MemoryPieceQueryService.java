package com.dailystudio.memory.querypiece;

import java.util.List;

import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

public abstract class MemoryPieceQueryService extends IntentService {

	public MemoryPieceQueryService(String name) {
		super(name);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		Logger.debug("intent = %s", intent);
		if (intent == null) {
			return;
		}
		
		final Context context = getApplicationContext();
		
		final String action = intent.getAction();
		if (Constants.ACTION_QUERY_MEMORY_PIECE.equals(action) == false) {
			return;
		}
		
		final String queryType = intent.getStringExtra(
				Constants.EXTRA_QUERY_CONTENT_TYPE);
		final String queryArgs = intent.getStringExtra(
				Constants.EXTRA_QUERY_CONTENT_ARGS);
		final long querySerial = intent.getLongExtra(
				Constants.EXTRA_QUERY_SERIAL, -1);
		Logger.debug("[SERIAL-%d]: queryType = %s, queryArgs = %s",
				querySerial, queryType, queryArgs);
		if (querySerial < 0) {
			return;
		}
		
		Bundle resultExtras = new Bundle();

		if (Constants.QUERY_CONTENT_TYPE_COUNT.equals(queryType)) {
			attachMemoryCount(context, queryArgs, resultExtras);
		} else if (Constants.QUERY_CONTENT_TYPE_DIGEST.equals(queryType)) {
			attachMemoryDigests(context, queryArgs, resultExtras);
		} else if (Constants.QUERY_CONTENT_TYPE_CARD.equals(queryType)) {
			attachMemoryCards(context, queryArgs, resultExtras);
		}
		
		Logger.debug("[SERIAL-%d]: queryType = %s, queryArgs = %s, newResultExtras = %s",
				querySerial, queryType, queryArgs, resultExtras);
		
		Intent retIntent = new Intent(Constants.ACTION_PROVIDE_MEMORY_PIECE);
		
		retIntent.putExtras(resultExtras);
		retIntent.putExtra(Constants.EXTRA_QUERY_SOURCE, 
				getSource());
		retIntent.putExtra(Constants.EXTRA_QUERY_SERIAL, 
				querySerial);
		retIntent.putExtra(Constants.EXTRA_QUERY_CONTENT_TYPE, 
				queryType);
		
		sendBroadcast(retIntent);
	}
	
	private String getSource() {
		final ComponentName mySelf = 
				new ComponentName(getApplicationContext(),
						this.getClass());
		
		return mySelf.flattenToShortString();
	}
	
	private void attachMemoryCount(Context context, String queryArgs, Bundle resultExtras) {
		if (resultExtras == null) {
			return;
		}
		
		final long prevCount = resultExtras.getLong(
				Constants.EXTRA_QUERY_COUNT, 0l);
		
		final long currCount = onQueryMemoryPiceceCount(context, queryArgs);
		final long newCount = prevCount + currCount;
		Logger.debug("newCount(%d) = prevCount(%d) + currCount(%d)",
				newCount,
				prevCount,
				currCount);
		
		resultExtras.putLong(Constants.EXTRA_QUERY_COUNT, newCount);
	}
	
	private void attachMemoryDigests(Context context, String queryArgs, Bundle resultExtras) {
		if (resultExtras == null) {
			return;
		}
		
		try {
			Parcelable[] digests = resultExtras.getParcelableArray(
					Constants.EXTRA_QUERY_DIGEST);
			
			List<MemoryPieceDigest> newDigests =
					onQueryMemoryPiceceDigests(context, queryArgs);
			Logger.debug("newDigests = %s", newDigests);
			
			if (newDigests != null) {
				if (digests != null) {
					for (Parcelable p: digests) {
						if (p instanceof MemoryPieceDigest) {
							newDigests.add((MemoryPieceDigest)p);
						}
					}
				}
				
				if (newDigests.size() > 0) {
					resultExtras.putParcelableArray(
							Constants.EXTRA_QUERY_DIGEST, 
							newDigests.toArray(new MemoryPieceDigest[0]));
				}
			}
		} catch (Exception e) {
			Logger.warnning("attach digestg to result failure: %s",
					e.toString());
		}
	}
	
	private void attachMemoryCards(Context context, String queryArgs, Bundle resultExtras) {
		if (resultExtras == null) {
			return;
		}
		
		try {
			Parcelable[] cards = resultExtras.getParcelableArray(
					Constants.EXTRA_QUERY_CARD);
			
			List<MemoryPieceCard> newCards =
					onQueryMemoryPiceceCards(context, queryArgs);
			Logger.debug("newCards = %s", newCards);
			
			if (newCards != null) {
				if (cards != null) {
					for (Parcelable p: cards) {
						if (p instanceof MemoryPieceCard) {
							newCards.add((MemoryPieceCard)p);
						}
					}
				}
				
				if (newCards.size() > 0) {
					resultExtras.putParcelableArray(
							Constants.EXTRA_QUERY_CARD, 
							newCards.toArray(new MemoryPieceCard[0]));
				}
			}
		} catch (Exception e) {
			Logger.warnning("attach cards to result failure: %s",
					e.toString());
		}
	}
	
	protected abstract long onQueryMemoryPiceceCount(Context context, String queryArgs);
	protected abstract List<MemoryPieceDigest> onQueryMemoryPiceceDigests(Context context, String queryArgs);
	protected abstract List<MemoryPieceCard> onQueryMemoryPiceceCards(Context context, String queryArgs);

}
