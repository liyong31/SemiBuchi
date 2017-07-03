package test.emptiness;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import automata.IBuchi;
import operation.emptiness.BuchiIsEmptyASCC;
import test.BAStore;

public class TestBuchiIsEmptyASCC {
	
	
	@Test
	public void testAIsEmpty() {
		IBuchi A = BAStore.getA();
//		System.out.println(A.toDot());
		assertEquals(false, new BuchiIsEmptyASCC(A, 10000).isEmpty());
	} 
	
	@Test
	public void testBIsEmpty() {
		IBuchi B = BAStore.getB();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiIsEmptyASCC(B, 10000).isEmpty());
	} 
	
	
	@Test
	public void testCIsEmpty() {
		IBuchi C = BAStore.getC();
//		System.out.println(C.toDot());
		assertEquals(false, new BuchiIsEmptyASCC(C, 10000).isEmpty());
	} 

	@Test
	public void testDIsEmpty() {
		IBuchi D = BAStore.getD();
//		System.out.println(D.toDot());
		assertEquals(false, new BuchiIsEmptyASCC(D, 10000).isEmpty());
	} 
	
	@Test
	public void testEIsEmpty() {
		IBuchi E = BAStore.getE();
		System.out.println(E.toDot());
		assertEquals(true, new BuchiIsEmptyASCC(E, 10000).isEmpty());
	} 
}
