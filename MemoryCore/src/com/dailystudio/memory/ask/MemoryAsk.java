package com.dailystudio.memory.ask;

import com.dailystudio.memory.Constants;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

public class MemoryAsk {

	public static void askQuestion(Context context,
			int questionId,
			String contentText,
			int priority,
			Intent launchIntent) {
		askQuestion(context, questionId, contentText, -1,
				priority, launchIntent);
	}
	
	public static void askQuestion(Context context,
			int questionId,
			int contextResId,
			int priority,
			Intent launchIntent) {
		askQuestion(context, questionId, null, contextResId,
				priority, launchIntent);
	}
	
	public static void askQuestion(Context context,
			int questionId,
			String contentText,
			int contentResId,
			int priority,
			Intent launchIntent) {
		if (context == null || questionId == Constants.INVALID_ID) {
			return;
		}
		
		if (priority < 0) {
			priority = MemoryQuestion.PRIORITY_NORMAL;
		}
		
		Intent i = new Intent(Constants.ACTION_ASK_QUESTION);
		
		i.putExtra(Constants.EXTRA_QUESTION_ID, questionId);
		i.putExtra(Constants.EXTRA_SOURCE_PACKAGE, context.getPackageName());
		
		if (contentText != null) {
			i.putExtra(Constants.EXTRA_CONTENT_TEXT, contentText);
		}
		
		if (contentResId > 0) {
			final Resources res = context.getResources();
			
			final String contentResName = res.getResourceName(contentResId); 
			if (contentResName != null) {
				i.putExtra(Constants.EXTRA_CONTENT_RESOURCE_NAME, contentResName);
			}
		}
		
		i.putExtra(Constants.EXTRA_PRIORITY, priority);
		
		if (launchIntent != null) {
			i.putExtra(Constants.EXTRA_LAUNCH_INTENT,
					launchIntent.toURI());
		}
		
		context.sendBroadcast(i);
	}
	
	public static void answerQuestion(Context context, int questionId,
			String answer) {
		if (context == null 
				|| questionId == Constants.INVALID_ID
				|| answer == null) {
			return;
		}
		
		Intent i = new Intent(Constants.ACTION_ANSWER_QUESTION);
		
		i.putExtra(Constants.EXTRA_QUESTION_ID, questionId);
		i.putExtra(Constants.EXTRA_SOURCE_PACKAGE, context.getPackageName());
		i.putExtra(Constants.EXTRA_ANSWER, answer);
		
		context.sendBroadcast(i);
	}
	
	public static MemoryQuestion getAskedQuestion(Context context, int questionId) {
		return MemoryQuestionDatabaseModal.findQuestion(context, 
				questionId, context.getPackageName());
	}
	
	public static boolean isQuestionAsked(Context context, int questionId) {
		final MemoryQuestion question = MemoryQuestionDatabaseModal.findQuestion(
				context, questionId, context.getPackageName());
		
		return (question != null);
	}
	
}
