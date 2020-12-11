package com.dailystudio.memory.showcase;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ShowcaseBundle {

	public String bundleName;
	public long bundleVersion;
	public String[] bundleFiles;
	
	@Override
	public String toString() {
		return String.format("%s(x0%08x): name = %s, ver = 0x%08x, files = [%s]",
				getClass().getName(),
				hashCode(),
				bundleName,
				bundleVersion,
				bundleFilesToString());
	}
	
	private String bundleFilesToString() {
		if (bundleFiles == null) {
			return null;
		}
		
		final int N = bundleFiles.length;
		if (N <= 0) {
			return null;
		}
		
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < N; i++) {
			builder.append(bundleFiles[i]);
			if (i != (N - 1)) {
				builder.append(", ");
			}
		}
		
		return builder.toString();
	}
	
	public static boolean isUpdatedNeed(Context context, ShowcaseBundle bundle) {
		if (bundle == null) {
			return false;
		}
		
		final long lastVer = 
				getLastBundleVersion(context, bundle);

		return (lastVer < bundle.bundleVersion);
	}

    private final static String KEY_BUNDLE_VERSION = "bundleVersion";

    private static String getBundlePrefName(Context context, ShowcaseBundle bundle) {
        if (context == null || bundle == null) {
            return null;
        }
            
        if (bundle.bundleName == null) {
            return null;
        }
            
        final String pkgName = context.getPackageName();
        if (pkgName == null) {
        	return null;
        }
        
        return String.format("%s.%s", 
                pkgName, 
                bundle.bundleName.replace(' ', '_'));
    }

	public static long getLastBundleVersion(Context context, ShowcaseBundle bundle) {
        if (context == null || bundle == null) {
			return -1l;
		}
		
		final String prefName = getBundlePrefName(
				context, bundle);
		if (prefName == null) {
			return -1l;
		}
		
		final SharedPreferences pref = context.getSharedPreferences(
				prefName, Context.MODE_PRIVATE);
		if (pref == null) {
			return -1l;
		}
		
		return pref.getLong(KEY_BUNDLE_VERSION, -1l);
	}
	
	public static void setLastBundleVersion(Context context, ShowcaseBundle bundle) {
		if (context == null || bundle == null) {
			return;
		}
		
		final String prefName = getBundlePrefName(
				context, bundle);
		if (prefName == null) {
			return;
		}
		
		final SharedPreferences pref = context.getSharedPreferences(
				prefName, Context.MODE_PRIVATE);
		if (pref == null) {
			return;
		}
		
		final Editor editor = pref.edit();
		if (editor == null) {
			return;
		}
		
		editor.putLong(KEY_BUNDLE_VERSION, bundle.bundleVersion);
		editor.commit();
	}
	
}
