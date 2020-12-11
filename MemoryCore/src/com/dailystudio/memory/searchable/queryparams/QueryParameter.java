package com.dailystudio.memory.searchable.queryparams;

public class QueryParameter {
	
	public final static String QUERY_TYPE_DATE = "time";
	public final static String QUERY_TYPE_TEXT = "text";
	public final static String QUERY_TYPE_KEYWORD = "keywords";
	
	public String queryType;
	
	public boolean isValid() {
		return true;
	}

	@Override
	public String toString() {
		return String.format("%s(0x%08x): type = %s [valid: %s]",
				getClass().getSimpleName(),
				hashCode(),
				queryType,
				isValid());
	}
	
}
