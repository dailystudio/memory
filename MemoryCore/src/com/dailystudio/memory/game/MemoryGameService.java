package com.dailystudio.memory.game;

import java.util.List;

import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;
import com.dailystudio.memory.game.UncommittedAchievement.AchievementType;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

public class MemoryGameService extends Service {
	
	private final class ServiceHandler extends Handler {
	      
		public ServiceHandler(Looper looper) {
			super(looper);
		}
	      
		@Override
		public void handleMessage(Message msg) {
			if (msg.obj instanceof Intent == false) {
				Logger.warnning("invalid msg object: [%s]",
						msg.obj);
				
				return;
			}
			
			Intent srvIntent = (Intent)msg.obj;
			
			handleIntent(srvIntent);
		}
		
		private void handleIntent(Intent srvIntent) {
			if (srvIntent == null) {
				return;
			}
			
			final String action = srvIntent.getAction();
			if (action == null) {
				return;
			}
			
			final Context appContext = getApplicationContext();
			
			final boolean fromLocalDB = srvIntent.getBooleanExtra(
					Constants.EXTRA_FROM_LOCAL_DB, false);
			
			if (Constants.ACTION_UNLOCK_ACHIVEMENT.equals(action)) {
				final String aid = srvIntent.getStringExtra(
						Constants.EXTRA_ACHIEVEMENT_ID);
				if (aid == null) {
					return;
				}
				
				Logger.debug("GameService unlock achievement: %s, [from db: %s]",
						aid, fromLocalDB);
				
				saveAsUncommittedUnlockAchievement(appContext, aid);
			} else if (Constants.ACTION_INCREMENT_ACHIVEMENT.equals(action)) {
				final String aid = srvIntent.getStringExtra(
						Constants.EXTRA_ACHIEVEMENT_ID);
				if (aid == null) {
					return;
				}
				
				final int increment = srvIntent.getIntExtra(
						Constants.EXTRA_ACHIEVEMENT_INCREMENT_STEPS, -1);
				Logger.debug("GameService increment achievement: %s, inc: %d",
						aid, increment);

				if (increment > 0) {
					saveAsUncommittedIncrementAchievement(
							appContext, aid, increment);
				}
			} else if (Constants.ACTION_SUBMIT_LEADERBOARD_SCORE.equals(action)) {
				final String lid = srvIntent.getStringExtra(
						Constants.EXTRA_LEADERBOARD_ID);
				if (lid == null) {
					return;
				}
				
				final long score = srvIntent.getLongExtra(
						Constants.EXTRA_LEADERBOARD_SCORE, 0l);
				Logger.debug("GameService leaderboard score: %s, inc: %d",
						lid, score);

				if (score > 0) {
					saveAsUncommittedLeaderboardScore(
							appContext, lid, score);
				}
			}
		}
		
		private void saveAsUncommittedUnlockAchievement(Context context, String achievementId) {
			if (context == null || achievementId == null) {
				return;
			}
			
			UncommittedAchievementModal.saveUncommittedUnlockAchievement(
					getApplicationContext(), achievementId);
		}
		
		private void saveAsUncommittedIncrementAchievement(Context context, 
				String achievementId, int incrementSteps) {
			if (context == null || achievementId == null) {
				return;
			}
			
			UncommittedAchievementModal.saveOrUpdateUncommittedIncrementAchievement(
					context, achievementId, incrementSteps);
		}
		
		private void saveAsUncommittedLeaderboardScore(Context context, 
				String leaderboardId, long score) {
			if (context == null || leaderboardId == null) {
				return;
			}
			
			UncommittedLeaderboardScoreModal.saveOrUpdateUncommittedLeaderboardScore(
					context, leaderboardId, score);
		}
		
	}
	
	private Looper mServiceLooper;
	
	private ServiceHandler mServiceHandler;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
	    HandlerThread thread = new HandlerThread("ServiceStartArguments",
	            Process.THREAD_PRIORITY_BACKGROUND);
	    thread.start();
	    
	    mServiceLooper = thread.getLooper();
	    mServiceHandler = new ServiceHandler(mServiceLooper);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		transferIntent(intent);
		
		return START_STICKY;
	}
	
	private void transferIntent(Intent intent) {
		if (intent == null) {
			return;
		}
		
		Message msg = mServiceHandler.obtainMessage();
		msg.obj = intent;
		mServiceHandler.sendMessage(msg);
	}
	
	@SuppressWarnings("unused")
	private void checkAndPushUncommittedAchievement() {
		List<UncommittedAchievement> achievements = 
				UncommittedAchievementModal.listUncommittedAchievements(
						getApplicationContext());
		if (achievements == null) {
			return;
		}
		
		final Context appContext = getApplicationContext();
		
		DesEncrypter desEncrypter = new DesEncrypter();
		
		String achievementId = null;
		Intent intent = null;
		for (UncommittedAchievement achievement: achievements) {
			achievementId = achievement.getAchievementId();
			
			Logger.debug("Push achievement: [%s]", achievement);
			
			if (achievement.getAchievementType() == AchievementType.INCREMENT) {
				intent = new Intent(Constants.ACTION_INCREMENT_ACHIVEMENT);
				intent.putExtra(Constants.EXTRA_ACHIEVEMENT_ID, achievementId);
				intent.putExtra(Constants.EXTRA_ACHIEVEMENT_INCREMENT_STEPS, 
						achievement.getIncrementSteps());
			} else {
				intent = new Intent(Constants.ACTION_UNLOCK_ACHIVEMENT);
				intent.putExtra(Constants.EXTRA_ACHIEVEMENT_ID, achievementId);
			}
			
			intent.putExtra(Constants.EXTRA_FROM_LOCAL_DB, true);

			transferIntent(intent);
		}
	}
	
}
