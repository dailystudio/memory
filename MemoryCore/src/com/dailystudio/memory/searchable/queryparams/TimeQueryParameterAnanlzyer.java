package com.dailystudio.memory.searchable.queryparams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

import com.dailystudio.datetime.CalendarUtils;
import com.dailystudio.development.Logger;

import android.text.TextUtils;

public class TimeQueryParameterAnanlzyer extends QueryParamterAnanlyzer {
	
    @SuppressWarnings("unused")
	private static final String OLD_TIME_QUERY_PATTERN = 
    		"([tT][iI][mM][eE]|[tT])\\s*((\\d{8}|\\d{4,6})-(\\d{8}|\\d{4,6})|(\\d{8}|\\d{4,6}))";
    
    private static final String TIME_QUERY_PATTERN = 
    		"((\\d{8}|\\d{6})-(\\d{8}|\\d{6})|(\\d{8}|\\d{6}))";
 
    private static final String TIME_RANGE_SPLITTER = "-"; 
    
    private static final String TIME_8DIGITS_FORMAT = "yyyyMMdd"; 
    private static final String TIME_6DIGITS_FORMAT = "yyMMdd"; 
   
    private static final int MATCHER_GROUP_COUNT_MIN = 2;
	private static final int MATCHER_GROUP_QUERY_TIME = 1;

	@Override
	public String getQueryPattern() {
		return TIME_QUERY_PATTERN;
	}

	@Override
	protected QueryParameter createParamter() {
		return new TimeQueryParameter();
	}

	@Override
	protected void onFillMatchedParameters(Matcher matcher,
			QueryParameter queryParamter) {
		if (matcher == null ||
				queryParamter instanceof TimeQueryParameter == false) {
			return;
		}
		
		final TimeQueryParameter tqp = (TimeQueryParameter) queryParamter;
		
		tqp.queryType = QueryParameter.QUERY_TYPE_DATE;
		
		if (matcher.groupCount() < MATCHER_GROUP_COUNT_MIN) {
			return;
		}
		
		final String timeParams = matcher.group(MATCHER_GROUP_QUERY_TIME);
		if (TextUtils.isEmpty(timeParams)) {
			return;
		}
		
		if (timeParams.contains(TIME_RANGE_SPLITTER)) {
			analyzeTimeRange(tqp, timeParams);
		} else {
			final long time = analyzeTime(timeParams);
			
			tqp.timeBegin = CalendarUtils.getStartOfDay(time);
			tqp.timeEnd = CalendarUtils.getEndOfDay(time);
		}
	}

	private void analyzeTimeRange(TimeQueryParameter queryParameter, String timeParams) {
		if (queryParameter == null || timeParams == null) {
			return;
		}
		
		String[] rangeParts = timeParams.split(TIME_RANGE_SPLITTER);
		if (rangeParts == null || rangeParts.length != 2) {
			return;
		}
		
		queryParameter.timeBegin = CalendarUtils.getStartOfDay(analyzeTime(rangeParts[0]));
		queryParameter.timeEnd = CalendarUtils.getEndOfDay(analyzeTime(rangeParts[1]));
	}

	private long analyzeTime(String timeParams) {
		if (timeParams == null) {
			return -1l;
		}
		
		SimpleDateFormat sdf = null;
		if (timeParams.length() >= 8) {
			sdf = new SimpleDateFormat(TIME_8DIGITS_FORMAT);
		} else {
			sdf = new SimpleDateFormat(TIME_6DIGITS_FORMAT);
		}
		
		Date date = null;
		try {
			date = sdf.parse(timeParams);
		} catch (ParseException e) {
			Logger.warnning("parse time failure: %s", e.toString());
			
			date = null;
		}
		
		if (date == null) {
			return -1l;
		}
		
		return date.getTime();
	}

}
