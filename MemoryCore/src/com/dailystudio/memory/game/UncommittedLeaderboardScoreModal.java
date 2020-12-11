package com.dailystudio.memory.game;

import java.util.List;

import com.dailystudio.app.dataobject.DatabaseReader;
import com.dailystudio.dataobject.query.ExpressionToken;
import com.dailystudio.dataobject.query.Query;
import com.dailystudio.datetime.dataobject.TimeCapsuleDatabaseWriter;
import com.dailystudio.development.Logger;

import android.content.Context;

public class UncommittedLeaderboardScoreModal {
	
	public static void saveOrUpdateUncommittedLeaderboardScore(Context context, 
			String leaderboardId, long score) {
		if (context == null 
				|| leaderboardId == null) {
			return;
		}
		
		UncommittedLeaderboardScore leaderboardScore = 
				findLastUncommittedLeaderboardScore(
						context, leaderboardId);
		
		if (leaderboardScore == null) {
			leaderboardScore = new UncommittedLeaderboardScore(context);
		}
		
		final long now = System.currentTimeMillis();
		
		leaderboardScore.setTime(now);
		leaderboardScore.setLeaderboardId(leaderboardId);
		leaderboardScore.setLeaderboardScore(score);
		
		TimeCapsuleDatabaseWriter<UncommittedLeaderboardScore> writer =
				new TimeCapsuleDatabaseWriter<UncommittedLeaderboardScore>(context,
						UncommittedLeaderboardScore.class);
		
		if (leaderboardScore.getId() > 0) {
			Logger.debug("[UPDATED] leaderboard score: %s", leaderboardScore);
			writer.update(leaderboardScore);
		} else {
			Logger.debug("[SAVED] leaderboard score: %s", leaderboardScore);
			writer.insert(leaderboardScore);
		}
	}
	
	public static void removeUncommitedLeaderboardScore(Context context, 
			UncommittedLeaderboardScore leaderboardScore) {
		if (context == null || leaderboardScore == null) {
			return;
		}

		TimeCapsuleDatabaseWriter<UncommittedLeaderboardScore> writer =
				new TimeCapsuleDatabaseWriter<UncommittedLeaderboardScore>(
						context, UncommittedLeaderboardScore.class);
		
		writer.delete(leaderboardScore);
	}

	public static UncommittedLeaderboardScore findLastUncommittedLeaderboardScore(
			Context context, 
			String leaderboardId) {
		if (context == null 
				|| leaderboardId == null) {
			return null;
		}
		
		final byte[] encrpytedLid = 
				UncommittedLeaderboardScore.encryptString(leaderboardId);
		
		final DatabaseReader<UncommittedLeaderboardScore> reader = 
				new DatabaseReader<UncommittedLeaderboardScore>(context, 
						UncommittedLeaderboardScore.class);

		Query query = new Query(UncommittedLeaderboardScore.class);
		
		ExpressionToken selToken = 
				UncommittedLeaderboardScore.COLUMN_ENCRYPTED_LEADERBOARD_ID.eq(
						encrpytedLid);
		if (selToken != null) {
			query.setSelection(selToken);
		}
		
		return reader.queryLastOne(query);
	}
	
	public static List<UncommittedLeaderboardScore> listUncommittedLeaderboardScores(
			Context context) {
		if (context == null) {
			return null;
		}
		
		final DatabaseReader<UncommittedLeaderboardScore> reader = 
				new DatabaseReader<UncommittedLeaderboardScore>(context, 
						UncommittedLeaderboardScore.class);

		Query query = new Query(UncommittedLeaderboardScore.class);
		
		return reader.query(query);
	}
	
}
