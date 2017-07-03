package util;

import java.util.BitSet;
import java.util.Iterator;

class PowerSetPositive implements Iterator<BitSet> {

	private Valuation mValuation;
	
	private final BitSet mSet;
	private final int[] mIntArr;
	
	public PowerSetPositive(BitSet set) {
		assert ! set.isEmpty();
		this.mSet = set;
		mIntArr = new int[mSet.cardinality()];
		int index = 0;
		for(int n = mSet.nextSetBit(0); n >= 0; n = mSet.nextSetBit(n + 1)) {
			mIntArr[index ++] = n;
		}
		this.mValuation = new Valuation(mSet.cardinality());
	}

	@Override
	public boolean hasNext() {
		int index = mValuation.nextSetBit(0); // whether we have got out of the array
		return index < mValuation.size();
	}

	@Override
	public BitSet next() {
		assert hasNext();
		Valuation val = mValuation.clone();
		mValuation.increment();
		BitSet bits = new BitSet();
		for(int n = val.nextSetBit(0); n >= 0 ; n = val.nextSetBit(n + 1)) {
			bits.set(mIntArr[n]);
		}
		return bits;
	}

}
