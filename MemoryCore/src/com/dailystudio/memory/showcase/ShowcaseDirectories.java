package com.dailystudio.memory.showcase;

import java.io.File;

import android.content.Context;

public class ShowcaseDirectories {
	
	private static final String SHOWCASE_CACHES_DIR = "/showcase_caches/";

	public static final String getShowcaseCachesDirectory(Context context) {
		final File extDir = context.getExternalFilesDir(null);
		if (extDir == null) {
			return null;
		}
		
		final File cachesDir = new File(extDir, SHOWCASE_CACHES_DIR);
		
		return cachesDir.getAbsolutePath();
	}

}
