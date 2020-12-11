package com.dailystudio.memory.chart;

import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint.Align;

import com.dailystudio.memory.ui.R;
import com.dailystudio.memory.ui.utils.ColorHelper;

public class ChartUtils {
	
	public static void applyDefaulChartStyle(Context context, DefaultRenderer renderer) {
		if (context == null || renderer == null) {
			return;
		}
		
		renderer.setAxesColor(getColorResource(context, R.color.gray));
		renderer.setLabelsColor(getColorResource(context, R.color.light_black));
		
		int labelTextSize = 10;
		int titleTextSize = 10;
		int legendTextSize = 10;
		int legendHeight = 50;
		int chartMarginLeft = 20;
		int chartMarginTop = 20;
		int chartMarginRight = 20;
		int chartMarginBottom = 20;
		
		final Resources res = context.getResources();
		if (res != null) {
			labelTextSize = res.getDimensionPixelSize(R.dimen.default_chart_label_size);
			titleTextSize = res.getDimensionPixelSize(R.dimen.default_chart_title_size);
			legendTextSize = res.getDimensionPixelSize(R.dimen.default_chart_legend_size);
			legendHeight = res.getDimensionPixelSize(R.dimen.default_chart_legend_height);
			chartMarginLeft = res.getDimensionPixelSize(R.dimen.default_chart_margin_left);
			chartMarginTop = res.getDimensionPixelSize(R.dimen.default_chart_margin_top);
			chartMarginRight = res.getDimensionPixelSize(R.dimen.default_chart_margin_right);
			chartMarginBottom = res.getDimensionPixelSize(R.dimen.default_chart_margin_bottom);
		}

	    renderer.setLabelsTextSize(labelTextSize);
	    renderer.setChartTitleTextSize(titleTextSize);
		renderer.setLegendTextSize(legendTextSize);
		renderer.setLegendHeight(legendHeight);
		renderer.setFitLegend(true);
		
	    renderer.setMargins(new int[] { chartMarginTop, chartMarginLeft,
	    		chartMarginBottom, chartMarginRight });
	    renderer.setShowGrid(true);
	}
	
	public static void applyDefaulChartStyle(Context context, XYMultipleSeriesRenderer renderer) {
		applyDefaulChartStyle(context, (DefaultRenderer)renderer);
		if (context == null || renderer == null) {
			return;
		}
		
		renderer.setXLabelsAlign(Align.CENTER);
	    renderer.setYLabelsAlign(Align.RIGHT);
	    renderer.setMarginsColor(Color.WHITE);
		
		int axisLabelTextSize = 10;
		
		final Resources res = context.getResources();
		if (res != null) {
			axisLabelTextSize = res.getDimensionPixelSize(R.dimen.default_chart_axis_title_size);
		}

		renderer.setAxisTitleTextSize(axisLabelTextSize);
		
	    renderer.setPointSize(5);
	}
	
	public static void applyDefaulWidgetChartStyle(Context context, DefaultRenderer renderer) {
		applyDefaulChartStyle(context, renderer);
		
		if (context == null || renderer == null) {
			return;
		}
		
		renderer.setAxesColor(getColorResource(context, R.color.black));
		
		int chartMarginLeft = 0;
		int chartMarginTop = 0;
		int chartMarginRight = 0;
		int chartMarginBottom = 0;
		
		final Resources res = context.getResources();
		if (res != null) {
			chartMarginLeft = res.getDimensionPixelSize(R.dimen.app_widget_chart_margin_left);
			chartMarginTop = res.getDimensionPixelSize(R.dimen.app_widget_chart_margin_top);
			chartMarginRight = res.getDimensionPixelSize(R.dimen.app_widget_chart_margin_right);
			chartMarginBottom = res.getDimensionPixelSize(R.dimen.app_widget_chart_margin_bottom);
		}

	    renderer.setMargins(new int[] { chartMarginTop, chartMarginLeft,
	    		chartMarginBottom, chartMarginRight });
	    
	    renderer.setShowGrid(true);
	    renderer.setShowLegend(false);
//	    renderer.setShowLabels(false);
	    
		renderer.setLegendHeight(0);
		renderer.setFitLegend(false);
	}
	
	public static void applyDefaulWidgetChartStyle(Context context, XYMultipleSeriesRenderer renderer) {
		applyDefaulWidgetChartStyle(context, (DefaultRenderer)renderer);
		if (context == null || renderer == null) {
			return;
		}
		
		renderer.setXLabelsAlign(Align.CENTER);
	    renderer.setYLabelsAlign(Align.RIGHT);
		
		int axisLabelTextSize = 10;
		
		final Resources res = context.getResources();
		if (res != null) {
			axisLabelTextSize = res.getDimensionPixelSize(R.dimen.default_chart_axis_title_size);
		}

		renderer.setAxisTitleTextSize(axisLabelTextSize);

		renderer.setMarginsColor(getColorResource(context, R.color.chart_transparent));
		renderer.setXLabelsColor(getColorResource(context, R.color.black));
		
	    renderer.setPointSize(5);
	}

	public static int getColorResource(Context context, int resId) {
		return ColorHelper.getColorResource(context, resId);
	}

}
