package com.dailystudio.memory.ask;

import com.dailystudio.app.fragment.AbsLoaderFragment;
import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;
import com.dailystudio.memory.loader.CoreLoaderIds;
import com.dailystudio.memory.ui.R;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MemoryQuestionFragment extends AbsLoaderFragment<Cursor> {

	private int mQid;
	private String mSourcePackage;
	
	private TextView mQuestionContent;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_memory_question, null);
		
		setupViews(view);
		
		return view;
	}
	
	private void setupViews(View fragmentView) {
		if (fragmentView == null) {
			return;
		}
		
		mQuestionContent = (TextView) fragmentView.findViewById(
				R.id.question_content);
	}

	@Override
	public void bindIntent(Intent intent) {
		super.bindIntent(intent);
		
		if (intent == null) {
			return;
		}
		
		mQid = intent.getIntExtra(Constants.EXTRA_QUESTION_ID, 
				Constants.INVALID_ID);
		mSourcePackage = intent.getStringExtra(Constants.EXTRA_SOURCE_PACKAGE);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
		return new MemoryQuestionLoader(getActivity(), mQid, mSourcePackage);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader,
			Cursor data) {
		if (data == null) {
			return;
		}
		
		if (data.moveToFirst() == false) {
			return;
		}
		
		MemoryQuestion question = new MemoryQuestion(getActivity());
		question.fillValuesFromCursor(data);
		
		Logger.debug("question = %s", question);
		
		updateContent(question);
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}

	@Override
	protected int getLoaderId() {
		return CoreLoaderIds.LOADER_ID_QUESTION;
	}

	@Override
	protected Bundle createLoaderArguments() {
		return null;
	}

	private void updateContent(MemoryQuestion question) {
		if (question == null) {
			return;
		}
		
		if (mQuestionContent != null) {
			final String content = question.dumpQuestionText();

			mQuestionContent.setText(content);
		}
	}

}
