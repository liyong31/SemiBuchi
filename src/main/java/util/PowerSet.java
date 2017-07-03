package util;

import java.util.BitSet;
import java.util.Iterator;

public class PowerSet implements Iterator<BitSet> {
	
	private Iterator<BitSet> iterator;
	public PowerSet(BitSet set) {
		if(set.isEmpty()) {
			iterator = new PowerSetEmpty();
		}else {
			iterator = new PowerSetPositive(set);
		}
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public BitSet next() {
		assert hasNext();
		return iterator.next();
	}
	
	
	public static void main(String[] args) {
		BitSet bits = new BitSet();
		bits.set(2);
		bits.set(3);
		bits.set(6);
//		bits.set(7);
		bits.set(9);
		System.out.println(bits);
		PowerSet ps = new PowerSet(bits);
		int i = 0;
		while(ps.hasNext()) {
			BitSet subset = ps.next();
			i ++;
			System.out.println(" " + subset);
		}
		System.out.println("number "+ i);
		
		
		bits.clear();
		System.out.println(bits);
		ps = new PowerSet(bits);
		i = 0;
		while(ps.hasNext()) {
			BitSet subset = ps.next();
			i ++;
			System.out.println(" " + subset);
		}
		System.out.println("number "+ i);
	}

}
