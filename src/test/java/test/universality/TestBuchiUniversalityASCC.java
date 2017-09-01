package test.universality;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import automata.IBuchiWa;
import operation.emptiness.BuchiIsEmptyASCC;
import operation.universality.BuchiUniversalityASCC;
import test.BAStore;

public class TestBuchiUniversalityASCC {
	
	@Test
	public void testAIsUniversal() {
		IBuchiWa A = BAStore.getA();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityASCC(A).isUniversal());
	} 
	
	@Test
	public void testBIsUniversal() {
		IBuchiWa B = BAStore.getB();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityASCC(B).isUniversal());
	} 
	
	
	
	@Test
	public void testCIsUniversal() {
		IBuchiWa C = BAStore.getC();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityASCC(C).isUniversal());
	} 

	@Test
	public void testDIsUniversal() {
		IBuchiWa D = BAStore.getD();
//		System.out.println(B.toDot());
		assertEquals(true, new BuchiUniversalityASCC(D).isUniversal());
	} 

	@Test
	public void testEIsUniversal() {
		IBuchiWa E = BAStore.getE();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityASCC(E).isUniversal());
	} 


}
