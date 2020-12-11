package com.dailystudio.memory.querypiece;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;

import com.dailystudio.app.utils.ArrayUtils;
import com.dailystudio.app.utils.FileUtils;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;
import com.dailystudio.memory.core.BuildConfig;
import com.google.gson.Gson;

abstract class AbsMemoryPieceQuery<Piece> {
	
	private final static String SKIP_FILE = "mpieces_skip";
	
	private static class CandidatesSkip {
		String[] skipped_candidates;
		
		@Override
		public String toString() {
			return String.format("%s(0x%08x): skips = [%s]",
					getClass().getSimpleName(),
					hashCode(),
					ArrayUtils.arrayToString(skipped_candidates));
		}
	}
	
	private class QueryPieceCandidatesReceiver extends BroadcastReceiver {
		
		private Set<String> mQueriedCandidates = 
				new HashSet<String>();
		
		private long mSerial;
		
		public QueryPieceCandidatesReceiver(long serial) {
			mSerial = serial;
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null) {
				releaseLock();
				return;
			}
			
			final long serial = intent.getLongExtra(
					Constants.EXTRA_QUERY_SERIAL, -1);
			Logger.debug("serial = %d [mSerial: %d]", 
					serial, mSerial);
			
			if (serial != mSerial) {
				return;
			}
			
			final String action = intent.getAction();
			if (Constants.ACTION_QUERY_MEMORY_PIECE.equals(action)) {
				Bundle resultExtras = getResultExtras(true);
				
				String[] candidates = resultExtras.getStringArray(
								Constants.EXTRA_QUERY_CANDIDATES);
				if (candidates != null && candidates.length > 0) {
					for (String c: candidates) {
						if (!skipIt(c)) {
							Logger.debug("[SERIAL-%d]: STAGE ONE-RECV, add candidate = %s",
									serial, c);
							mQueriedCandidates.add(c);
						}
					}
				}
			}
			
			releaseLock();
		}
		
		private boolean skipIt(String candidate) {
			if (TextUtils.isEmpty(candidate)) {
				return true;
			}
			
			if (mSkippedCandidates == null
					|| mSkippedCandidates.size() <= 0) {
				return false;
			}
			
			ComponentName comp = 
					ComponentName.unflattenFromString(candidate);
			
			return mSkippedCandidates.contains(comp.getPackageName());
		}
		
		private void releaseLock() {
			synchronized (this) {
				notifyAll();
			}
		}
		
		private Set<String> getCandidates() {
			return mQueriedCandidates;
		}
		
	};
	
	private class QueryPiecesReceiver extends BroadcastReceiver {
		
		private long mSerial;
		private Set<String> mPendingCandidates;
		private List<Piece> mQueriedPieces;
		
		public QueryPiecesReceiver(long serial, Set<String> candidates) {
			mSerial = serial;
			
			mPendingCandidates = new HashSet<String>(candidates);
			
			mQueriedPieces = new ArrayList<Piece>();
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null) {
				return;
			}
			
			String source = intent.getStringExtra(
						Constants.EXTRA_QUERY_SOURCE);
			Logger.debug("source = %s", source);

			final String action = intent.getAction();
			if (Constants.ACTION_PROVIDE_MEMORY_PIECE.equals(action)) {
				final long serial = intent.getLongExtra(
						Constants.EXTRA_QUERY_SERIAL, -1);
				Logger.debug("serial = %d [mSerial: %d]", 
						serial, mSerial);
				if (serial != mSerial) {
					return;
				}
				
				Bundle resultExtras = intent.getExtras();
				Logger.debug("resultExtras = %s", resultExtras);
					
				final Piece[] pieces = dumpPiecesFromExtra(context, resultExtras);
				if (pieces != null) {
					for (Piece p: pieces) {
						Logger.debug("[SERIAL-%d]: STAGE TWO-RECV, add piece = %s",
								serial, p);
						mQueriedPieces.add(p);
					}
				}
			}
			
			checkOrReleaseLock(source);
		}
		
		private void checkOrReleaseLock(String source) {
			if (mPendingCandidates == null) {
				releaseLock();
				
				return;
			}
			
			if (mPendingCandidates.contains(source)) {
				mPendingCandidates.remove(source);
			}
			
			final int remained = mPendingCandidates.size();
			
			Logger.debug("check source = [%s], %d remained", 
					source, remained);
			if (remained <= 0) {
				releaseLock();
			}
		}

		private void releaseLock() {
			synchronized (this) {
				notifyAll();
			}
		}
		
		private List<Piece> getPieces() {
			return mQueriedPieces;
		}
		
	};

	private String mQueryType;
	
	private Set<String> mSkippedCandidates = 
			new HashSet<String>();
	
	AbsMemoryPieceQuery(String queryType) {
		mQueryType = queryType;
	}
	
	List<Piece> doQuery(Context context, String queryArgs) {
		if (context == null 
				|| TextUtils.isEmpty(mQueryType)) {
			return null;
		}
		
		/*
		 * XXX: System.currentTimeMillis() will get same value 
		 * 		which will cause the serial duplicated. Use unique
		 * 		id generator to avoid this happen.
		 */
//		final long serial = System.currentTimeMillis();
		final long serial = MemoryPieceQueryId.generateQueryId();
		
		final Set<String> candidates = 
				queryCandidates(context, queryArgs, serial);
		Logger.debug("[SERIAL-%d]: STAGE ONE, candidates = %s",
				serial, candidates);
		if (candidates == null || candidates.size() <= 0) {
			return null;
		}

		final List<Piece> pieces = queryPieces(context, serial, queryArgs, 
				candidates);
		Logger.debug("[SERIAL-%d]: STAGE TWO, pieces = %s", 
				serial, pieces);
		
		return pieces;
	}
	
	private Set<String> queryCandidates(Context context, 
			String queryArgs, long serial) {
		buildSkipCandidates();
		
		Intent i = new Intent(Constants.ACTION_QUERY_MEMORY_PIECE);
		
		i.putExtra(Constants.EXTRA_QUERY_CONTENT_TYPE, 
				mQueryType);
		i.putExtra(Constants.EXTRA_QUERY_SERIAL, 
				serial);
	
		if (queryArgs != null) {
			i.putExtra(Constants.EXTRA_QUERY_CONTENT_ARGS, 
					queryArgs);
		}
		
		QueryPieceCandidatesReceiver candidatesReceiver = 
			new QueryPieceCandidatesReceiver(serial);
		
		synchronized (candidatesReceiver) {
			context.sendOrderedBroadcast(i, null, candidatesReceiver, 
					null, Activity.RESULT_OK, null, null);
			
			try {
				Logger.debug("[SERIAL-%d]: WAIT() for candidates", serial);
				candidatesReceiver.wait();
			} catch (InterruptedException e) {
				Logger.warnning("[SERIAL-%d]: WAIT() candidates interrupted: %s",
						serial,
						e.toString());
			}
			
			Logger.debug("[SERIAL-%d]: WAIT() candidates released", serial);
		}
		
		return candidatesReceiver.getCandidates();
	}
	
	private List<Piece> queryPieces(Context context, long serial,
			String queryArgs,
			Set<String> candidates) {
		QueryPiecesReceiver piecesReceiver = 
			new QueryPiecesReceiver(serial, candidates);
		
		final Context appContext = context.getApplicationContext();
		
		synchronized (piecesReceiver) {
			try {
				IntentFilter filter = new IntentFilter(
						Constants.ACTION_PROVIDE_MEMORY_PIECE);
				
				appContext.registerReceiver(piecesReceiver, filter);
				
				requestProvidePieces(context, serial, queryArgs, candidates);
				
				Logger.debug("[SERIAL-%d]: WAIT() for pieces", serial);
				piecesReceiver.wait();
			} catch (InterruptedException e) {
				Logger.warnning("[SERIAL-%d]: WAIT() pieces interrupted: %s",
						serial,
						e.toString());
			}
			
			Logger.debug("[SERIAL-%d]: WAIT() pieces released", serial);
			
			appContext.unregisterReceiver(piecesReceiver);
		}
		
		return piecesReceiver.getPieces();
	}

	private void requestProvidePieces(Context context,
			long serial, String queryArgs,
			Set<String> candidates) {
		if (context == null || candidates == null) {
			return;
		}
		
		Intent i = null;
		
		for(String c: candidates) {
			i = new Intent(Constants.ACTION_QUERY_MEMORY_PIECE);
		
			i.putExtra(Constants.EXTRA_QUERY_CONTENT_TYPE, 
					mQueryType);
			i.putExtra(Constants.EXTRA_QUERY_SERIAL, 
					serial);

			if (queryArgs != null) {
				i.putExtra(Constants.EXTRA_QUERY_CONTENT_ARGS, 
						queryArgs);
			}
		
			i.putExtra(Constants.EXTRA_QUERY_SERIAL, serial);
			i.setComponent(ComponentName.unflattenFromString(c));
		
			context.startService(i);
		}
	}
	
	private void buildSkipCandidates() {
		mSkippedCandidates.clear();
		
		if (!BuildConfig.DEBUG) {
			return;
		}
		
		File sdcard = Environment.getExternalStorageDirectory();
		if (sdcard == null) {
			Logger.debug("sdcard is not available");
			return;
		}
		
		File skipFile = new File(sdcard, SKIP_FILE);
		if (!FileUtils.isFileExisted(skipFile.getAbsolutePath())) {
			Logger.debug("skip is not existed on %s", skipFile);
			
			return;
		}
		
		String json = null;
		try {
			json = FileUtils.getFileContent(skipFile.getAbsolutePath());
		} catch (IOException e) {
			Logger.debug("could not parse skip candidates from [%s]: %s",
					skipFile.getAbsoluteFile(),
					e.toString());
			json = null;
		}
		
		Gson gson = new Gson();
		
		CandidatesSkip skips = 
				gson.fromJson(json, CandidatesSkip.class);
		if (skips == null || skips.skipped_candidates == null) {
			return;
		}
		
		for (String s: skips.skipped_candidates) {
			mSkippedCandidates.add(s);
		}
	}
	
	abstract Piece[] dumpPiecesFromExtra(Context context, Bundle extras);

}
