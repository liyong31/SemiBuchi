package operation.inclusion;

import automata.StateWa;
import complement.StateWaNCSB;
import util.IPair;

public class InclusionPairNCSB implements IPair<Integer, StateWaNCSB>{
	
	private final int mFstStateId;
	private final StateWaNCSB mSndState;
		
	public InclusionPairNCSB(StateWa fstState, StateWaNCSB sndState) {
		this.mFstStateId = fstState.getId();
		this.mSndState = sndState;
	}
	
	public InclusionPairNCSB(int fstStateId, StateWaNCSB sndState) {
		this.mFstStateId = fstStateId;
		this.mSndState = sndState;
	}
	
	@Override
	public boolean equals(Object other) {
		if(! (other instanceof InclusionPairNCSB)) {
			return false;
		}
		// check equivalence
		InclusionPairNCSB otherState = (InclusionPairNCSB)other;
		return mFstStateId == otherState.mFstStateId
		  &&  mSndState.equals(otherState.mSndState);
	}
	
	@Override
	public String toString() {
		return "(" + mFstStateId + "," + mSndState.toString() + ")";
	}
	
	int hashCode;
	boolean hasCode = false;
	@Override
	public int hashCode() {
		if(hasCode) return hashCode;
		else {
			hasCode = true;
			hashCode = toString().hashCode();
		}
		return hashCode;
	}
	
	// by definition, if true, the language of this pair is covered 
	public boolean coveredBy(InclusionPairNCSB other) {
		return mFstStateId == other.mFstStateId
		  &&  mSndState.getNCSB().coveredBy(other.mSndState.getNCSB());
	}
	
	public boolean strictlyCoveredBy(InclusionPairNCSB other) {
		return mFstStateId == other.mFstStateId
				  &&  mSndState.getNCSB().strictlyCoveredBy(other.mSndState.getNCSB());
	}
	
	@Override
	public Integer getFstElement() {
		return mFstStateId;
	}
	
	@Override
	public StateWaNCSB getSndElement() {
		return mSndState;
	}
}
