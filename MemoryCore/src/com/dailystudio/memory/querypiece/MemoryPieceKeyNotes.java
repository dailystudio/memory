package com.dailystudio.memory.querypiece;

import com.dailystudio.datetime.CalendarUtils;
import com.dailystudio.development.Logger;

import android.text.TextUtils;

public class MemoryPieceKeyNotes {
	
	public static final String KEY_NOTE_USE_MAIN_COLOR = "@kn_main_color";
	public static final String KEY_NOTE_USE_SUB_COLOR = "@kn_sub_color";
	
	public static final String KEY_NOTE_USE_MAIN_STYLE_START = "<keynote_main>";
	public static final String KEY_NOTE_USE_SUB_STYLE_START = "<keynote_sub>";
	public static final String KEY_NOTE_USE_MAIN_STYLE_END = "</keynote_main>";
	public static final String KEY_NOTE_USE_SUB_STYLE_END = "</keynote_sub>";
	
	public static final String ARG_KEY_NOTES = "key-notes";
	
	private static final String ARG_SPLITTER = ":";

	public static long[] extractTimeSpan(String arg) {
		final long now = System.currentTimeMillis();
		final long defstart = CalendarUtils.getStartOfDay(now);
		final long defend = CalendarUtils.getEndOfDay(now);
		
		long[] span = new long[2];
		
		span[0] = defstart;
		span[1] = defend;
		
		if (TextUtils.isEmpty(arg)) {
			return span;
		}
		
		String[] parts = arg.split(ARG_SPLITTER);
		if (parts == null || parts.length < 3) {
			return span;
		}
		
		try {
			span[0] = Long.parseLong(parts[1]);
			span[1] = Long.parseLong(parts[2]);
		} catch (NumberFormatException e) {
			Logger.warnning("could not parse time span from arg[%s]: %s",
					arg, e.toString());
			
			span[0] = defstart;
			span[1] = defend;
		}
		
		return span;
	}
	
	public static String composeTimeSpan(long start, long end) {
		if (start >= end) {
			return ARG_KEY_NOTES;
		}
		
		StringBuilder builder = new StringBuilder(ARG_KEY_NOTES);
		
		builder.append(ARG_SPLITTER);
		builder.append(start);
		builder.append(ARG_SPLITTER);
		builder.append(end);
		
		return builder.toString();
	}
	
}
