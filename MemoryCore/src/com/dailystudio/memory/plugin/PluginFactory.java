package com.dailystudio.memory.plugin;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Xml;

import com.dailystudio.development.Logger;
import com.dailystudio.factory.Factory;
import com.dailystudio.factory.SingletoneFactory;
import com.dailystudio.memory.Constants;
import com.dailystudio.memory.task.PeriodicalTaskHost;
import com.dailystudio.memory.task.TaskHost;
import com.dailystudio.memory.task.TaskHostFactory;
import com.dailystudio.memory.task.TaskHostManager;
import com.dailystudio.memory.utils.XmlHelper;

public class PluginFactory extends SingletoneFactory<MemoryPluginInfo, ComponentName> {
	
	public synchronized static final PluginFactory getInstance() {
		return Factory.getInstance(PluginFactory.class);
	}

	public synchronized static final MemoryPluginInfo createPluginInfo(Context context, ResolveInfo rInfo) {
		if (context == null || rInfo == null) {
			return null;
		}
		
		final PluginFactory factory = PluginFactory.getInstance();
		if (factory == null) {
			return null;
		}
	
    	final PackageManager pkgmgr = context.getPackageManager();
    	if (pkgmgr == null) {
    		return null;
    	}
		
		final ActivityInfo activityInfo = rInfo.activityInfo;
    	if (rInfo.activityInfo == null) {
    		return null;
    	}
    	
    	ComponentName component = new ComponentName(
    			activityInfo.packageName,
    			activityInfo.name);
    	
		Resources res = null;
		try {
			res = pkgmgr.getResourcesForApplication(
					activityInfo.applicationInfo);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			
			res = null;
		}
		
		if (res == null) {
			return null;
		}
		
    	MemoryPluginInfo pInfo = PluginFactory.createPluginInfo(component);
    	if (pInfo == null) {
    		return null;
    	}
    	
    	XmlResourceParser parser = activityInfo.loadXmlMetaData(pkgmgr,
                Constants.META_DATA_DAILY_PLUING);
		if (parser == null) {
			return pInfo;
		}

        boolean ret = factory.parseAttributesFromXml(pInfo, parser, res);
        
        if (parser != null) {
        	parser.close();
        }

        if (ret == false) {
        	return null;
        }
        
		return pInfo;
	}

	public synchronized static final MemoryPluginInfo createPluginInfo(ComponentName comp) {
		final PluginFactory factory = PluginFactory.getInstance();
		if (factory == null) {
			return null;
		}
	
		MemoryPluginInfo pInfo = factory.createObject(comp);
		
		return pInfo;
	}
	
	@Override
	protected MemoryPluginInfo newObject(ComponentName params) {
		if (params == null) {
			return null;
		}
		
		final Context context = getContext();
		if (context == null) {
			return null;
		}
		
		MemoryPluginInfo pInfo = new MemoryPluginInfo(context, params);
		
		return pInfo;
	}

	@Override
	protected void cacheObject(MemoryPluginInfo object) {
		if (object == null) {
			return;
		}
		
		PluginManager.registerPlugin(object);
	}

	@Override
	protected MemoryPluginInfo findObject(ComponentName params) {
		if (params == null) {
			return null;
		}
		
		return PluginManager.getPlugin(params.flattenToShortString());
	}

    private boolean parseAttributesFromXml(MemoryPluginInfo pInfo, 
    		XmlPullParser parser,
    		Resources res) {
    	if (pInfo == null || parser == null || res == null) {
			return false;
    	}
    	
    	final ComponentName comp = pInfo.getComponent();
//    	Logger.debug("comp(%s)", comp);
    	if (comp == null) {
    		return false;
    	}
    	
    	AttributeSet attrs = Xml.asAttributeSet(parser);
//    	Logger.debug("attrs(%s)", attrs);

    	boolean success = false;
    	try {
    		final int depth = parser.getDepth();
    		boolean gotRoot = false;
    		
		    int type;
		    
	        while (((type = parser.next()) != XmlPullParser.END_TAG ||
	                parser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {
	
	            if (type != XmlPullParser.START_TAG) {
	                continue;
	            }
	            
			    String nodeName = parser.getName();
//			    Logger.debug("nodeName(%s)", nodeName);
			    if (gotRoot == false) {
				    if (!Constants.XML_TAG_PLUGIN.equals(nodeName)) {
				    	Logger.warnning("meta-data does not start with tag %s", 
				    			Constants.XML_TAG_PLUGIN);
				        
				        return false;
				    }
				    
				    gotRoot = true;
			    }
			    
			    if (Constants.XML_TAG_PLUGIN.equals(nodeName)) {
			    	parsePluginAttributes(pInfo, attrs, res);
			    } else if (Constants.XML_TAG_TASK.equals(nodeName)) {
			    	TaskHost host = createHost(attrs, res);
			    	if (host != null) {
			    		host.setComponent(comp);
			    		
			    		TaskHostManager.registerTask(host);
			    	}
			    }
	        }

	        success = true;
    	} catch (XmlPullParserException e) {
    		e.printStackTrace();
    		
    		success = false;
    	} catch (IOException e) {
			e.printStackTrace();
    		
    		success = false;
		}
    	
    	return success;
    }

    private boolean parsePluginAttributes(MemoryPluginInfo pInfo, 
    		AttributeSet attrs,
    		Resources res) {
    	if (attrs == null || pInfo == null || res == null) {
    		return false;
    	}
    	
        int attrCount = attrs.getAttributeCount();
        if (attrCount <= 0) {
        	return false;
        }
        
        String attrName = null;

        for (int i = 0; i < attrCount; i++) {
        	attrName = attrs.getAttributeName(i);

        	if (Constants.META_ATTR_LABEL.equals(attrName)) {
        		String label = XmlHelper.parseLabel(attrs, i, res);
//	            Logger.debug("label(%s)", label);
        		if (label != null) {
        			pInfo.setLabel(label);
        		}
        	} else if (Constants.META_ATTR_ICON.equals(attrName)) {
        		int resid = XmlHelper.parseResource(attrs, i, res);
//	            Logger.debug("resid(%d)", resid);
        		if (resid != Constants.INVALID_RESOURCE_ID) {
        			pInfo.setIconResource(resid);
        		}
        	} else if (Constants.META_ATTR_SHOWCASE.equals(attrName)) {
        		String uriString = XmlHelper.parseString(attrs, i, res);
        		if (uriString != null) {
        			Uri uri = Uri.parse("content://" 
        					+ pInfo.getPackageName() + ".showcase.files"
        					+ "/"
        					+ uriString);
    	            Logger.debug("showcase uri = %s", uri);
        			pInfo.setShowcasePage(uri);
        		}
        	} else if (Constants.META_ATTR_PRIVACY.equals(attrName)) {
        		int resid = XmlHelper.parseResource(attrs, i, res);
//	            Logger.debug("resid(%d)", resid);
        		if (resid != Constants.INVALID_RESOURCE_ID) {
        			pInfo.setPrivacyPolicResource(resid);
        		}
        	} else if (Constants.META_ATTR_SEACHABLE_AUTHORITY.equals(attrName)) {
        		String authority = XmlHelper.parseString(attrs, i, res);
//	            Logger.debug("resid(%d)", resid);
        		if (authority != null) {
        			pInfo.setSearchableAuthority(authority);
        		}
        	}
        }
	    
    	return true;
    }
    
    private TaskHost createHost(AttributeSet attrs, Resources res) {
    	if (attrs == null || res == null) {
    		return null;
    	}
    	
        int attrCount = attrs.getAttributeCount();
        if (attrCount <= 0) {
        	return null;
        }
        
        String taskType = null;
        String klassName = null;
        String label = null;
        int millis = 0;
        boolean isOneShot = false;
        
        String attrName = null;

        for (int i = 0; i < attrCount; i++) {
        	attrName = attrs.getAttributeName(i);

        	if (Constants.META_ATTR_TYPE.equals(attrName)) {
        		taskType = attrs.getAttributeValue(i);
        	} else if (Constants.META_ATTR_NAME.equals(attrName)) {
        		klassName = attrs.getAttributeValue(i);
        	} else if (Constants.META_ATTR_LABEL.equals(attrName)) {
        		label = XmlHelper.parseLabel(attrs, i, res);
        	} else if (Constants.META_ATTR_EXECUTE_PEROID.equals(attrName)) {
        		millis = attrs.getAttributeIntValue(i, 0);
        	} else if (Constants.META_ATTR_IS_ONESHOT.equals(attrName)) {
        		isOneShot = attrs.getAttributeBooleanValue(i, false);
        	}  
  
        }
        
        if (taskType == null) {
        	taskType = Constants.TASK_TYPE_PERIODICAL;
        }
        
/*        Logger.debug("taskType(%s), klassName(%s), label(%s), millis(%d), isOneShot(%s)",
        		taskType,
        		klassName,
        		label,
        		millis,
        		isOneShot);
*/        
        if (klassName == null) {
        	return null;
        }
        
        if (Constants.TASK_TYPE_PERIODICAL.equals(taskType)) {
        	if (isOneShot == false && millis < Constants.MIN_EXEC_PERIOD) {
        		return null;
        	}
        }
        
        TaskHost task = TaskHostFactory.allocateHost(taskType);
        if (task == null) {
        	return null;
        }
        
        task.setTaskClass(klassName);
        task.setLabel(label);
        task.setOneShot(isOneShot);

        if (task instanceof PeriodicalTaskHost) {
        	((PeriodicalTaskHost)task).setExecPeroid(millis);
        }

        Logger.debug("task = %s", task);
        
    	return task;
    }

}
