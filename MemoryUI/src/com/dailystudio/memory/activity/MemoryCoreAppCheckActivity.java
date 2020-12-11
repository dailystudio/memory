package com.dailystudio.memory.activity;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.dailystudio.app.utils.ActivityLauncher;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;
import com.dailystudio.memory.ui.AlertDialogFragment;
import com.dailystudio.memory.ui.R;
import com.dailystudio.memory.ui.AlertDialogFragment.AlertDialogFragmentCallbacks;
import com.dailystudio.nativelib.application.AppObservable;
import com.dailystudio.nativelib.observable.NativeObservable;
import com.dailystudio.nativelib.observable.ObservableManager;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class MemoryCoreAppCheckActivity extends ActionBarActivity {

	private static final String MEMORY_GOOGLE_PLAY_LINK = 
			"market://details?id=com.dailystudio.memory";
	
	private static final String MEMORY_PACKGE_NAME = 
			"com.dailystudio.memory";
	private static final String MEMORY_ACTIVITY_NAME = 
			"com.dailystudio.memory.MainActivity";
	
	public static class ConfirmDialogFragment extends AlertDialogFragment {

		@Override
		protected CharSequence getConfirmText() {
			return getActivity().getString(
					R.string.alert_dialog_button_confirm);
		}

		@Override
		protected CharSequence getCancelText() {
			return getActivity().getString(
					R.string.alert_dialog_button_cancel);
		}

		@Override
		protected CharSequence getTitle() {
			return getActivity().getString(
					R.string.core_app_check_confirm_dialog_title);
		}

		@Override
		protected CharSequence getMessage() {
			return getActivity().getString(
					R.string.core_app_check_confirm_dialog_message);
		}

		@Override
		protected void onDialogConfirmed(DialogInterface dialog, int which) {
		}

		@Override
		protected void onDialogCancelled(DialogInterface dialog, int which) {
		}
		
	}
	
	private class CoreAppCheckAsyncTask extends AsyncTask<Void, Void, Boolean> {

		private Context mContext;
		
		private CoreAppCheckAsyncTask(Context context) {
			mContext = context;
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			final Context context = mContext;
			if (context == null) {
				return false;
			}
					
			final PackageManager pkgmgr = context.getPackageManager();
			if (pkgmgr == null) {
				return false;
			}
			
			final Intent queryIntent = new Intent();
			
			queryIntent.setClassName(MEMORY_PACKGE_NAME, MEMORY_ACTIVITY_NAME);
			
			List<ResolveInfo> rInfo =
					pkgmgr.queryIntentActivities(queryIntent, 0);
			
			return (rInfo != null && rInfo.size() > 0);
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			
			if (result != null) {
				final boolean existed = result.booleanValue();
				setStepOneEnable(!existed);
				setStepTwoEnable(existed);
			}
		}
		
	}
	
	private View mGooglPlayBadge;
	private View mStepOneView;
	private TextView mMemoryInstalledView;
	
	private View mStepTwoView;
	private View mPluginLayout;
	private ImageView mPluginIcon;
	private TextView mPluginName;
	
	private Button mKnowButton;
	private CheckBox mRemoveCheck;
	
	private CoreAppCheckAsyncTask mAsyncTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_core_app_check);
		
		setupViews();
		
		final NativeObservable observable = 
				ObservableManager.getObservable(AppObservable.class);
		if (observable != null) {
			observable.addObserver(mAppObserver);
		}
		
		bindPlugin();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		startChecking();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		final NativeObservable observable = 
				ObservableManager.getObservable(AppObservable.class);
		if (observable != null) {
			observable.deleteObserver(mAppObserver);
		}
	}

	private void setupViews() {
		mPluginIcon = (ImageView) findViewById(R.id.core_app_check_plugin_icon);
		mPluginName = (TextView) findViewById(R.id.core_app_check_plugin_name);
		
		mPluginLayout = findViewById(R.id.core_app_check_plugin);
		if (mPluginLayout != null) {
			mPluginLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					launchPluginPage();
				}
				
				@SuppressWarnings("unused")
				protected void launchMainActivity() {
					Intent i = new Intent();
					
					i.setClassName(MEMORY_PACKGE_NAME,
							MEMORY_ACTIVITY_NAME);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					
					ActivityLauncher.launchActivity(
							MemoryCoreAppCheckActivity.this, i);
				}
				
				protected void launchPluginPage() {
					Intent i = new Intent();
					
					i.setAction(Constants.ACTION_LIST_PLUGIN_ACTIVITIES);
					i.putExtra(Constants.EXTRA_ACTIVITY_CATEGORY, getPluginCategory());
					i.putExtra(Constants.EXTRA_ACTIVITY_PACKAGE, getPluginPackage());
					
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					
					int clearTaskFlag = 0;
					if (Build.VERSION.SDK_INT >= 11) { // Android v3.0+
						try {
							Field field = Intent.class.getField("FLAG_ACTIVITY_CLEAR_TASK");
							field.setAccessible(true);
							
							clearTaskFlag = field.getInt(Intent.class);
						} catch (Exception e) {
							Logger.warnning("get FLAG_ACTIVITY_CLEAR_TASK failure: %s", e.toString());
						}
					}
					
					Logger.debug("FLAG_ACTIVITY_CLEAR_TASK = %d", clearTaskFlag);
					if (clearTaskFlag != 0) {
						i.addFlags(clearTaskFlag);
					}
					
					ActivityLauncher.launchActivity(
							MemoryCoreAppCheckActivity.this, i);

				}
				
			});
		}
		
		mGooglPlayBadge = findViewById(R.id.core_app_check_google_play_badge);
		if (mGooglPlayBadge != null) {
			mGooglPlayBadge.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(Intent.ACTION_VIEW);
					
					i.setData(Uri.parse(MEMORY_GOOGLE_PLAY_LINK));
					
					ActivityLauncher.launchActivity(
							MemoryCoreAppCheckActivity.this, i);
				}
				
			});
		}
		
		mStepOneView = findViewById(R.id.core_app_check_step_one);
		mStepTwoView = findViewById(R.id.core_app_check_step_two);
		
		mMemoryInstalledView = (TextView) findViewById(R.id.core_app_check_memory_installed);
		
		mRemoveCheck = (CheckBox) findViewById(R.id.core_app_check_not_show_again_check);
		if (mRemoveCheck != null) {
			mRemoveCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						AlertDialogFragment confirmDialog = 
								new ConfirmDialogFragment();
							
							confirmDialog.setCallbacks(mConfirmCallbacks);
							
							confirmDialog.show(getSupportFragmentManager(), 
									"check-confirm-dialog");
					}
				}
				
			});
		}
		
		mKnowButton = (Button) findViewById(R.id.core_app_check_know_button);
		if (mKnowButton != null) {
			mKnowButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (mRemoveCheck == null) {
						return;
					}
					
					if (mRemoveCheck.isChecked()) {
						disableCurrentActivity();
					}
					
					finish();
				}
				
			});
		}
	}
	
	protected void disableCurrentActivity() {
		final PackageManager pkgmgr = getPackageManager();
		
		final ComponentName componentName = getComponentName();
		Logger.debug("componentName = %s", componentName);
		
		pkgmgr.setComponentEnabledSetting(componentName, 
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 
				PackageManager.DONT_KILL_APP);
	}
	
	protected void bindPlugin() {
		final Drawable pluginIcon = getPluginIcon();
		final CharSequence pluginName = getPluginName();
		
		if (mPluginIcon != null && pluginIcon != null) {
			mPluginIcon.setImageDrawable(pluginIcon);
		}
		
		if (mPluginName != null && pluginName != null) {
			mPluginName.setText(pluginName);
		}
	}

	private void startChecking() {
		stopChecking();
		
		mAsyncTask = new CoreAppCheckAsyncTask(this);
		
		mAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void)null);
	}
	
	private void stopChecking() {
		if (mAsyncTask != null
				&& mAsyncTask.getStatus() == AsyncTask.Status.FINISHED) {
			mAsyncTask.cancel(true);
		}
	}
	
	private void setStepOneEnable(boolean enabled) {
		Logger.debug("enabled = %s", enabled);
		if (mGooglPlayBadge != null) {
			mGooglPlayBadge.setEnabled(enabled);
		}
		
		if (mStepOneView != null) {
			mStepOneView.setEnabled(enabled);
		}
		
		if (mMemoryInstalledView != null) {
			final int resId = (!enabled ? R.string.core_app_check_installed 
					: R.string.core_app_check_not_installed);
			
			mMemoryInstalledView.setEnabled(enabled);
			mMemoryInstalledView.setText(getString(resId));
		}
	}
	
	private void setStepTwoEnable(boolean enabled) {
		Logger.debug("enabled = %s", enabled);
		if (mStepTwoView != null) {
			mStepTwoView.setEnabled(enabled);
		}
		
		if (mPluginLayout != null) {
			mPluginLayout.setEnabled(enabled);
		}
		
		if (mPluginName != null) {
			mPluginName.setEnabled(enabled);
		}
		
		if (mPluginIcon != null) {
			final Drawable drawable = 
					(enabled ? getPluginIcon() : getPluginIconDisabled());
			mPluginIcon.setImageDrawable(drawable);
		}
	}
	
	protected String getPluginPackage() {
		return getApplication().getPackageName();
	}
	
	protected String getPluginCategory() {
		return getPluginPackage();
	}
	
	abstract protected Drawable getPluginIcon();
	abstract protected Drawable getPluginIconDisabled();
	abstract protected CharSequence getPluginName();
	
	private Observer mAppObserver = new Observer() {
		
		@Override
		public void update(Observable observable, Object data) {
			startChecking();
		}
		
	};

	
	private AlertDialogFragmentCallbacks mConfirmCallbacks = new AlertDialogFragmentCallbacks() {
		
		@Override
		public void onDialogConfirmed(AlertDialogFragment fragment, 
				DialogInterface dialog, int which) {
		}

		@Override
		public void onDialogCancelled(AlertDialogFragment fragment,
				DialogInterface dialog, int which) {
			if (mRemoveCheck != null) {
				mRemoveCheck.setChecked(false);
			}
		}
		
	};
	

}
