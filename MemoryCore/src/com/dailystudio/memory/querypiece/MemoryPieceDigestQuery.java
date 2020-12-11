package com.dailystudio.memory.querypiece;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;

public abstract class MemoryPieceDigestQuery<Results> extends AbsMemoryPieceQuery<MemoryPieceDigest>{

	public MemoryPieceDigestQuery() {
		super(Constants.QUERY_CONTENT_TYPE_DIGEST);
	}
	
	public Results queryDigests(Context context) {
		return queryDigests(context, null);
	}
	
	public Results queryDigests(Context context, String queryArgs) {
		List<MemoryPieceDigest> pieces =
				super.doQuery(context, queryArgs);
		
		return parseResults(context, pieces);
	}
	
	@Override
	protected MemoryPieceDigest[] dumpPiecesFromExtra(Context context, 
			Bundle extras) {
		final String type = extras.getString(
				Constants.EXTRA_QUERY_CONTENT_TYPE);
		Logger.debug("type = %s", type);
		
		MemoryPieceDigest[] digests = null;
		if (Constants.QUERY_CONTENT_TYPE_DIGEST.equals(type)) {
			Parcelable[] data = extras.getParcelableArray(
					Constants.EXTRA_QUERY_DIGEST);
				
			digests = convertToDigests(data);
		}
		
		return digests;
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

	abstract protected Results parseResults(Context context, 
			List<MemoryPieceDigest> pieces);

};
