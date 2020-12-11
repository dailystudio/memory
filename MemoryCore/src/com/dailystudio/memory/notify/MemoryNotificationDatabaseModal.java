package com.dailystudio.memory.notify;

import com.dailystudio.app.dataobject.DatabaseWriter;
import com.dailystudio.dataobject.query.ExpressionToken;
import com.dailystudio.dataobject.query.Query;
import com.dailystudio.datetime.dataobject.TimeCapsuleDatabaseReader;
import com.dailystudio.datetime.dataobject.TimeCapsuleDatabaseWriter;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;

import android.content.Context;

public class MemoryNotificationDatabaseModal {
	
	public static void addNotify(Context context, int nid, String srcPkg, 
			String titleText, String cntText) {
		addNotify(context, nid, srcPkg, 
				Constants.INVALID_RESOURCE_ID, titleText,
				Constants.INVALID_RESOURCE_ID, cntText, 
				null);
	}
	
	public static void addNotify(Context context, int nid, String srcPkg, 
			int titleResId, int cntResId) {
		addNotify(context, nid, srcPkg,
				titleResId, null, 
				cntResId, null,
				null);
	}
	
	public static void addNotify(Context context, 
			int nid, String srcPkg, 
			int titleResId, String titleText,
			int cntResId, String cntText,
			String launchIntent) {
		if (context == null 
				|| nid == Constants.INVALID_ID
				|| srcPkg == null 
				|| (titleResId == Constants.INVALID_RESOURCE_ID && titleText == null)
				|| (cntResId == Constants.INVALID_RESOURCE_ID && cntText == null)) {
			Logger.warnning("invalid notify params: nid(%d), srcPkg(%s), titleResId(%d), titleText(%s), cntResId(%d), cntText(%s)", 
					nid, srcPkg, 
					titleResId, titleText,
					cntResId, cntText);
			return;
		}
		
		final long now = System.currentTimeMillis();
		
		MemoryNotification notification = new MemoryNotification(context);
		
		notification.setTime(now);
		notification.setNotifyId(nid);
		notification.setSourcePackage(srcPkg);
		
		if (titleResId != Constants.INVALID_RESOURCE_ID) {
			notification.setTitleResource(titleResId);
		}
		
		if (titleText != null) {
			notification.setTitleText(titleText);
		}
		
		if (cntResId != Constants.INVALID_RESOURCE_ID) {
			notification.setContentResource(cntResId);
		}
		
		if (cntText != null) {
			notification.setContentText(cntText);
		}
		
		notification.setNotifyIntent(launchIntent);
		
		Logger.debug("notification = %s", notification);
		
		DatabaseWriter<MemoryNotification> writer = 
			new DatabaseWriter<MemoryNotification>(context,
					Constants.MEMORY_DATABASE_AUTHORITY,
					MemoryNotification.class);
		
		writer.insert(notification);
	}
	
	public static void updateNotify(Context context, int id, 
			int nid, String srcPkg, 
			int titleResId, String titleText,
			int cntResId, String cntText,
			String notifyIntent) {
		if (context == null 
				|| id == Constants.INVALID_ID) {
			Logger.warnning("invalid notification params: id(%d)", id);
			return;
		}

		MemoryNotification notification = new MemoryNotification(context);

		notification.setId(id);
		
		updateNotify(context, notification, 
				nid, srcPkg, 
				titleResId, titleText,
				cntResId, cntText, 
				notifyIntent);
	}
	
	public static void updateNotify(Context context, 
			MemoryNotification notification, 
			int nid, String srcPkg, 
			int titleResId, String titleText,
			int cntResId, String cntText,
			String notifyIntent) {
		if (context == null 
				|| notification == null
				|| nid == Constants.INVALID_ID
				|| srcPkg == null 
				|| (titleResId == Constants.INVALID_RESOURCE_ID && titleText == null)
				|| (cntResId == Constants.INVALID_RESOURCE_ID && cntText == null)) {
			Logger.warnning("invalid notification params: notification(%s), nid(%d), srcPkg(%s), titleResId(%d), titleText(%s), cntResId(%d), cntText(%s)", 
					notification,
					nid, srcPkg, 
					titleResId, titleText,
					cntResId, cntText);
			return;
		}
		
		final long now = System.currentTimeMillis();
		
		notification.setTime(now);
		notification.setNotifyId(nid);
		notification.setSourcePackage(srcPkg);
		
		if (titleResId != Constants.INVALID_RESOURCE_ID) {
			notification.setTitleResource(titleResId);
		}
		
		if (titleText != null) {
			notification.setTitleText(titleText);
		}
		
		if (cntResId != Constants.INVALID_RESOURCE_ID) {
			notification.setContentResource(cntResId);
		}
		
		if (cntText != null) {
			notification.setContentText(cntText);
		}
		
		notification.setNotifyIntent(notifyIntent);
		
		Logger.debug("notification = %s", notification);
		
		TimeCapsuleDatabaseWriter<MemoryNotification> writer =
			new TimeCapsuleDatabaseWriter<MemoryNotification>(context,
					Constants.MEMORY_DATABASE_AUTHORITY,
					MemoryNotification.class);
		
		writer.update(notification);
	}

	public static MemoryNotification findNotify(Context context, int nid, String srcPkg) {
		if (context == null 
				|| nid == Constants.INVALID_ID
				|| srcPkg == null) {
			return null;
		}
		
		TimeCapsuleDatabaseReader<MemoryNotification> reader =
			new TimeCapsuleDatabaseReader<MemoryNotification>(context,
					Constants.MEMORY_DATABASE_AUTHORITY,
					MemoryNotification.class);
		
		Query query = new Query(MemoryNotification.class);
		
		ExpressionToken selToken = 
			MemoryNotification.COLUMN_NOTIFY_ID.eq(nid)
				.and(MemoryNotification.COLUMN_SRC_PACKAGE.eq(srcPkg));
		if (selToken == null) {
			return null;
		}
		
		query.setSelection(selToken);
		
		return reader.queryLastOne(query);
	}
	
	public static MemoryNotification findNotify(Context context, int databaseId) {
		if (context == null 
				|| databaseId == Constants.INVALID_ID) {
			return null;
		}

		TimeCapsuleDatabaseReader<MemoryNotification> reader =
			new TimeCapsuleDatabaseReader<MemoryNotification>(context,
					Constants.MEMORY_DATABASE_AUTHORITY,
					MemoryNotification.class);
		
		Query query = new Query(MemoryNotification.class);
		
		ExpressionToken selToken = 
			MemoryNotification.COLUMN_ID.eq(databaseId);
		if (selToken == null) {
			return null;
		}
		
		query.setSelection(selToken);
		
		return reader.queryLastOne(query);
	}
	
	public static void deleteNotify(Context context, 
			MemoryNotification notification) {
		if (context == null 
				|| notification == null) {
			Logger.warnning("invalid notification params: question(%s)", 
					notification);
			return;
		}
		
		final int id = notification.getId();
		if (id <= 0) {
			Logger.warnning("invalid notification id: id(%d)", 
					id);
			return;
		}

		TimeCapsuleDatabaseWriter<MemoryNotification> writer =
			new TimeCapsuleDatabaseWriter<MemoryNotification>(context,
					Constants.MEMORY_DATABASE_AUTHORITY,
					MemoryNotification.class);
		
		writer.delete(notification);
	}

	public static void deleteNotify(Context context, 
			int notifyDBId) {
		if (context == null 
				|| notifyDBId <= 0) {
			Logger.warnning("invalid notification params: notifyDBId(%d)", 
					notifyDBId);
			return;
		}
		
		Logger.debug("DELETE notifyDBId = %d", notifyDBId);

		TimeCapsuleDatabaseWriter<MemoryNotification> writer =
			new TimeCapsuleDatabaseWriter<MemoryNotification>(context,
					Constants.MEMORY_DATABASE_AUTHORITY,
					MemoryNotification.class);
		
		Query query = new Query(MemoryNotification.class);
		
		ExpressionToken selToken = 
			MemoryNotification.COLUMN_ID.eq(notifyDBId);
		if (selToken == null) {
			return;
		}
		
		query.setSelection(selToken);
		
		writer.delete(query);
	}

}
