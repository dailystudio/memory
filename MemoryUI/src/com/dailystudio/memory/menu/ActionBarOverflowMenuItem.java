package com.dailystudio.memory.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

public class ActionBarOverflowMenuItem implements MenuItem {
    private final int mId;
    private final int mGroup;
    
    @SuppressWarnings("unused")
	private final int mCategoryOrder;
    
    private final int mOrdering;
    
    private CharSequence mTitle;
    private CharSequence mTitleCondensed;
    private Intent mIntent;
    private char mShortcutNumericChar;
    private char mShortcutAlphabeticChar;
    
    private Drawable mIconDrawable;
    
    @SuppressWarnings("unused")
	private int mIconResId = NO_ICON;
    
    private Context mContext;
    
    private MenuItem.OnMenuItemClickListener mClickListener;
    
    private static final int NO_ICON = 0;
    
    private int mFlags = ENABLED;
    private static final int CHECKABLE      = 0x00000001;
    private static final int CHECKED        = 0x00000002;
    private static final int EXCLUSIVE      = 0x00000004;
    private static final int HIDDEN         = 0x00000008;
    private static final int ENABLED        = 0x00000010;
    
    private ActionBarOverflowMenu mActbarOverflowMenu;
    
    public ActionBarOverflowMenuItem(ActionBarOverflowMenu menu, Context context, int group, int id, int categoryOrder, int ordering,
            CharSequence title) {
    	mActbarOverflowMenu = menu;
        mContext = context;
        mId = id;
        mGroup = group;
        mCategoryOrder = categoryOrder;
        mOrdering = ordering;
        mTitle = title;
    }
    
    public char getAlphabeticShortcut() {
        return mShortcutAlphabeticChar;
    }

    public int getGroupId() {
        return mGroup;
    }

    public Drawable getIcon() {
        return mIconDrawable;
    }

    public Intent getIntent() {
        return mIntent;
    }

    public int getItemId() {
        return mId;
    }

    public ContextMenuInfo getMenuInfo() {
        return null;
    }

    public char getNumericShortcut() {
        return mShortcutNumericChar;
    }

    public int getOrder() {
        return mOrdering;
    }

    public SubMenu getSubMenu() {
        return null;
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    public CharSequence getTitleCondensed() {
        return mTitleCondensed;
    }

    public boolean hasSubMenu() {
        return false;
    }

    public boolean isCheckable() {
        return (mFlags & CHECKABLE) != 0; 
    }

    public boolean isChecked() {
        return (mFlags & CHECKED) != 0;
    }

    public boolean isEnabled() {
        return (mFlags & ENABLED) != 0;
    }

    public boolean isVisible() {
        return (mFlags & HIDDEN) == 0;
    }

    public MenuItem setAlphabeticShortcut(char alphaChar) {
        mShortcutAlphabeticChar = alphaChar;
        
        mActbarOverflowMenu.onItemChanged();
        
       return this;
    }

    public MenuItem setCheckable(boolean checkable) {
        mFlags = (mFlags & ~CHECKABLE) | (checkable ? CHECKABLE : 0);
        
        mActbarOverflowMenu.onItemChanged();
        
       return this;
    }
    
    public ActionBarOverflowMenuItem setExclusiveCheckable(boolean exclusive) {
        mFlags = (mFlags & ~EXCLUSIVE) | (exclusive ? EXCLUSIVE : 0);
        
        mActbarOverflowMenu.onItemChanged();
        
        return this;
    }

    public MenuItem setChecked(boolean checked) {
        mFlags = (mFlags & ~CHECKED) | (checked ? CHECKED : 0);
        
        mActbarOverflowMenu.onItemChanged();
        
        return this;
    }

    public MenuItem setEnabled(boolean enabled) {
        mFlags = (mFlags & ~ENABLED) | (enabled ? ENABLED : 0);
        
        mActbarOverflowMenu.onItemChanged();
        
        return this;
    }

    public MenuItem setIcon(Drawable icon) {
        mIconDrawable = icon;
        mIconResId = NO_ICON;
        
        mActbarOverflowMenu.onItemChanged();
        
        return this;
    }

    public MenuItem setIcon(int iconRes) {
        mIconResId = iconRes;
        mIconDrawable = mContext.getResources().getDrawable(iconRes);
        
        mActbarOverflowMenu.onItemChanged();
        
        return this;
    }

    public MenuItem setIntent(Intent intent) {
        mIntent = intent;
        
        mActbarOverflowMenu.onItemChanged();
        
        return this;
    }

    public MenuItem setNumericShortcut(char numericChar) {
        mShortcutNumericChar = numericChar;
        
        mActbarOverflowMenu.onItemChanged();
        
        return this;
    }

    public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
        mClickListener = menuItemClickListener;
        return this;
    }

    public MenuItem setShortcut(char numericChar, char alphaChar) {
        mShortcutNumericChar = numericChar;
        mShortcutAlphabeticChar = alphaChar;
        
        mActbarOverflowMenu.onItemChanged();
        
        return this;
    }

    public MenuItem setTitle(CharSequence title) {
        mTitle = title;
        
        mActbarOverflowMenu.onItemChanged();
        
       return this;
    }

    public MenuItem setTitle(int title) {
        mTitle = mContext.getResources().getString(title);
        
        mActbarOverflowMenu.onItemChanged();
        
       return this;
    }

    public MenuItem setTitleCondensed(CharSequence title) {
        mTitleCondensed = title;
        
        mActbarOverflowMenu.onItemChanged();
        
        return this;
    }

    public MenuItem setVisible(boolean visible) {
        mFlags = (mFlags & HIDDEN) | (visible ? 0 : HIDDEN);
        
        mActbarOverflowMenu.onItemChanged();
        
        return this;
    }

    public boolean invoke() {
        if (mClickListener != null && mClickListener.onMenuItemClick(this)) {
            return true;
        }
        
        if (mIntent != null) {
            mContext.startActivity(mIntent);
            return true;
        }
        
        return false;
    }
    
    public void setShowAsAction(int show) {
        // Do nothing. ActionMenuItems always show as action buttons.
    }

    @Override
    public MenuItem setShowAsActionFlags(int actionEnum) {
        return null;
    }

    public MenuItem setActionView(View actionView) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MenuItem setActionView(int resId) {
        return null;
    }

    public View getActionView() {
        return null;
    }

    @Override
    public MenuItem setActionProvider(ActionProvider actionProvider) {
        return null;
    }

    @Override
    public ActionProvider getActionProvider() {
        return null;
    }

    @Override
    public boolean expandActionView() {
        return false;
    }

    @Override
    public boolean collapseActionView() {
        return false;
    }

    @Override
    public boolean isActionViewExpanded() {
        return false;
    }

    @Override
    public MenuItem setOnActionExpandListener(OnActionExpandListener listener) {
        return null;
    }

}
