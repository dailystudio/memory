package com.dailystudio.memory.showcase;

import java.io.File;
import java.io.FileNotFoundException;

import com.dailystudio.app.utils.FileUtils;
import com.dailystudio.development.Logger;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

public class ShowcaseFileAccessContentProvider extends ContentProvider {
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public boolean onCreate() {
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}
	
	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode)
			throws FileNotFoundException {
		String filePath = uri.getPath();
        Logger.debug("[%s]: try to access file: %s", 
        		uri, filePath);
		if (filePath == null) {
			return null;
		}

		final Context context = getContext();
		if (context == null) {
			return null;
		}
		
		final String cachesDir = 
				ShowcaseDirectories.getShowcaseCachesDirectory(context);
		if (cachesDir == null) {
			return null;
		}
		
		final String filename = 
				String.format("%s/%s", cachesDir, filePath);
        Logger.debug("[CACHE FILE]: %s [existed: %s]", 
        		filename, FileUtils.isFileExisted(filename));
        
		ParcelFileDescriptor parcel = 
				ParcelFileDescriptor.open(new File(filename), 
						ParcelFileDescriptor.MODE_READ_ONLY);
		
		return parcel;
	}

}

