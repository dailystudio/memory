package com.dailystudio.memory.plugin;

import com.dailystudio.development.Logger;
import com.dailystudio.manager.IGroupObject;
import com.dailystudio.memory.Constants;
import com.dailystudio.memory.plugin.privacy.PrivacyPolicy;
import com.dailystudio.memory.plugin.privacy.PrivacyPolicyXmlParser;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.net.Uri;

public class MemoryPluginInfo implements IGroupObject<String, String> {
	
	private Context mContext;
	
	private ComponentName mComponent;

	private String mLabel;
	private int mIconRes;
	private int mPrivacyPolicyRes;
	private Uri mShowcasePageUri;
	private String mSearchDatabaseAuthority;
	
	MemoryPluginInfo(Context context, ComponentName comp) {
		mContext = context;
		
		initMembers();

		mComponent = comp;
	}
	
	private void initMembers() {
		mLabel = null;
		mIconRes = Constants.INVALID_RESOURCE_ID;
	}
	
	public ComponentName getComponent() {
		return mComponent;
	}
	
	public String getPackageName() {
		if (mComponent == null) {
			return null;
		}
		
		final String pkg = mComponent.getPackageName();
		if (pkg == null) {
			return null;
		}

		return pkg;
	}
	
	public void setLabel(String label) {
		mLabel = label;
	}
	
	public String getLabel() {
		return mLabel;
	}

	public void setIconResource(int resid) {
		mIconRes = resid;
	}
	
	public int getIconResource() {
		return mIconRes;
	}
	
	public void setShowcasePage(Uri uri) {
		mShowcasePageUri = uri;
	}
	
	public Uri getShowcasePage() {
		return mShowcasePageUri;
	}
	
	public void setPrivacyPolicResource(int resid) {
		mPrivacyPolicyRes = resid;
	}
	
	public int getPrivacyPolicResource() {
		return mPrivacyPolicyRes;
	}
	
	public PrivacyPolicy dumpPrivacyPolicy() {
		if (mPrivacyPolicyRes <= 0) {
			return null;
		}
		
		final String pkg = getPackageName();
		if (pkg == null) {
			return null;
		}
		
		final PackageManager pkgmgr = mContext.getPackageManager();
		if (pkgmgr == null) {
			return null;
		}
		
		Resources res = null;
		try {
			res = pkgmgr.getResourcesForApplication(pkg);
		} catch (NameNotFoundException e) {
			Logger.warnning("parse privacy failure: %s", e.toString());
			
			res = null;
		}
		
		if (res == null) {
			return null;
		}
		
		final XmlResourceParser parser = 
			res.getXml(mPrivacyPolicyRes);
		if (parser == null) {
			return null;
		}
		
		PrivacyPolicy pp = PrivacyPolicyXmlParser.parse(parser);
		
//		Logger.debug("privacy[%s, %d]: pp = %s",
//				pkg, mPrivacyPolicyRes, pp);
		if (pp == null) {
			return null;
		}
		
		return pp;
	}
	
	public void setSearchableAuthority(String authority) {
		mSearchDatabaseAuthority = authority;
	}
	
	public String getSearchableAuthority() {
		return mSearchDatabaseAuthority;
	}
	
	public void register() {
		sendBroadcast(new Intent(Constants.ACTION_REGISTER_PLUGIN));
		sendBroadcast(new Intent(Constants.ACTION_CLEAR_TASKS));
	}
	
	public void unregister() {
		sendBroadcast(new Intent(Constants.ACTION_UNREGISTER_PLUGIN));
	}
	
	public void createTask(String taskClass, int taskId) {
		Intent intent = new Intent(Constants.ACTION_CREATE_TASK);
		
		intent.putExtra(Constants.EXTRA_TASK_CLASS, taskClass);
		intent.putExtra(Constants.EXTRA_TASK_ID, taskId);
		
		sendBroadcast(intent);
	}
	
	public void destroyTask(int taskId) {
		Intent intent = new Intent(Constants.ACTION_DESTROY_TASK);
		
		intent.putExtra(Constants.EXTRA_TASK_ID, taskId);
		
		sendBroadcast(intent);
	}

	public void resumeTask(int taskId) {
		Intent intent = new Intent(Constants.ACTION_RESUME_TASK);
		
		intent.putExtra(Constants.EXTRA_TASK_ID, taskId);
		
		sendBroadcast(intent);
	}
	
	public void pauseTask(int taskId) {
		Intent intent = new Intent(Constants.ACTION_PAUSE_TASK);
		
		intent.putExtra(Constants.EXTRA_TASK_ID, taskId);
		
		sendBroadcast(intent);
	}
	
	public void executeTask(int taskId) {
		Intent intent = new Intent(Constants.ACTION_EXECUTE_TASK);
		
		intent.putExtra(Constants.EXTRA_TASK_ID, taskId);
		
		sendBroadcast(intent);
	}
	
	public void keepAliveTask(int taskId) {
		Intent intent = new Intent(Constants.ACTION_KEEP_ALIVE_TASK);
		
		intent.putExtra(Constants.EXTRA_TASK_ID, taskId);
		
		sendBroadcast(intent);
	}
	
	private void sendBroadcast(Intent intent) {
		if (intent == null) {
			return;
		}
		
		if (mContext == null) {
			return;
		}
		
		if (mComponent == null) {
			return;
		}
		
        intent.setComponent(mComponent);
        
        mContext.sendBroadcast(intent);
	}

	@Override
	public String getSingletonKey() {
		if (mComponent == null) {
			return null;
		}
		
		return mComponent.flattenToShortString();
	}
	
	@Override
	public String toString() {
		return String.format("%s(0x%08x): componet(%s), label(%s), iconRes(%d), showcase(%s), privacy(%d), searchable(%s)",
				getClass().getSimpleName(),
				hashCode(),
				(mComponent == null ? "null" : mComponent.toShortString()),
				mLabel,
				mIconRes,
				mShowcasePageUri,
				mPrivacyPolicyRes,
				mSearchDatabaseAuthority);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof MemoryPluginInfo == false) {
			return false;
		}
		
		MemoryPluginInfo info = (MemoryPluginInfo)o;
		
		if ((mComponent == null && info.getComponent() != null)
				|| (mComponent != null && info.getComponent() == null)) {
			return false;
		} else if (mComponent == null && info.getComponent() == null) { 
		} else if (mComponent.toShortString().equals(info.getComponent().toShortString()) == false) {
			return false;
		}
		
		if ((mLabel == null && info.getLabel() != null)
				|| (mLabel != null && info.getLabel() == null)) {
			return false;
		} else if (mLabel == null && info.getLabel() == null) { 
		} else if (mLabel.equals(info.getLabel()) == false) {
			return false;
		}
		
		if (mIconRes != info.getIconResource()) {
			return false;
		}
		
		if (mPrivacyPolicyRes != info.getPrivacyPolicResource()) {
			return false;
		}
		
		return true;
	}

	@Override
	public String getGroup() {
		if (mComponent == null) {
			return null;
		}
		
		return mComponent.getPackageName();
	}
	
}
