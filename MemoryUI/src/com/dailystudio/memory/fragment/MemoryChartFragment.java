package com.dailystudio.memory.fragment;

import java.util.List;

import com.dailystudio.app.fragment.AbsLoaderFragment;
import com.dailystudio.dataobject.DatabaseObject;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.loader.ConvertedDatabaseChartLoader;
import com.dailystudio.memory.ui.utils.ViewHelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.ViewGroup;

public abstract class MemoryChartFragment<C extends DatabaseObject> 
	extends AbsLoaderFragment<List<C>> {

	private View mEmptyView;
	private boolean mOldEmpty = true;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Intent intent = getActivity().getIntent();
		
		bindIntent(intent);
		bindEmptyView();
		
		super.onActivityCreated(savedInstanceState);
		
		getLoaderManager().initLoader(getLoaderId(), createLoaderArguments(), this);
	}
	
	protected void bindEmptyView() {
		final View fragmentView = getView();
		if (fragmentView == null) {
			return;
		}

		final View emptyView = 
				fragmentView.findViewById(getEmptyViewId());
		setEmptyView(emptyView);
	}
	
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;

        updateEmptyStatus(mOldEmpty);
    }
    
    protected void updateEmptyStatus(boolean empty) {
    	Logger.debug("empty = %s", empty);
		final ViewGroup chartHolder = getChartHolder();
		
        if (empty) {
            if (mEmptyView != null) {
                mEmptyView.setVisibility(View.VISIBLE);
                
                if (chartHolder != null) {
                	chartHolder.setVisibility(View.GONE);
                }
            } else {
                // If the caller just removed our empty view, make sure the holder is visible
            	if (chartHolder != null) {
            		chartHolder.setVisibility(View.VISIBLE);
            	}
            }

            if (chartHolder != null) {
        		chartHolder.requestLayout();
        	}
        } else {
            if (mEmptyView != null) {
            	mEmptyView.setVisibility(View.GONE);
            }
            
        	if (chartHolder != null) {
        		chartHolder.setVisibility(View.VISIBLE);
        	}
        }
        
        mOldEmpty = empty;
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public void onLoadFinished(Loader<List<C>> loader, List<C> data) {
		if (loader instanceof ConvertedDatabaseChartLoader == false) {
			return;
		}
		
		final ConvertedDatabaseChartLoader<?, ?, C> charLoader = 
			(ConvertedDatabaseChartLoader<?, ?, C>)loader;
		
		final boolean empty = charLoader.isEmpty();
		
		final View chart = getChart(
				charLoader.getDataSet(), charLoader.getChartRenderer());
		
		final ViewGroup chartHolder = getChartHolder();
		if (chartHolder != null && chart != null) {
			if (chartHolder.getChildCount() > 0) {
				chartHolder.removeAllViews();
			}
			
			ViewHelper.disableHardwareAccelerated(chart);
			
			chartHolder.addView(chart);
			chartHolder.scheduleLayoutAnimation();
		}

		updateEmptyStatus(empty);
	}

	@Override
	public void onLoaderReset(Loader<List<C>> loader) {
		final ViewGroup chartHolder = getChartHolder();
		if (chartHolder != null) {
			chartHolder.removeAllViews();
		}
	}
	
	public Bitmap dumpChart() {
		return dumpChart(getChartHolder());
	}
	
	protected Bitmap dumpChart(View charView) {
		if (charView == null) {
			return null;
		}
		
		final int width = charView.getWidth();
		final int height = charView.getHeight();
		if (width <= 0 || height <= 0) {
			return null;
		}
		
		final Bitmap bitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
		if (bitmap == null) {
			return null;
		}
		
		final Canvas canvas = new Canvas(bitmap);
		
		charView.draw(canvas);
		
		return bitmap;
	}
	
	abstract protected View getChart(Object dataset, Object renderer);
	abstract protected ViewGroup getChartHolder();

}
