package test.universality;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import automata.IBuchiWa;
import operation.universality.BuchiUniversalityNestedDFS;
import test.BAStore;

public class TestBuchiUniversalityNestedDFS {
	
	@Test
	public void testAIsUniversal() {
		IBuchiWa A = BAStore.getA();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityNestedDFS(A).getResult());
	} 
	
	@Test
	public void testBIsUniversal() {
		IBuchiWa B = BAStore.getB();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityNestedDFS(B).getResult());
	} 
	
	
	
	@Test
	public void testCIsUniversal() {
		IBuchiWa C = BAStore.getC();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityNestedDFS(C).getResult());
	} 

	@Test
	public void testDIsUniversal() {
		IBuchiWa D = BAStore.getD();
//		System.out.println(B.toDot());
		assertEquals(true, new BuchiUniversalityNestedDFS(D).getResult());
	} 

	@Test
	public void testEIsUniversal() {
		IBuchiWa E = BAStore.getE();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityNestedDFS(E).getResult());
	} 


}
