package com.dailystudio.memory.ask;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;

import com.dailystudio.dataobject.Column;
import com.dailystudio.dataobject.IntegerColumn;
import com.dailystudio.dataobject.Template;
import com.dailystudio.dataobject.TextColumn;
import com.dailystudio.datetime.dataobject.TimeCapsule;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;

public class MemoryQuestion extends TimeCapsule {
	
	public final static int PRIORITY_NORMAL = 0;
	public final static int PRIORITY_ROUTINE = 1;
	public final static int PRIORITY_EMERGENT = 2;
	
	public final static int STATE_NEW = 0;
	public final static int STATE_UPDATED = 1;
	public final static int STATE_ASKED = 2;
	public final static int STATE_CANCELLED = 3;
	public final static int STATE_ANSWERED = 4;
	
	public static final Column COLUMN_QUESTION_ID = new IntegerColumn("question_id", false);
	public static final Column COLUMN_SRC_PACKAGE = new TextColumn("src_package", false);
	public static final Column COLUMN_CONTENT_RESOURCE_NAME = new TextColumn("content_res_name");
	public static final Column COLUMN_CONTENT_TEXT = new TextColumn("content_text");
	public static final Column COLUMN_LAUNCH_INTENT = new TextColumn("launch_intent");
	public static final Column COLUMN_PRIORITY = new IntegerColumn("priority", false);
	public static final Column COLUMN_STATE = new IntegerColumn("state", false);
	public static final Column COLUMN_ANSWER = new TextColumn("answer");
	
	private final static Column[] sColumns = {
		COLUMN_QUESTION_ID,
		COLUMN_SRC_PACKAGE,
		COLUMN_CONTENT_RESOURCE_NAME,
		COLUMN_CONTENT_TEXT,
		COLUMN_LAUNCH_INTENT,
		COLUMN_PRIORITY,
		COLUMN_STATE,
		COLUMN_ANSWER,
	};

	public MemoryQuestion(Context context) {
		this(context, VERSION_START);
	}
	
	public MemoryQuestion(Context context, int version) {
		super(context, version);
		
		final Template templ = getTemplate();
		
		templ.addColumns(sColumns);
	}
	
	public void setQuestionId(int qid) {
		setValue(COLUMN_QUESTION_ID, qid);
	}
	
	public int getQuestionId() {
		return getIntegerValue(COLUMN_QUESTION_ID);
	}
	
	public void setSourcePackage(String srcPkg) {
		setValue(COLUMN_SRC_PACKAGE, srcPkg);
	}
	
	public String getSourcePackage() {
		return getTextValue(COLUMN_SRC_PACKAGE);
	}

	public void setContentResourceName(String resName) {
		setValue(COLUMN_CONTENT_RESOURCE_NAME, resName);
	}
	
	public String getContentResourceName() {
		return getTextValue(COLUMN_CONTENT_RESOURCE_NAME);
	}
	
	public void setContentResourceId(int resId) {
		final PackageManager pkgmgr = mContext.getPackageManager();
		if (pkgmgr == null) {
			return;
		}
		
		final String srcPkg = getSourcePackage();
		if (srcPkg == null) {
			return;
		}

		Resources res = null;
		try {
			res = pkgmgr.getResourcesForApplication(srcPkg);
		} catch (NameNotFoundException e) {
			Logger.warnning("Get resource for pkg[%s] failure: %s",
					srcPkg,
					e.toString());
			res = null;
		}
		
		if (res == null) {
			return;
		}
		
		setContentResourceName(res.getResourceName(resId));
	}
	
	public int getContentResourceId() {
		final String srcPkg = getSourcePackage();
		if (srcPkg == null) {
			return -1;
		}
		
		final String resName = getTextValue(COLUMN_CONTENT_RESOURCE_NAME);
		if (resName == null) {
			return -1;
		}
		
		final PackageManager pkgmgr = mContext.getPackageManager();
		if (pkgmgr == null) {
			return -1;
		}
		
		Resources res = null;
		try {
			res = pkgmgr.getResourcesForApplication(srcPkg);
		} catch (NameNotFoundException e) {
			Logger.warnning("Get resource for pkg[%s] failure: %s",
					srcPkg,
					e.toString());
			res = null;
		}
		
		if (res == null) {
			return -1;
		}
		
		return res.getIdentifier(resName, null, srcPkg);
	}
	
	public void setContentText(String content) {
		setValue(COLUMN_CONTENT_TEXT, content);
	}
	
	public String getContentText() {
		return getTextValue(COLUMN_CONTENT_TEXT);
	}

	public String dumpQuestionText() {
		final String srcPkg = getSourcePackage();
		final int cntResId = getContentResourceId();
		
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
			Logger.warnning("get resources for [pkg: %s, resId: %d/%s] failure: %s", 
					srcPkg, cntResId,
					getContentResourceName(),
					e.toString());
			
			res = null;
		}
		
		if (res == null) {
			return getContentText();
		}
		
		String resText = null;
		try {
			resText = res.getString(cntResId);
		} catch (NotFoundException e) {
			Logger.debug("getString() failure: %s", e.toString());
			resText = null;
		}
		
		if (resText == null) {
			return getContentText();
		}
		
		return resText;
	}
	
	public void setLaunchIntent(String srcPkg) {
		setValue(COLUMN_LAUNCH_INTENT, srcPkg);
	}
	
	public String getLaunchIntent() {
		return getTextValue(COLUMN_LAUNCH_INTENT);
	}

	public void setPriority(int priority) {
		setValue(COLUMN_PRIORITY, priority);
	}
	
	public int getPriority() {
		return getIntegerValue(COLUMN_PRIORITY);
	}
	
	public void setState(int state) {
		setValue(COLUMN_STATE, state);
	}
	
	public int getState() {
		return getIntegerValue(COLUMN_STATE);
	}
	
	public void setAnswer(String answer) {
		setValue(COLUMN_ANSWER, answer);
	}
	
	public String getAnswer() {
		return getTextValue(COLUMN_ANSWER);
	}

	@Override
	public String toString() {
		return String.format("%s, qid(%d), src(%s), cntResId(%d, name:%s), cntTxt(%s), launchIntent(%s), priority(%d), state(%d), answer(%s)",
				super.toString(),
				getQuestionId(),
				getSourcePackage(),
				getContentResourceId(),
				getContentResourceName(),
				getContentText(),
				getLaunchIntent(),
				getPriority(),
				getState(),
				getAnswer());
	}
	
}
