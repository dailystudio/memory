package com.dailystudio.memory.card;

import java.util.HashMap;
import java.util.Map;

public class CardElements {

	private Map<String, String> mElements = 
			new HashMap<String, String>();
	
	public void putElement(String elemKey, String elemValue) {
		mElements.put(elemKey, elemValue);
	}
	
	public String getElement(String elemKey) {
		return mElements.get(elemKey);
	}
	
}
