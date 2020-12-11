package com.dailystudio.memory.game;

import android.content.Context;
import android.text.TextUtils;

import com.dailystudio.dataobject.BlobColumn;
import com.dailystudio.dataobject.Column;
import com.dailystudio.dataobject.Template;
import com.dailystudio.development.Logger;

public class UncommittedAchievement extends EncryptedMemoryObject {

	public static enum AchievementType {
		STANDARD,
		INCREMENT,
	};
	
	public static final Column COLUMN_ENCRYPTED_ACHIEVEMENT_ID = new BlobColumn("enc_aid", false);
	public static final Column COLUMN_ENCRYPTED_ACHIEVEMENT_TYPE = new BlobColumn("enc_type");
	public static final Column COLUMN_ENCRYPTED_INCREMENT_STEPS = new BlobColumn("enc_increment_steps");
	
	private final static Column[] sCloumns = {
		COLUMN_ENCRYPTED_ACHIEVEMENT_ID,
		COLUMN_ENCRYPTED_ACHIEVEMENT_TYPE,
		COLUMN_ENCRYPTED_INCREMENT_STEPS,
	};

	public UncommittedAchievement(Context context) {
		super(context);
		
		final Template templ = getTemplate();
		
		templ.addColumns(sCloumns);
	}
	
	public byte[] getEncryptedAchievementId() {
		return getBlobValue(COLUMN_ENCRYPTED_ACHIEVEMENT_ID);
	}

	public void setEncryptedAchievementId(byte[] encryptedAid) {
		setValue(COLUMN_ENCRYPTED_ACHIEVEMENT_ID, encryptedAid);
	}
	
	public String getAchievementId() {
		return decryptString(getEncryptedAchievementId());
	}
	
	public void setAchievementId(String achievementId) {
		setEncryptedAchievementId(encryptString(achievementId));
	}
	
	public byte[] getEncryptedAchievementType() {
		return getBlobValue(COLUMN_ENCRYPTED_ACHIEVEMENT_TYPE);
	}
	
	public void setEncryptedAchievementType(byte[] encryptedType) {
		setValue(COLUMN_ENCRYPTED_ACHIEVEMENT_TYPE, encryptedType);
	}
	
	public AchievementType getAchievementType() {
		String typestr = decryptString(getEncryptedAchievementType());
		if (TextUtils.isEmpty(typestr)) {
			return AchievementType.STANDARD;
		}
		
		AchievementType atype = AchievementType.STANDARD;
		try {
			atype = AchievementType.valueOf(typestr);
		} catch (Exception e) {
			Logger.warnning("parse achievement failure: %s", e.toString());
			
			atype = AchievementType.STANDARD;
		}
		
		return atype;
	}
	
	public void setAchievementType(AchievementType type) {
		if (type == null) {
			type = AchievementType.STANDARD;
		}
		
		setEncryptedAchievementType(encryptString(String.valueOf(type)));
	}
	
	public byte[] getEncryptedIncrementSteps() {
		return getBlobValue(COLUMN_ENCRYPTED_INCREMENT_STEPS);
	}
	
	public void setEncryptedIncrementSteps(byte[] encryptedSteps) {
		setValue(COLUMN_ENCRYPTED_INCREMENT_STEPS, encryptedSteps);
	}
	
	public int getIncrementSteps() {
		String stepsstr = decryptString(getEncryptedIncrementSteps());
		if (TextUtils.isEmpty(stepsstr)) {
			return 0;
		}
		
		int steps = 0;
		try {
			steps = Integer.parseInt(stepsstr);
		} catch (Exception e) {
			Logger.warnning("parse steps failure: %s", e.toString());
			
			steps = 0;
		}
		
		return steps;
	}
	
	public void setIncrementSteps(int steps) {
		setEncryptedIncrementSteps(encryptString(String.valueOf(steps)));
	}

	@Override
	public String toString() {
		return String.format("%s, aid: %s[%s], type: %s[%s], steps: %d[%s]", 
				super.toString(),
				getAchievementId(),
				byteArrayToHex(getEncryptedAchievementId()),
				getAchievementType(),
				byteArrayToHex(getEncryptedAchievementType()),
				getIncrementSteps(),
				byteArrayToHex(getEncryptedIncrementSteps()));
	}

}
