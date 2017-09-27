package test.universality;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import automata.wa.IBuchiWa;
import operation.emptiness.BuchiIsEmptyASCC;
import operation.universality.BuchiUniversalityASCC;
import test.BAStore;

public class TestBuchiUniversalityASCC {
	
	@Test
	public void testAIsUniversal() {
		IBuchiWa A = BAStore.getA();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityASCC(A).getResult());
	} 
	
	@Test
	public void testBIsUniversal() {
		IBuchiWa B = BAStore.getB();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityASCC(B).getResult());
	} 
	
	
	
	@Test
	public void testCIsUniversal() {
		IBuchiWa C = BAStore.getC();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityASCC(C).getResult());
	} 

	@Test
	public void testDIsUniversal() {
		IBuchiWa D = BAStore.getD();
//		System.out.println(B.toDot());
		assertEquals(true, new BuchiUniversalityASCC(D).getResult());
	} 

	@Test
	public void testEIsUniversal() {
		IBuchiWa E = BAStore.getE();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityASCC(E).getResult());
	} 


}
