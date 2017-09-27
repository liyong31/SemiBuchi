package complement;

import util.IntSet;
import util.UtilIntSet;

public class SuccessorResult {
	
	public IntSet mSuccs ;
	public IntSet mMinusFSuccs ;
	public IntSet mInterFSuccs ;
	public boolean hasSuccessor ;
	
	public SuccessorResult() {
		mSuccs = UtilIntSet.newIntSet();
		mMinusFSuccs = UtilIntSet.newIntSet();
		mInterFSuccs = UtilIntSet.newIntSet();
		hasSuccessor = true;
	}
	
	@Override
	public String toString() {
		return "[" + mSuccs.toString() + ":" + mMinusFSuccs.toString() + ":"
				   + mInterFSuccs.toString() + ":" + hasSuccessor + "]";
	}

}
