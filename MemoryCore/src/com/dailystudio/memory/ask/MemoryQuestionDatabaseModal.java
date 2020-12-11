package com.dailystudio.memory.ask;

import com.dailystudio.app.dataobject.DatabaseWriter;
import com.dailystudio.dataobject.query.ExpressionToken;
import com.dailystudio.dataobject.query.Query;
import com.dailystudio.datetime.dataobject.TimeCapsuleDatabaseReader;
import com.dailystudio.datetime.dataobject.TimeCapsuleDatabaseWriter;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;

import android.content.Context;

public class MemoryQuestionDatabaseModal {
	
	public static void addQuestionWithText(Context context, int qid, String srcPkg, String cntText) {
		addQuestion(context, qid, srcPkg, null, cntText, null, -1);
	}
	
	public static void addQuestionWithResource(Context context, int qid, String srcPkg, String cntResName) {
		addQuestion(context, qid, srcPkg, cntResName, null, null, -1);
	}
	
	public static void addQuestion(Context context, int qid, String srcPkg, 
			String cntResName, String cntText,
			String launchIntent, int priority) {
		if (context == null 
				|| qid == Constants.INVALID_ID
				|| srcPkg == null 
				|| (cntResName == null && cntText == null)) {
			Logger.warnning("invalid question params: qid(%d), srcPkg(%s), cntResName(%s), cntText(%s)", 
					qid, srcPkg, cntResName, cntText);
			return;
		}
		
		if (priority < 0) {
			priority = MemoryQuestion.PRIORITY_NORMAL;
		}
		
		final long now = System.currentTimeMillis();
		
		MemoryQuestion question = new MemoryQuestion(context);
		
		question.setTime(now);
		question.setQuestionId(qid);
		question.setSourcePackage(srcPkg);
		
		if (cntResName != null) {
			question.setContentResourceName(cntResName);
		}
		
		if (cntText != null) {
			question.setContentText(cntText);
		}
		
		question.setLaunchIntent(launchIntent);
		question.setPriority(priority);
		question.setState(MemoryQuestion.STATE_NEW);
		
		Logger.debug("question = %s", question);
		
		DatabaseWriter<MemoryQuestion> writer = 
			new DatabaseWriter<MemoryQuestion>(context,
					Constants.MEMORY_DATABASE_AUTHORITY,
					MemoryQuestion.class);
		
		writer.insert(question);
	}
	
	public static void updateQuestion(Context context, int id, 
			int qid, String srcPkg, 
			String cntResName, String cntText,
			String launchIntent, int priority) {
		if (context == null 
				|| id == Constants.INVALID_ID) {
			Logger.warnning("invalid question params: id(%d)", id);
			return;
		}

		MemoryQuestion question = new MemoryQuestion(context);

		question.setId(id);
		
		updateQuestion(context, question, 
				qid, srcPkg, 
				cntResName, cntText, launchIntent, priority);
	}
	
	public static void updateQuestion(Context context, 
			MemoryQuestion question, 
			int qid, String srcPkg, 
			String cntResName, String cntText,
			String launchIntent, int priority) {
		if (context == null 
				|| question == null
				|| qid == Constants.INVALID_ID
				|| srcPkg == null 
				|| (cntResName == null && cntText == null)) {
			Logger.warnning("invalid question params: question(%s), qid(%d), srcPkg(%s), cntResName(%s), cntText(%s)", 
					question, qid, srcPkg, cntResName, cntText);
			return;
		}
		
		if (priority < 0) {
			priority = MemoryQuestion.PRIORITY_NORMAL;
		}
		
		final long now = System.currentTimeMillis();
		
		question.setTime(now);
		question.setQuestionId(qid);
		question.setSourcePackage(srcPkg);
		
		if (cntResName != null) {
			question.setContentResourceName(cntResName);
		}
		
		if (cntText != null) {
			question.setContentText(cntText);
		}
		
		question.setLaunchIntent(launchIntent);
		question.setPriority(priority);
		question.setState(MemoryQuestion.STATE_UPDATED);
		
		Logger.debug("question = %s", question);
		
		TimeCapsuleDatabaseWriter<MemoryQuestion> writer =
			new TimeCapsuleDatabaseWriter<MemoryQuestion>(context,
					Constants.MEMORY_DATABASE_AUTHORITY,
					MemoryQuestion.class);
		
		writer.update(question);
	}

	public static void updateQuestionAnswer(Context context, 
			MemoryQuestion question, 
			String answer) {
		if (context == null 
				|| question == null
				|| answer == null) {
			Logger.warnning("invalid question params: question(%s), answer(%s)", 
					question, answer);
			return;
		}
		
		final long now = System.currentTimeMillis();
		
		question.setTime(now);
		question.setState(MemoryQuestion.STATE_ANSWERED);
		question.setAnswer(answer);
		
		Logger.debug("question = %s", question);

		TimeCapsuleDatabaseWriter<MemoryQuestion> writer =
			new TimeCapsuleDatabaseWriter<MemoryQuestion>(context,
					Constants.MEMORY_DATABASE_AUTHORITY,
					MemoryQuestion.class);
		
		writer.update(question);
	}

	public static MemoryQuestion findQuestion(Context context, int qid, String srcPkg) {
		if (context == null 
				|| qid == Constants.INVALID_ID
				|| srcPkg == null) {
			return null;
		}
		
		TimeCapsuleDatabaseReader<MemoryQuestion> reader =
			new TimeCapsuleDatabaseReader<MemoryQuestion>(context,
					Constants.MEMORY_DATABASE_AUTHORITY,
					MemoryQuestion.class);
		
		Query query = new Query(MemoryQuestion.class);
		
		ExpressionToken selToken = 
			MemoryQuestion.COLUMN_QUESTION_ID.eq(qid)
				.and(MemoryQuestion.COLUMN_SRC_PACKAGE.eq(srcPkg));
		if (selToken == null) {
			return null;
		}
		
		query.setSelection(selToken);
		
		return reader.queryLastOne(query);
	}
	
}
