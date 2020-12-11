package com.dailystudio.memory.searchable.queryparams;

import android.text.TextUtils;

public class TextQueryParameter extends QueryParameter {

	public String text = null;
	
	@Override
	public boolean isValid() {
		return !TextUtils.isEmpty(text);
	}

	@Override
	public String toString() {
		return String.format("%s, text[%s]",
				super.toString(), text);
	}

}
