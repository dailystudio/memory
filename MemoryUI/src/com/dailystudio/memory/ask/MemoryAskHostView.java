package com.dailystudio.memory.ask;

import java.net.URISyntaxException;

import com.dailystudio.app.utils.ActivityLauncher;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;
import com.dailystudio.memory.ui.R;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MemoryAskHostView extends FrameLayout {

	private final static long SCHEDULE_INTERVAL_SHORT = 1000;
	private final static long SCHEDULE_INTERVAL_LONG = 2000;
	private final static long MOVE_NEXT_INTERVAL = 5000;
	
	private static final int CHECK_POSITION_SEARCH_DISTANCE = 20;

	private class AskDataSetObserver extends DataSetObserver {
		
		@Override
		public void onChanged() {
			super.onChanged();
			
			boolean restored = checkAndRestoredPositionsById();
			if (!restored) {
				if (mAdapter != null && mAdapter.getCount() > 0) {
					mNextQuestionPos = 0;
				} else {
					mNextQuestionPos = -1;
				}
			}
			
			scheduleNextQuestion();
		}
		
		@Override
		public void onInvalidated() {
			super.onInvalidated();
			
			hideAskBoard(true);
			
			cancelPendingQuestion();
		}

	}
	
	protected Context mContext;
	
	private TextView mAskBoard;
	private Animation mAskBoardInAnim;
	private Animation mAskBoardOutAnim;

	private AskDataSetObserver mDataSetObserver;
	
	private Adapter mAdapter;
	
	private long mCurrQuestionId = -1;
	private int mCurrQuestionPos = -1;
	private int mNextQuestionPos = -1;
	
	private GestureDetector mGestureDetector;
	
	public MemoryAskHostView(Context context) {
        this(context, null);
    }

    public MemoryAskHostView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MemoryAskHostView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
		mContext = context;
		
		initMembers();
    }

	private void initMembers() {
		LayoutInflater.from(mContext).inflate(
				R.layout.ask_host_view, this);
		
		mAskBoard = (TextView) findViewById(R.id.ask_board);
		if (mAskBoard != null) {
			mAskBoard.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Logger.debug("mCurrQuestionPos = %d", mCurrQuestionPos);
					
					if (mAdapter instanceof MemoryAskCursorAdapter == false) {
						return;
					}
					
					if (mCurrQuestionPos < 0 
							|| mCurrQuestionPos >= mAdapter.getCount()) {
						return;
					}
					
					MemoryQuestion q = ((MemoryAskCursorAdapter)mAdapter).dumpItem(
							mCurrQuestionPos);
					if (q == null) {
						return;
					}
					
					Intent launchIntent = null;
					
					final String intentString = q.getLaunchIntent();
					if (intentString != null) {
						try {
							launchIntent = Intent.getIntent(intentString);
						} catch (URISyntaxException e) {
							Logger.warnning("parse Intent failure: %s", 
									e.toString());
							
							launchIntent = null;
						}
					}
					
					if (launchIntent == null) {
						launchIntent = new Intent();
						
						launchIntent.setClass(mContext, MemoryAskQuestionActivity.class);
					}
					
					launchIntent.putExtra(Constants.EXTRA_QUESTION_ID, q.getQuestionId());
					launchIntent.putExtra(Constants.EXTRA_SOURCE_PACKAGE, q.getSourcePackage());
					
					Logger.debug("launchIntent = %s", launchIntent);
					
					ActivityLauncher.launchActivity(mContext, launchIntent);
				}
				
			});
		}
		
		mAskBoardInAnim = AnimationUtils.loadAnimation(
					mContext, R.anim.ask_board_in);
		mAskBoardOutAnim = AnimationUtils.loadAnimation(
				mContext, R.anim.ask_board_out);
		if (mAskBoardOutAnim != null) {
			mAskBoardOutAnim.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					if (mAskBoard != null) {
						mAskBoard.setVisibility(INVISIBLE);
					}
				}
				
			});
		}
		
		mGestureDetector = new GestureDetector(mContext, mGestureListener);
	}
	
    private boolean checkAndRestoredPositionsById() {
    	Logger.debug("[BEFORE RECOVER]: id = %d, pos[curr: %d, next: %d]",
    			mCurrQuestionId,
    			mCurrQuestionPos,
    			mNextQuestionPos);
    	
    	if (mAdapter == null || mAdapter.getCount() <=0 ){
    		mCurrQuestionId = -1;
    		mCurrQuestionPos = -1;
    		mNextQuestionPos = -1;
   		
    		return false;
    	}
    	
        final long id = mCurrQuestionId;
        final int lastPos = mCurrQuestionPos;
        final int N = mAdapter.getCount();
        
    	boolean found = false;

        final long lastPosId = mAdapter.getItemId(lastPos);
//            Logger.debug("id = %d, lastPos = %d, lastPosId = %d", id, lastPos, lastPosId);
        if (id != lastPosId) {
        	final int start = Math.max(0, lastPos - CHECK_POSITION_SEARCH_DISTANCE);
        	final int end = Math.min(lastPos + CHECK_POSITION_SEARCH_DISTANCE, mAdapter.getCount());
        	for (int searchPos = start; searchPos < end; searchPos++) {
        		final long searchId = mAdapter.getItemId(searchPos);
        		if (id == searchId) {
        			found = true;
        			
        			mCurrQuestionId = searchId;
        			mCurrQuestionPos = searchPos;
        			mNextQuestionPos = (searchPos + 1) % N;
                }
        	}
//                Logger.debug("found = %s", found);
        	if (!found) {
        		mCurrQuestionId = -1;
            	mCurrQuestionPos = -1;
            	mNextQuestionPos = -1;
        	}
        }
        
    	Logger.debug("[AFTER RECOVER]: id = %d, pos[curr: %d, next: %d] [found = %s]",
    			mCurrQuestionId,
    			mCurrQuestionPos,
    			mNextQuestionPos,
    			found);
    	
        return found;
    }

	public void scheduleQuestions(Adapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        mAdapter = adapter;
        
        if (mAdapter != null) {
            mDataSetObserver = new AskDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);
        }
        
		boolean restored = checkAndRestoredPositionsById();
		if (!restored) {
			if (mAdapter != null && mAdapter.getCount() > 0) {
				mNextQuestionPos = 0;
			} else {
				mNextQuestionPos = -1;
			}
		}
		
		scheduleNextQuestion();
	}
	
	private void scheduleNextQuestion() {
		scheduleNextQuestion(SCHEDULE_INTERVAL_LONG);
	}
	
	private void scheduleNextQuestion(long interval) {
		if (mAdapter == null) {
			return;
		}
		
 		if (mNextQuestionPos < 0 
 				|| mNextQuestionPos >= mAdapter.getCount() ) {
			return;
		}
 		
 		cancelPendingQuestion();
		
		postDelayed(mScheduleAskRunnable, interval);
	}
	
	public void cancelPendingQuestion() {
		cancelPendingQuestion(true);
	}
	
	public void cancelPendingQuestion(boolean hideAskBoard) {
//		Logger.debug("hideAskBoard = %s", hideAskBoard);
		
		if (mScheduleAskRunnable != null) {
			removeCallbacks(mScheduleAskRunnable);
		}
		
		removeCallbacks(mMoveNextRunnable);
		
		if (hideAskBoard) {
			hideAskBoard(true);
		}
	}
	
	public void pasueAskQuestions() {
		cancelPendingQuestion(false);
	}
	
	public void resumeAskQuestions() {
		scheduleNextQuestion();
	}
	
	public void showAskBoard(String question) {
		if (mAskBoard != null) {
			mAskBoard.setText(question);
			
			final int oldVisibility = mAskBoard.getVisibility();
			if (oldVisibility == VISIBLE) {
				return;
			}

			mAskBoard.setVisibility(View.VISIBLE);
			
			if (mAskBoardInAnim != null) {
				mAskBoard.startAnimation(mAskBoardInAnim);
			}
		}
	}
	
	public void hideAskBoard() {
		hideAskBoard(false);
	}
	
	public void hideAskBoard(boolean smoothly) {
		if (mAskBoard != null) {
			final int oldVisibility = mAskBoard.getVisibility();
			if (oldVisibility != VISIBLE) {
				return;
			}

			if (mAskBoardOutAnim != null && smoothly) {
				mAskBoard.startAnimation(mAskBoardOutAnim);
			} else {
				mAskBoard.setVisibility(View.INVISIBLE);
			}
		}
	}

	private Runnable mScheduleAskRunnable = new Runnable() {

		@Override
		public void run() {
			if (mAdapter == null) {
				return;
			}
			
			final int N = mAdapter.getCount();
			
			if (mNextQuestionPos < 0 || mNextQuestionPos >= N) {
				return;
			}
			
			Object o = mAdapter.getItem(mNextQuestionPos);
			if (o instanceof Cursor == false) {
				return;
			}
			
			Cursor c = (Cursor)o;
			
			MemoryQuestion q = new MemoryQuestion(mContext);
			q.fillValuesFromCursor(c);
			
			mCurrQuestionPos = mNextQuestionPos;
			mCurrQuestionId = q.getId();
			mNextQuestionPos = (mCurrQuestionPos + 1) % N;
			
			final String questionText = q.dumpQuestionText();
			
			showAskBoard(questionText);
			
			if (N > 1) {
				postDelayed(mMoveNextRunnable, MOVE_NEXT_INTERVAL);
			}
		}
		
	};
	
	public boolean onInterceptTouchEvent(MotionEvent event) {
		boolean ret = mGestureDetector.onTouchEvent(event);
//		Logger.debug("[RET = %s], ev = %s", ret, event);
		
		return ret;
	};
	
	private Runnable mMoveNextRunnable = new Runnable() {
		
		@Override
		public void run() {
			scheduleNextQuestion();
		}
		
	};
	
	private OnGestureListener mGestureListener = 
			new GestureDetector.SimpleOnGestureListener() {
		
		private final static float XY_FACTOR = 1.2f;
		
		public boolean onFling(MotionEvent e1, MotionEvent e2, 
				float velocityX, float velocityY) {
			Logger.debug("[GESTURE] velocityX = %f, velocityY = %f (* %f = %f)",
					velocityX, velocityY,
					XY_FACTOR,
					(Math.abs(velocityY) * XY_FACTOR));
			if (Math.abs(velocityX) > Math.abs(velocityY) * XY_FACTOR) {
				Logger.debug("[GESTURE] swipe %s", (velocityX > 0 ? "right" : "left"));
				if (velocityX > 0 ) {
					scheduleNextQuestion(SCHEDULE_INTERVAL_SHORT);
				}
			}
			return true;
		};
		
	};
	
}
