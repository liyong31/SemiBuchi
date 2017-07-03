package test.universality;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import automata.IBuchi;
import operation.emptiness.BuchiIsEmptyASCC;
import operation.universality.BuchiUniversalityASCC;
import test.BAStore;

public class TestBuchiUniversalityASCC {
	
	@Test
	public void testAIsUniversal() {
		IBuchi A = BAStore.getA();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityASCC(A).isUniversal());
	} 
	
	@Test
	public void testBIsUniversal() {
		IBuchi B = BAStore.getB();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityASCC(B).isUniversal());
	} 
	
	
	
	@Test
	public void testCIsUniversal() {
		IBuchi C = BAStore.getC();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityASCC(C).isUniversal());
	} 

	@Test
	public void testDIsUniversal() {
		IBuchi D = BAStore.getD();
//		System.out.println(B.toDot());
		assertEquals(true, new BuchiUniversalityASCC(D).isUniversal());
	} 

	@Test
	public void testEIsUniversal() {
		IBuchi E = BAStore.getE();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityASCC(E).isUniversal());
	} 


}
