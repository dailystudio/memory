package com.dailystudio.memory.plugin.privacy;

public class PrivacyPolicy {

	public String collection;

	@Override
	public String toString() {
		return String.format("%s(0x%08x): collection = [%s]",
				getClass().getSimpleName(),
				hashCode(),
				collection);
	}
	
}
