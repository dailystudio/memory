package com.dailystudio.memory.ask;

import com.dailystudio.memory.Constants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MemoryAskCommandReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent == null) {
			return;
		}
		
		final Context appContext = context.getApplicationContext();
		
		final String action = intent.getAction();
		if (Constants.ACTION_ASK_QUESTION.equals(action)) {
			final int qid = intent.getIntExtra(
					Constants.EXTRA_QUESTION_ID, Constants.INVALID_ID);
			final String srcPkg = intent.getStringExtra(Constants.EXTRA_SOURCE_PACKAGE);
			final String cntResName = intent.getStringExtra(Constants.EXTRA_CONTENT_RESOURCE_NAME);
			final String cntText = intent.getStringExtra(Constants.EXTRA_CONTENT_TEXT);
			final String launchIntent = intent.getStringExtra(Constants.EXTRA_LAUNCH_INTENT);
			final int priority = intent.getIntExtra(
				Constants.EXTRA_PRIORITY, MemoryQuestion.PRIORITY_NORMAL);
			
			MemoryQuestion question = MemoryQuestionDatabaseModal.findQuestion(
					appContext, qid, srcPkg);
			
			if (question != null) {
				MemoryQuestionDatabaseModal.updateQuestion(appContext, question, 
						qid, srcPkg, cntResName, cntText, launchIntent, priority);
			} else {
				MemoryQuestionDatabaseModal.addQuestion(appContext, 
						qid, srcPkg, cntResName, cntText, launchIntent, priority);
			}
		} else if (Constants.ACTION_ANSWER_QUESTION.equals(action)) {
			final int qid = intent.getIntExtra(
					Constants.EXTRA_QUESTION_ID, Constants.INVALID_ID);
			final String srcPkg = intent.getStringExtra(Constants.EXTRA_SOURCE_PACKAGE);
			final String answer = intent.getStringExtra(Constants.EXTRA_ANSWER);
			
			MemoryQuestion question = MemoryQuestionDatabaseModal.findQuestion(
					appContext, qid, srcPkg);
			
			if (question != null) {
				MemoryQuestionDatabaseModal.updateQuestionAnswer(
						appContext, question, answer);
			} 
		} 
	}

}
