package complement;

import util.IntSet;
import util.UtilIntSet;

class SuccessorResult {
	
	protected IntSet mSuccs ;
	protected IntSet mMinusFSuccs ;
	protected IntSet mInterFSuccs ;
	protected boolean hasSuccessor ;
	
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
