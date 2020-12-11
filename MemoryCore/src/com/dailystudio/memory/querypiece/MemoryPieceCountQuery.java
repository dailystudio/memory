package com.dailystudio.memory.querypiece;

import java.util.List;

import android.content.Context;
import android.os.Bundle;

import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;

public abstract class MemoryPieceCountQuery<Results> extends AbsMemoryPieceQuery<Long>{

	public MemoryPieceCountQuery() {
		super(Constants.QUERY_CONTENT_TYPE_COUNT);
	}
	
	public Results queryCounts(Context context) {
		return queryCounts(context, null);
	}
	
	public Results queryCounts(Context context, String queryArgs) {
		List<Long> pieces =
				super.doQuery(context, queryArgs);
		
		return parseResults(context, pieces);
	}
	
	@Override
	protected Long[] dumpPiecesFromExtra(Context context, 
			Bundle extras) {
		final String type = extras.getString(
				Constants.EXTRA_QUERY_CONTENT_TYPE);
		Logger.debug("type = %s", type);
		
		long count = 0l;
		if (Constants.QUERY_CONTENT_TYPE_COUNT.equals(type)) {
			count = extras.getLong(
					Constants.EXTRA_QUERY_COUNT, 0);
		}
		
		return new Long[] { count };
	}

	abstract protected Results parseResults(Context context, 
			List<Long> pieces);

};
