package com.dailystudio.memory.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class ActionBarActivityContent extends FrameLayout {
	
	private boolean mContentLocked;
	
	private Paint mPaint;
	private Bitmap mDrawingCache;
	
    public ActionBarActivityContent(Context context) {
        this(context, null);
    }

    public ActionBarActivityContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        initMembers();
    }

	private void initMembers() {
		mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		if (mContentLocked && mDrawingCache != null) {
			canvas.drawBitmap(mDrawingCache, 0, 0, mPaint);
			
			return;
		}
		
		super.dispatchDraw(canvas);
	}
	
	public void lockContent() {
		if (Build.VERSION.SDK_INT >= 11) {
			return;
		}
		
		removeCallbacks(mUnlockContentRunnable);
		
		final int width = getWidth();
		final int height = getHeight();
		
		if (width > 0 && height > 0) {
			Bitmap bitmap = Bitmap.createBitmap(
					getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
			
			Canvas canvas = new Canvas(bitmap);
			
			dispatchDraw(canvas);

			mDrawingCache = bitmap;
		}		

		mContentLocked = true;

		invalidate();
	}
	
	public void unlockContent() {
		if (Build.VERSION.SDK_INT >= 11) {
			return;
		}
		
		removeCallbacks(mUnlockContentRunnable);

//		post(mUnlockContentRunnable);
		postDelayed(mUnlockContentRunnable, 500);
	}
	
	private Runnable mUnlockContentRunnable = new Runnable() {
		
		@Override
		public void run() {
			if (mDrawingCache != null) {
				mDrawingCache.recycle();
			}
			
			mContentLocked = false;
			
			invalidate();
		}
		
	};
	
}
