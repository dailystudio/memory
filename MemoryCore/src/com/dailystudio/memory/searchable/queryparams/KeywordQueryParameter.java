package com.dailystudio.memory.searchable.queryparams;

import java.util.HashMap;
import java.util.Map;

import com.dailystudio.memory.searchable.queryparams.keywords.AbsKeywordTranslator;
import com.dailystudio.memory.searchable.queryparams.keywords.KeywordLastWeekTranslator;
import com.dailystudio.memory.searchable.queryparams.keywords.KeywordThisMonthTranslator;
import com.dailystudio.memory.searchable.queryparams.keywords.KeywordThisWeekTranslator;
import com.dailystudio.memory.searchable.queryparams.keywords.KeywordTodayTranslator;
import com.dailystudio.memory.searchable.queryparams.keywords.KeywordYesterdayTranslator;
import com.dailystudio.memory.searchable.queryparams.keywords.BuildinKeywords;

import android.text.TextUtils;

public class KeywordQueryParameter extends QueryParameter {

	private static Map<String, AbsKeywordTranslator> sTranlators =
			new HashMap<String, AbsKeywordTranslator>();
	
	static {
		sTranlators.put(BuildinKeywords.KEYWORD_TODAY, new KeywordTodayTranslator());
		sTranlators.put(BuildinKeywords.KEYWORD_YESTERDAY, new KeywordYesterdayTranslator());
		sTranlators.put(BuildinKeywords.KEYWORD_THIS_WEEK, new KeywordThisWeekTranslator());
		sTranlators.put(BuildinKeywords.KEYWORD_LAST_WEEK, new KeywordLastWeekTranslator());
		sTranlators.put(BuildinKeywords.KEYWORD_THIS_MONTH, new KeywordThisMonthTranslator());
	}
	
	public String keyword = null;
	
	@Override
	public boolean isValid() {
		return !TextUtils.isEmpty(keyword);
	}

	@Override
	public String toString() {
		return String.format("%s, keyword[%s]",
				super.toString(), keyword);
	}

	public static QueryParameter checkOrTranlateBuildinKeyword(KeywordQueryParameter kqp) {
		if (kqp == null || kqp.keyword == null) {
			return kqp;
		}
		
		final AbsKeywordTranslator translator = 
				sTranlators.get(kqp.keyword.toLowerCase());
		if (translator == null) {
			return kqp;
		}
		
		return translator.tranlate(kqp);
	}

}
