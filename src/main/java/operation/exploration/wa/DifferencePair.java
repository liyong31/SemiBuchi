package operation.exploration.wa;

import complement.wa.BuchiWaComplement;
import complement.wa.StateWaNCSB;

class DifferencePair {
    
    final int mFstState;
    final int mSndState;
    final BuchiWaComplement mSndComplement;
    
    public DifferencePair(BuchiWaComplement sndComplement, int fst, int snd) {
        mSndComplement = sndComplement;
        mFstState = fst;
        mSndState = snd;
    }
    
    @Override
    public boolean equals(Object other) {
        if(this == other) return true;
        if(! (other instanceof DifferencePair)) {
            return false;
        }
        // check equivalence
        DifferencePair otherState = (DifferencePair)other;
        return mFstState == otherState.mFstState
          &&  mSndState == otherState.mSndState;
    }
    
    @Override
    public String toString() {
        return "(" + mFstState + "," + mSndComplement.getState(mSndState).toString() + ")";
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + mFstState;
        result = prime * result + mSndState;
        return result;
    }
    
    // by definition, if true, the language of this pair is covered 
    public boolean coveredBy(DifferencePair other) {
        if(mFstState != other.mFstState) return false;
        StateWaNCSB state = (StateWaNCSB) mSndComplement.getState(mSndState);
        StateWaNCSB otherState = (StateWaNCSB) mSndComplement.getState(other.mSndState);
        return state.getNCSB().coveredBy(otherState.getNCSB());
    }
    
    public boolean strictlyCoveredBy(DifferencePair other) {
        if(mFstState != other.mFstState) return false;
        StateWaNCSB state = (StateWaNCSB) mSndComplement.getState(mSndState);
        StateWaNCSB otherState = (StateWaNCSB) mSndComplement.getState(other.mSndState);
        return state.getNCSB().strictlyCoveredBy(otherState.getNCSB());
    }
    
    public int getFirstState() {
        return mFstState;
    }
    
    public int getSecondState() {
        return mSndState;
    }
    
}