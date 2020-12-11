package com.dailystudio.memory.person;

import android.os.Parcel;
import android.os.Parcelable;

public class PersonFeatureBundle implements Parcelable {
	
	private String mFeatureId;
	private String mFeatureValue;

	public PersonFeatureBundle() {
	}
	
	public PersonFeatureBundle(String fid, String val) {
		mFeatureId = fid;
		mFeatureValue = val;
	}
	
	public PersonFeatureBundle(Parcel in) {
		mFeatureId = in.readString();
		mFeatureValue = in.readString();
	}

	public void setFeatureId(String fid) {
		mFeatureId = fid;
	}
	
	public String getFeatureId() {
		return mFeatureId;
	}

	public void setFeatureValue(String val) {
		mFeatureValue = val;
	}
	
	public String getFeatureValue() {
		return mFeatureValue;
	}

	@Override
	public String toString() {
		return String.format("%s(0x%08x): fid: %s, val: %s",
				getClass().getSimpleName(),
				hashCode(),
				getFeatureId(),
				getFeatureValue());
	}
	
	@Override
	public int describeContents() {
	    return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mFeatureId == null ? "": mFeatureId);
		dest.writeString(mFeatureValue == null ? "": mFeatureValue);
	}
	
	public static final Parcelable.Creator<PersonFeatureBundle> CREATOR = 
			new Creator<PersonFeatureBundle>() {  
		
		@Override  
		public PersonFeatureBundle[] newArray(int size) {  
			return new PersonFeatureBundle[size];  
		}  
	          
		@Override  
		public PersonFeatureBundle createFromParcel(Parcel source) {  
			return new PersonFeatureBundle(source);  
		}  
	
	};  
	
}

