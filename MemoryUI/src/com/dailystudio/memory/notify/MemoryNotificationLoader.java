package com.dailystudio.memory.notify;

import android.content.Context;

import com.dailystudio.app.dataobject.loader.DatabaseCursorLoader;
import com.dailystudio.dataobject.DatabaseObject;
import com.dailystudio.dataobject.query.ExpressionToken;
import com.dailystudio.dataobject.query.OrderingToken;
import com.dailystudio.dataobject.query.Query;

public class MemoryNotificationLoader extends DatabaseCursorLoader {

	private int mNid;
	private String mSourcePackage;
	
	public MemoryNotificationLoader(Context context, int nid, String srcPkg) {
		super(context);
		
		mNid = nid;
		mSourcePackage = srcPkg;
	}

	@Override
	protected Class<? extends DatabaseObject> getObjectClass() {
		return MemoryNotification.class;
	}

	@Override
	protected Query getQuery(Class<? extends DatabaseObject> klass) {
		Query query = super.getQuery(klass);
		
		ExpressionToken selToken = 
			MemoryNotification.COLUMN_NOTIFY_ID.eq(mNid)
				.and(MemoryNotification.COLUMN_SRC_PACKAGE.eq(mSourcePackage));
		if (selToken != null) {
			query.setSelection(selToken);
		}
		
		OrderingToken orderByToken = 
			MemoryNotification.COLUMN_TIME.orderByDescending();
		if (orderByToken != null) {
			query.setOrderBy(orderByToken);
		}
		
		ExpressionToken limitToken = new ExpressionToken(1);

		query.setLimit(limitToken);

		return query;
	}
	
}
