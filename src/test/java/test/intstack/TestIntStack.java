package test.intstack;

import static org.junit.Assert.*;

import org.junit.Test;

import util.MarkedIntStack;

public class TestIntStack {
	
	static MarkedIntStack mStack = new MarkedIntStack();
	static int[] mArray1 = {2, 3, 6, 9, 3};
	static int[] mRevArray1 = {3, 9, 6, 3, 2};
	static int[] mArray2 = {4, 8, 0, 1};
	
	private void inStack() {
		mStack.clear();
		for(int i : mArray1) {
			mStack.push(i);
		}
	}
	
	@Test
	public void testInStack() {
		inStack();
		for(int i : mArray1) {
			assertEquals(mStack.contains(i), true);
		}
		
		for(int i : mArray2) {
			assertEquals(mStack.contains(i), false);
		}
	}
	
	@Test
	public void testIndices() {
		inStack();
		
		for(int i = 0; i < mArray1.length; i ++) {
			assertEquals(mStack.get(i), mArray1[i]);
		}
		
		for(int i = 0; i < mRevArray1.length; i ++) {
			int peek = mStack.peek();
			int top = mStack.pop();
			assertEquals(peek, mRevArray1[i]);
			assertEquals(top, mRevArray1[i]);
		}
		
		assertEquals(mStack.empty(), true);
		
	}
	
	public void testPushInStack() {
		mStack.clear();
		for(int i : mArray1) {
			mStack.push(i);
			assertEquals(mStack.peek(), i);
		}
		
		assertEquals(mStack.size(), mArray1.length);
		mStack.clear();
		assertEquals(mStack.empty(), true);
	}
	

}
