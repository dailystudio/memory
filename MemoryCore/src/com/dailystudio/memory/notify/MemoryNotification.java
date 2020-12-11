package com.dailystudio.memory.notify;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;

import com.dailystudio.dataobject.Column;
import com.dailystudio.dataobject.IntegerColumn;
import com.dailystudio.dataobject.Template;
import com.dailystudio.dataobject.TextColumn;
import com.dailystudio.datetime.dataobject.TimeCapsule;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;

public class MemoryNotification extends TimeCapsule {
	
	public static final Column COLUMN_NOTIFY_ID = new IntegerColumn("notify_id", false);
	public static final Column COLUMN_SRC_PACKAGE = new TextColumn("src_package", false);
	public static final Column COLUMN_TITLE_RES = new IntegerColumn("title_res");
	public static final Column COLUMN_TITLE_TEXT = new TextColumn("title_text");
	public static final Column COLUMN_CONTENT_RES = new IntegerColumn("content_res");
	public static final Column COLUMN_CONTENT_TEXT = new TextColumn("content_text");
	public static final Column COLUMN_LAUNCH_INTENT = new TextColumn("launch_intent");
	
	private final static Column[] sColumns = {
		COLUMN_NOTIFY_ID,
		COLUMN_SRC_PACKAGE,
		COLUMN_TITLE_RES,
		COLUMN_TITLE_TEXT,
		COLUMN_CONTENT_RES,
		COLUMN_CONTENT_TEXT,
		COLUMN_LAUNCH_INTENT,
	};

	public MemoryNotification(Context context) {
		this(context, VERSION_START);
	}
	
	public MemoryNotification(Context context, int version) {
		super(context, version);
		
		final Template templ = getTemplate();
		
		templ.addColumns(sColumns);
	}
	
	public void setNotifyId(int qid) {
		setValue(COLUMN_NOTIFY_ID, qid);
	}
	
	public int getNotifyId() {
		return getIntegerValue(COLUMN_NOTIFY_ID);
	}
	
	public void setSourcePackage(String srcPkg) {
		setValue(COLUMN_SRC_PACKAGE, srcPkg);
	}
	
	public String getSourcePackage() {
		return getTextValue(COLUMN_SRC_PACKAGE);
	}

	public void setTitleResource(int resId) {
		setValue(COLUMN_TITLE_RES, resId);
	}
	
	public int getTitleResource() {
		return getIntegerValue(COLUMN_TITLE_RES);
	}
	
	public void setTitleText(String title) {
		setValue(COLUMN_TITLE_TEXT, title);
	}
	
	public String getTitleText() {
		return getTextValue(COLUMN_TITLE_TEXT);
	}
	
	public void setContentResource(int resId) {
		setValue(COLUMN_CONTENT_RES, resId);
	}
	
	public int getContentResource() {
		return getIntegerValue(COLUMN_CONTENT_RES);
	}
	
	public void setContentText(String content) {
		setValue(COLUMN_CONTENT_TEXT, content);
	}
	
	public String getContentText() {
		return getTextValue(COLUMN_CONTENT_TEXT);
	}

	public String dumpNotifyTitle() {
		final String srcPkg = getSourcePackage();
		final int titleResId = getTitleResource();
		
		if (srcPkg == null || titleResId == Constants.INVALID_RESOURCE_ID) {
			return getTitleText();
		}
		
		final PackageManager pkgmgr = mContext.getPackageManager();
		if (pkgmgr == null) {
			return getTitleText();
		}

		Resources res = null;
		try {
			res = pkgmgr.getResourcesForApplication(srcPkg);
		} catch (NameNotFoundException e) {
			Logger.warnning("get resources for [pkg: %s, resId: %d] failure: %s", 
					srcPkg, titleResId,
					e.toString());
			
			res = null;
		}
		
		if (res == null) {
			return getTitleText();
		}
		
		String resText = res.getString(titleResId);
		if (resText == null) {
			return getTitleText();
		}
		
		return resText;
	}
	
	public String dumpNotifyText() {
		final String srcPkg = getSourcePackage();
		final int cntResId = getContentResource();
		
		if (srcPkg == null || cntResId == Constants.INVALID_RESOURCE_ID) {
			return getContentText();
		}
		
		final PackageManager pkgmgr = mContext.getPackageManager();
		if (pkgmgr == null) {
			return getContentText();
		}

		Resources res = null;
		try {
			res = pkgmgr.getResourcesForApplication(srcPkg);
		} catch (NameNotFoundException e) {
			Logger.warnning("get resources for [pkg: %s, resId: %d] failure: %s", 
					srcPkg, cntResId,
					e.toString());
			
			res = null;
		}
		
		if (res == null) {
			return getContentText();
		}
		
		String resText = res.getString(cntResId);
		if (resText == null) {
			return getContentText();
		}
		
		return resText;
	}
	
	public void setNotifyIntent(String srcPkg) {
		setValue(COLUMN_LAUNCH_INTENT, srcPkg);
	}
	
	public String getNotifyIntent() {
		return getTextValue(COLUMN_LAUNCH_INTENT);
	}

	@Override
	public String toString() {
		return String.format("%s, nid(%d), src(%s), titleResId(%d), titleTxt(%s), cntResId(%d), cntTxt(%s), notifyIntent(%s)",
				super.toString(),
				getNotifyId(),
				getSourcePackage(),
				getTitleResource(),
				getTitleText(),
				getContentResource(),
				getContentText(),
				getNotifyIntent());
	}
	
}
