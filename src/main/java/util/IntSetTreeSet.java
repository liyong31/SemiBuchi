package util;

import java.util.Iterator;
import java.util.TreeSet;


public class IntSetTreeSet implements IntSet {
	
	private final TreeSet<Integer> mSet;
	
	public IntSetTreeSet() {
		mSet = new TreeSet<>();
	}

	@Override
	public IntIterator iterator() {
		return new TreeSetIterator(this);
	}

	@Override
	public IntSet clone() {
		IntSetTreeSet copy = new IntSetTreeSet();
		copy.mSet.addAll(mSet);
		return copy;
	}

	@Override
	public void andNot(IntSet set) {
		if(! (set instanceof IntSetTreeSet)) {
			System.err.println("OPERAND should be TreeSet");
			System.exit(-1);
		}
		IntSetTreeSet temp = (IntSetTreeSet)set;
		this.mSet.removeAll(temp.mSet);
	}

	@Override
	public void and(IntSet set) {
		if(! (set instanceof IntSetTreeSet)) {
			System.err.println("OPERAND should be TreeSet");
			System.exit(-1);
		}
		IntSetTreeSet temp = (IntSetTreeSet)set;
		this.mSet.retainAll(temp.mSet);
	}

	@Override
	public void or(IntSet set) {
		if(! (set instanceof IntSetTreeSet)) {
			System.err.println("OPERAND should be TreeSet");
			System.exit(-1);
		}
		IntSetTreeSet temp = (IntSetTreeSet)set;
		this.mSet.addAll(temp.mSet);
	}

	@Override
	public boolean get(int value) {
		return mSet.contains(value);
	}

	@Override
	public void clear(int value) {
		mSet.remove(value);
	}
	
	@Override
	public String toString() {
		return mSet.toString();
	}
	
	@Override
	public void clear() {
		mSet.clear();
	}
	
	@Override
	public void set(int value) {
		mSet.add(value);
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
		if(! (set instanceof IntSetTreeSet)) {
			System.err.println("OPERAND should be TreeSet");
			System.exit(-1);
		}
		IntSetTreeSet temp = (IntSetTreeSet)set;
		return temp.mSet.containsAll(this.mSet);
	}

	@Override
	public boolean contentEq(IntSet set) {
		if(! (set instanceof IntSetTreeSet)) {
			System.err.println("OPERAND should be TreeSet");
			System.exit(-1);
		}
		IntSetTreeSet temp = (IntSetTreeSet)set;
		return this.mSet.equals(temp.mSet);
	}

	@Override
	public Object get() {
		return mSet;
	}
	
	public boolean equals(Object obj) {
		if(! (obj instanceof IntSetTreeSet)) {
			System.err.println("OPERAND should be TreeSet");
			System.exit(-1);
		}
		IntSetTreeSet temp = (IntSetTreeSet)obj;
		return this.contentEq(temp);
	}
	
	public static class TreeSetIterator implements IntIterator {

		private Iterator<Integer> mSetIter;
		
		public TreeSetIterator(IntSetTreeSet set) {
			this.mSetIter = set.mSet.iterator();
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
		return mSet;
	}
	

}
