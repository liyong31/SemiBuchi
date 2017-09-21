package util;

import java.util.Iterator;

import com.zaxxer.sparsebits.SparseBitSet;

public class IntSetSparseBits implements IntSet {
	
	private SparseBitSet mSet;
	
	public IntSetSparseBits() {
		mSet = new SparseBitSet();
	}

	@Override
	public IntIterator iterator() {
		return new SparseBitsIterator(this);
	}

	@Override
	public IntSet clone() {
		IntSetSparseBits bits = new IntSetSparseBits();
		bits.mSet = mSet.clone();
		return bits;
	}

	@Override
	public void andNot(IntSet set) {
		if(! (set instanceof IntSetSparseBits)) {
			System.err.println("OPERAND should be SparseBitSet");
			System.exit(-1);
		}
		SparseBitSet bits = (SparseBitSet) set.get();
		this.mSet.andNot(bits);
	}

	@Override
	public void and(IntSet set) {
		if(! (set instanceof IntSetSparseBits)) {
			System.err.println("OPERAND should be SparseBitSet");
			System.exit(-1);
		}
		SparseBitSet bits = (SparseBitSet) set.get();
		this.mSet.and(bits);
	}

	@Override
	public void or(IntSet set) {
		if(! (set instanceof IntSetSparseBits)) {
			System.err.println("OPERAND should be SparseBitSet");
			System.exit(-1);
		}
		SparseBitSet bits = (SparseBitSet) set.get();
		this.mSet.or(bits);		
	}

	@Override
	public boolean get(int value) {
		return mSet.get(value);
	}
	
	@Override
	public void set(int value) {
		mSet.set(value);
	}

	@Override
	public void clear(int value) {
		mSet.clear(value);
	}
	
	@Override
	public void clear() {
		mSet.clear();
	}

	@Override
	public boolean isEmpty() {
		return mSet.isEmpty();
	}

	@Override
	public int cardinality() {
		return mSet.cardinality();
	}

	@Override
	public boolean overlap(IntSet set) {
		if(! (set instanceof IntSetSparseBits)) {
			System.err.println("OPERAND should be BitSet");
			System.exit(-1);
		}
		IntSetSparseBits temp = (IntSetSparseBits)set;
		return temp.mSet.intersects(this.mSet);
	}
	
	@Override
	public boolean subsetOf(IntSet set) {
		if(! (set instanceof IntSetSparseBits)) {
			System.err.println("OPERAND should be SparseBitSet");
			System.exit(-1);
		}
		SparseBitSet temp = this.mSet.clone();
		SparseBitSet bits = (SparseBitSet) set.get();
		temp.andNot(bits);
		return temp.isEmpty();
	}

	@Override
	public boolean contentEq(IntSet set) {
		if(! (set instanceof IntSetSparseBits)) {
			System.err.println("OPERAND should be SparseBitSet");
			System.exit(-1);
		}
		SparseBitSet bits = (SparseBitSet) set.get();
		return this.mSet.equals(bits);
	}

	@Override
	public String toString() {
		return mSet.toString();
	}
	
	@Override
	public Object get() {
		return mSet;
	}
	
	public boolean equals(Object obj) {
		if(! (obj instanceof IntSetSparseBits)) {
			System.err.println("OPERAND should be SparseBitSet");
			System.exit(-1);
		}
		IntSetSparseBits bits = (IntSetSparseBits)obj;
		return this.contentEq(bits);
	}
	
	public static class SparseBitsIterator implements IntIterator {

		private SparseBitSet mBits;
		private int mIndex;
		
		public SparseBitsIterator(IntSetSparseBits set) {
			this.mBits = set.mSet;
			mIndex = mBits.nextSetBit(0);
		}
		
		public boolean hasNext() {
			return (mIndex >= 0);
		}
		
		public int next() {
			int rv = mIndex;
			mIndex = mBits.nextSetBit(mIndex + 1);
			return rv;
		}
		
	}
	
	@Override
	public Iterable<Integer> iterable() {
		return () -> new Iterator<Integer>() {
			IntIterator iter = iterator();
			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public Integer next() {
				return iter.next();
			}
			
		};
	}

}
