package com.dailystudio.memory.searchable.queryparams;

import java.util.regex.Matcher;

import android.text.TextUtils;

public class KeywordQueryParameterAnanlzyer extends QueryParamterAnanlyzer {
	
    private static final String KEYWORD_QUERY_PATTERN = 
    		"(@([a-zA-Z]+))";
    
    private static final String KEYWORD_PREFIX = "@";
 
    private static final int MATCHER_GROUP_COUNT_MIN = 2;
	private static final int MATCHER_GROUP_QUERY_KEYWORD = 1;

	@Override
	public String getQueryPattern() {
		return KEYWORD_QUERY_PATTERN;
	}

	@Override
	protected QueryParameter createParamter() {
		return new KeywordQueryParameter();
	}

	@Override
	protected void onFillMatchedParameters(Matcher matcher,
			QueryParameter queryParamter) {
		if (matcher == null ||
				queryParamter instanceof KeywordQueryParameter == false) {
			return;
		}
		
		printMatcher(matcher);
		
		final KeywordQueryParameter kqp = (KeywordQueryParameter) queryParamter;
		
		kqp.queryType = QueryParameter.QUERY_TYPE_KEYWORD;
		
		if (matcher.groupCount() < MATCHER_GROUP_COUNT_MIN) {
			return;
		}
		
		final String keywordParams = matcher.group(MATCHER_GROUP_QUERY_KEYWORD);
		if (TextUtils.isEmpty(keywordParams)) {
			return;
		}
		
		kqp.keyword = keywordParams.replace(KEYWORD_PREFIX, "");
	}

}
