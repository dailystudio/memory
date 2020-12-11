package com.dailystudio.memory.person;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;

public class PersonInformation {

	private class PersonFeatureReceiver extends BroadcastReceiver {
		
		private PersonFeatureBundle mFeatureBundle;
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null) {
				return;
			}
			
			final String action = intent.getAction();
			if (Constants.ACTION_MEMORY_PERSON_GET_FEATURE.equals(action)) {
				Bundle resultExtras = getResultExtras(true);
				
				if (resultExtras != null) {
					final String pid = resultExtras.getString(Constants.EXTRA_PERSON_ID);
					final String fid = resultExtras.getString(Constants.EXTRA_FEATURE_ID);
					final String fval = resultExtras.getString(Constants.EXTRA_FEATURE_VAL);
					Logger.debug("pid = %s, fid = %s, fval = %s",
							pid, fid, fval);
					
					if (!TextUtils.isEmpty(fid)) {
						mFeatureBundle = new PersonFeatureBundle(fid, fval);
					}
				} else {
					Logger.warnning("empty result for action: %s",
							action);
					
					mFeatureBundle = null;
				}
			}
			
			releaseLock();
		}
		
		private PersonFeatureBundle getFeatureBundle() {
			return mFeatureBundle;
		}
		
		
		private void releaseLock() {
			synchronized (this) {
				notifyAll();
			}
		}

	};
	
	private String mPersonId;
	private Context mContext;
	
	public PersonInformation(Context context, String personId) {
		mContext = context;
		
		mPersonId = personId;
	}
	
	public void setFeature(String feature, String value) {
		Logger.debug("feature = %s, value = %s", feature, value);
		if (!isValidFeature(feature)) {
			return;
		}
		
		Intent i = new Intent(Constants.ACTION_MEMORY_PERSON_SET_FEATURE);
		
		i.putExtra(Constants.EXTRA_PERSON_ID, mPersonId);
		i.putExtra(Constants.EXTRA_FEATURE_ID, feature);
		i.putExtra(Constants.EXTRA_FEATURE_VAL, value);

		mContext.startService(i);
	}
	
	public void setFeatures(Map<String, String> features) {
		final PersonFeatureBundle[] array = toFeaturesArray(features);
		if (array == null || array.length <= 0) {
			return;
		}
		
		Intent i = new Intent(Constants.ACTION_MEMORY_PERSON_SET_FEATURES);

		i.setClassName(mContext.getApplicationContext(),
				"com.dailystudio.memory.person.PersonFeatureUpdateService");
		
		i.putExtra(Constants.EXTRA_PERSON_ID, mPersonId);
		i.putExtra(Constants.EXTRA_FEATURES, array);

		mContext.startService(i);
	}
	
	private PersonFeatureBundle[] toFeaturesArray(Map<String, String> features) {
		if (features == null || features.size() <= 0) {
			return null;
		}
		
		final Set<String> keys = features.keySet();
		if (keys ==  null || keys.size() <= 0) {
			return null;
		}
		
		List<PersonFeatureBundle> list = new ArrayList<PersonFeatureBundle>();
		for (String key: keys) {
			list.add(new PersonFeatureBundle(key, features.get(key)));
		}
		
		return list.toArray(new PersonFeatureBundle[0]);
	}
	
	public String getFeature(String feature) {
		final PersonFeatureBundle bundle = getFeatureInBundle(feature);
		if (bundle == null) {
			return null;
		}
		
		return bundle.getFeatureValue();
	}
	
	public PersonFeatureBundle getFeatureInBundle(String feature) {
		if (!isValidFeature(feature)) {
			return null;
		}
		
		Intent i = new Intent(Constants.ACTION_MEMORY_PERSON_GET_FEATURE);
		
		i.putExtra(Constants.EXTRA_PERSON_ID, mPersonId);
		i.putExtra(Constants.EXTRA_FEATURE_ID, feature);
		
		PersonFeatureReceiver resultReceiver = 
			new PersonFeatureReceiver();
		
		synchronized (resultReceiver) {
			mContext.sendOrderedBroadcast(i, null, resultReceiver, 
					null, Activity.RESULT_OK, null, null);
			
			try {
				Logger.debug("WAIT() for result");
				resultReceiver.wait();
			} catch (InterruptedException e) {
				Logger.warnning("WAIT() release: %s",
						e.toString());
			}
		}
		
		final PersonFeatureBundle bundle = 
				resultReceiver.getFeatureBundle();
		
		Logger.debug("feature bundle = %s", bundle);
		
		return bundle;
	}
	
	private boolean isValidFeature(String feature) {
		if (TextUtils.isEmpty(feature)
				|| TextUtils.isEmpty(mPersonId)) {
			Logger.debug("invalid person feature: pid(%s), feature(%s)",
					mPersonId, feature);
			return false;
		}

		return true;
	}

}
