package com.dailystudio.memory.plugin;

import java.util.List;

import android.content.ComponentName;

import com.dailystudio.development.Logger;
import com.dailystudio.manager.GroupManager;
import com.dailystudio.manager.Manager;
import com.dailystudio.memory.task.TaskHost;
import com.dailystudio.memory.task.TaskHostManager;
import com.dailystudio.nativelib.observable.NativeObservable;
import com.dailystudio.nativelib.observable.ObservableManager;

public class PluginManager extends GroupManager<String, String, MemoryPluginInfo> {

	public static synchronized PluginManager getInstance() {
		return Manager.getInstance(PluginManager.class);
	}
	
	private void removeTaskHosts(MemoryPluginInfo pInfo) {
		Logger.debug("removeTaskHosts = %s", pInfo);
		if (pInfo == null) {
			return;
		}
		
		final ComponentName comp = pInfo.getComponent();
		if (comp == null) {
			return;
		}

		List<TaskHost> hosts = TaskHostManager.getTasks(comp);
		Logger.debug("hosts = %s", hosts);
		if (hosts == null) {
			return;
		}
		
		for (TaskHost host: hosts) {
			TaskHostManager.unregisterTask(host);
		}
	}
	
	@Override
	public void removeObject(MemoryPluginInfo object) {
		removeTaskHosts(object);

		super.removeObject(object);
	}
	
	@Override
	public MemoryPluginInfo removeObjectByKey(String key) {
		MemoryPluginInfo pInfo = getObject(key);
		if (pInfo == null) {
			return null;
		}
		
		removeTaskHosts(pInfo);
		
		return super.removeObjectByKey(key);
	}
	
	@Override
	public void clearObjects() {
		List<MemoryPluginInfo> pInfos = listPlugins();
		if (pInfos != null && pInfos.size() > 0) {
			for (MemoryPluginInfo pInfo: pInfos) {
				removeTaskHosts(pInfo);
			}
		}
		
		super.clearObjects();
	}
	
	public static void registerPlugin(MemoryPluginInfo plugin) {
		Logger.debug("plugin(%s)", plugin);
		if (plugin == null) {
			return;
		}
		
		Logger.debug("plugin.Group(%s)", plugin.getGroup());
		
		PluginManager plgmgr = PluginManager.getInstance();
		if (plgmgr == null) {
			return;
		}
		
		plugin.register();
		
		plgmgr.addObject(plugin);
		
		NativeObservable observable = 
			ObservableManager.getObservable(PluginObserverable.class);
		if (observable != null) {
			observable.notifyObservers();
		}
	}
	
	public static void unregisterPlugin(MemoryPluginInfo plugin) {
		if (plugin == null) {
			return;
		}
		
		PluginManager plgmgr = PluginManager.getInstance();
		if (plgmgr == null) {
			return;
		}
		
		plugin.unregister();
		
		plgmgr.removeObject(plugin);
		
		NativeObservable observable = 
			ObservableManager.getObservable(PluginObserverable.class);
		if (observable != null) {
			observable.notifyObservers();
		}
	}
	
	public static MemoryPluginInfo getPlugin(String comp) {
		if (comp == null) {
			return null;
		}
		
		PluginManager plgmgr = PluginManager.getInstance();
		if (plgmgr == null) {
			return null;
		}
		
		return plgmgr.getObject(comp);
	}
	
	public static void clearPlugins() {
		PluginManager plgmgr = PluginManager.getInstance();
		if (plgmgr == null) {
			return;
		}
		
		plgmgr.clearObjects();
		
		NativeObservable observable = 
			ObservableManager.getObservable(PluginObserverable.class);
		if (observable != null) {
			observable.notifyObservers();
		}
	}
	
	public static List<MemoryPluginInfo> listPlugins(String pkgName) {
		PluginManager pmgr = PluginManager.getInstance();
		if (pmgr == null) {
			return null;
		}
		
		Logger.debug("pkgName(%s)", pkgName);
		return pmgr.getObjectsInGroup(pkgName);
	}
	
	public static List<MemoryPluginInfo> listPlugins() {
		PluginManager pmgr = PluginManager.getInstance();
		if (pmgr == null) {
			return null;
		}
		
		return pmgr.listObjects();
	}

	public static int countPlugins() {
		PluginManager pmgr = PluginManager.getInstance();
		if (pmgr == null) {
			return 0;
		}
		
		return pmgr.getCount();
	}
	
	public static int countPlugins(String pkgName) {
		PluginManager pmgr = PluginManager.getInstance();
		if (pmgr == null) {
			return 0;
		}
		
		List<MemoryPluginInfo> plugins = 
			listPlugins(pkgName);
		
		return (plugins == null ? 0 : plugins.size());
	}
	
}
