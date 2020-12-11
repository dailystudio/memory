package com.dailystudio.memory.showcase;

import com.dailystudio.memory.Constants;

import android.content.Context;
import android.content.Intent;

public class Showcase {

	public static void prepareShowcase(Context context, String targetPackage) {
		if (context == null || targetPackage == null) {
			return;
		}
		
		Intent i = new Intent(Constants.ACTION_PREPARE_SHOWCASE);
		
		i.addCategory(Constants.CATEGORY_MAIN);
		i.setPackage(targetPackage);
		
		context.sendBroadcast(i);
	}
	
	public static void updateShowcase(Context context, String targetPackage) {
		if (context == null || targetPackage == null) {
			return;
		}
		
		Intent i = new Intent(Constants.ACTION_UPDATE_SHOWCASE);
		
		i.addCategory(Constants.CATEGORY_MAIN);
		
		i.putExtra(Constants.EXTRA_SHOWCASE_PACKAGE, targetPackage);
		
		context.sendBroadcast(i);
	}
	
}
