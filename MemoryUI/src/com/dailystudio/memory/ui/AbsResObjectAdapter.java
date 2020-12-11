package com.dailystudio.memory.ui;

import java.util.ArrayList;

import com.dailystudio.nativelib.application.IResourceObject;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class AbsResObjectAdapter<T extends IResourceObject> extends ArrayAdapter<T> {

	public AbsResObjectAdapter(Context context) {
		super(context, 0, new ArrayList<T>());
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final View view = createViewIfRequired(position, convertView, parent);
		
		T object = getItem(position);
		
		bindViewWithResource(view, getContext(), object);
		
		return view;
	}
	
	protected void bindViewWithResource(View view, Context context, T object) {
		if (view == null || context == null || object == null) {
			return;
		}

		Resources res = context.getResources();
			
		TextView labelView = (TextView) view.findViewById(R.id.res_label);
		if (labelView != null) {
			CharSequence label = object.getLabel();
			if (label == null) {
				if (res != null) {
                    final int defaultRes = getDefaultLabelRes();
                    if (defaultRes > 0) {
                        label = res.getString(defaultRes);
                    }
				}
			}
			
			labelView.setText(label);
		}
		
		ImageView iconView = (ImageView) view.findViewById(R.id.res_icon);
		if (iconView != null) {
			Drawable icon = object.getIcon();
			if (icon == null) {
				if (res != null) {
                    final int defaultRes = getDefaultIconRes();
                    if (defaultRes > 0) {
                        icon = res.getDrawable(defaultRes);
                    }
				}
			}
			
			iconView.setImageDrawable(icon);
		}
		
	}

    protected int getDefaultIconRes() {
        return R.drawable.ic_launcher;
    }

    protected int getDefaultLabelRes() {
        return R.string.default_app_label;
    }

	abstract protected View createViewIfRequired(
			int position, View convertView, ViewGroup parent);

}
