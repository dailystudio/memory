package com.dailystudio.memory.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.dailystudio.app.utils.ActivityLauncher;
import com.dailystudio.app.utils.BitmapUtils;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.fragment.MemoryChartFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

public abstract class MemoryPeroidChartActivity extends MemoryPeroidBasedActivity {

	private final static String EXPORTED_CHART_FILE = "chart_export.png";

	protected void shareChart(CharSequence title, 
			CharSequence subject, CharSequence content) {
		final MemoryChartFragment<?> chartFragment = getChartFragment();
		if (chartFragment == null) {
			return;
		}
		
		final Bitmap chartBitmap = chartFragment.dumpChart();
		Logger.debug("chartBitmap = %s", chartBitmap);
		if (chartBitmap == null) {
			return;
		}
		
		final File exportFile = saveCharToFile(chartBitmap);
		Logger.debug("exportFile = %s", exportFile);
		if (exportFile == null) {
			return;
		}
		
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		
		shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(exportFile));  
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		shareIntent.putExtra(Intent.EXTRA_TEXT,  content);  
		shareIntent.setType("image/png");
		
		ActivityLauncher.launchActivity(this, 
				Intent.createChooser(shareIntent, title));  
	}
	
	@SuppressLint("WorldReadableFiles")
	protected File saveCharToFile(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		
		FileOutputStream ostream = null;
		try {
			ostream = openFileOutput(EXPORTED_CHART_FILE, 
					Context.MODE_WORLD_READABLE);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				ostream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		File outputFile = getFileStreamPath(EXPORTED_CHART_FILE);
		
		boolean success = BitmapUtils.saveBitmap(bitmap, outputFile);
		if (success == false) {
			return null;
		}
		
		return outputFile;
	}

	abstract protected MemoryChartFragment<?> getChartFragment();

}
