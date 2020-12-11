package com.dailystudio.memory.menu;

import com.dailystudio.memory.menu.ActionBarOverflowMenu.MenuItemChangedListener;
import com.dailystudio.memory.ui.R;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ActionBarOverflowMenuListAdapter extends BaseAdapter {

	protected Context mContext;
	
	private Menu mMenuData;
	
	public ActionBarOverflowMenuListAdapter(Context context) {
		mContext = context;
	}
	
	public void attachMenu(Menu menu) {
		mMenuData = menu;
		
		if (mMenuData != null) {
			notifyDataSetChanged();
			
			if (mMenuData instanceof ActionBarOverflowMenu) {
				ActionBarOverflowMenu abmenu = 
					((ActionBarOverflowMenu)mMenuData);
				abmenu.setMenuItemChangedListener(mMenuItemChangedListener);
			}
		} else {
			notifyDataSetInvalidated();
		}
	}
	
	@Override
	public int getCount() {
		if (mMenuData == null) {
			return 0;
		}
		
		return mMenuData.size();
	}

	@Override
	public Object getItem(int position) {
		if (mMenuData == null) {
			return null;
		}
		
		final int N = getCount();
		
		if (position < 0 || position >= N) {
			return null;
		}
		
		return mMenuData.getItem(N - 1 - position);
	}

	@Override
	public long getItemId(int position) {
		if (mMenuData == null) {
			return -1l;
		}
		
		final MenuItem mitem = mMenuData.getItem(position);
		
		return mitem.getItemId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView instanceof ViewGroup == false) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.actbar_overflow_menu_item, null);
		}
		
		Object o = getItem(position);
		if (o instanceof MenuItem) {
			MenuItem mitem = (MenuItem)o;
			
			ImageView iconView = (ImageView)convertView.findViewById(R.id.menu_item_icon);
			if (iconView != null) {
				iconView.setImageDrawable(mitem.getIcon());
				iconView.setAlpha(mitem.isEnabled() ? 255 : 204);
			}
			
			TextView labelView = (TextView)convertView.findViewById(R.id.menu_item_text);
			if (labelView != null) {
				labelView.setText(mitem.getTitle());
				labelView.setEnabled(mitem.isEnabled());
			}
		}
		
		return convertView;
	}
	
	private MenuItemChangedListener mMenuItemChangedListener = 
		new MenuItemChangedListener() {
		
		@Override
		public void onItemsChanged() {
			mHandler.post(mNotifyItemsChangedRunnable);
		}
		
	};
	
	private Runnable mNotifyItemsChangedRunnable = new Runnable() {
		
		@Override
		public void run() {
			notifyDataSetChanged();
		}
		
	};
	
	private Handler mHandler = new Handler();

}
