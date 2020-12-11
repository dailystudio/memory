package com.dailystudio.memory.querypiece;

import com.dailystudio.datetime.CalendarUtils;
import com.google.gson.Gson;

import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class MemoryPieceDigest implements Parcelable {
	
	private String mContent;
	private String mPluginComponent;
	private long mTimestamp;
	private String mExtraDataJson;

	public MemoryPieceDigest() {
		mTimestamp = System.currentTimeMillis();
	}
	
	public MemoryPieceDigest(String content) {
		mContent = content;
		mTimestamp = System.currentTimeMillis();
	}
	
	public MemoryPieceDigest(Parcel in) {
		mContent = in.readString();
		mPluginComponent = in.readString();
		mTimestamp = in.readLong();
		mExtraDataJson = in.readString();
	}

	public void setContent(String content) {
		mContent = content;
	}
	
	public String getContent() {
		return mContent;
	}

	public void setExtraData(Object data) {
		if (data == null) {
			return;
		}
		
		Gson gson = new Gson();
		
		mExtraDataJson = gson.toJson(data);
	}
	
	public Object getExtraData(Class<?> classOfExtra) {
		if (TextUtils.isEmpty(mExtraDataJson)) {
			return null;
		}
		
		Gson gson = new Gson();
		
		return gson.fromJson(mExtraDataJson, classOfExtra);
	}
	
	public ComponentName getPluginComponent() {
		return ComponentName.unflattenFromString(mPluginComponent);
	}
	
	public void setPluginComponent(ComponentName comp) {
		if (comp == null) {
			return;
		}
		
		mPluginComponent = comp.flattenToString();
	}
	
	public long getTimestamp() {
		return mTimestamp;
	}
	
	public void setTimestamp(long time) {
		mTimestamp = time;
	}
	
	@Override
	public String toString() {
		return String.format("%s(0x%08x): time[%s]: %s, (from: %s), extra[%s]",
				getClass().getSimpleName(),
				hashCode(),
				CalendarUtils.timeToReadableString(mTimestamp),
				mContent,
				mPluginComponent,
				mExtraDataJson);
	}
	
	@Override
	public int describeContents() {
	    return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mContent == null ? "": mContent);
		dest.writeString(mPluginComponent == null ? "": mPluginComponent);
		dest.writeLong(mTimestamp);
		dest.writeString(mExtraDataJson == null ? "": mExtraDataJson);
	}
	
	public static final Parcelable.Creator<MemoryPieceDigest> CREATOR = 
			new Creator<MemoryPieceDigest>() {  
		
		@Override  
		public MemoryPieceDigest[] newArray(int size) {  
			return new MemoryPieceDigest[size];  
		}  
	          
		@Override  
		public MemoryPieceDigest createFromParcel(Parcel source) {  
			return new MemoryPieceDigest(source);  
		}  
	
	};  
	
}

