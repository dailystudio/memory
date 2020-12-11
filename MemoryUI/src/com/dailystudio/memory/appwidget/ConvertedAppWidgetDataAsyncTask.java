package com.dailystudio.memory.appwidget;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.RemoteViews;

import com.dailystudio.app.dataobject.asynctask.ConvertedDatabaseObjectsAsyncTask;
import com.dailystudio.dataobject.DatabaseObject;
import com.dailystudio.dataobject.query.Query;
import com.dailystudio.datetime.dataobject.TimeCapsuleQueryBuilder;
import com.dailystudio.memory.ui.R;

public abstract class ConvertedAppWidgetDataAsyncTask<D extends DatabaseObject, P extends DatabaseObject, C extends DatabaseObject> 
	extends ConvertedDatabaseObjectsAsyncTask<D, P, C> {

	protected final int DEFAULT_APPWIDGET_GRID_WIDTH = 80;
	protected final int DEFAULT_APPWIDGET_GRID_HEIGHT = 80;
	
	static interface AsyncTaskCallbacks {
		
		public void onAsyncTaskFinished(ConvertedAppWidgetDataAsyncTask<?, ?, ?> asyncTask,
				List<?> result, RemoteViews remoteViews);
		
	}
	
	private AsyncTaskCallbacks mCallbacks;
	private RemoteViews mRemoteViews;

	private long mPeroidStart;
	private long mPeroidEnd;
	
	public ConvertedAppWidgetDataAsyncTask(Context context) {
		this(context, -1, -1);
	}

	public ConvertedAppWidgetDataAsyncTask(Context context, long start, long end) {
		super(context);
		
		setPeroid(start, end);
	}
	
	public void setPeroid(long start, long end) {
		mPeroidStart = start;
		mPeroidEnd = end;
		
		if (mPeroidStart < 0) {
			mPeroidStart = System.currentTimeMillis();
		}
		
		if (mPeroidEnd < mPeroidStart) {
			mPeroidEnd = mPeroidStart;
		}
	}
	
	public long getPeroidStart() {
		return mPeroidStart;
	}
	
	public long getPeroidEnd() {
		return mPeroidEnd;
	}
	
	@Override
	protected void onPostExecute(List<C> data) {
		super.onPostExecute(data);
		
		mRemoteViews = bindRemoteViews(data);
		
		if (mCallbacks != null) {
			mCallbacks.onAsyncTaskFinished(this, data, mRemoteViews);
		}
	}
	
	public void setCallbacks(AsyncTaskCallbacks callbacks) {
		mCallbacks = callbacks;
	}
	
	@Override
	protected Query getQuery(Class<D> klass) {
		if (mPeroidEnd <= mPeroidStart) {
			return super.getQuery(getObjectClass());
		}
		
		TimeCapsuleQueryBuilder builer =
			new TimeCapsuleQueryBuilder(getObjectClass());
		
		return builer.getQuery(mPeroidStart, mPeroidEnd);		
	}
	
	protected int[] estimateDimensionInWidget(int widgetLayoutResId, int viewId,
			int widgetSpanX, int widgetSpanY) {
		int[] dimens = new int[2];
		
		dimens[0] = 0;
		dimens[1] = 0;
		
		final Context context = getContext();
		if (context == null) {
			return null;
		}
		
		View widget = LayoutInflater.from(context).inflate(
				widgetLayoutResId, null);
		if (widget == null) {
			return null;
		}
		
		View targetView = widget.findViewById(viewId);
		if (targetView == null) {
			return null;
		}
		
		
		final int width = estimateWidgetWidth(widgetSpanX);
		final int height = estimateWidgetHeight(widgetSpanY);
		if (width <= 0 || height <= 0) {
			return dimens;
		}
		
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(
				width, MeasureSpec.EXACTLY);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(
				height, MeasureSpec.EXACTLY);
		
		widget.measure(widthMeasureSpec, heightMeasureSpec);
		
		dimens[0] = targetView.getMeasuredWidth();
		dimens[1] = targetView.getMeasuredHeight();
		
		return dimens;
	}
	
	protected int estimateWidgetWidth(int spanX) {
		if (spanX <= 0) {
			return 0;
		}
		
		final Context context = getContext();
		if (context == null) {
			return 0;
		}
		
		final Resources res = context.getResources();
		if (res == null) {
			return 0;
		}
		
		final int gridW = res.getDimensionPixelSize(R.dimen.app_widget_grid_width);
		
		return (spanX * gridW);
	}

	protected int estimateWidgetHeight(int spanY) {
		if (spanY <= 0) {
			return 0;
		}
		
		final Context context = getContext();
		if (context == null) {
			return 0;
		}
		
		final Resources res = context.getResources();
		if (res == null) {
			return 0;
		}
		
		final int gridH = res.getDimensionPixelSize(R.dimen.app_widget_grid_height);
		
		return (spanY * gridH);
	}

	abstract protected RemoteViews bindRemoteViews(List<C> data);
	abstract protected ComponentName getAppWidgetProvider();

}
