package com.dailystudio.memory.searchable.queryparams.keywords;

import com.dailystudio.memory.searchable.queryparams.KeywordQueryParameter;
import com.dailystudio.memory.searchable.queryparams.QueryParameter;

public abstract class AbsKeywordTranslator {

	abstract public QueryParameter tranlate(KeywordQueryParameter kqp);

}
