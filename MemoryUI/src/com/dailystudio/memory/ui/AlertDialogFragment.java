package com.dailystudio.memory.ui;

import com.dailystudio.development.Logger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

public abstract class AlertDialogFragment extends DialogFragment {
	
	public static final int THEME_DEVICE_DEFAULT_DARK = 0x00000004;
	public static final int THEME_DEVICE_DEFAULT_LIGHT = 0x00000005;
	public static final int THEME_HOLO_DARK = 0x00000002;
	public static final int THEME_HOLO_LIGHT = 0x00000003;
	public static final int THEME_TRADITIONAL = 0x00000001;

	public static interface AlertDialogFragmentCallbacks {
		
		public void onDialogConfirmed(AlertDialogFragment fragment,
				DialogInterface dialog, int which);
		public void onDialogCancelled(AlertDialogFragment fragment,
				DialogInterface dialog, int which);

	}
	
	private class CompatAlertDialog extends AlertDialog {

		public CompatAlertDialog(Context context, int theme) {
			super(context, theme);
		}
		
		public CompatAlertDialog(Context context) {
			super(context);
		}
		
	}
	
	private AlertDialogFragmentCallbacks mCallbacks;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Activity activity = getActivity();
		
		AlertDialog dialog = null;
		
		if (Build.VERSION.SDK_INT >= 11) {
			dialog = new CompatAlertDialog(activity, getDefaultTheme());
		} else {
			dialog = new CompatAlertDialog(activity);
		}
		
		final CharSequence title = getTitle();
		final CharSequence message = getMessage();

		Logger.debug("title = %s, message = %s", 
				title, message);
		
		dialog.setTitle(title);
		dialog.setMessage(message);
		
		if (hasCustomView()) {
			final View customView = createCustomView();
			
			int cvPaddingLeft = 0;
			int cvPaddingRight = 0;
			int cvPaddingTop = 0;
			int cvPaddingBottom = 0;
			
			final Resources res = getResources();
			if (res != null) {
				cvPaddingLeft = res.getDimensionPixelSize(
						R.dimen.alert_dialog_custom_view_padding_left);
				cvPaddingRight = res.getDimensionPixelSize(
						R.dimen.alert_dialog_custom_view_padding_right);
				cvPaddingTop = res.getDimensionPixelSize(
						R.dimen.alert_dialog_custom_view_padding_top);
				cvPaddingBottom = res.getDimensionPixelSize(
						R.dimen.alert_dialog_custom_view_padding_bottom);
			}

			if (customView != null) {
				dialog.setView(customView, 
						cvPaddingLeft, cvPaddingRight,
						cvPaddingTop, cvPaddingBottom);
			}
		}
		
		if (hasConfirmButton()) {
			dialog.setButton(AlertDialog.BUTTON_POSITIVE, 
					getConfirmText(),
					new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							onDialogConfirmed(dialog, which);
							if (mCallbacks != null) {
								mCallbacks.onDialogConfirmed(
										AlertDialogFragment.this,
										dialog, 
										which);
							}
						}
						
					});
		}
		
		if (hasCancelButton()) {
			dialog.setButton(AlertDialog.BUTTON_NEGATIVE, 
					getCancelText(),
					new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							onDialogCancelled(dialog, which);
							if (mCallbacks != null) {
								mCallbacks.onDialogCancelled(
										AlertDialogFragment.this,
										dialog, 
										which);
							}
						}
						
					});
		}
		
		return dialog;
	}
	
	protected int getDefaultTheme() {
		return THEME_HOLO_LIGHT;
	}

	protected boolean hasConfirmButton() {
		return true;
	}
	
	protected boolean hasCancelButton() {
		return true;
	}
	
	protected boolean hasCustomView() {
		return false;
	}
	
	public void setCallbacks(AlertDialogFragmentCallbacks callbacks) {
		mCallbacks = callbacks;
	}
	
	protected View createCustomView() {
		return null;
	}

	abstract protected CharSequence getConfirmText();
	abstract protected CharSequence getCancelText();
	abstract protected CharSequence getTitle();
	abstract protected CharSequence getMessage();
	
	abstract protected void onDialogConfirmed(DialogInterface dialog, int which);
	abstract protected void onDialogCancelled(DialogInterface dialog, int which);
	
}