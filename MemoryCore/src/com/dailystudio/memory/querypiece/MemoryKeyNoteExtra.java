package com.dailystudio.memory.querypiece;

import android.text.TextUtils;

import com.google.gson.Gson;

public class MemoryKeyNoteExtra {

	public boolean overTheDayEnd;
	
	public String toString() {
		return String.format("%s(0x%08x): overTheDay = %s",
				getClass().getSimpleName(),
				hashCode(),
				overTheDayEnd);
	};
	
	public String encodeToDigestExtra() {
		Gson gson = new Gson();
		
		return gson.toJson(this);
	}
	
	public Object decodeFromDigestExtra(String extra, Class<?> dataType) {
		if (TextUtils.isEmpty(extra) || dataType == null) {
			return null;
		}
		
		Gson gson = new Gson();
		
		return gson.fromJson(extra, dataType);
	}

}
