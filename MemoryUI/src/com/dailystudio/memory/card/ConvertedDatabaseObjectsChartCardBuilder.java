package com.dailystudio.memory.card;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.dailystudio.app.utils.BitmapUtils;
import com.dailystudio.dataobject.DatabaseObject;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.chart.ChartBuilder;

public abstract class ConvertedDatabaseObjectsChartCardBuilder<D extends DatabaseObject, P extends DatabaseObject, C extends DatabaseObject>  
	extends ConvertedDatabaseObjectsCardBuilder<D, P, C> {

	private final static int DEFAULT_CHART_WIDTH = 520;
	private final static int DEFAULT_CHART_HEIGHT = 250;
	
	public static final String ELEMENT_KEY_CHART_BITMAP = "element_chart_bitmap";
	
	private ChartBuilder<C> mChartBuilder;
	
	private View mChartView = null;
	
	private Object mLockObject = new Object();
	
	private Object mDataSet = null;
	private Object mRenderer = null;
	private Object mSharedArguments = null;
	
	private Handler mHandler;
	
	public ConvertedDatabaseObjectsChartCardBuilder(Context context,
			String templFile, String targetFile) {
		super(context, templFile, targetFile);
	}

	protected void buildCardElementsWithDatabaseObjects(Context context,
			CardElements elements, List<C> objects) {
		mDataSet = null;
		mRenderer = null;
		mChartView = null;
		mSharedArguments = null;
		
		mChartBuilder = createChartBuilder();
		Logger.debug("mChartBuilder = %s", mChartBuilder);
		if (mChartBuilder == null) {
			return;
		}
		
		mSharedArguments = createSharedArguments();

		mDataSet = mChartBuilder.buildDataset(objects, mSharedArguments);
		mRenderer = mChartBuilder.buildRenderer(objects, mSharedArguments);
		Logger.debug("mDataSet = [%s], mRenderer = [%s]",
				mChartBuilder, mDataSet, mRenderer);

		/*
		 * XXX: we must create handler in main thread,
		 * 		otherwise it will be create in IntentService thread,
		 * 		which may be a dead thread soon.
		 */
		mHandler = new Handler(context.getMainLooper());
		
		mHandler.post(mBuildChartRunnable);
		
		synchronized (mLockObject) {
			try {
				Logger.debug("WAIT() for createView");
				mLockObject.wait();
			} catch (InterruptedException e) {
				Logger.warnning("WAIT() release: %s",
						e.toString());
			}
		}
		
		final int chartWidth = getChartWidth();
		final int chartHeight = getChartHeight();
		Logger.debug("chartCard.chartView = [%s]", mChartView);
		
		final Bitmap bitmap = BitmapUtils.createViewSnapshot(context,
				mChartView, 
				chartWidth, chartHeight);
		Logger.debug("chartCard.bitmap = [%s]", bitmap);
		if (bitmap != null) {
//			BitmapUtils.saveBitmap(bitmap, "/sdcard/chart.png");
			final String base64str = 
					bitmapToBase64String(bitmap);
			Logger.debug("chartCard.base64str = [%s]", bitmap);
			
			if (!TextUtils.isEmpty(base64str)) {
				elements.putElement(ELEMENT_KEY_CHART_BITMAP, 
						base64str);
			}
		}
	}
	
	protected Object createSharedArguments() {
		if (mChartBuilder == null) {
			return null;
		}
		
		return mChartBuilder.createShareArguments();
	}
	
	public int getChartWidth() {
		return DEFAULT_CHART_WIDTH;
	}
	
	public int getChartHeight() {
		return DEFAULT_CHART_HEIGHT;
	}
	
	protected Object getChartDataSet() {
		return mDataSet;
	}
	
	protected Object getChartRenderer() {
		return mRenderer;
	}
	
	protected Object getChartSharedArguments() {
		return mSharedArguments;
	}
	
	abstract protected ChartBuilder<C> createChartBuilder();

	private Runnable mBuildChartRunnable = new Runnable() {
		
		@Override
		public void run() {
			if (mChartBuilder != null) {
				mChartView = mChartBuilder.getChart(mDataSet, mRenderer);
			}
			
			synchronized (mLockObject) {
				mLockObject.notifyAll();
			}
		}
		
	};
	
}
