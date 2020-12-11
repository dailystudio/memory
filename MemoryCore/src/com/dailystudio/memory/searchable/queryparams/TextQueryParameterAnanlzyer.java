package com.dailystudio.memory.searchable.queryparams;

import java.util.regex.Matcher;

import android.text.TextUtils;

public class TextQueryParameterAnanlzyer extends QueryParamterAnanlyzer {
	
    private static final String TEXT_QUERY_PATTERN = 
    		"([\u4E00-\u9FFF]|[a-zA-Z])*";
 
    private static final int MATCHER_GROUP_COUNT_MIN = 1;
	private static final int MATCHER_GROUP_QUERY_TEXT = 0;

	@Override
	public String getQueryPattern() {
		return TEXT_QUERY_PATTERN;
	}

	@Override
	protected QueryParameter createParamter() {
		return new TextQueryParameter();
	}

	@Override
	protected void onFillMatchedParameters(Matcher matcher,
			QueryParameter queryParamter) {
		if (matcher == null ||
				queryParamter instanceof TextQueryParameter == false) {
			return;
		}
		
		final TextQueryParameter tqp = (TextQueryParameter) queryParamter;
		
		tqp.queryType = QueryParameter.QUERY_TYPE_TEXT;
		
		if (matcher.groupCount() < MATCHER_GROUP_COUNT_MIN) {
			return;
		}
		
		final String textParams = matcher.group(MATCHER_GROUP_QUERY_TEXT);
		if (TextUtils.isEmpty(textParams)) {
			return;
		}
		
		tqp.text = textParams;
	}

}
