package test.universality;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import automata.IBuchiWa;
import operation.universality.BuchiUniversalityTarjanOriginal;
import test.BAStore;

public class TestBuchiUniversalityTarjanOriginal {
	
	@Test
	public void testAIsUniversal() {
		IBuchiWa A = BAStore.getA();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityTarjanOriginal(A).getResult());
	} 
	
	@Test
	public void testBIsUniversal() {
		IBuchiWa B = BAStore.getB();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityTarjanOriginal(B).getResult());
	} 
	
	
	
	@Test
	public void testCIsUniversal() {
		IBuchiWa C = BAStore.getC();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityTarjanOriginal(C).getResult());
	} 

	@Test
	public void testDIsUniversal() {
		IBuchiWa D = BAStore.getD();
//		System.out.println(B.toDot());
		assertEquals(true, new BuchiUniversalityTarjanOriginal(D).getResult());
	} 

	@Test
	public void testEIsUniversal() {
		IBuchiWa E = BAStore.getE();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityTarjanOriginal(E).getResult());
	} 


}
