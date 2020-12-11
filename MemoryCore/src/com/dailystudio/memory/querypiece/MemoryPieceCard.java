package com.dailystudio.memory.querypiece;

import com.dailystudio.datetime.CalendarUtils;

import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;

public class MemoryPieceCard implements Parcelable {
	
	private String mCardTitle;
	private String mCardUri;
	private String mPluginComponent;
	private long mTimestamp;

	public MemoryPieceCard() {
		mTimestamp = System.currentTimeMillis();
	}
	
	public MemoryPieceCard(String title, String uri) {
		mCardTitle = title;
		mCardUri = uri;
		mTimestamp = System.currentTimeMillis();
	}
	
	public MemoryPieceCard(Parcel in) {
		mCardTitle = in.readString();
		mCardUri = in.readString();
		mPluginComponent = in.readString();
		mTimestamp = in.readLong();
	}

	public void setCardTitle(String title) {
		mCardTitle = title;
	}
	
	public String getCardTitle() {
		return mCardTitle;
	}

	public void setCardUri(String uri) {
		mCardUri = uri;
	}
	
	public String getCardUri() {
		return mCardUri;
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
		return String.format("%s(0x%08x): time[%s]: card = %s[%s], (from: %s)",
				getClass().getSimpleName(),
				hashCode(),
				CalendarUtils.timeToReadableString(mTimestamp),
				mCardTitle,
				mCardUri,
				mPluginComponent);
	}
	
	@Override
	public int describeContents() {
	    return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mCardTitle == null ? "": mCardTitle);
		dest.writeString(mCardUri == null ? "": mCardUri);
		dest.writeString(mPluginComponent == null ? "": mPluginComponent);
		dest.writeLong(mTimestamp);
	}
	
	public static final Parcelable.Creator<MemoryPieceCard> CREATOR = 
			new Creator<MemoryPieceCard>() {  
		
		@Override  
		public MemoryPieceCard[] newArray(int size) {  
			return new MemoryPieceCard[size];  
		}  
	          
		@Override  
		public MemoryPieceCard createFromParcel(Parcel source) {  
			return new MemoryPieceCard(source);  
		}  
	
	};  
	
}

