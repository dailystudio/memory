package com.dailystudio.memory.ui;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.dailystudio.app.ui.InLayoutWindow;

public class PaperClipLayout extends InLayoutWindow {

	private Animation mInAnim;
	private Animation mOutAnim;

    public PaperClipLayout(Context context) {
        this(context, null);
    }

    public PaperClipLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaperClipLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initMembers();
    }

	private void initMembers() {
		mInAnim = AnimationUtils.loadAnimation(mContext,
				R.anim.paper_clip_in);
		mOutAnim = AnimationUtils.loadAnimation(mContext,
				R.anim.paper_clip_out);
	}

	@Override
	protected Animation getWindowOpenAnimation() {
		return mInAnim;
	}

	@Override
	protected Animation getWindowCloseAnimation() {
		return mOutAnim;
	}

	@Override
	protected void onCreateWindow() {
		LayoutInflater.from(getContext()).inflate(
				R.layout.paper_clip_window, this);

		final Resources res = getResources();
		if (res != null) {
			final int padding = res.getDimensionPixelSize(
					R.dimen.paper_clip_frame_padding);

			setPadding(padding, padding, padding, padding);
		}
	}

	@Override
	protected ViewGroup findContent() {
		return (ViewGroup) findViewById(R.id.paper_clip_content);
	}

	@Override
	protected View findCloseButton() {
		return findViewById(R.id.paper_clip_close_button);
	}

	@Override
	protected TextView findTitle() {
		return (TextView) findViewById(R.id.paper_clip_title);
	}

	@Override
	protected ImageView findTitleIcon() {
		return (ImageView) findViewById(R.id.paper_clip_icon);
	}

}
