package com.dailystudio.memory.searchable;

import java.util.List;

import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;
import com.google.gson.Gson;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

public abstract class MemorySearchableContentProvider extends ContentProvider {

	@Override
	public boolean onCreate() {
		return true;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
    
    @Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
    	if (uri == null) {
    		return null;
    	}
    	
    	String segment = uri.getLastPathSegment();
    	if (segment == null) {
            throw new UnsupportedOperationException();
    	}
    	
    	if (Constants.SEARCH_SEGMENT_SUGGESTION.equals(segment)) {
    		return querySuggestions(projection, selection,
    				selectionArgs, sortOrder);
    	} else if (Constants.SEARCH_SEGMENT_DATA.equals(segment)) {
    		return queryData(projection, selection,
    				selectionArgs, sortOrder);
    	}
    	
        throw new UnsupportedOperationException();
    }

    private Cursor queryData(String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
    	MemorySearchableQuery query = buildSearchableQuery(
    			projection, selection, selectionArgs, sortOrder);
    	
    	final Cursor dataCursor = doQueryData(query);
    	if (dataCursor == null) {
    		return null;
    	}
    	
    	MatrixCursor outCursor = new MatrixCursor(MemorySearchableContent.COLUMNS);
        MemorySearchableContent content = null;
		try {
			if (dataCursor.moveToFirst()) {
				do {
					content = memoryToSearchableContent(dataCursor);
					if (content != null) {
						outCursor.addRow(content.toColumnValues());
					}
				} while (dataCursor.moveToNext());
			}
		} finally {
			
		}
		
		dataCursor.close();

		return outCursor;
	}

	protected Cursor querySuggestions(String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
    	MemorySearchableQuery query = buildSearchableQuery(
    			projection, selection, selectionArgs, sortOrder);
    	
    	List<MemorySearchableSuggestion> suggestions = 
    			doQuerySuggestions(query);
    	if (suggestions == null) {
    		return null;
    	}
    	
    	MatrixCursor outCursor = 
    			new MatrixCursor(MemorySearchableSuggestion.COLUMNS);
    	
    	for (MemorySearchableSuggestion suggestion : suggestions) {
    		suggestion.query = query.queryInputs;
    		suggestion.intentData = getSearchableAuthority();
    		outCursor.addRow(suggestion.toColumnValues());
		}

		return outCursor;
	}
	
	private MemorySearchableQuery buildSearchableQuery(String[] projection, 
			String selection, String[] selectionArgs, String sortOrder) {
    	MemorySearchableQuery query = null;
    	
    	if (selectionArgs != null && selectionArgs.length > 0) {
        	Logger.debug("selectionArgs[0] = %s", selectionArgs[0]);
        	
    		Gson gson = new Gson();
    		
    		query = gson.fromJson(selectionArgs[0], MemorySearchableQuery.class);
//        	Logger.debug("query = %s", query);
    	}
    	
    	return query;
	}
	
    public static String composeResourceUri(Context context, int resid) {
    	return composeResourceUri(context, null, resid);
    }

    public static String composeResourceUri(Context context,String appPacakge, int resid) {
    	if (context == null || resid <= 0) {
    		return null;
    	}
    	
    	
    	Resources res = null;
    	
    	if (appPacakge == null) {
    		res = context.getResources();
    	} else {
    		final PackageManager pkgmgr = context.getPackageManager();
    		try {
				res = pkgmgr.getResourcesForApplication(appPacakge);
			} catch (NameNotFoundException e) {
				Logger.debug("Get resources for pkg[%s] failure",
						appPacakge, e.toString());
				res = null;
			}
    	}

    	if (res == null) {
    		return null;
    	}
    	
		StringBuilder builder = new StringBuilder();
		
		builder.append(ContentResolver.SCHEME_ANDROID_RESOURCE);
		builder.append("://");
		builder.append(res.getResourcePackageName(resid));
		builder.append("/");
		builder.append(res.getResourceTypeName(resid));
		builder.append("/");
		builder.append(res.getResourceEntryName(resid));
		
		return builder.toString();
    }
    
    abstract protected Cursor doQueryData(MemorySearchableQuery searchQuery);
    abstract protected List<MemorySearchableSuggestion> doQuerySuggestions(
			MemorySearchableQuery query);

    abstract protected MemorySearchableContent memoryToSearchableContent(
    		Cursor memoryCursor);
   
    abstract protected String getSearchableAuthority();
    
}