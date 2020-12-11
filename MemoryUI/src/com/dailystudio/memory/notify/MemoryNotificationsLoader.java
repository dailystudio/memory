package com.dailystudio.memory.notify;

import android.content.Context;

import com.dailystudio.app.dataobject.loader.DatabaseCursorLoader;
import com.dailystudio.dataobject.DatabaseObject;
import com.dailystudio.dataobject.query.ExpressionToken;
import com.dailystudio.dataobject.query.OrderingToken;
import com.dailystudio.dataobject.query.Query;

public class MemoryNotificationsLoader extends DatabaseCursorLoader {

	private final static int DEFAULT_NOTIFICATION_QUERY_COUNT = 10;
	
	public MemoryNotificationsLoader(Context context) {
		super(context);
	}

	@Override
	protected Class<? extends DatabaseObject> getObjectClass() {
		return MemoryNotification.class;
	}

	@Override
	protected Query getQuery(Class<? extends DatabaseObject> klass) {
		Query query = super.getQuery(klass);
		
		OrderingToken orderByToken = 
			MemoryNotification.COLUMN_TIME.orderByDescending();
		if (orderByToken != null) {
			query.setOrderBy(orderByToken);
		}
		
		ExpressionToken limitToken = 
			new ExpressionToken(DEFAULT_NOTIFICATION_QUERY_COUNT);
		query.setLimit(limitToken);

		return query;
	}
	
}
