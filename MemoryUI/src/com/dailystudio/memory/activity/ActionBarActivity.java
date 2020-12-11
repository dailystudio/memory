package com.dailystudio.memory.activity;

import java.util.List;

import com.dailystudio.app.fragment.BaseIntentFragment;
import com.dailystudio.memory.activity.ActionBar.ActionBarOverflowCallbacks;
import com.dailystudio.memory.menu.ActionBarOverflowMenu;
import com.dailystudio.memory.menu.ActionBarOverflowMenuView;
import com.dailystudio.memory.menu.ActionBarOverflowMenuView.ActionBarOverflowMenuItemCallbacks;
import com.dailystudio.memory.ui.R;
import com.dailystudio.memory.ui.utils.ViewHelper;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class ActionBarActivity extends FragmentActivity 
	implements ActionBarOverflowMenuItemCallbacks {

	private final static String SAVED_STATE_OVERFLOW_SHOWN = "overflow-shown";
	
	private class MenuItemSelectedRunnable implements Runnable {

		private MenuItem mItem;
		
		private MenuItemSelectedRunnable(MenuItem item) {
			mItem = item;
		}
		
		@Override
		public void run() {
			if (mItem == null) {
				return;
			}
			
			boolean handled = onOverflowItemSelected(mItem);
			if (handled == false) {
				return;
			}
			
			if (mActionBar != null) {
				mActionBar.hideOverflow();
			}
		}
		
	}
	
	private ActionBar mActionBar;
	private ActionBarActivityContent mContentView;
	
	private Menu mOverflowMenu;
	private ActionBarOverflowMenuView mOverflowMenuView;
	private Rect mOverflowMenuHitRect = new Rect();
	private Rect mOverflowButtonHitRect = new Rect();
	
	private View mOverflowPopupOverlay;
	private Animation mOverflowPopupOverlayInAnim;
	private Animation mOverflowPopupOverlayOutAnim;
	
	private TextView mPromptView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		super.setContentView(R.layout.actbar_activity);
		
		buildUpActionBar();
		restoreActionBar(savedInstanceState);		
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		
		final Intent intent = getIntent();
		
		bindIntent(intent);

		bindIntentOnFragments(intent);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		bindIntent(intent);

		bindIntentOnFragments(intent);
	}

	protected void bindIntentOnFragments(Intent intent) {
		FragmentManager frgmgr = 
				getSupportFragmentManager();
		if (frgmgr == null) {
			return;
		}
		
		List<Fragment> fragments = frgmgr.getFragments();
		if (fragments == null) {
			return;
		}
		
		for (Fragment f: fragments) {
			if (f instanceof BaseIntentFragment) {
				((BaseIntentFragment)f).bindIntent(intent);
			}
		}
	}

	protected void bindIntent(Intent intent) {
	}

	private void buildUpActionBar() {
	    mActionBar = (ActionBar) findViewById(R.id.actbar_activity_actionbar);
	    if (mActionBar != null) {
	    	mActionBar.setTitle(getTitle());

	    	mActionBar.setOverflowCallbacks(mOverflowCallbacks);
	    }
	    
	    mContentView = (ActionBarActivityContent) findViewById(
	    		R.id.actbar_activity_content);
	   
	    mOverflowMenuView = (ActionBarOverflowMenuView) findViewById(
	    			R.id.actbar_activity_overflow_menu);
	    if (mOverflowMenuView != null) {
	    	mOverflowMenuView.setActionBarOverflowMenuItemCallbacks(this);
	    }
	    
	    mOverflowMenu = new ActionBarOverflowMenu(this);
	    
	    boolean menuCreated = onCreateOverflowMenu(mOverflowMenu);
	    
	    setOverflowEnabled(menuCreated && mOverflowMenuView != null);

	    if (mOverflowMenuView != null) {
	    	mOverflowMenuView.attchToMenu(menuCreated ?
	    			mOverflowMenu : null);
	    }
	    
	    mOverflowPopupOverlay = findViewById(
	    		R.id.actbar_overflow_popup_overlay);
	    if (mOverflowPopupOverlay != null 
	    		&& !ViewHelper.isLargeScreen(getApplicationContext())) {
	    	mOverflowPopupOverlayInAnim = AnimationUtils.loadAnimation(
	    			this, R.anim.actbar_overflow_popup_overlay_in);
	    	mOverflowPopupOverlayOutAnim = AnimationUtils.loadAnimation(
	    			this, R.anim.actbar_overflow_popup_overlay_out);
	    }
	    
	    mPromptView = (TextView) findViewById(R.id.actbar_prompt);
	}
	
	private void restoreActionBar(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			return;
		}
		
		if (mActionBar == null) {
			return;
		}
		
		boolean overflowShown = savedInstanceState.getBoolean(
				SAVED_STATE_OVERFLOW_SHOWN, false);
		
		if (overflowShown) {
			mActionBar.displayOverflow(false);
		}
	}

	protected void setActionBarEnabled(boolean enabled) {
		if (mActionBar == null) {
			return;
		}
		
		int visibility = mActionBar.getVisibility();
		if (enabled && visibility != View.VISIBLE) {
			mActionBar.setVisibility(View.VISIBLE);
		} else if (!enabled && visibility == View.VISIBLE) {
			mActionBar.setVisibility(View.GONE);
		}
	}
	
	public ActionBar getCompatibleActionBar() {
		return mActionBar;
	}
	
	public void showFragment(int fragmentId) {
		showFragment(fragmentId, 0);
	}
	
	public void showFragment(int fragmentId, int enterAnim) {
		showFragment(findFragment(fragmentId), enterAnim);
	}
	
	public void showFragment(Fragment fragment) {
		showFragment(fragment, 0);
	}
	
	public void showFragment(Fragment fragment, int enterAnim) {
        if (fragment == null || fragment.isVisible()) {
        	return;
        }
        
        FragmentTransaction ft = 
        		getSupportFragmentManager().beginTransaction();
        
        if (enterAnim > 0) {
        	ft.setCustomAnimations(enterAnim, 0);
        }
        
    	ft.show(fragment);
        
        ft.commit();
	}
	
	public void hideFragment(int fragmentId) {
		hideFragment(fragmentId, 0);
	}
	
	public void hideFragment(int fragmentId, int enterAnim) {
		hideFragment(findFragment(fragmentId), enterAnim);
	}
	
	public void hideFragment(Fragment fragment) {
		hideFragment(fragment, 0);
	}
	
	public void hideFragment(Fragment fragment, int exitAnim) {
        if (fragment == null || !fragment.isVisible()) {
        	return;
        }
        
        FragmentTransaction ft = 
        		getSupportFragmentManager().beginTransaction();
        
        if (exitAnim > 0) {
        	ft.setCustomAnimations(0, exitAnim);
        }
        
    	ft.hide(fragment);
    	
    	ft.commit();
	}
	
	public void hideFragmentOnCreate(Fragment fragment) {
        if (fragment == null) {
        	return;
        }
        
        FragmentTransaction ft = 
        		getSupportFragmentManager().beginTransaction();
        
    	ft.hide(fragment);
    	
    	ft.commit();
	}
	
	public boolean isFragmentVisible(int fragmentId) {
		return isFragmentVisible(findFragment(fragmentId));
	}
	
	public boolean isFragmentVisible(Fragment fragment) {
		if (fragment == null) {
			return false;
		}
		
		return fragment.isVisible();
	}
	
	public Fragment findFragment(int fragmentId) {
		FragmentManager frgmgr = getSupportFragmentManager();
		if (frgmgr == null) {
			return null;
		}
		
		return frgmgr.findFragmentById(fragmentId);
	}
	
	@Override
	public void setContentView(int layoutResID) {
		if (mContentView == null) {
			super.setContentView(layoutResID);
		}

		LayoutInflater.from(this).inflate(layoutResID, mContentView);
	}
	
	@Override
	public void setContentView(View view) {
		if (mContentView == null) {
			super.setContentView(view);
		}

		mContentView.addView(view);
	}
	
	@Override
	public void setContentView(View view, LayoutParams params) {
		if (mContentView == null) {
			super.setContentView(view, params);
		}
		
		mContentView.addView(view, params);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	
		if (mActionBar == null) {
			return;
		}
		
		if (mActionBar.isOverflowEnabled() && mActionBar.isOverflowShown()) {
			outState.putBoolean(SAVED_STATE_OVERFLOW_SHOWN, true);
		}
	}
	
	@Override
	public void onBackPressed() {
		if (mActionBar != null 
				&& mActionBar.isOverflowEnabled() 
				&& mActionBar.isOverflowShown()) {
			mActionBar.hideOverflow();
			
			return;
		}
		
		super.onBackPressed();
	}
	
	@Override
	protected void onPause() {
		super.onPause(); 
		
		if (mIndeterminateProgressRunning) {
			mHandler.removeCallbacks(mIndeterminateProgressRunnable);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (mIndeterminateProgressRunning) {
			mHandler.post(mIndeterminateProgressRunnable);
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		
		if (action == MotionEvent.ACTION_DOWN) {
			final int x = (int)event.getRawX();
			final int y = (int)event.getRawY();
			
			if (mActionBar != null 
					&& mActionBar.isOverflowEnabled() 
					&& mActionBar.isOverflowShown()) {
				
				mOverflowMenuView.getGlobalVisibleRect(mOverflowMenuHitRect);
				
				if (!mOverflowMenuHitRect.contains(x, y)) {
					boolean hitOverflowButton = false;
					View mOverflowButton = findViewById(R.id.actbar_overflow);
					if (mOverflowButton != null) {
						mOverflowButton.getGlobalVisibleRect(mOverflowButtonHitRect);
						
						hitOverflowButton = mOverflowButtonHitRect.contains(x, y);
					}

					if (!hitOverflowButton) {
						mActionBar.hideOverflow();
						
						return true;
					}
				}
			}
		}
		
		return super.dispatchTouchEvent(event);
	}
	
	public void setActionBarIndeterminateProgress(boolean enabled) {
		if (mActionBar == null) {
			return;
		}

		mHandler.removeCallbacks(mIndeterminateProgressRunnable);
		
		mIndeterminateProgress = 0;
		
		if (enabled) {
			mIndeterminateProgressRunning = true;
			mHandler.post(mIndeterminateProgressRunnable);
		} else {
			mIndeterminateProgressRunning = false;
			clearActionBarProgress();
		}
	}
	
	public void setActionBarProgress(int progress) {
		if (mActionBar == null) {
			return;
		}
		
		mActionBar.setProgress(progress);
	}
	
	public void clearActionBarProgress() {
		if (mActionBar == null) {
			return;
		}
		
		mActionBar.clearProgress();
	}
	
	@Override
	final public void onActionBarOverflowMenuItemSelected(MenuItem item) {
		if (item == null) {
			return;
		}
		
		if (item.isEnabled() == false) {
			return;
		}
		
		mHandler.post(new MenuItemSelectedRunnable(item));
	}

	protected boolean onOverflowItemSelected(MenuItem item) {
		return false;
	}

	public void setOverflowEnabled(boolean enabled) {
		if (mActionBar == null) {
			return;
		}
		
		mActionBar.setOverflowEnabled(enabled);
	}
	
	protected boolean onCreateOverflowMenu(Menu menu) {
		return false;
	}	
	
	public Menu getOverflowMenu() {
		return mOverflowMenu;
	}

	protected void onActionBarOverflowHidden() {
	}

	protected void onActionBarOverflowDisplayed() {
	}
	
	private ActionBarOverflowCallbacks mOverflowCallbacks = 
			new ActionBarOverflowCallbacks()  {
		
		@Override
		public void preOverflowDropdownShow(View overflow, boolean withAnimation) {
			if (mOverflowPopupOverlay != null) {
				mOverflowPopupOverlay.setVisibility(View.VISIBLE);
				
				if (mOverflowPopupOverlayInAnim != null && withAnimation) {
					mOverflowPopupOverlay.startAnimation(
							mOverflowPopupOverlayInAnim);
				}
			}
			
			if (mContentView != null) {
				mContentView.lockContent();
			}
		}
		
		@Override
		public void postOverflowDropdownShow(View overflow, boolean withAnimation) {
			onActionBarOverflowDisplayed();
		}

		@Override
		public void preOverflowDropdownHide(View overflow, boolean withAnimation) {
			if (mOverflowPopupOverlay != null) {
				if (mOverflowPopupOverlayOutAnim != null && withAnimation) {
					mOverflowPopupOverlay.startAnimation(
							mOverflowPopupOverlayOutAnim);
				}
				
				mOverflowPopupOverlay.setVisibility(View.GONE);
				
				onActionBarOverflowHidden();
			}
		}
		
		@Override
		public void postOverflowDropdownHide(View overflow, boolean withAnimation) {
			if (mContentView != null) {
				mContentView.unlockContent();
			}
		}

	};

	private static final long INDETERMINATE_PRG_INTERVAL = 200;
	private static final long INDETERMINATE_PRG_INCREMENT = 5;
	private int mIndeterminateProgress = 0;
	private boolean mIndeterminateProgressRunning = false;
	
	private Runnable mIndeterminateProgressRunnable = new Runnable() {
		
		@Override
		public void run() {
			mIndeterminateProgress += INDETERMINATE_PRG_INCREMENT;
			
			final int prg = mIndeterminateProgress % 100;
//			Logger.debug("prg = %d", prg);
			
			setActionBarProgress(prg < INDETERMINATE_PRG_INCREMENT ? -1 : prg);
			
			mHandler.postDelayed(this, INDETERMINATE_PRG_INTERVAL);
		}
		
	};
	
	private CharSequence mPromptContent = null;
	private final static long DEFAULT_PROMPT_DELAY = 500;
	
	public void showPrompt(CharSequence prompt) {
		showPrompt(prompt, DEFAULT_PROMPT_DELAY);
	}
	
	public void showPrompt(CharSequence prompt, long delay) {
		if (prompt == null) {
			return;
		}
		
		mPromptContent = prompt;
		
		mHandler.removeCallbacks(mPromptShowRunnable);
		mHandler.removeCallbacks(mPromptHideRunnable);
		
		if (delay <= 0) {
			mHandler.post(mPromptShowRunnable);
		} else {
			mHandler.postDelayed(mPromptShowRunnable, delay);
		}
	}
	
	public void hidePrompt() {
		mPromptContent = null;
		
		mHandler.removeCallbacks(mPromptShowRunnable);
		mHandler.removeCallbacks(mPromptHideRunnable);
		
		mHandler.post(mPromptHideRunnable);
	}
	
	private void realShowPrompt() {
		if (mPromptView == null) {
			return;
		}
		
		mPromptView.setText(mPromptContent);
		
		if (mPromptView.getVisibility() == View.VISIBLE) {
			return;
		}
		
		mPromptView.setVisibility(View.VISIBLE);
		mPromptView.startAnimation(AnimationUtils.loadAnimation(
				this,
				R.anim.slide_fade_in_bottom));
	}
	
	private void realHidePrompt() {
		if (mPromptView == null) {
			return;
		}
		
		if (mPromptView.getVisibility() != View.VISIBLE) {
			return;
		}
		
		mPromptView.startAnimation(AnimationUtils.loadAnimation(
				this,
				R.anim.slide_fade_out_bottom));
		mPromptView.setVisibility(View.GONE);
		mPromptView.setText(null);
	}
	
	private Runnable mPromptShowRunnable = new Runnable() {
		
		@Override
		public void run() {
			realShowPrompt();
		}
		
	};
	
	private Runnable mPromptHideRunnable = new Runnable() {
		
		@Override
		public void run() {
			realHidePrompt();
		}
		
	};
	

	private Handler mHandler = new Handler();
}
