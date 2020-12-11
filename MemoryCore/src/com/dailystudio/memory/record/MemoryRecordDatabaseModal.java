package com.dailystudio.memory.record;

import com.dailystudio.dataobject.query.ExpressionToken;
import com.dailystudio.dataobject.query.Query;
import com.dailystudio.datetime.dataobject.TimeCapsuleDatabaseReader;
import com.dailystudio.datetime.dataobject.TimeCapsuleDatabaseWriter;
import com.dailystudio.memory.Constants;

import android.content.Context;

public class MemoryRecordDatabaseModal {

	public static MemoryRecord getRecord(Context context, String recordName) {
		return getRecord(context, 
				Constants.MEMORY_DATABASE_AUTHORITY,
				recordName);
	}
	
	public static MemoryRecord getRecord(Context context, 
			String authority,
			String recordName) {
		if (context == null || recordName == null) {
			return null;
		}
		
		TimeCapsuleDatabaseReader<MemoryRecord> reader =
			new TimeCapsuleDatabaseReader<MemoryRecord>(context,
					authority,
					MemoryRecord.class);
		
		Query query = new Query(MemoryRecord.class);
		
		ExpressionToken selToken = 
			MemoryRecord.COLUMN_NAME.eq(recordName);
		if (selToken == null) {
			return null;
		}
		
		query.setSelection(selToken);
		
		return reader.queryLastOne(query);
	}
	
	public static void saveNewRecord(Context context, 
			String recordName, long recordScore) {
		saveNewRecord(context, Constants.MEMORY_DATABASE_AUTHORITY, 
				recordName, recordScore);
	}
	
	public static void saveNewRecord(Context context, 
			String authority,
			String recordName, long recordScore) {
		if (context == null || recordName == null) {
			return;
		}

		MemoryRecord record = 
			getRecord(context, authority, recordName);
		
		final long now = System.currentTimeMillis();
		final boolean toUpdate = (record != null);
		
		if (record == null) {
			record = new MemoryRecord(context);
			
			record.setRecordName(recordName);
		} 

		final long oldScore = record.getRecordScore();
		
		record.setTime(now);
		record.setRecordScore(recordScore);
		record.setDeltaScore(recordScore - oldScore);
		record.setReviewed(false);

		TimeCapsuleDatabaseWriter<MemoryRecord> writer =
			new TimeCapsuleDatabaseWriter<MemoryRecord>(context,
					authority,
					MemoryRecord.class);
		
		
		if (toUpdate) {
			writer.update(record);
		} else {
			writer.insert(record);
		}
	}
	
	public static void reviewRecord(Context context,
			String recordName) {
		reviewRecord(context,  Constants.MEMORY_DATABASE_AUTHORITY, 
				recordName);
	}
	
	public static void reviewRecord(Context context, 
			String authority, String recordName) {
		if (context == null || recordName == null) {
			return;
		}

		MemoryRecord record = 
			getRecord(context, authority, recordName);
		
		if (record == null) {
			return;
		}
		
		final long now = System.currentTimeMillis();

		record.setTime(now);
		record.setReviewed(true);
		
		TimeCapsuleDatabaseWriter<MemoryRecord> writer =
			new TimeCapsuleDatabaseWriter<MemoryRecord>(context,
					authority,
					MemoryRecord.class);
		
		writer.update(record);
	}

}
