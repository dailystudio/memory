package com.dailystudio.memory.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class CardFragmentRootView extends RelativeLayout {
	
	public static interface OnCardFragmentDispatchTouchEventListener {
		
		public boolean dispatchTouchEvent(MotionEvent event);
		
	}
	
	private OnCardFragmentDispatchTouchEventListener mOnCardFragmentDispatchTouchEventListener;
	
    public CardFragmentRootView(Context context) {
        this(context, null);
    }

    public CardFragmentRootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        initMembers();
    }

	private void initMembers() {
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (mOnCardFragmentDispatchTouchEventListener == null) {
			return super.dispatchTouchEvent(event);
		}
		
		final boolean ret =  
				mOnCardFragmentDispatchTouchEventListener.dispatchTouchEvent(
						event);
		if (ret) {
			return ret;
		}
		
		return super.dispatchTouchEvent(event);
	}
	
	public void setOnCardFragmentDispatchTouchEventListener(OnCardFragmentDispatchTouchEventListener l) {
		mOnCardFragmentDispatchTouchEventListener = l;
	}

}
