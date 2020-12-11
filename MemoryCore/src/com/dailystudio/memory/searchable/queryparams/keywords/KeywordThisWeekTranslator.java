package com.dailystudio.memory.searchable.queryparams.keywords;

import com.dailystudio.datetime.CalendarUtils;
import com.dailystudio.memory.searchable.queryparams.KeywordQueryParameter;
import com.dailystudio.memory.searchable.queryparams.QueryParameter;
import com.dailystudio.memory.searchable.queryparams.TimeQueryParameter;

public class KeywordThisWeekTranslator extends AbsKeywordTranslator {

	@Override
	public QueryParameter tranlate(KeywordQueryParameter kqp) {
		if (kqp == null) {
			return null;
		}
		
		TimeQueryParameter tqp = new TimeQueryParameter();
		
		final long now = System.currentTimeMillis();
		
		tqp.timeBegin = CalendarUtils.getStartOfWeek(now);
		tqp.timeEnd = CalendarUtils.getEndOfWeek(now);
		
		return tqp;
	}

}
