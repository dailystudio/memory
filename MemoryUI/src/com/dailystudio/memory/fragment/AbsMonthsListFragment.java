package com.dailystudio.memory.fragment;

import android.widget.BaseAdapter;

import com.dailystudio.dataobject.DatabaseObject;
import com.dailystudio.memory.dataobject.MonthObject;
import com.dailystudio.memory.ui.MonthsAdapter;

public abstract class AbsMonthsListFragment<D extends DatabaseObject>
	extends AbsDateGroupListFragment<D, MonthObject> {

/*	@Override
	public Loader<List<MonthObject>> onCreateLoader(int arg0, Bundle arg1) {
		return new MonthsLoader(getActivity(), 
				getPeroidStart(),
				getPeroidEnd(),
				getDataObjectClass());
	}
*/	
	@Override
	protected BaseAdapter onCreateAdapter() {
		return new MonthsAdapter(getActivity());
	}

}
