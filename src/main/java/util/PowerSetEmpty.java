package util;

import java.util.BitSet;
import java.util.Iterator;

class PowerSetEmpty implements Iterator<BitSet> {
	
	private boolean hasNext;
	
	public PowerSetEmpty() {
		hasNext = true;
	}

	@Override
	public boolean hasNext() {
		return hasNext;
	}

	@Override
	public BitSet next() {
		assert hasNext();
		hasNext = false;
		return new BitSet();
	}

}
