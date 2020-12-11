package com.dailystudio.memory.searchable.queryparams.keywords;

import com.dailystudio.datetime.CalendarUtils;
import com.dailystudio.memory.searchable.queryparams.KeywordQueryParameter;
import com.dailystudio.memory.searchable.queryparams.QueryParameter;
import com.dailystudio.memory.searchable.queryparams.TimeQueryParameter;

public class KeywordYesterdayTranslator extends AbsKeywordTranslator {

	@Override
	public QueryParameter tranlate(KeywordQueryParameter kqp) {
		if (kqp == null) {
			return null;
		}
		
		TimeQueryParameter tqp = new TimeQueryParameter();
		
		final long time = System.currentTimeMillis() - CalendarUtils.DAY_IN_MILLIS;
		
		tqp.timeBegin = CalendarUtils.getStartOfDay(time);
		tqp.timeEnd = CalendarUtils.getEndOfDay(time);
		
		return tqp;
	}

}
