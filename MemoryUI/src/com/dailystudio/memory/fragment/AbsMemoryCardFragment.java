package com.dailystudio.memory.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.dailystudio.app.fragment.AbsLoaderFragment;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.activity.ActionBarActivityContent;
import com.dailystudio.memory.activity.ActionBar.ActionBarOverflowCallbacks;
import com.dailystudio.memory.menu.ActionBarOverflowMenu;
import com.dailystudio.memory.menu.ActionBarOverflowMenuView;
import com.dailystudio.memory.menu.ActionBarOverflowMenuView.ActionBarOverflowMenuItemCallbacks;
import com.dailystudio.memory.ui.CardFragmentRootView;
import com.dailystudio.memory.ui.CardFragmentRootView.OnCardFragmentDispatchTouchEventListener;
import com.dailystudio.memory.ui.R;

public abstract class AbsMemoryCardFragment<T> 
	extends AbsLoaderFragment<T> 
	implements ActionBarOverflowMenuItemCallbacks, OnCardFragmentDispatchTouchEventListener {

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
			
			hideOverflow();
		}
		
	}
	
	private TextView mCardTitle;
	private View mOverflowButton;
	
	private Animation mOverflowDropdownAnimIn;
	private Animation mOverflowDropdownAnimOut;
	
	private Menu mOverflowMenu;
	private ActionBarOverflowMenuView mOverflowMenuView;
	private Rect mOverflowMenuHitRect = new Rect();
	private Rect mOverflowButtonHitRect = new Rect();
	
	private View mOverflowPopupOverlay;
	private Animation mOverflowPopupOverlayInAnim;
	private Animation mOverflowPopupOverlayOutAnim;
	
	private ActionBarActivityContent mContentView;
	private CardFragmentRootView mRootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_card, null);
		
		setupViews(view);
		
		buildContentLayout(view, inflater, savedInstanceState);
		
		return view;
	}

	private void setupViews(View fragmentView) {
		if (fragmentView == null) {
			return;
		}
		
		final Context context = getActivity();
		Logger.debug("context= %s", context);
		if (context == null) {
			return;
		}
		
		mRootView = (CardFragmentRootView)fragmentView;
		if (mRootView != null) {
			mRootView.setOnCardFragmentDispatchTouchEventListener(this);
		}
		
		mCardTitle = (TextView) fragmentView.findViewById(R.id.card_name);
	    
	    mContentView = (ActionBarActivityContent) fragmentView.findViewById(
	    		R.id.card_content_holder);
	    
	    mOverflowMenuView = (ActionBarOverflowMenuView) fragmentView.findViewById(
    			R.id.card_overflow_menu);
	    if (mOverflowMenuView != null) {
	    	mOverflowMenuView.setActionBarOverflowMenuItemCallbacks(this);
	    }
    
		mOverflowButton = fragmentView.findViewById(R.id.card_overflow);
		if (mOverflowButton != null) {
			mOverflowButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					toggleOverflowDropdown();
				}

			});
		}
		
	    mOverflowMenu = new ActionBarOverflowMenu(context);
	    
	    boolean menuCreated = onCreateOverflowMenu(mOverflowMenu);
	    
	    setOverflowEnabled(menuCreated && mOverflowMenuView != null);

	    if (mOverflowMenuView != null) {
	    	mOverflowMenuView.attchToMenu(menuCreated ?
	    			mOverflowMenu : null);
	    }
    
	    mOverflowPopupOverlay = fragmentView.findViewById(
	    		R.id.card_overflow_popup_overlay);
	    
	    if (mOverflowPopupOverlay != null) { 
//	    		&& !ViewHelper.isLargeScreen(context.getApplicationContext())) {
	    	mOverflowPopupOverlayInAnim = AnimationUtils.loadAnimation(
	    			context, R.anim.actbar_overflow_popup_overlay_in);
	    	mOverflowPopupOverlayOutAnim = AnimationUtils.loadAnimation(
	    			context, R.anim.actbar_overflow_popup_overlay_out);
	    }
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		
		if (action == MotionEvent.ACTION_DOWN) {
			final int x = (int)event.getRawX();
			final int y = (int)event.getRawY();
			
			if (isOverflowEnabled() 
					&& isOverflowShown()) {
				
				mOverflowMenuView.getGlobalVisibleRect(mOverflowMenuHitRect);
				
				if (!mOverflowMenuHitRect.contains(x, y)) {
					boolean hitOverflowButton = false;
					if (mOverflowButton != null) {
						mOverflowButton.getGlobalVisibleRect(mOverflowButtonHitRect);
						
						hitOverflowButton = mOverflowButtonHitRect.contains(x, y);
					}

					if (!hitOverflowButton) {
						hideOverflow();
						
						return true;
					}
				}
			}
		}
		
		return false;
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
			mOverflowCallbacks.preOverflowDropdownShow(overflowDropdown, withAnimation);
		};
		
		overflowDropdown.setVisibility(View.VISIBLE);
		if (mOverflowDropdownAnimIn != null && withAnimation) {
			overflowDropdown.startAnimation(mOverflowDropdownAnimIn);
		} else {
			if (mOverflowCallbacks != null) {
				mOverflowCallbacks.postOverflowDropdownShow(overflowDropdown, withAnimation);
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
			mOverflowCallbacks.preOverflowDropdownHide(overflowDropdown, withAnimation);
		};

		if (mOverflowDropdownAnimOut != null && withAnimation) {
			overflowDropdown.startAnimation(mOverflowDropdownAnimOut);
		} else {
			if (mOverflowCallbacks != null) {
				mOverflowCallbacks.postOverflowDropdownHide(overflowDropdown, withAnimation);
			}
		}
		
		overflowDropdown.clearFocus();
		overflowDropdown.setVisibility(View.INVISIBLE);
	}
	
	private View getOverflowDropdown() {
		final View fragmentView = getView();
		if (fragmentView == null)  {
			return null;
		}

		return fragmentView.findViewById(R.id.card_overflow_menu);
	}
	
	public boolean isOverflowEnabled() {
		if (mOverflowButton == null) {
			return false;
		}
		
		return (mOverflowButton.getVisibility() == View.VISIBLE);
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
			if (oldVisibility != View.VISIBLE) {
				mOverflowButton.setVisibility(View.VISIBLE);
			}
		} else {
			if (oldVisibility == View.VISIBLE) {
				mOverflowButton.setVisibility(View.GONE);
			}
		}
		
		hideOverflow(false);
	}
	
	public boolean isOverflowShown() {
		View overflowDropdown = getOverflowDropdown();
		if (overflowDropdown == null) {
			return false;
		}
		
		final int visibility = overflowDropdown.getVisibility();
		
		return (visibility == View.VISIBLE);
	}
	
	public void setOverflowCallbacks(ActionBarOverflowCallbacks callbacks) {
		mOverflowCallbacks = callbacks;
	}
	
	protected void buildContentLayout(View fragmentView, LayoutInflater inflater,
			Bundle savedInstanceState) {
		if (fragmentView == null || inflater == null) {
			return;
		}
		
		if (mContentView != null) {
			View content = createContentLayout(inflater, 
					mContentView, savedInstanceState);
			if (content != null) {
				mContentView.addView(content);
			}
		}
	}
	
	public void setTitle(CharSequence title) {
		if (mCardTitle != null) {
			mCardTitle.setText(title);
		}
	}

	public void setTitle(int titleResId) {
		if (mCardTitle != null) {
			mCardTitle.setText(titleResId);
		}
	}
	
	protected boolean onCreateOverflowMenu(Menu menu) {
		return false;
	}
	
	protected boolean onOverflowItemSelected(MenuItem item) {
		return false;
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
	
	protected void onActionBarOverflowHidden() {
	}

	protected void onActionBarOverflowDisplayed() {
	}
	
	abstract protected View createContentLayout(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState);
	
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
	
	private Handler mHandler = new Handler();

}
