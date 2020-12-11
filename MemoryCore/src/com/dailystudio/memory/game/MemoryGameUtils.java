package com.dailystudio.memory.game;

import com.dailystudio.memory.Constants;

import android.content.Context;
import android.content.Intent;

public class MemoryGameUtils {

	public static void unlockAchievement(Context context, String achievementId) {
		if (context == null || achievementId == null) {
			return;
		}
		
		Intent i = new Intent();

		i.setClass(context.getApplicationContext(),
				MemoryGameService.class);

		i.setAction(Constants.ACTION_UNLOCK_ACHIVEMENT);
		i.putExtra(Constants.EXTRA_ACHIEVEMENT_ID, 
				achievementId);
		
		context.startService(i);
	}
	
	public static void incrementAchievement(Context context, String achievementId, 
			int incrementSteps) {
		if (context == null 
				|| achievementId == null
				|| incrementSteps <= 0) {
			return;
		}
		
		Intent i = new Intent();

		i.setClass(context.getApplicationContext(),
				MemoryGameService.class);

		i.setAction(Constants.ACTION_INCREMENT_ACHIVEMENT);
		i.putExtra(Constants.EXTRA_ACHIEVEMENT_ID, 
				achievementId);
		i.putExtra(Constants.EXTRA_ACHIEVEMENT_INCREMENT_STEPS, incrementSteps);
		
		context.startService(i);
	}

	public static void submitLeaderboardScore(Context context, String leaderboardId, 
			long score) {
		if (context == null 
				|| leaderboardId == null
				|| score <= 0) {
			return;
		}
		
		Intent i = new Intent();

		i.setClass(context.getApplicationContext(),
				MemoryGameService.class);

		i.setAction(Constants.ACTION_SUBMIT_LEADERBOARD_SCORE);
		i.putExtra(Constants.EXTRA_LEADERBOARD_ID, 
				leaderboardId);
		i.putExtra(Constants.EXTRA_LEADERBOARD_SCORE, score);
		
		context.startService(i);
	}

}
