package com.dailystudio.memory.searchable;

import com.dailystudio.development.Logger;

import android.app.SearchManager;
import android.database.Cursor;

public class MemorySearchableContent {
	
    public static final String[] COLUMNS = {
        "_id", 
        SearchManager.SUGGEST_COLUMN_TEXT_1,
        SearchManager.SUGGEST_COLUMN_TEXT_2,
        SearchManager.SUGGEST_COLUMN_INTENT_DATA,
        SearchManager.SUGGEST_COLUMN_INTENT_ACTION,
        SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA,
        SearchManager.SUGGEST_COLUMN_SHORTCUT_ID,
        SearchManager.SUGGEST_COLUMN_ICON_1,
        SearchManager.SUGGEST_COLUMN_ICON_2,
    };

    public int id;
    public String text1;
    public String text2;
    public String icon1ResId;
    public String icon2ResId;
    public String time;
   
	public Object[] toColumnValues() {
		return new Object[] { 
			id, // _id
            text1, // text1
            text2, // text2
            text1, 
            "android.intent.action.SEARCH", // action
            time,
            SearchManager.SUGGEST_NEVER_MAKE_SHORTCUT,
            icon1ResId,
            icon2ResId,
		};
	}
	
	public static MemorySearchableContent parseFromCursor(Cursor c) {
		if (c == null) {
			return null;
		}
		
		MemorySearchableContent content = 
				new MemorySearchableContent();
		
		try {
			final int idColumnIndex = 
					c.getColumnIndexOrThrow("_id");
			final int text1ColumnIndex = 
					c.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1);
			final int text2ColumnIndex = 
					c.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_2);
			final int icon1ColumnIndex = 
					c.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_ICON_1);
			final int icon2ColumnIndex = 
					c.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_ICON_2);
			final int timeColumnIndex = 
					c.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA);
			
			if (c.isNull(idColumnIndex) == false) {
				content.id = c.getInt(idColumnIndex);
			}
			
			if (c.isNull(text1ColumnIndex) == false) {
				content.text1 = c.getString(text1ColumnIndex);
			}
			
			if (c.isNull(text2ColumnIndex) == false) {
				content.text2 = c.getString(text2ColumnIndex);
			}
			
			if (c.isNull(icon1ColumnIndex) == false) {
				content.icon1ResId = c.getString(icon1ColumnIndex);
			}
			
			if (c.isNull(icon2ColumnIndex) == false) {
				content.icon2ResId = c.getString(icon2ColumnIndex);
			}
			
			if (c.isNull(timeColumnIndex) == false) {
				content.time = c.getString(timeColumnIndex);
			}
		} catch (IllegalArgumentException e) {
			Logger.debug("parser cursor data failure: %s", e.toString());
			
			content = null;
		}

		return content;
	}
   
}