package com.dailystudio.memory.utils;

import com.dailystudio.memory.Constants;

import android.content.res.Resources;
import android.util.AttributeSet;

public class XmlHelper {
    
	public static String parseString(AttributeSet attrs, int index, Resources res) {
    	if (attrs == null || res == null) {
    		return null;
    	}
    	
		String label = null;

		int resId = attrs.getAttributeResourceValue(index, -1);
		if (resId > 0) {
			label = res.getString(resId);
		}
		
		if (label != null) {
			return label;
		}
		
		return attrs.getAttributeValue(index);
    }

	public static String parseLabel(AttributeSet attrs, int index, Resources res) {
    	if (attrs == null || res == null) {
    		return null;
    	}
    	
		String label = null;

		int resId = attrs.getAttributeResourceValue(index, -1);
		if (resId > 0) {
			label = res.getString(resId);
		}
		
		if (label != null) {
			return label;
		}
		
		return attrs.getAttributeValue(index);
    }
    
	public static int parseResource(AttributeSet attrs, int index, Resources res) {
    	if (attrs == null || res == null) {
    		return Constants.INVALID_RESOURCE_ID;
    	}
    	
		return attrs.getAttributeResourceValue(index, Constants.INVALID_RESOURCE_ID);
    }
    
}
