package util;

import java.util.Iterator;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public class IntSetTIntSet implements IntSet {
	
	private final TIntSet mSet;
	
	public IntSetTIntSet() {
		mSet = new TIntHashSet();
	}
	
	public IntSetTIntSet(TIntSet set) {
		mSet = set;
	}

	@Override
	public IntIterator iterator() {
		return new TIntSetIterator(this);
	}

	@Override
	public IntSet clone() {
		IntSetTIntSet copy = new IntSetTIntSet();
		copy.mSet.addAll(mSet);
		return copy;
	}

	@Override
	public void andNot(IntSet set) {
		if(! (set instanceof IntSetTIntSet)) {
			System.err.println("OPERAND should be TIntSet");
			System.exit(-1);
		}
		IntSetTIntSet temp = (IntSetTIntSet)set;
		this.mSet.removeAll(temp.mSet);
	}

	@Override
	public void and(IntSet set) {
		if(! (set instanceof IntSetTIntSet)) {
			System.err.println("OPERAND should be TIntSet");
			System.exit(-1);
		}
		IntSetTIntSet temp = (IntSetTIntSet)set;
		this.mSet.retainAll(temp.mSet);
	}

	@Override
	public void or(IntSet set) {
		if(! (set instanceof IntSetTIntSet)) {
			System.err.println("OPERAND should be TIntSet");
			System.exit(-1);
		}
		IntSetTIntSet temp = (IntSetTIntSet)set;
		this.mSet.addAll(temp.mSet);
	}

	@Override
	public boolean get(int value) {
		return mSet.contains(value);
	}
	
	@Override
	public void set(int value) {
		mSet.add(value);
	}

	@Override
	public void clear(int value) {
		mSet.remove(value);
	}
	
	@Override
	public void clear() {
		mSet.clear();
	}
	
	@Override
	public String toString() {
		return mSet.toString();
	}

	@Override
	public boolean isEmpty() {
		return mSet.isEmpty();
	}

	@Override
	public int cardinality() {
		return mSet.size();
	}

	@Override
	public boolean subsetOf(IntSet set) {
		if(! (set instanceof IntSetTIntSet)) {
			System.err.println("OPERAND should be TIntSet");
			System.exit(-1);
		}
		IntSetTIntSet temp = (IntSetTIntSet)set;
		return temp.mSet.containsAll(this.mSet);
	}

	@Override
	public boolean contentEq(IntSet set) {
		if(! (set instanceof IntSetTIntSet)) {
			System.err.println("OPERAND should be TIntSet");
			System.exit(-1);
		}
		IntSetTIntSet temp = (IntSetTIntSet)set;
		return this.mSet.equals(temp.mSet);
	}

	@Override
	public Object get() {
		return mSet;
	}
	
	public boolean equals(Object obj) {
		if(! (obj instanceof IntSetTIntSet)) {
			System.err.println("OPERAND should be TIntSet");
			System.exit(-1);
		}
		IntSetTIntSet temp = (IntSetTIntSet)obj;
		return this.contentEq(temp);
	}
	
	public static class TIntSetIterator implements IntIterator {

		private TIntIterator mSetIter;
		
		public TIntSetIterator(IntSetTIntSet set) {
			mSetIter = set.mSet.iterator();
		}
		
		public boolean hasNext() {
			return mSetIter.hasNext();
		}
		
		public int next() {
			return mSetIter.next();
		}
		
	}
	
	@Override
	public Iterable<Integer> iterable() {
		return () -> new Iterator<Integer>() {
			TIntIterator iter = mSet.iterator();
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
