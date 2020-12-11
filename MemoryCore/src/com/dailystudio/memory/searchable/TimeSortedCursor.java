package com.dailystudio.memory.searchable;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.database.Cursor;
import android.database.CursorWrapper;

public class TimeSortedCursor extends CursorWrapper {
	
    public class SortEntry { 
    	
    	public String timeKey; 
        public int order; 
        
    } 
     
	private static Collator sCollator = Collator.getInstance(); 
  
    public static Comparator<SortEntry> sTimeStringComparator = new Comparator<SortEntry>(){        
    	
        @Override 
        public int compare(SortEntry entry1, SortEntry entry2) {             
            return -sCollator.compare(entry1.timeKey, entry2.timeKey);        
        }     
        
    }; 
    
	List<SortEntry> sortList = new ArrayList<SortEntry>(); 
    Cursor mCursor; 
    int mPos = 0; 
     
    public TimeSortedCursor(Cursor cursor,String columnName) {         
        super(cursor); 
        
        mCursor = cursor; 
        if(mCursor != null && mCursor.getCount() > 0) { 
            int i = 0; 
            int column = cursor.getColumnIndexOrThrow(columnName); 
            
            for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext(),i++) { 
                SortEntry sortKey = new SortEntry(); 
                sortKey.timeKey = cursor.getString(column); 
                sortKey.order = i; 
                
                sortList.add(sortKey); 
            } 
        } 

        Collections.sort(sortList, sTimeStringComparator); 
    } 

    public boolean moveToPosition(int position) { 
        if (position >= 0 && position < sortList.size()){ 
            mPos = position; 
            
            int order = sortList.get(position).order; 
            
            return mCursor.moveToPosition(order); 
        } 
        
        if(position < 0){ 
            mPos = -1; 
        } 
        
        if(position >= sortList.size()){ 
            mPos = sortList.size(); 
        } 
        
        return mCursor.moveToPosition(position);         
    } 
     
    public boolean moveToFirst() {         
        return moveToPosition(0); 
    } 
     
    public boolean moveToLast(){ 
        return moveToPosition(getCount() - 1); 
    } 
     
    public boolean moveToNext() {                 
        return moveToPosition(mPos+1); 
    } 
     
    public boolean moveToPrevious() {         
        return moveToPosition(mPos-1); 
    } 
     
    public boolean move(int offset) {         
        return moveToPosition(mPos + offset); 
    } 
     
    public int getPosition() {         
        return mPos; 
    } 
    
}
