package com.dailystudio.memory.game;

import java.util.List;

import com.dailystudio.app.dataobject.DatabaseReader;
import com.dailystudio.dataobject.query.ExpressionToken;
import com.dailystudio.dataobject.query.Query;
import com.dailystudio.datetime.dataobject.TimeCapsuleDatabaseWriter;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.game.UncommittedAchievement.AchievementType;

import android.content.Context;

public class UncommittedAchievementModal {
	
	public static void saveUncommittedUnlockAchievement(Context context, 
			String achievementId) {
		if (context == null 
				|| achievementId == null) {
			return;
		}
		
		if (hasUncommittedAchievement(context, achievementId)) {
			return;
		}
		
		UncommittedAchievement achievement = 
				new UncommittedAchievement(context);
		
		final long now = System.currentTimeMillis();
		
		achievement.setTime(now);
		achievement.setAchievementId(achievementId);
		achievement.setAchievementType(AchievementType.STANDARD);
		
		TimeCapsuleDatabaseWriter<UncommittedAchievement> writer =
				new TimeCapsuleDatabaseWriter<UncommittedAchievement>(context,
						UncommittedAchievement.class);
		Logger.debug("[SAVED] unlock achievement: %s", achievement);
		
		writer.insert(achievement);
	}
	
	public static void saveOrUpdateUncommittedIncrementAchievement(Context context, 
			String achievementId, int incrementSteps) {
		if (context == null 
				|| achievementId == null) {
			return;
		}
		
		UncommittedAchievement achievement = findLastUncommittedIncrementAchievement(
				context, achievementId);
		
		int oldStep = 0;
		if (achievement == null) {
			achievement = new UncommittedAchievement(context);
		} else {
			oldStep = achievement.getIncrementSteps();
		}
		
		final long now = System.currentTimeMillis();
		
		achievement.setTime(now);
		achievement.setAchievementId(achievementId);
		achievement.setIncrementSteps(oldStep + incrementSteps);
		achievement.setAchievementType(AchievementType.INCREMENT);

		TimeCapsuleDatabaseWriter<UncommittedAchievement> writer =
				new TimeCapsuleDatabaseWriter<UncommittedAchievement>(context,
						UncommittedAchievement.class);
		
		if (achievement.getId() > 0) {
			Logger.debug("[UPDATED] increment achievement: %s", achievement);
			writer.update(achievement);
		} else {
			Logger.debug("[SAVED] increment achievement: %s", achievement);
			writer.insert(achievement);
		}
	}
	
	public static void removeUncommittedAchievement(Context context, 
			UncommittedAchievement achievement) {
		if (context == null || achievement == null) {
			return;
		}

		TimeCapsuleDatabaseWriter<UncommittedAchievement> writer =
				new TimeCapsuleDatabaseWriter<UncommittedAchievement>(
						context, UncommittedAchievement.class);
		
		writer.delete(achievement);
	}
	
	public static boolean hasUncommittedAchievement(Context context, 
			String achievementId) {
		if (context == null 
				|| achievementId == null) {
			return false;
		}
		
		final byte[] encrpytedAid = UncommittedAchievement.encryptString(achievementId);
		
		final DatabaseReader<UncommittedAchievement> reader = 
				new DatabaseReader<UncommittedAchievement>(context, 
						UncommittedAchievement.class);

		Query query = new Query(UncommittedAchievement.class);
		
		ExpressionToken selToken = 
				UncommittedAchievement.COLUMN_ENCRYPTED_ACHIEVEMENT_ID.eq(encrpytedAid);
		if (selToken != null) {
			query.setSelection(selToken);
		}
		
		return (reader.queryCount(query) > 0);
	}
	
	public static UncommittedAchievement findLastUncommittedIncrementAchievement(Context context, 
			String achievementId) {
		if (context == null 
				|| achievementId == null) {
			return null;
		}
		
		final byte[] encrpytedAid = UncommittedAchievement.encryptString(achievementId);
		
		final DatabaseReader<UncommittedAchievement> reader = 
				new DatabaseReader<UncommittedAchievement>(context, 
						UncommittedAchievement.class);

		Query query = new Query(UncommittedAchievement.class);
		
		ExpressionToken selToken = 
				UncommittedAchievement.COLUMN_ENCRYPTED_ACHIEVEMENT_ID.eq(encrpytedAid);
		if (selToken != null) {
			query.setSelection(selToken);
		}
		
		return reader.queryLastOne(query);
	}
	
	public static List<UncommittedAchievement> listUncommittedAchievements(
			Context context) {
		if (context == null) {
			return null;
		}
		
		final DatabaseReader<UncommittedAchievement> reader = 
				new DatabaseReader<UncommittedAchievement>(context, 
						UncommittedAchievement.class);

		Query query = new Query(UncommittedAchievement.class);
		
		return reader.query(query);
	}
	
}
