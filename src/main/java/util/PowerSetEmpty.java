package util;

import java.util.Iterator;

class PowerSetEmpty implements Iterator<IntSet> {
	
	private boolean mHasNext;
	
	public PowerSetEmpty() {
		mHasNext = true;
	}

	@Override
	public boolean hasNext() {
		return mHasNext;
	}

	@Override
	public IntSet next() {
		assert hasNext();
		mHasNext = false;
		return UtilIntSet.newIntSet();
	}

}
