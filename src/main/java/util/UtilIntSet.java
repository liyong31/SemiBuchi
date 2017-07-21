package util;

import main.Main;

public class UtilIntSet {
	
	private UtilIntSet() {
		
	}
	
	public static IntSet newIntSet() {
		IntSet set = null;
		switch(Main.SET_CHOICE) {
		case 1:
			set = new IntSetSparseBits();
			break;
		case 2:
			set = new IntSetTIntSet();
			break;
		case 3:
			set = new IntSetTreeSet();
			break;
		default:
			set = new IntSetBits();
			break;
		}
		return set;
	}

}
