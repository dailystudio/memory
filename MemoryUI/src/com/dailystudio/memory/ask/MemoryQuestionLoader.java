package com.dailystudio.memory.ask;

import android.content.Context;

import com.dailystudio.app.dataobject.loader.DatabaseCursorLoader;
import com.dailystudio.dataobject.DatabaseObject;
import com.dailystudio.dataobject.query.ExpressionToken;
import com.dailystudio.dataobject.query.OrderingToken;
import com.dailystudio.dataobject.query.Query;

public class MemoryQuestionLoader extends DatabaseCursorLoader {

	private int mQid;
	private String mSourcePackage;
	
	public MemoryQuestionLoader(Context context, int qid, String srcPkg) {
		super(context);
		
		mQid = qid;
		mSourcePackage = srcPkg;
	}

	@Override
	protected Class<? extends DatabaseObject> getObjectClass() {
		return MemoryQuestion.class;
	}

	@Override
	protected Query getQuery(Class<? extends DatabaseObject> klass) {
		Query query = super.getQuery(klass);
		
		ExpressionToken selToken = 
			MemoryQuestion.COLUMN_QUESTION_ID.eq(mQid)
				.and(MemoryQuestion.COLUMN_SRC_PACKAGE.eq(mSourcePackage))
				.and(MemoryQuestion.COLUMN_STATE.neq(MemoryQuestion.STATE_ANSWERED))
				.and(MemoryQuestion.COLUMN_STATE.neq(MemoryQuestion.STATE_CANCELLED));
		if (selToken != null) {
			query.setSelection(selToken);
		}
		
		OrderingToken orderByToken = 
			MemoryQuestion.COLUMN_TIME.orderByDescending();
		if (orderByToken != null) {
			query.setOrderBy(orderByToken);
		}
		
		ExpressionToken limitToken = new ExpressionToken(1);

		query.setLimit(limitToken);

		return query;
	}
	
}
