package com.dailystudio.memory.javascript;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.dailystudio.GlobalContextWrapper;
import com.dailystudio.app.utils.ActivityLauncher;
import com.dailystudio.app.utils.ActivityLauncher.OnExceptionHandler;
import com.dailystudio.development.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class JSIntentLauncherPlugin {
	
	private final static String KEY_ACTION = "action";
    private final static String KEY_CATEGORY = "category";
	private final static String KEY_COMP = "comp";
	private final static String KEY_TYPE = "type";
	private final static String KEY_EXTRAS = "extras";
	private final static String KEY_ETYPE = "etype";
	private final static String KEY_EVAL = "eval";

	private final static String ETYPE_STRING = "string";
	private final static String ETYPE_INTEGER = "integer";
	private final static String ETYPE_BOOLEAN = "boolean";
	private final static String ETYPE_LONG = "long";
	private final static String ETYPE_DOUBLE = "double";

	public void sendBroadcast(String intentArgs) {
    	final Context context = GlobalContextWrapper.getContext();
		if (context == null || TextUtils.isEmpty(intentArgs)) {
			return;
		}
		
		JSONObject json = null;
		try {
			json = new JSONObject(intentArgs);
		} catch (JSONException e) {
			Logger.warnning("cannot extract intent descriptor from args[%s]: %s",
					intentArgs, e.toString());

			json = null;
		}
		
		final Intent intent = parseIntent(json);
		if (intent == null) {
			return;
		}
		
		Logger.debug("intent = [%s]", intent);
		context.sendBroadcast(intent);
	}

    public void launchActivity(String intentArgs) {
    	Logger.debug("intentArgs: (%s)", intentArgs);
    	final Context context = GlobalContextWrapper.getContext();
		if (context == null || TextUtils.isEmpty(intentArgs)) {
			return;
		}
		
		JSONObject json = null;
		try {
			json = new JSONObject(intentArgs);
		} catch (JSONException e) {
			Logger.warnning("cannot extract intent descriptor from args[%s]: %s",
					intentArgs, e.toString());
			
			json = null;
		}
		
		final Intent intent = parseIntent(json);
		if (intent == null) {
			return;
		}
		
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		Logger.debug("intent = [%s]", intent);
        ActivityLauncher.launchActivity(context, intent, new OnExceptionHandler() {

            @Override
            public void onException(Intent intent, Exception e) {
                Logger.warnning("cannot launch intent from args[%s]: %s",
                        intent, e.toString());
            };

        });
	}

	public void createChooser(String title, String intentArgs) {
    	final Context context = GlobalContextWrapper.getContext();
		if (context == null || TextUtils.isEmpty(intentArgs)) {
			return;
		}
		
		if (TextUtils.isEmpty(title)) {
			return;
		}
		
		JSONObject json = null;
		try {
			json = new JSONObject(intentArgs);
		} catch (JSONException e) {
			Logger.warnning("cannot extract intent descriptor from args[%s]: %s",
					intentArgs, e.toString());

			json = null;
		}
		
		final Intent intent = parseIntent(json);
		if (intent == null) {
			return;
		}
		
		Logger.debug("intent = [%s]", intent);
		ActivityLauncher.launchActivity(context, 
				Intent.createChooser(intent, title));
	}

	private Intent parseIntent(JSONObject json) {
		if (json == null) {
			return null;
		}

		Intent intent = new Intent();
		
		try {
			if (json.has(KEY_ACTION)) {
				intent.setAction(json.getString(KEY_ACTION));
			}

            if (json.has(KEY_CATEGORY)) {
                JSONArray categories = json.getJSONArray(KEY_CATEGORY);
                if (categories != null && categories.length() > 0) {
                    String category = null;
                    for (int i = 0; i < categories.length(); i++) {
                        category = categories.getString(i);

                        if (TextUtils.isEmpty(category)) {
                            continue;
                        }

                        intent.addCategory(category);
                    }
                }
            }

			if (json.has(KEY_COMP)) {
				String compstr = json.getString(KEY_COMP);
				if (!TextUtils.isEmpty(compstr)) {
					ComponentName comp = 
							ComponentName.unflattenFromString(compstr);
					if (comp != null) {
						intent.setComponent(comp);
					}
				}
 			}
			
			if (json.has(KEY_TYPE)) {
				intent.setType(json.getString(KEY_TYPE));
			}
			
			Bundle extras = parseExtras(json);
			if (extras != null) {
				intent.putExtras(parseExtras(json));
 			}
		} catch (JSONException e) {
			Logger.warnning("parse intent from json[%s] failure: %s",
					json, e.toString());
		}
		
		return intent;
	}
	
	private Bundle parseExtras(JSONObject json) throws JSONException {
		if (json == null || !json.has(KEY_EXTRAS)) {
			return null;
		}
		
		JSONObject extras = json.getJSONObject(KEY_EXTRAS);
		
		Object extra = null;
		String etype = null;
		String key = null;
		
		if (extras == null || extras.length() <= 0) {
			return null;
		}
		
		Bundle bundle = new Bundle();
		
		@SuppressWarnings("unchecked")
		Iterator<String> iter = extras.keys();
	    while (iter.hasNext()) {
	    	key = iter.next();
        	
	        try {
	        	extra = extras.get(key);
	        } catch (JSONException e) {
				Logger.warnning("parse extras from json[%s] failure: %s",
						json, e.toString());
	        }
	        
	        if (extra instanceof JSONObject == false) {
	        	continue;
	        }
	        
	        etype = ((JSONObject)extra).getString(KEY_ETYPE);
	        if (etype == null) {
	        	continue;
	        }
	        
	        if (ETYPE_STRING.equals(etype)) {
	        	String sVal = ((JSONObject)extra).getString(KEY_EVAL);
	        	Logger.debug("parse string extra: %s, [val: %s]",
	        			key, sVal);
	        	
	        	bundle.putString(key, sVal);
	        } else if (ETYPE_INTEGER.equals(etype)) {
	        	int iVal = ((JSONObject)extra).getInt(KEY_EVAL);
	        	Logger.debug("parse integer extra: %s, [val: %d]", 
	        			key, iVal);
	        	
	        	bundle.putInt(key, iVal);
	        } else if (ETYPE_BOOLEAN.equals(etype)) {
	        	boolean bVal = ((JSONObject)extra).getBoolean(KEY_EVAL);
	        	Logger.debug("parse boolean extra: %s, [val: %s]", 
	        			key, bVal);
	        	
	        	bundle.putBoolean(key, bVal);
	        } else if (ETYPE_LONG.equals(etype)) {
	        	long lVal = ((JSONObject)extra).getLong(KEY_EVAL);
	        	Logger.debug("parse long extra: %s, [val: %d]", 
	        			key, lVal);
	        	
	        	bundle.putLong(key, lVal);
	        } else if (ETYPE_DOUBLE.equals(etype)) {
	        	double dVal = ((JSONObject)extra).getDouble(KEY_EVAL);
	        	Logger.debug("parse long extra: %s, [val: %f]", 
	        			key, dVal);
	        	
	        	bundle.putDouble(key, dVal);
	        }
	    }
	    
	    return bundle;
	}
	
}
