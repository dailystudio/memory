package com.dailystudio.memory.plugin.privacy;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;

public class PrivacyPolicyXmlParser {
	
    public static PrivacyPolicy parse(XmlPullParser parser) {
		if (parser == null) {
			return null;
    	}
    	
    	PrivacyPolicy pp = new PrivacyPolicy();
    	
    	boolean success = false;
    	try {
    		final int depth = parser.getDepth();
    		boolean gotRoot = false;
    		
		    int type;
		    
	        while (((type = parser.next()) != XmlPullParser.END_TAG ||
	                parser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {
	
	            if (type != XmlPullParser.START_TAG) {
	                continue;
	            }
	            
			    String nodeName = parser.getName();
			    if (gotRoot == false) {
				    if (!Constants.XML_TAG_PRIVACY.equals(nodeName)) {
				    	Logger.warnning("meta-data does not start with tag %s", 
				    			Constants.XML_TAG_PRIVACY);
				        
				        return null;
				    }
				    
				    gotRoot = true;
			    }
			    
			    if (Constants.XML_TAG_COLLECTION.equals(nodeName)) {
			    	pp.collection = readColltection(parser);
			    }
	        }

	        success = true;
    	} catch (XmlPullParserException e) {
    		e.printStackTrace();
    		
    		success = false;
    	} catch (IOException e) {
			e.printStackTrace();
    		
    		success = false;
		}
    	
    	if (success) {
    		return pp;
    	}
    	
    	return null;
    }
    
    private static String readColltection(XmlPullParser parser) throws IOException, XmlPullParserException {
     	if (parser == null) {
     		return null;
     	}
     	
    	parser.require(XmlPullParser.START_TAG, null, Constants.XML_TAG_COLLECTION);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, Constants.XML_TAG_COLLECTION);
        
        return title;
    }

    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
     	if (parser == null) {
     		return null;
     	}
     	
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        
        return result;
    }

}
