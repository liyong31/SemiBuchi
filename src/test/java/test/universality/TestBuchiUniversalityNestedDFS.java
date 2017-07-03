package test.universality;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import automata.IBuchi;
import operation.universality.BuchiUniversalityNestedDFS;
import test.BAStore;

public class TestBuchiUniversalityNestedDFS {
	
	@Test
	public void testAIsUniversal() {
		IBuchi A = BAStore.getA();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityNestedDFS(A).isUniversal());
	} 
	
	@Test
	public void testBIsUniversal() {
		IBuchi B = BAStore.getB();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityNestedDFS(B).isUniversal());
	} 
	
	
	
	@Test
	public void testCIsUniversal() {
		IBuchi C = BAStore.getC();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityNestedDFS(C).isUniversal());
	} 

	@Test
	public void testDIsUniversal() {
		IBuchi D = BAStore.getD();
//		System.out.println(B.toDot());
		assertEquals(true, new BuchiUniversalityNestedDFS(D).isUniversal());
	} 

	@Test
	public void testEIsUniversal() {
		IBuchi E = BAStore.getE();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityNestedDFS(E).isUniversal());
	} 


}
