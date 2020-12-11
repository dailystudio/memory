package com.dailystudio.memory.javascript;

import java.util.List;

import android.content.ComponentName;

import com.dailystudio.development.Logger;
import com.dailystudio.memory.plugin.MemoryPluginInfo;
import com.dailystudio.memory.plugin.PluginManager;
import com.dailystudio.memory.plugin.privacy.PrivacyPolicy;
import com.google.gson.Gson;

public class MemoryJSPluginManager {

	public static class PluginPrivacy {
		
		public String pkg;
		public String label;
		public String collection;
		
	}
	
	public boolean isPluginInastalled(String plgPackage) {
		if (plgPackage == null) {
			return false;
		}
		
		final int count = PluginManager.countPlugins(plgPackage);
		
		return (count > 0);
	}
	
	public String getPrivacy(String plginComp) {
		if (plginComp == null) {
			return null;
		}
				
		MemoryPluginInfo pInfo = PluginManager.getPlugin(plginComp);
		if (pInfo == null) {
			return null;
		}
		
		final PrivacyPolicy pp = pInfo.dumpPrivacyPolicy();
		Logger.debug("pp = %s", pp);
		if (pp == null) {
			return null;
		}
		
		PluginPrivacy plgPrivacy = new PluginPrivacy();
		
		plgPrivacy.pkg = pInfo.getPackageName();
		plgPrivacy.label = pInfo.getLabel();
		plgPrivacy.collection = pp.collection;
		
		Gson gson = new Gson();
		
		return gson.toJson(plgPrivacy);
	}

	public String getPluginPackages() {
		List<MemoryPluginInfo> pInfos = PluginManager.listPlugins();
		Logger.debug("pInfos = %s", pInfos);
		if (pInfos == null) {
			return null;
		}
		
		final int N = pInfos.size();
		if (N <= 0) {
			return null;
		}
		
		String[] packages = new String[N];
		
		MemoryPluginInfo pInfo = null;
		ComponentName comp;
		for (int i = 0; i < N; i++) {
			pInfo = pInfos.get(i);
			
			comp = pInfo.getComponent();
			if (comp == null) {
				continue;
			}
			
			packages[i] = comp.flattenToShortString();
		}
		
		if (pInfo == null) {
			return null;
		}
		
		Gson gson = new Gson();
		
		return gson.toJson(packages);
	}

}
