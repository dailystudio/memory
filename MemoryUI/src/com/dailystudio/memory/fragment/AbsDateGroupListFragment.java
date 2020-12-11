package com.dailystudio.memory.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dailystudio.dataobject.DatabaseObject;
import com.dailystudio.memory.dataobject.DateGroupObject;
import com.dailystudio.memory.ui.R;

public abstract class AbsDateGroupListFragment<D extends DatabaseObject, G extends DateGroupObject> 
	extends MemoryPeroidObjectsListFragment<G> {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list, null);
		
		return view;
	}

	@Override
	protected Bundle createLoaderArguments() {
		return null;
	}
	
}
