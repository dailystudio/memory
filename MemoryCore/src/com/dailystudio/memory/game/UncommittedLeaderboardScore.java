package com.dailystudio.memory.game;

import android.content.Context;
import android.text.TextUtils;

import com.dailystudio.dataobject.BlobColumn;
import com.dailystudio.dataobject.Column;
import com.dailystudio.dataobject.Template;
import com.dailystudio.development.Logger;

public class UncommittedLeaderboardScore extends EncryptedMemoryObject {

	public static final Column COLUMN_ENCRYPTED_LEADERBOARD_ID = new BlobColumn("enc_lid", false);
	public static final Column COLUMN_ENCRYPTED_LEADERBOARD_SCORE = new BlobColumn("enc_score");
	
	private final static Column[] sCloumns = {
		COLUMN_ENCRYPTED_LEADERBOARD_ID,
		COLUMN_ENCRYPTED_LEADERBOARD_SCORE,
	};

	public UncommittedLeaderboardScore(Context context) {
		super(context);
		
		final Template templ = getTemplate();
		
		templ.addColumns(sCloumns);
	}
	
	public byte[] getEncryptedLeaderboardId() {
		return getBlobValue(COLUMN_ENCRYPTED_LEADERBOARD_ID);
	}

	public void setEncryptedLeaderboardId(byte[] encryptedAid) {
		setValue(COLUMN_ENCRYPTED_LEADERBOARD_ID, encryptedAid);
	}
	
	public String getLeaderboardId() {
		return decryptString(getEncryptedLeaderboardId());
	}
	
	public void setLeaderboardId(String LeaderboardId) {
		setEncryptedLeaderboardId(encryptString(LeaderboardId));
	}
	
	public byte[] getEncryptedLeaderboardScore() {
		return getBlobValue(COLUMN_ENCRYPTED_LEADERBOARD_SCORE);
	}
	
	public void setEncryptedLeaderboardScore(byte[] encryptedSteps) {
		setValue(COLUMN_ENCRYPTED_LEADERBOARD_SCORE, encryptedSteps);
	}
	
	public long getLeaderboardScore() {
		String scorestr = decryptString(getEncryptedLeaderboardScore());
		if (TextUtils.isEmpty(scorestr)) {
			return 0l;
		}
		
		long score = 0l;
		try {
			score = Long.parseLong(scorestr);
		} catch (Exception e) {
			Logger.warnning("parse score failure: %s", e.toString());
			
			score = 0l;
		}
		
		return score;
	}
	
	public void setLeaderboardScore(long score) {
		setEncryptedLeaderboardScore(encryptString(String.valueOf(score)));
	}

	@Override
	public String toString() {
		return String.format("%s, lid: %s[%s], score: %d[%s]", 
				super.toString(),
				getLeaderboardId(),
				byteArrayToHex(getEncryptedLeaderboardId()),
				getLeaderboardScore(),
				byteArrayToHex(getEncryptedLeaderboardScore()));
	}

}
