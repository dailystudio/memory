package com.dailystudio.memory.searchable.queryparams.keywords;

import com.dailystudio.datetime.CalendarUtils;
import com.dailystudio.memory.searchable.queryparams.KeywordQueryParameter;
import com.dailystudio.memory.searchable.queryparams.QueryParameter;
import com.dailystudio.memory.searchable.queryparams.TimeQueryParameter;

public class KeywordLastWeekTranslator extends AbsKeywordTranslator {

	@Override
	public QueryParameter tranlate(KeywordQueryParameter kqp) {
		if (kqp == null) {
			return null;
		}
		
		TimeQueryParameter tqp = new TimeQueryParameter();
		
		final long time = System.currentTimeMillis() - CalendarUtils.WEEK_IN_MILLIS;
		
		tqp.timeBegin = CalendarUtils.getStartOfWeek(time);
		tqp.timeEnd = CalendarUtils.getEndOfWeek(time);
		
		return tqp;
	}

}
