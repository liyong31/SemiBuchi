package test.emptiness;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import automata.IBuchiWa;
import operation.emptiness.BuchiIsEmptyNestedDFS;
import test.BAStore;

public class TestBuchiIsEmptyNestedDFS {

	
	
	@Test
	public void testAIsEmpty() {
		IBuchiWa A = BAStore.getA();
//		System.out.println(A.toDot());
		assertEquals(false, new BuchiIsEmptyNestedDFS(A, 10000).isEmpty());
	} 
	
	@Test
	public void testBIsEmpty() {
		IBuchiWa B = BAStore.getB();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiIsEmptyNestedDFS(B, 10000).isEmpty());
	} 
	
	
	@Test
	public void testCIsEmpty() {
		IBuchiWa C = BAStore.getC();
//		System.out.println(C.toDot());
		assertEquals(false, new BuchiIsEmptyNestedDFS(C, 10000).isEmpty());
	} 

	@Test
	public void testDIsEmpty() {
		IBuchiWa D = BAStore.getD();
//		System.out.println(D.toDot());
		assertEquals(false, new BuchiIsEmptyNestedDFS(D, 10000).isEmpty());
	} 
	
	@Test
	public void testEIsEmpty() {
		IBuchiWa E = BAStore.getE();
//		System.out.println(E.toDot());
		assertEquals(true, new BuchiIsEmptyNestedDFS(E, 10000).isEmpty());
	} 
	
}
