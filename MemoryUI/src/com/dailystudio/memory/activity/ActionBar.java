package com.dailystudio.memory.activity;

import com.dailystudio.memory.ui.R;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class ActionBar extends FrameLayout {
		
	public static interface ActionBarOverflowCallbacks {
		
		public void preOverflowDropdownShow(View overflow, boolean withAnimation);
		public void postOverflowDropdownShow(View overflow, boolean withAnimation);
		public void preOverflowDropdownHide(View overflow, boolean withAnimation);
		public void postOverflowDropdownHide(View overflow, boolean withAnimation);
		
	}
	
	public static class LayoutParams extends FrameLayout.LayoutParams {

		public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height);
            this.gravity = gravity;
        }

		public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }
		
	};
	
	private static int[] sPrgoressResIds = {
		R.drawable.actbar_prg_01,
		R.drawable.actbar_prg_02,
		R.drawable.actbar_prg_03,
		R.drawable.actbar_prg_04,
		R.drawable.actbar_prg_05,
		R.drawable.actbar_prg_06,
		R.drawable.actbar_prg_07,
		R.drawable.actbar_prg_08,
	};
	
	private View mRootView;
	private TextView mTitleView;
	private ViewGroup mCustomView;
	private View mOverflowButton;
	
	private ImageView mProgressInd;
	private int mProgress;
	
	private Animation mOverflowDropdownAnimIn;
	private Animation mOverflowDropdownAnimOut;
	
	private ActionBarOverflowCallbacks mOverflowCallbacks;
	
    public ActionBar(Context context) {
        this(context, null);
    }

	public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        initMembers();
    }

	private void initMembers() {
		LayoutInflater.from(getContext()).inflate(R.layout.actbar_base, this);
		
		mRootView = findViewById(R.id.actbar_root);
		mTitleView = (TextView) findViewById(R.id.actbar_title);
		mCustomView = (ViewGroup) findViewById(R.id.actbar_custome_view);
		mOverflowButton = findViewById(R.id.actbar_overflow);
		if (mOverflowButton != null) {
			mOverflowButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					toggleOverflowDropdown();
				}

			});
		}
		
		mOverflowDropdownAnimIn = 
			AnimationUtils.loadAnimation(getContext(), R.anim.actbar_overflow_dropdown_in);
		if (mOverflowDropdownAnimIn != null) {
			mOverflowDropdownAnimIn.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					View overflowDropdown = getOverflowDropdown();

					if (mOverflowCallbacks != null) {
						mOverflowCallbacks.postOverflowDropdownShow(
								overflowDropdown,
								true);
					};
				}
				
			});
		}
		
		mOverflowDropdownAnimOut = 
			AnimationUtils.loadAnimation(getContext(), R.anim.actbar_overflow_dropdown_out);
		if (mOverflowDropdownAnimOut != null) {
			mOverflowDropdownAnimOut.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					View overflowDropdown = getOverflowDropdown();

					if (mOverflowCallbacks != null) {
						mOverflowCallbacks.postOverflowDropdownHide(
								overflowDropdown,
								true);
					};
				}
				
			});
		}
		
		mProgressInd = (ImageView) findViewById(R.id.actbar_progress);
	}
	
	public void setTitle(int resid) {
		final Resources res = getResources();
		if (res != null) {
			setTitle(res.getText(resid));
		}
	}
	
	public void setTitle(CharSequence title) {
		if (mTitleView != null) {
			mTitleView.setText(title);
			
			final Resources res = getResources();
			final int leftPadding = 
					res.getDimensionPixelSize(R.dimen.action_bar_padding);
			if (TextUtils.isEmpty(title) && mRootView != null) {
				mRootView.setPadding(0, 0, 0, 0);
			} else {
				mRootView.setPadding(leftPadding, 0, 0, 0);
			}
		}
	}
	
	public void setCustomView(int layoutResId) {
		setCustomView(LayoutInflater.from(getContext()).inflate(
				layoutResId, null));
	}
	
	public void setCustomView(View view) {
		setCustomView(view, null);
	}
	
	public void setCustomView(View view, LayoutParams lp) {
		if (view == null) {
			return;
		}
		
		if (mCustomView != null) {
			mCustomView.removeAllViews();
		}
		
		if (view != null) {
			view.setLayoutParams(lp);

			mCustomView.addView(view);
		}
		
		requestLayout();
	}
	
	public void setTitleBackgroundColor(int color) {
		if (mRootView != null) {
			mRootView.setBackgroundColor(color);
		}
	}
	
	public void setTitleBackgroundDrawable(Drawable d) {
		if (mRootView != null) {
			mRootView.setBackgroundDrawable(d);
		}
	}
	
	public void setTitleBackgroundResource(int resid) {
		if (mRootView != null) {
			mRootView.setBackgroundResource(resid);
		}
	}
	
	public void setTitleTextColor(int color) {
		if (mTitleView != null) {
			mTitleView.setTextColor(color);
		}
	}
	
	public void setTitleTextColor(ColorStateList colors) {
		if (mTitleView != null) {
			mTitleView.setTextColor(colors);
		}
	}
	
	public void setOverflowEnabled(boolean enabled) {
		if (mOverflowButton == null) {
			return;
		}
		
		final int oldVisibility = mOverflowButton.getVisibility();
/*		Logger.debug("enabled = %s, oldVisibility = %d", 
				enabled,
				oldVisibility);
*/
		if (enabled) {
			if (oldVisibility != VISIBLE) {
				mOverflowButton.setVisibility(VISIBLE);
			}
		} else {
			if (oldVisibility == VISIBLE) {
				mOverflowButton.setVisibility(GONE);
			}
		}
	}
	
	public boolean isOverflowEnabled() {
		if (mOverflowButton == null) {
			return false;
		}
		
		return (mOverflowButton.getVisibility() == VISIBLE);
	}
	
	public boolean isOverflowShown() {
		View overflowDropdown = getOverflowDropdown();
		if (overflowDropdown == null) {
			return false;
		}
		
		final int visibility = overflowDropdown.getVisibility();
		
		return (visibility == VISIBLE);
	}
	
	public void setOverflowCallbacks(ActionBarOverflowCallbacks callbacks) {
		mOverflowCallbacks = callbacks;
	}
	
	private void toggleOverflowDropdown() {
		final boolean overflowShown = isOverflowShown();
		
		if (overflowShown) {
			hideOverflow();
		} else {
			displayOverflow();
		}
	}
	
	public void displayOverflow() {
		displayOverflow(true);
	}
	
	public void displayOverflow(boolean withAnimation) {
		View overflowDropdown = getOverflowDropdown();
		if (overflowDropdown == null) {
			return;
		}
		
		if (mOverflowCallbacks != null) {
			mOverflowCallbacks.preOverflowDropdownShow(
					overflowDropdown, withAnimation);
		};
		
		overflowDropdown.setVisibility(VISIBLE);
		if (mOverflowDropdownAnimIn != null && withAnimation) {
			overflowDropdown.startAnimation(mOverflowDropdownAnimIn);
		} else {
			if (mOverflowCallbacks != null) {
				mOverflowCallbacks.postOverflowDropdownShow(
						overflowDropdown, withAnimation);
			}
		}
		
		overflowDropdown.requestFocus();
	}
	
	public void hideOverflow() {
		hideOverflow(true);
	}
	
	public void hideOverflow(boolean withAnimation) {
		View overflowDropdown = getOverflowDropdown();
		if (overflowDropdown == null) {
			return;
		}
		
		if (mOverflowCallbacks != null) {
			mOverflowCallbacks.preOverflowDropdownHide(
					overflowDropdown, withAnimation);
		};

		if (mOverflowDropdownAnimOut != null && withAnimation) {
			overflowDropdown.startAnimation(mOverflowDropdownAnimOut);
		} else {
			if (mOverflowCallbacks != null) {
				mOverflowCallbacks.postOverflowDropdownHide(
						overflowDropdown, withAnimation);
			}
		}
		
		overflowDropdown.clearFocus();
		overflowDropdown.setVisibility(INVISIBLE);
	}
	
	private View getOverflowDropdown() {
		final View rootView = getRootView();
		if (rootView == null)  {
			return null;
		}

		return rootView.findViewById(R.id.actbar_activity_overflow_menu);
	}
	
	public void setProgress(int progress) {
		if (mProgressInd == null) {
			return;
		}

		mHandler.removeCallbacks(mUpdateProgressRunnable);
		
		mProgress = progress;
		
		mHandler.post(mUpdateProgressRunnable);
	}
	
	public void clearProgress() {
		setProgress(-1);
	}
	
	private void showProgress() {
		if (mProgressInd == null) {
			return;
		}
		
		if (mProgressInd.getVisibility() == VISIBLE) {
			return;
		}
		
		mProgressInd.setImageDrawable(null);
		mProgressInd.setVisibility(VISIBLE);
		mProgressInd.startAnimation(AnimationUtils.loadAnimation(
				getContext(),
				R.anim.fade_in));
	}
	
	private void hideProgress() {
		if (mProgressInd == null) {
			return;
		}
		
		if (mProgressInd.getVisibility() != VISIBLE) {
			return;
		}
		
		mProgressInd.startAnimation(AnimationUtils.loadAnimation(
				getContext(),
				R.anim.fade_out));
		mProgressInd.setVisibility(GONE);
	}
	
    protected void doUpdateProgress(int progress) {
		if (mProgressInd == null) {
			return;
		}
		
		if (progress > 0 && mProgressInd.getVisibility() != VISIBLE) {
			showProgress();
    	} else if (progress < 0) {
			hideProgress();
    	}
		
		float percent = ((float)progress / 100);
		
		int index = (int)Math.round(sPrgoressResIds.length * percent) - 1;
		
//		Logger.debug("progress(%d) / 100 = percent(%f)", progress, percent);
//		Logger.debug("sPrgoressResIds.length(%d) * percent(%f) - 1 = index(%d)",
//				sPrgoressResIds.length,
//				percent, 
//				index);

		if (index >= 0 && index < sPrgoressResIds.length) {		
			mProgressInd.setImageResource(sPrgoressResIds[index]);
		}
		
		if (progress >= 100) {
			clearProgress();
		}
	}
    
    private Runnable mUpdateProgressRunnable = new Runnable() {
		
		@Override
		public void run() {
			doUpdateProgress(mProgress);
		}
		
	};
	
	private Handler mHandler = new Handler();
	
}
