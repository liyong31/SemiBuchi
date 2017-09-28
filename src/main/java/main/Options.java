package main;

public class Options {
	
	public static boolean verbose = false;
	
	// 0 for BitSet, 1 for SparseBitSet, 2 for TInSet, 3 for TreeSet, and 4 for HashSet 
	public static int setChoice = 0;
	
	// whether to enable optimized version of NCSB
	// delay the word from C (newly incomers from N) to S 
	// so set B to be distribution source 
	public static boolean lazyS = false;
	
	// delay the word from C (newly incomers from N) to B 
	public static boolean lazyB = false;
	
	public static boolean useGBA = false;
	
	// N\/C\/S smaller first
	public static boolean smallerNCS = false;

}
