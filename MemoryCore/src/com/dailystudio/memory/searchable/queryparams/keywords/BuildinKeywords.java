package com.dailystudio.memory.searchable.queryparams.keywords;

import java.util.ArrayList;
import java.util.List;

public class BuildinKeywords {
	
	public static final String KEYWORD_TODAY = "today";
	public static final String KEYWORD_YESTERDAY = "yesterday";
	public static final String KEYWORD_THIS_WEEK = "thisweek";
	public static final String KEYWORD_LAST_WEEK = "lastweek";
	public static final String KEYWORD_THIS_MONTH = "thismonth";
	public static final String KEYWORD_LAST_MONTH = "lastmonth";
	
	private static String[] sKeywords = {
		KEYWORD_TODAY,
		KEYWORD_YESTERDAY,
		KEYWORD_THIS_WEEK,
		KEYWORD_LAST_WEEK,
		KEYWORD_THIS_MONTH,
		KEYWORD_LAST_MONTH,
	};

	public static String[] listBuildinKeywords() {
		return sKeywords;
	}
	
	public static String[] matchedKeywords(String key) {
		if (sKeywords == null) {
			return null;
		}
		
		List<String> matches = new ArrayList<String>();
		for (String keyword: sKeywords) {
			if (keyword.contains(key)) {
				matches.add(keyword);
			}
		}
		
		return matches.toArray(new String[0]);
	}
	
}
