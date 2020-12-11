package com.dailystudio.memory.menu;

import com.dailystudio.memory.ui.R;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;

public class ActionBarOverflowMenuView extends FrameLayout {
	
	public static interface ActionBarOverflowMenuItemCallbacks {
		
		public void onActionBarOverflowMenuItemSelected (MenuItem item);
		
	}
	
	private ListView mMenuList;
	private ActionBarOverflowMenuListAdapter mMenuListAdapter;
	
	private ActionBarOverflowMenuItemCallbacks mOverflowMenuItemCallbacks;
	
    public ActionBarOverflowMenuView(Context context) {
        this(context, null);
    }

    public ActionBarOverflowMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        initMembers();
    }

	private void initMembers() {
        LayoutInflater.from(getContext()).inflate(
        		R.layout.actbar_overflow_menu, this);
        
        mMenuList = (ListView)findViewById(R.id.actbar_overflow_menu_list);
        if (mMenuList != null) {
        	
        	mMenuList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (mOverflowMenuItemCallbacks == null) {
						return;
					}
					
					if (mMenuListAdapter == null) {
						return;
					}
					
					Object o = mMenuListAdapter.getItem(position);
					if (o instanceof MenuItem == false) {
						return;
					}
					
					MenuItem mitem = (MenuItem)o;
					
					mOverflowMenuItemCallbacks.onActionBarOverflowMenuItemSelected(mitem);
				}
        		
			});
        	
        	mMenuListAdapter = 
        		new ActionBarOverflowMenuListAdapter(getContext());
        	
        	mMenuList.setAdapter(mMenuListAdapter);
        }
	}
	
	public void attchToMenu(Menu menu) {
		if (mMenuListAdapter == null) {
			return;
		}
		
		mMenuListAdapter.attachMenu(menu);
	}
	
	public void setActionBarOverflowMenuItemCallbacks(
			ActionBarOverflowMenuItemCallbacks callbacks) {
		mOverflowMenuItemCallbacks = callbacks;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final Resources res = getResources();
		if (res != null) {
			int maxWidth = res.getDimensionPixelSize(
					R.dimen.actbar_overflow_menu_max_width);
			
			if (maxWidth > 0) {
				widthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth, 
						MeasureSpec.AT_MOST);
			}
		}
			
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
}
