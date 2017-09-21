package util;

import java.util.HashSet;
import java.util.Iterator;


public class IntSetHashSet implements IntSet {
	
	private final HashSet<Integer> mSet;
	
	public IntSetHashSet() {
		mSet = new HashSet<>();
	}

	@Override
	public IntIterator iterator() {
		return new HashSetIterator(this);
	}

	@Override
	public IntSet clone() {
		IntSetHashSet copy = new IntSetHashSet();
		copy.mSet.addAll(mSet);
		return copy;
	}

	@Override
	public void andNot(IntSet set) {
		if(! (set instanceof IntSetHashSet)) {
			System.err.println("OPERAND should be HashSet");
			System.exit(-1);
		}
		IntSetHashSet temp = (IntSetHashSet)set;
		this.mSet.removeAll(temp.mSet);
	}

	@Override
	public void and(IntSet set) {
		if(! (set instanceof IntSetHashSet)) {
			System.err.println("OPERAND should be HashSet");
			System.exit(-1);
		}
		IntSetHashSet temp = (IntSetHashSet)set;
		this.mSet.retainAll(temp.mSet);
	}

	@Override
	public void or(IntSet set) {
		if(! (set instanceof IntSetHashSet)) {
			System.err.println("OPERAND should be HashSet");
			System.exit(-1);
		}
		IntSetHashSet temp = (IntSetHashSet)set;
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
		if(! (set instanceof IntSetHashSet)) {
			System.err.println("OPERAND should be HashSet");
			System.exit(-1);
		}
		IntSetHashSet temp = (IntSetHashSet)set;
		return temp.mSet.containsAll(this.mSet);
	}

	@Override
	public boolean contentEq(IntSet set) {
		if(! (set instanceof IntSetHashSet)) {
			System.err.println("OPERAND should be HashSet");
			System.exit(-1);
		}
		IntSetHashSet temp = (IntSetHashSet)set;
		return this.mSet.equals(temp.mSet);
	}

	@Override
	public Object get() {
		return mSet;
	}
	
	public boolean equals(Object obj) {
		if(! (obj instanceof IntSetHashSet)) {
			System.err.println("OPERAND should be HashSet");
			System.exit(-1);
		}
		IntSetHashSet temp = (IntSetHashSet)obj;
		return this.contentEq(temp);
	}
	
	public static class HashSetIterator implements IntIterator {

		private Iterator<Integer> mSetIter;
		
		public HashSetIterator(IntSetHashSet set) {
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
		return mSet;
	}

}
