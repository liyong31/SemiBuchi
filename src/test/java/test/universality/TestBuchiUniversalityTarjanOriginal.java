package test.universality;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import automata.IBuchi;
import operation.universality.BuchiUniversalityTarjanOriginal;
import test.BAStore;

public class TestBuchiUniversalityTarjanOriginal {
	
	@Test
	public void testAIsUniversal() {
		IBuchi A = BAStore.getA();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityTarjanOriginal(A).isUniversal());
	} 
	
	@Test
	public void testBIsUniversal() {
		IBuchi B = BAStore.getB();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityTarjanOriginal(B).isUniversal());
	} 
	
	
	
	@Test
	public void testCIsUniversal() {
		IBuchi C = BAStore.getC();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityTarjanOriginal(C).isUniversal());
	} 

	@Test
	public void testDIsUniversal() {
		IBuchi D = BAStore.getD();
//		System.out.println(B.toDot());
		assertEquals(true, new BuchiUniversalityTarjanOriginal(D).isUniversal());
	} 

	@Test
	public void testEIsUniversal() {
		IBuchi E = BAStore.getE();
//		System.out.println(B.toDot());
		assertEquals(false, new BuchiUniversalityTarjanOriginal(E).isUniversal());
	} 


}
