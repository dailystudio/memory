package com.dailystudio.memory.fragment;

import android.widget.BaseAdapter;

import com.dailystudio.dataobject.DatabaseObject;
import com.dailystudio.memory.dataobject.WeekObject;
import com.dailystudio.memory.ui.WeeksAdatper;

public abstract class AbsWeeksListFragment<D extends DatabaseObject>
	extends AbsDateGroupListFragment<D, WeekObject> {

/*	@Override
	public Loader<List<WeekObject>> onCreateLoader(int arg0, Bundle arg1) {
		return new WeeksLoader(getActivity(), getDataObjectClass());
	}
*/	
	@Override
	protected BaseAdapter onCreateAdapter() {
		return new WeeksAdatper(getActivity());
	}

}
