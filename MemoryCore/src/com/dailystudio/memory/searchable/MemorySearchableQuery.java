package com.dailystudio.memory.searchable;

import java.util.ArrayList;
import java.util.List;

import com.dailystudio.memory.searchable.queryparams.KeywordQueryParameter;
import com.dailystudio.memory.searchable.queryparams.KeywordQueryParameterAnanlzyer;
import com.dailystudio.memory.searchable.queryparams.QueryParameter;
import com.dailystudio.memory.searchable.queryparams.QueryParamterAnanlyzer;
import com.dailystudio.memory.searchable.queryparams.TextQueryParameter;
import com.dailystudio.memory.searchable.queryparams.TextQueryParameterAnanlzyer;
import com.dailystudio.memory.searchable.queryparams.TimeQueryParameter;
import com.dailystudio.memory.searchable.queryparams.TimeQueryParameterAnanlzyer;

public class MemorySearchableQuery {
	
	public static class AnalyzedResult {
	
		public List<QueryParameter> params;
		public String remainedKeywords;
		
		@Override
		public String toString() {
			return String.format("%s(0x%08x): remainedKeywords = %s, params = %s",
					getClass().getSimpleName(),
					hashCode(),
					remainedKeywords,
					params);
		}
		
	}
	
	private static QueryParamterAnanlyzer[] sAnalyzers = {
		new KeywordQueryParameterAnanlzyer(),
		new TextQueryParameterAnanlzyer(),
		new TimeQueryParameterAnanlzyer(),
	};
	
	
	public String queryInputs;
	public List<KeywordQueryParameter> keywordQueryParams;
	public List<TimeQueryParameter> timeQueryParams;
	public List<TextQueryParameter> textQueryParams;
	
	public void buildFromInputs(String inputs) {
		if (inputs == null) {
			return;
		}
		
		this.queryInputs = inputs;
		
		keywordQueryParams = new ArrayList<KeywordQueryParameter>();
		timeQueryParams = new ArrayList<TimeQueryParameter>();
		textQueryParams = new ArrayList<TextQueryParameter>();
		
		AnalyzedResult result = null;
		List<QueryParameter> subParams = null;
		for (QueryParamterAnanlyzer ananlyzer: sAnalyzers) {
			result = ananlyzer.analyzeKeywords(inputs);
			if (result == null) {
				continue;
			}
			
			subParams = result.params;
			if (subParams == null || subParams.size() <= 0) {
				continue;
			}
			
			for (QueryParameter qp: subParams) {
				if (qp instanceof KeywordQueryParameter) {
					qp = KeywordQueryParameter.checkOrTranlateBuildinKeyword(
							(KeywordQueryParameter)qp);
				} 
				
				dispatchAndAddQueryParameters(qp);
			}
			
			inputs = result.remainedKeywords;
		}
	}
	
	private void dispatchAndAddQueryParameters(QueryParameter qp) {
		if (qp instanceof KeywordQueryParameter) {
			keywordQueryParams.add((KeywordQueryParameter)qp);
		} else if (qp instanceof TimeQueryParameter) {
			timeQueryParams.add((TimeQueryParameter)qp);
		} else if (qp instanceof TextQueryParameter) {
			textQueryParams.add((TextQueryParameter)qp);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(getClass().getSimpleName());

		builder.append(String.format("keywordQueryParams  : %d\n", 
				(keywordQueryParams == null ? 0 : keywordQueryParams.size())));
		if (keywordQueryParams != null) {
			for (QueryParameter qp: keywordQueryParams) {
				builder.append(qp.toString());
			}
		}

		builder.append(String.format("timeQueryParams  : %d\n", 
				(timeQueryParams == null ? 0 : timeQueryParams.size())));
		if (timeQueryParams != null) {
			for (QueryParameter qp: timeQueryParams) {
				builder.append(qp.toString());
			}
		}

		builder.append(String.format("textQueryParams  : %d\n", 
				(textQueryParams == null ? 0 : textQueryParams.size())));
		if (textQueryParams != null) {
			for (QueryParameter qp: textQueryParams) {
				builder.append(qp.toString());
			}
		}
		
		return builder.toString();
	}
	
}
