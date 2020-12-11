package com.dailystudio.memory.card;

import java.io.File;

import android.content.Context;

public class CardDirectories {
	
	public static final String CARDS_IN_ASSETS = "cards";

	
	private static final String CARDS = "/cards/";

	public static final String getCardsDirectory(Context context) {
		final File extDir = context.getExternalFilesDir(null);
		if (extDir == null) {
			return null;
		}
		
		final File cardsDir = new File(extDir, CARDS);
		
		return cardsDir.getAbsolutePath();
	}

}
