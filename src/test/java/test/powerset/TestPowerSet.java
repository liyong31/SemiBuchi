package test.powerset;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import util.IntSet;
import util.PowerSet;
import util.UtilIntSet;

public class TestPowerSet {
	
	static IntSet mElems = UtilIntSet.newIntSet();
	static int[] mArray1 = {2, 3, 6, 9, 3};
	static int[] mArray2 = {4, 8, 0, 1};
	
	static {
		for(int i : mArray1) {
			mElems.set(i);
		}
	}
	
	@Test
	public void testInElements() {
		for(int i : mArray1) {
			assertEquals(mElems.get(i), true);
		}
	}
	
	@Test
	public void testNotInElements() {
		for(int i : mArray2) {
			assertEquals(mElems.get(i), false);
		}
	}
	
	@Test
	public void testEmptyPowerSet() {
		PowerSet ps = new PowerSet(UtilIntSet.newIntSet());
		List<IntSet> arr = new ArrayList<>();
		while(ps.hasNext()) {
			arr.add(ps.next());
		}
		assertEquals(arr.size(), 1);
		assertEquals(arr.get(0).isEmpty(), true);
	}
	
	@Test
	public void testPositivePowerSet() {
		PowerSet ps = new PowerSet(mElems);
		List<IntSet> arr = new ArrayList<>();
		IntSet allSets = UtilIntSet.newIntSet();
		while(ps.hasNext()) {
			IntSet set = ps.next();
			allSets.or(set);
			notIn(set, arr);
			assertEquals(set.subsetOf(mElems), true);
			arr.add(set);
		}
		assertEquals(arr.size(), 16);
		assertEquals(allSets.contentEq(mElems), true);
	}
	
	private void notIn(IntSet set, List<IntSet> list) {
		for(IntSet elem: list) {
			assertEquals(elem.contentEq(set), false);
		}
	}

}
