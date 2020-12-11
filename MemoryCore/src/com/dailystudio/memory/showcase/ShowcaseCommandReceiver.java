package com.dailystudio.memory.showcase;

import java.io.IOException;

import com.dailystudio.GlobalContextWrapper;
import com.dailystudio.app.utils.FileUtils;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;

public class ShowcaseCommandReceiver extends BroadcastReceiver {

	private static class PrepareShowcaseAsyncTask extends AsyncTask<Void, Void, Boolean> {

		private Context mContext;
		
		private PrepareShowcaseAsyncTask(Context context) {
			mContext = context;
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			String cachesDir = 
					ShowcaseDirectories.getShowcaseCachesDirectory(mContext);
			Logger.debug("cachesDir = %s", cachesDir);
			if (cachesDir == null) {
				return false;
			}
			
			boolean success = 
					FileUtils.checkOrCreateNoMediaDirectory(cachesDir);
			Logger.debug("success = %s", success);
			if (success == false) {
				return false;
			}
			
			AssetManager assetmgr = mContext.getAssets();
			if (assetmgr == null) {
				return false;
			}
		
			String lfcontent = null;
			try {
				lfcontent = FileUtils.getAssetFileContent(
						mContext, String.format("%s/%s",
								Constants.ASSETS_SHOWCASE_DIRECTORY,
								Constants.ASSETS_SHOWCASE_FILE_LIST));
			} catch (IOException e) {
				Logger.warnning("could not load showcase filelist from [%s/%s]: %s",
						Constants.ASSETS_SHOWCASE_DIRECTORY,
						Constants.ASSETS_SHOWCASE_FILE_LIST,
						e.toString());
				
				lfcontent = null;
			}
			
			Logger.debug("lfcontent = %s", lfcontent);
			if (lfcontent == null) {
				return false;
			}
			
			ShowcaseBundle scBundle = null; 
			try {
				Gson gson = new Gson();
				scBundle = gson.fromJson(lfcontent, ShowcaseBundle.class);
			} catch (JsonSyntaxException e){
				Logger.warnning("parse filelist[%s] failure: %s", 
						lfcontent,
						e.toString());
				scBundle = null;
			}
			
			if (scBundle == null) {
				return false;
			}
			
			if (!ShowcaseBundle.isUpdatedNeed(mContext, scBundle)) {
				Logger.warnning("Skip, bundle is alreay updated: %s",
						scBundle);
				
				return true;
			}
			
			String srcFile = null;
			String dstFile = null;
			success = true;
			for (String scfile: scBundle.bundleFiles) {
				srcFile = String.format("%s/%s",
						Constants.ASSETS_SHOWCASE_DIRECTORY,
						scfile);
				dstFile = String.format("%s/%s",
						cachesDir,
						scfile);
				
				Logger.debug("[COPY SHOWCASE FILE]: %s, to: %s", 
						srcFile,
						dstFile);
				try {
					success = FileUtils.copyAssetFile(mContext, 
							srcFile, dstFile);
				} catch (IOException e) {
					Logger.warnning("copy showcase file[%s] failure: %s", 
							srcFile,
							e.toString());
					success = false;
				}
				
				if (success == false) {
					break;
				}
			}
			
			if (success) {
				ShowcaseBundle.setLastBundleVersion(mContext, scBundle);
			}
			
			return success;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			Logger.debug("[PREPARE SHOWCASE]: result = %s, pkg = %s",
					result,
					mContext.getPackageName());
			if (result) {
				Showcase.updateShowcase(mContext, 
						mContext.getPackageName());
			}
		}
		
		
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Context appContext = context.getApplicationContext();

		GlobalContextWrapper.bindContext(appContext);
		
		if (intent == null) {
			return;
		}

		final String action = intent.getAction();
		Logger.debug("action = %s", action);

		if (action == null) {
			return;
		}
		
		final long now = System.currentTimeMillis();
		
		if (Constants.ACTION_PREPARE_SHOWCASE.equals(action)) {
			prepareShowcase(appContext, now);
		} else if (Constants.ACTION_DESTROY_SHOWCASE.equals(action)) {
			destoryShowcase(appContext, now);
		} 
	}

	private void destoryShowcase(Context c, long now) {
		
	}

	private void prepareShowcase(Context context, long now) {
		new PrepareShowcaseAsyncTask(context).executeOnExecutor(
				AsyncTask.THREAD_POOL_EXECUTOR,
				(Void)null);
	}

}
