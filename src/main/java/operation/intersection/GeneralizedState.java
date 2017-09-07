package operation.intersection;

import automata.IBuchiWa;
import automata.StateWa;
import util.IntIterator;
import util.IntSet;
import util.UtilIntSet;

public class GeneralizedState extends StateWa {

	private final GeneralizedBuchiIntersection mBuchi;
	
	public GeneralizedState(GeneralizedBuchiIntersection buchi, int id) {
		super(id);
		this.mBuchi = buchi;
	}
	
	private int mLeft;
	private int mRight;
	
	protected void setPairs(int left, int right) {
		mLeft = left;
		mRight = right;
	}
	
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof GeneralizedState)) {
			return false;
		}
		GeneralizedState other = (GeneralizedState)obj;
		return mLeft == other.mLeft && mRight == other.mRight;
	}
	
	public String toString() {
		return "(" + mLeft + "," + mRight + ")";
	}
	
	int hashCode;
	boolean hasCode = false;
	@Override
	public int hashCode() {
		if(hasCode) return hashCode;
		else {
			hasCode = true;
			hashCode = mLeft * mBuchi.getStateSize() + mRight;
		}
		return hashCode;
	}
	
	public int getLeft() {
		return mLeft;
	}
	
	public int getRight() {
		return mRight;
	}
	
	private IntSet visitedLetters = UtilIntSet.newIntSet();
	
	@Override
	public IntSet getSuccessors(int letter) {
		if(visitedLetters.get(letter)) {
			return super.getSuccessors(letter);
		}
		
		visitedLetters.set(letter);
		// compute successors
		IBuchiWa fstOp = mBuchi.getFirstOperand();
		IBuchiWa sndOp = mBuchi.getSecondOperand();
		IntSet fstSuccs = fstOp.getState(mLeft).getSuccessors(letter);
		IntSet sndSuccs = sndOp.getState(mRight).getSuccessors(letter);
		
		IntIterator fstIter = fstSuccs.iterator();
		while(fstIter.hasNext()) {
			int fstSucc = fstIter.next();
			IntIterator sndIter = sndSuccs.iterator();
			while(sndIter.hasNext()) {
				int sndSucc = sndIter.next();
				// pair (X, Y)
				GeneralizedState succ = mBuchi.addState(fstSucc, sndSucc);                
				this.addSuccessor(letter, succ.getId());
			}
	    }

		return super.getSuccessors(letter);
	}

}
