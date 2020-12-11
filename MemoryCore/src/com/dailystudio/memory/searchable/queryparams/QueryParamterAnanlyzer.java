package com.dailystudio.memory.searchable.queryparams;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

import com.dailystudio.development.Logger;
import com.dailystudio.memory.searchable.MemorySearchableQuery.AnalyzedResult;

public abstract class QueryParamterAnanlyzer {
	
	public AnalyzedResult analyzeKeywords(String keywords) {
		if (TextUtils.isEmpty(keywords)) {
			return null;
		}
		
		final String queryPattern = getQueryPattern();
		if (queryPattern == null) {
			return null;
		}
		
		final Pattern pattern = Pattern.compile(queryPattern);
		if (pattern == null) {
			return null;
		}
		
		final Matcher matcher = pattern.matcher(keywords);
		if (matcher == null) {
			return null;
		}
		
		AnalyzedResult result = new AnalyzedResult();
		
		result.params = new ArrayList<QueryParameter>();
		
		QueryParameter qp = null;
		while (matcher.find()) {
			qp = createParamter();
			if (qp == null) {
				continue;
			}
		
			onFillMatchedParameters(matcher, qp);

			if (qp.isValid()) {
				Logger.debug("qp = %s", qp);
				
				result.params.add(qp);
			}
			
		}
		
		if (result.params.size() <= 0) {
			return null;
		}
		
		result.remainedKeywords = matcher.replaceAll("");
		
		return result;
	}
	
	abstract protected QueryParameter createParamter();

	abstract protected String getQueryPattern();

	abstract protected void onFillMatchedParameters(
			Matcher matcher, QueryParameter queryParamter);
    
	protected void printMatcher(Matcher matcher) {
		if (matcher == null) {
			return;
		}
		
		for (int i = 0; i < matcher.groupCount(); i++) {
			Logger.debug("group[%d] = %s", i, matcher.group(i));
		} 
	}

}
