package operation.intersection;


import automata.IBuchiWa;
import automata.StateWa;
import util.IntIterator;
import util.IntSet;
import util.UtilIntSet;

class StateWaIntersection extends StateWa {

	private final BuchiWaIntersection mBuchi;
	private final int mFstState;
	private final int mSndState;
	private final TrackNumber mTrack;
	
	public StateWaIntersection(BuchiWaIntersection buchi
			, int id, int fstState, int sndState, TrackNumber track) {
		super(id);
		this.mBuchi = buchi;
		this.mFstState = fstState;
		this.mSndState = sndState;
		this.mTrack = track;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof StateWaIntersection)) {
			return false;
		}
		StateWaIntersection other = (StateWaIntersection)obj;
		return mFstState == other.mFstState
			&& mSndState == other.mSndState
			&& mTrack == other.mTrack;
	}
	
	@Override
	public String toString() {
		return "(" + mFstState + "," + mSndState + "):" + mTrack;
	}
	
	int hashCode;
	boolean hasCode = false;
	@Override
	public int hashCode() {
		if(hasCode) return hashCode;
		else {
			hasCode = true;
			hashCode = mFstState * mBuchi.getStateSize() + mSndState;
			hashCode += mTrack == TrackNumber.TRACK_ONE ? 1 : 2;
		}
		return hashCode;
	}
	
	protected int getFstState() {
		return mFstState;
	}
	
	protected int getSndState() {
		return mSndState;
	}
		
	private IntSet visitedLetters = UtilIntSet.newIntSet();

	@Override
	public IntSet getSuccessors(int letter) {
		if(visitedLetters.get(letter)) {
			return super.getSuccessors(letter);
		}
		visitedLetters.set(letter);
		
		// compute successors
		IBuchiWa fstOp = mBuchi.getFstOperand();
		IBuchiWa sndOp = mBuchi.getSndOperand();
		IntSet fstSuccs = fstOp.getState(mFstState).getSuccessors(letter);
		IntSet sndSuccs = sndOp.getState(mSndState).getSuccessors(letter);
		
		IntIterator fstIter = fstSuccs.iterator();
		while(fstIter.hasNext()) {
			int fstSucc = fstIter.next();
			IntIterator sndIter = sndSuccs.iterator();
			while(sndIter.hasNext()) {
				int sndSucc = sndIter.next();
				TrackNumber succTrack = getSuccStateTrack();
				// pair (X, Y)
				StateWaIntersection succ = mBuchi.addState(fstSucc, sndSucc, succTrack);                
				this.addSuccessor(letter, succ.getId());
			}
	    }

		return super.getSuccessors(letter);
	}
	
	/**
	 * current state is in TRACK_ONE
	     * If fst is final and the snd is not, then we jump to TRACK_TWO to wait snd to be final
	     * If fst and snd are both final, we already see final states in both operands, stay in track one
	 * current state is in TRACK_TWO: means that we already saw fst final before
	     * If snd is final, then we jump to TRACK_ONE to see fst final states
	     * if snd is not final, then we stay in TRACK_TWO
	 *    */
	private TrackNumber getSuccStateTrack() {
		IBuchiWa fstOp = mBuchi.getFstOperand();
		IBuchiWa sndOp = mBuchi.getSndOperand();
		
		TrackNumber succTrack;
		if (mTrack == TrackNumber.TRACK_ONE) {
			if (fstOp.isFinal(mFstState) && !sndOp.isFinal(mSndState)) {
				succTrack = TrackNumber.TRACK_TWO;
			} else {
				succTrack = TrackNumber.TRACK_ONE;
			}
		} else {
			assert mTrack == TrackNumber.TRACK_TWO;
			if (sndOp.isFinal(mSndState)) {
				succTrack = TrackNumber.TRACK_ONE;
			} else {
				succTrack = TrackNumber.TRACK_TWO;
			}
		}
		return succTrack;
	}

}
