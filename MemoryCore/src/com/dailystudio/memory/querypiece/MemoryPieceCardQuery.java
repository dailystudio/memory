package com.dailystudio.memory.querypiece;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;

public abstract class MemoryPieceCardQuery<Results> extends AbsMemoryPieceQuery<MemoryPieceCard>{

	public MemoryPieceCardQuery() {
		super(Constants.QUERY_CONTENT_TYPE_CARD);
	}
	
	public Results queryCards(Context context) {
		return queryCards(context, null);
	}
	
	public Results queryCards(Context context, String queryArgs) {
		List<MemoryPieceCard> pieces =
				super.doQuery(context, queryArgs);
		
		return parseResults(context, pieces);
	}
	
	@Override
	protected MemoryPieceCard[] dumpPiecesFromExtra(Context context, 
			Bundle extras) {
		final String type = extras.getString(
				Constants.EXTRA_QUERY_CONTENT_TYPE);
		Logger.debug("type = %s", type);
		
		MemoryPieceCard[] cards = null;
		if (Constants.QUERY_CONTENT_TYPE_CARD.equals(type)) {
			Parcelable[] data = extras.getParcelableArray(
					Constants.EXTRA_QUERY_CARD);
				
			cards = convertToCards(data);
		}
		
		return cards;
	}

	private MemoryPieceCard[] convertToCards(Parcelable[] data) {
		if (data == null || data.length < 0) {
			return null;
		}
		
		List<MemoryPieceCard> digests = 
				new ArrayList<MemoryPieceCard>();
		
		for (Parcelable p: data) {
			if (p instanceof MemoryPieceCard) {
				digests.add((MemoryPieceCard)p);
			}
		}
		
		return digests.toArray(new MemoryPieceCard[0]);
	}

	abstract protected Results parseResults(Context context, 
			List<MemoryPieceCard> pieces);

};
