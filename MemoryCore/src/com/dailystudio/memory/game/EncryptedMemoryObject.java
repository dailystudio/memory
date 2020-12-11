package com.dailystudio.memory.game;

import android.content.Context;

import com.dailystudio.datetime.dataobject.TimeCapsule;


public class EncryptedMemoryObject extends TimeCapsule {
	
	private static final String DEFAULT_ENCRPYTER_SEED = 
			"dAilYsTuDIo_ENcrptEr-SeEd" ;
	
	private static DesEncrypter sDesEncrypter = new DesEncrypter();
	
	private static String sEncrypterSeed = DEFAULT_ENCRPYTER_SEED;
	
	public EncryptedMemoryObject(Context context) {
		super(context);
	}
	
	public static String byteArrayToHex(byte[] a) {
		if (a == null) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		
		for(byte b: a) {
			sb.append(String.format("%02x", b&0xff));
		}
		
		return sb.toString();
	}
	
	protected synchronized static String getEncryptPassword() {
		return sEncrypterSeed;
	}
	
	public synchronized static void setEncryptPassword(String password) {
		sEncrypterSeed = password;
	}

	public static byte[] encryptString(String clearText) {
		return sDesEncrypter.encrypt(getEncryptPassword(), clearText);
	}
	
	public static String decryptString(byte[] encryptedText) {
		return sDesEncrypter.decrypt(getEncryptPassword(), encryptedText);
	}

}
