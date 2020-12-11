package com.dailystudio.memory.querypiece;

import java.util.ArrayList;
import java.util.List;

import com.dailystudio.app.utils.ArrayUtils;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

public abstract class MemoryPieceQueryReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Logger.debug("intent = %s", intent);
		if (intent == null) {
			return;
		}
		
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
		
		Bundle resultExtras = getResultExtras(true);
		if (querySerial < 0) {
			return;
		}
		
		Logger.debug("[SERIAL-%d]: queryType = %s, queryArgs = %s, resultExtras = %s",
				querySerial, queryType, queryArgs, resultExtras);
		
		
		boolean supportQuery = false;
		if (Constants.QUERY_CONTENT_TYPE_COUNT.equals(queryType)) {
			supportQuery = supportCountPiece(context);
		} else if (Constants.QUERY_CONTENT_TYPE_DIGEST.equals(queryType)) {
			supportQuery = supportDigestPiece(context);
		} else if (Constants.QUERY_CONTENT_TYPE_CARD.equals(queryType)) {
			supportQuery = supportCardPiece(context);
		}

		final ComponentName queryService = 
				getQueryService(context);
		
		if (supportQuery && queryService != null) {
			addQueryPieceCandidate(resultExtras, 
					queryService.flattenToShortString());
		} 
	}
	
	private void addQueryPieceCandidate(Bundle resultExtras, String querySource) {
		if (resultExtras == null
				|| TextUtils.isEmpty(querySource)) {
			return;
		}
		
		String[] existedCandidates = resultExtras.getStringArray(
				Constants.EXTRA_QUERY_CANDIDATES);
		Logger.debug("existedCandidates = %s",
				ArrayUtils.arrayToString(existedCandidates));

		List<String> newCandidates = new ArrayList<String>();
		if (existedCandidates != null) {
			for (String c: existedCandidates) {
				newCandidates.add(c);
			}
		}
		
		newCandidates.add(querySource);
		
		Logger.debug("newCandidates = %s",
				ArrayUtils.arrayToString(existedCandidates));
		
		resultExtras.putStringArray(
				Constants.EXTRA_QUERY_CANDIDATES, 
				newCandidates.toArray(new String[0]));
	}
	
	protected abstract boolean supportCountPiece(Context context);
	protected abstract boolean supportDigestPiece(Context context);
	protected abstract boolean supportCardPiece(Context context);
	protected abstract ComponentName getQueryService(Context context);

}
