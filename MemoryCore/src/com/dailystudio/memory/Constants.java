package com.dailystudio.memory;

import android.content.Intent;

public class Constants {
	
	public static final int INVALID_RESOURCE_ID = 0;
	public static final int MIN_EXEC_PERIOD = 1;

	public static final long INVALID_TIME = 0;
	public static final int INVALID_ID = -1;
	
	/*
	 * Search
	 */
	public static final String MEMORY_SEARCH_AUTHORITY = "com.dailystudio.memory.search";
	
	/*
	 * Database
	 */
	public static final String MEMORY_DATABASE_AUTHORITY = "com.dailystudio.memory";
	
	/*
	 * Activity definition
	 */
	public static final Intent DAILY_LIFE_ACTION_MAIN = 
		new Intent("android.intent.action.DAILY_LIFE_MAIN");

	public final static String DAILY_LIFE_CATEGORY = "android.intent.category.DAILY_LIFE";
	public final static String DAILY_ACTIVITY_CATEGORY_DEFAILT = "dailylife.category.default";
	
	/*
	 * Observable definition
	 */
	public static final String APP_OBSERVABLE = "observable-app";
	
	public final static String XML_TAG_OBSERVABLES = "observables";
	public final static String XML_TAG_OBSERVABLE = "observable";

	
	/*
	 * Plugin definition
	 */
	public static final String CATEGORY_MAIN = "dailylife.intent.category.MAIN";

	public static final String ACTION_REGISTER_PLUGIN =
		"dailylife.intent.ACTION_REGISTER_PLUGIN";

	public static final String ACTION_UNREGISTER_PLUGIN =
		"dailylife.intent.ACTION_UNREGISTER_PLUGIN";

	public static final String ACTION_CREATE_TASK =
		"dailylife.intent.ACTION_CREATE_TASK";
	public static final String ACTION_DESTROY_TASK = 
		"dailylife.intent.ACTION_DESTROY_TASK";
	public static final String ACTION_EXECUTE_TASK = 
		"dailylife.intent.ACTION_EXECUTE_TASK";
	public static final String ACTION_PAUSE_TASK = 
		"dailylife.intent.ACTION_PAUSE_TASK";
	public static final String ACTION_RESUME_TASK = 
		"dailylife.intent.ACTION_RESUME_TASK";
	public static final String ACTION_KEEP_ALIVE_TASK = 
		"dailylife.intent.ACTION_KEEP_ALIVE_TASK";
	public static final String ACTION_REQUEST_EXECUTE_TASK =
		"dailylife.intent.ACTION_REQUEST_EXECUTE_TASK";
	public static final String ACTION_CLEAR_TASKS =
		"dailylife.intent.ACTION_CLEAR_TASKS";

	public static final String EXTRA_TIME = 
		"dailylife.intent.extra.time";
	public static final String EXTRA_TASK_CLASS =
		"dailylife.intent.extra.task_class";
	public static final String EXTRA_TASK_ID =
		"dailylife.intent.extra.task_id";
	public static final String EXTRA_REQUEST_HOLDER = 
		"dailylife.intent.extra.request_holder";

	public static final String ACTION_RETURN_TASK_RESULT = 
		"dailylife.intent.ACTION_RETURN_TASK_RESULT";

	public static final String EXTRA_TASK_RESULT =
		"dailylife.intent.extra.task_action";
	public static final String EXTRA_TASK_ACTION =
		"dailylife.intent.extra.task_result";
	
	public static final int RESULT_SUCCESS = 0;
	public static final int RESULT_FAILURE = 1;

	public static final String ACTION_TASKS_BECOME_ALIVE =
		"dailylife.intent.ACTION_TASK_BECOME_ALIVE";

	public static final String EXTRA_ALIVE_TASKS_PACKAGE =
		"dailylife.intent.extra.task_package";
	
	public static final String EXTRA_ACTIVITY_PACKAGE = 
		"dailylife.intent.EXTRA_ACTIVITY_PACKAGE";
	public static final String EXTRA_ACTIVITY_CATEGORY = 
		"dailylife.intent.EXTRA_ACTIVITY_CATEGORY";

	public static final String DATA_SCEHME_TASK_REQUEST = 
			"memory-task";
	public static final String DATA_TEMPL_TASK_REQUEST = 
			DATA_SCEHME_TASK_REQUEST + "://%s";

	/*
	 * XML definition: PLUGIN
	 */
	public static final String META_DATA_DAILY_PLUING = 
		"memory.plugin.meta";

	public final static String XML_TAG_PLUGIN = "plugin";
	public final static String XML_TAG_TASK = "task";
	public final static String XML_TAG_PRIVACY = "privacy";
	public final static String XML_TAG_COLLECTION = "collection";

	public static final String META_ATTR_NAME = "name";
	public static final String META_ATTR_LABEL = "label";
	public static final String META_ATTR_TYPE = "type";
	public static final String META_ATTR_ICON = "icon";
	public static final String META_ATTR_SHOWCASE = "showcase";
	public static final String META_ATTR_PRIVACY = "privacy";
	public static final String META_ATTR_IS_ONESHOT = "oneshot";
	public static final String META_ATTR_EXECUTE_PEROID = "executePeroidMillis";
	public final static String META_ATTR_SEACHABLE_AUTHORITY = "searchableAuthority";

	public static final String TASK_TYPE_PERIODICAL = "periodical";
	public static final String TASK_TYPE_CONDITIONAL = "conditional";

	/*
	 * XML definition: ACTIVITY
	 */
	public static final String META_DATA_DAILY_ACTIVITY = "memory.activity.meta";

	public final static String XML_TAG_ACTIVITY = "activity";
	public final static String XML_TAG_ACTIVITY_CATEGORY = "activity-category";
	
	public static final String META_ATTR_CATEGORY = "category";

	/*
	 * Application EXTRAS:
	 */
	
	public static final String EXTRA_PEROID_START =
		"memory.intent.EXTRA_PEROID_START";
	public static final String EXTRA_PEROID_END =
		"memory.intent.EXTRA_PEROID_END";

	/*
	 * Memory Data Query:
	 */
	public static final String ACTION_QUERY_MEMORY_PIECE =
		"memory.intent.ACTION_QUERY_MEMORY_PIECE";
	public static final String ACTION_PROVIDE_MEMORY_PIECE =
			"memory.intent.ACTION_PROVIDE_MEMORY_PIECE";
	
	public static final String EXTRA_QUERY_CONTENT_TYPE =
		"memory.intent.EXTRA_QUERY_CONTENT_TYPE";
	public static final String EXTRA_QUERY_CONTENT_ARGS =
		"memory.intent.EXTRA_QUERY_CONTENT_ARGS";
	public static final String EXTRA_QUERY_COUNT =
		"memory.intent.EXTRA_QUERY_COUNT";
	public static final String EXTRA_QUERY_DIGEST =
		"memory.intent.EXTRA_QUERY_DIGEST";
	public static final String EXTRA_QUERY_CARD =
			"memory.intent.EXTRA_QUERY_CARD";
	public static final String EXTRA_QUERY_SERIAL =
			"memory.intent.EXTRA_QUERY_SERIAL";
	public static final String EXTRA_QUERY_CANDIDATES =
			"memory.intent.EXTRA_QUERY_CANDIDATES";
	public static final String EXTRA_QUERY_SOURCE =
			"memory.intent.EXTRA_QUERY_SOURCE";

	public static final String QUERY_CONTENT_TYPE_COUNT = "query-count";
	public static final String QUERY_CONTENT_TYPE_DIGEST = "query-digest";
	public static final String QUERY_CONTENT_TYPE_CARD = "query-card";

	/*
	 * Memory Notification
	 */
	
	public static final String CATEGORY_NOTIFY = "memory.intent.CATEGORY_NOTIFY";

	public static final String ACTION_SHOW_NOTIFY = "memory.intent.ACTION_SHOW_NOTIFY";
	public static final String ACTION_CANCEL_NOTIFY = "memory.intent.ACTION_CANCEL_NOTIFY";

	public static final String EXTRA_NOTIFY_ID =
			"memory.intent.EXTRA_NOTIFY_ID";
	public static final String EXTRA_NOTIFY_DATABASE_ID =
		"memory.intent.EXTRA_NOTIFY_DATABASE_ID";
	public static final String EXTRA_NOTIFY_TITLE_TEXT =
		"memory.intent.EXTRA_NOTIFY_TITLE_TEXT";
	public static final String EXTRA_NOTIFY_TITLE_RESOURCE = 
		"memory.intent.EXTRA_NOTIFY_TITLE_RESOURCE";
	public static final String EXTRA_NOTIFY_CONTENT_TEXT =
		"memory.intent.EXTRA_NOTIFY_CONTENT_TEXT";
	public static final String EXTRA_NOTIFY_CONTENT_RESOURCE = 
		"memory.intent.EXTRA_NOTIFY_CONTENT_RESOURCE";
	public static final String EXTRA_NOTIFY_SOURCE_PACKAGE =
		"memory.intent.EXTRA_NOTIFY_SOURCE_PACKAGE";
	public static final String EXTRA_NOTIFY_INTENT =
		"memory.intent.EXTRA_NOTIFY_INTENT";

	/*
	 * Memory Ask
	 */
	public static final String ACTION_ASK_QUESTION = "memory.intent.ACTION_ASK_QUESTION";
	public static final String ACTION_ANSWER_QUESTION = "memory.intent.ACTION_ANSWER_QUESTION";

	public static final String EXTRA_QUESTION_ID = "memory.intent.EXTRA_QUESTION_ID";
	public static final String EXTRA_SOURCE_PACKAGE = "memory.intent.EXTRA_SOURCE_PACKAGE";
	public static final String EXTRA_CONTENT_RESOURCE_NAME = "memory.intent.EXTRA_CONTENT_RESOURCE_NAME";
	public static final String EXTRA_CONTENT_TEXT = "memory.intent.EXTRA_CONTENT_TEXT";
	public static final String EXTRA_LAUNCH_INTENT = "memory.intent.EXTRA_LAUNCH_INTENT";
	public static final String EXTRA_PRIORITY = "memory.intent.EXTRA_PRIORITY";
	public static final String EXTRA_ANSWER = "memory.intent.EXTRA_ANSWER";

	
	/*
	 * Memory Search
	 */
	public static final String EXTRA_SEARCH_INPUT = "memory.intent.EXTRA_SEARCH_INPUT";

	public static final String SEARCH_SEGMENT_SUGGESTION = "suggestion";
	public static final String SEARCH_SEGMENT_DATA = "data";
	
	/* 
	 * Memory Showcase
	 */
	public static final String ACTION_PREPARE_SHOWCASE =
			"memory.intent.ACTION_PREPARE_SHOWCASE";
	public static final String ACTION_DESTROY_SHOWCASE =
			"memory.intent.ACTION_PREPARE_SHOWCASE";
	public static final String ACTION_UPDATE_SHOWCASE =
			"memory.intent.ACTION_UPDATE_SHOWCASE";
	
	public static final String EXTRA_SHOWCASE_PACKAGE = 
			"memory.intent.EXTRA_SHOWCASE_PACKAGE";
	public static final String EXTRA_SHOWCASE_URI = 
			"memory.intent.EXTRA_SHOWCASE_URI";
	
	public static final String ASSETS_SHOWCASE_DIRECTORY =
			"showcase";
	public static final String ASSETS_SHOWCASE_FILE_LIST =
			"filelist.json";

	/*
	 * Memory AppWidget
	 */
	public static final String EXTRA_APP_WIDGET_IDS = 
			"memory.intent.EXTRA_APP_WIDGET_IDS";
	public static final String EXTRA_APP_WIDGET_DATA_OBJECT_CLASS = 
			"memory.intent.EXTRA_APP_WIDGET_DATA_OBJECT_CLASS";

	/*
	 * Memory Achievements and Leaderboards
	 */
	public static final String ACTION_UNLOCK_ACHIVEMENT = 
			"memory.intent.ACTION_UNLOCK_ACHIVEMENT";
	public static final String ACTION_INCREMENT_ACHIVEMENT = 
			"memory.intent.ACTION_INCREMENT_ACHIVEMENT";
	public static final String ACTION_SUBMIT_LEADERBOARD_SCORE = 
			"memory.intent.ACTION_SUBMIT_LEADERBOARD_SCORE";

	public static final String EXTRA_ACHIEVEMENT_ID = 
			"memory.intent.EXTRA_ACHIEVEMENT_ID";
	public static final String EXTRA_ACHIEVEMENT_INCREMENT_STEPS = 
			"memory.intent.EXTRA_ACHIEVEMENT_INCREMENT";
	public static final String EXTRA_LEADERBOARD_ID = 
			"memory.intent.EXTRA_LEADERBOARD_ID";
	public static final String EXTRA_LEADERBOARD_SCORE = 
			"memory.intent.EXTRA_LEADERBOARD_SCORE";
	public static final String EXTRA_FROM_LOCAL_DB = 
			"memory.intent.EXTRA_FROM_LOCAL_DB";

	/* 
	 * Memory MainPage 
	 */
	public static final String ACTION_MAINPAGE_SHORTCUT =
			"memory.intent.ACTION_MAINPAGE_SHORTCUT";
	public static final String ACTION_LIST_PLUGIN_ACTIVITIES =
			"memory.intent.ACTION_LIST_PLUGIN_ACTIVITIES";

	/*
	 * Memory Card
	 */
	public static final String ACTION_MEMORY_CARD_UPDATED =
			"memory.intent.ACTION_MEMORY_CARD_UPDATED";

	public static final String EXTRA_CARD_NAME = 
			"memory.intent.EXTRA_CARD_NAME";

	/*
	 * Memory Person
	 */
	public static final String ACTION_MEMORY_PERSON_SET_FEATURE =
			"memory.intent.ACTION_MEMORY_PERSON_SET_FEATURE";
	public static final String ACTION_MEMORY_PERSON_SET_FEATURES =
			"memory.intent.ACTION_MEMORY_PERSON_SET_FEATURES";
	public static final String ACTION_MEMORY_PERSON_GET_FEATURE =
			"memory.intent.ACTION_MEMORY_PERSON_GET_FEATURE";

	public static final String EXTRA_PERSON_ID = 
			"memory.intent.EXTRA_PERSON_ID";
	public static final String EXTRA_FEATURE_ID = 
			"memory.intent.EXTRA_FEATURE_ID";
	public static final String EXTRA_FEATURE_VAL = 
			"memory.intent.EXTRA_FEATURE_VAL";
	public static final String EXTRA_FEATURES = 
			"memory.intent.EXTRA_FEATURES";
	
	/*
	 * Memory Keynotes
	 */
	public static final String ACTION_MEMORY_KEYNOTE_UPDATED =
			"memory.intent.ACTION_MEMORY_KEYNOTE_UPDATED";

}
