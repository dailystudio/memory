package com.dailystudio.memory.querypiece;

public class MemoryPieceQueryId {

	private static volatile long sSeedBase = 0;
	
	public static synchronized long generateQueryId() {
		return sSeedBase++;
	}
	
}
