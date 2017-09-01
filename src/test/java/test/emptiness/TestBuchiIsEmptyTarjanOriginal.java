package test.emptiness;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import automata.IBuchiWa;
import operation.emptiness.BuchiIsEmptyTarjanOriginal;
import test.BAStore;

public class TestBuchiIsEmptyTarjanOriginal {

	
	
	@Test
	public void testAIsEmpty() {
		IBuchiWa A = BAStore.getA();
//		System.out.println(A.toDot());
		assertEquals(false, new BuchiIsEmptyTarjanOriginal(A, 10000).isEmpty());
	} 
	
	@Test
	public void testBIsEmpty() {
		IBuchiWa B = BAStore.getB();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiIsEmptyTarjanOriginal(B, 10000).isEmpty());
	} 
	
	
	@Test
	public void testCIsEmpty() {
		IBuchiWa C = BAStore.getC();
//		System.out.println(C.toDot());
		assertEquals(false, new BuchiIsEmptyTarjanOriginal(C, 10000).isEmpty());
	} 

	@Test
	public void testDIsEmpty() {
		IBuchiWa D = BAStore.getD();
//		System.out.println(D.toDot());
		assertEquals(false, new BuchiIsEmptyTarjanOriginal(D, 10000).isEmpty());
	} 
	
	@Test
	public void testEIsEmpty() {
		IBuchiWa E = BAStore.getE();
//		System.out.println(E.toDot());
		assertEquals(true, new BuchiIsEmptyTarjanOriginal(E, 10000).isEmpty());
	} 
	
}
