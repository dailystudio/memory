package com.dailystudio.memory.loader;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;

import com.dailystudio.dataobject.DatabaseObject;
import com.dailystudio.memory.dataobject.DateGroupObject;

abstract class AbsDateGroupLoader<D extends DatabaseObject, G extends DateGroupObject> 
	extends ProjectedPeroidDatabaseObjectsLoader<D, G> {

	private class DescDateGroupObjectComparator implements Comparator<DateGroupObject> {

		@Override
		public int compare(DateGroupObject dgObject1, DateGroupObject dgObject2) {
			if (dgObject1 == null) {
				return -1;
			} else if (dgObject2 == null) {
				return 1;
			}
			
			final long time1 = dgObject1.getTime();
			final long time2 = dgObject2.getTime();
			
			if (time1 < time2) {
				return 1;
			} else if (time1 > time2) {
				return -1;
			}
			
			return 0;
		}
		
	}

	private class AscDateGroupObjectComparator implements Comparator<DateGroupObject> {

		@Override
		public int compare(DateGroupObject dgObject1, DateGroupObject dgObject2) {
			if (dgObject1 == null) {
				return -1;
			} else if (dgObject2 == null) {
				return 1;
			}
			
			final long time1 = dgObject1.getTime();
			final long time2 = dgObject2.getTime();
			
			if (time1 > time2) {
				return 1;
			} else if (time1 < time2) {
				return -1;
			}
			
			return 0;
		}
		
	}

	public AbsDateGroupLoader(Context context) {
		super(context);
	}

	public AbsDateGroupLoader(Context context, 
			long start, long end) {
		super(context, start, end);
	}

	@Override
	public List<G> loadInBackground() {
		List<G> dates = super.loadInBackground();
		
		sortDates(dates, true);
		
		return dates;		
	}
	
	protected void sortDates(List<G> dates, boolean descending) {
		if (dates == null) {
			return;
		}

		Collections.sort(dates,
				(descending ? mDescComparator : mAscComparator));	
	}
	
	private Comparator<DateGroupObject> mAscComparator = new AscDateGroupObjectComparator();
	private Comparator<DateGroupObject> mDescComparator = new DescDateGroupObjectComparator();

}
