package com.dailystudio.memory.card;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dailystudio.app.utils.BitmapUtils;
import com.dailystudio.app.utils.FileUtils;
import com.dailystudio.development.Logger;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.text.TextUtils;

public abstract class CardBuilder {
	
	private final static String ELEMENT_TAG_PATTERN =
			"(@element/)(.[^<\"\'\b\t\n\f\r]*)";

	private static final int MATCHER_GROUP_ELE_KEY = 2;
	private static final int MATCHER_GROUP_COUNT = 2;
	
	private String mHtmlTemplFile;
	private String mHtmlTargetFile;
	
	public CardBuilder(String templFile, String targetFile) {
		mHtmlTemplFile = templFile;
		mHtmlTargetFile = targetFile;
	}
	
	public boolean buildCard(Context context) {
		if (context == null) {
			return false;
		}
		
		final CardElements elements = new CardElements();
		
		buildCardElements(context, elements);
		
		String cardContent = composeCard(context, elements);
		if (TextUtils.isEmpty(cardContent)) {
			return false;
		}
		
		String cardsDir = CardDirectories.getCardsDirectory(context);
		if (FileUtils.checkOrCreateNoMediaDirectory(cardsDir) == false) {
			Logger.warnning("could not create cards dir: %s",
					cardsDir);
			
			return false;
		}
		
		String cardFile = String.format("%s/%s",
				cardsDir, getTargetFile());
		if (FileUtils.checkOrCreateFile(cardFile) == false) {
			Logger.warnning("could not create card file: %s",
					cardFile);
			
			return false;
		}
		
		boolean ret = false;
		try {
			FileUtils.writeFileContent(cardFile, cardContent);
			
			ret = true;
		} catch (IOException e) {
			Logger.warnning("could not write card file[%s]: %s",
					cardFile, e.toString());
			ret = false;
		}
		
		Logger.debug("write card file[%s]: ret = %s",
				cardFile, ret);
		
		return ret;
	}
	
	public String composeCard(Context context, CardElements elements) {
		if (context == null || TextUtils.isEmpty(mHtmlTemplFile)) {
			return null;
		}
		
		String htmlContent = null;
        try {
        	htmlContent = getHtmlInAssets(context, mHtmlTemplFile);
        } catch (IOException e) {
			Logger.warnning("could not load card templ from [%s/%s]: %s",
					CardDirectories.CARDS_IN_ASSETS,
					mHtmlTemplFile,
					e.toString());
			
        	htmlContent = null;
        }
        if (TextUtils.isEmpty(htmlContent)) {
        	return null;
        }

		Pattern p = Pattern.compile(ELEMENT_TAG_PATTERN);
		Matcher matcher = p.matcher(htmlContent);

		StringBuffer newHtmlContent = new StringBuffer();
		
		String elemValue = null;
		String elemKey = null;
		while (matcher.find()) {
			Logger.debug("matcher = %s (count = %d)", 
					matcher, matcher.groupCount());
			if (matcher.groupCount() < MATCHER_GROUP_COUNT) {
				continue;
			}
			
			elemKey = matcher.group(MATCHER_GROUP_ELE_KEY);
			if (elements != null) {
				elemValue = elements.getElement(elemKey);
			}
			Logger.debug("elemKey = %s [elemValue = %s]", 
					elemKey, elemValue);
			
			if (elemValue != null) {
				matcher.appendReplacement(newHtmlContent, 
						elemValue);
			}
		}
		
		matcher.appendTail(newHtmlContent);
		
		final String cardContent = newHtmlContent.toString();
		Logger.debug("cardContent[Html] = [%s]", cardContent);

		return cardContent;
	}

    private String getHtmlInAssets(Context context, String assetFile) throws IOException {
        if (context == null || assetFile == null) {
            return null;
        }
        
		AssetManager assetmgr = context.getAssets();
		if (assetmgr == null) {
			return null;
		}
	
		return FileUtils.getAssetFileContent(context,
				String.format("%s/%s",
				CardDirectories.CARDS_IN_ASSETS, assetFile));
    }
    
    public String getTemplFile() {
    	return mHtmlTemplFile;
    }

    public String getTargetFile() {
    	return mHtmlTargetFile;
    }

	protected String bitmapToBase64String(Bitmap bitmap) {
		return BitmapUtils.bitmapToBase64String(bitmap);
	}

	abstract protected void buildCardElements(Context context, CardElements elements);

}
