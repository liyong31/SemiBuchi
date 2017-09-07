package operation.intersection;

import automata.BuchiWa;
import automata.IBuchiWa;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import util.IntIterator;
import util.IntSet;
/**
 * Compute Intersection of two Buchi automta and the result is Buchi automaton
 * */
public class BuchiWaIntersection extends BuchiWa implements IBuchiWaIntersection {

	private final IBuchiWa mFstOperand;
	private final IBuchiWa mSndOperand;
		
	private final TObjectIntMap<StateWaIntersection> mState2Int = new TObjectIntHashMap<>();
	
	public BuchiWaIntersection(IBuchiWa fstOp, IBuchiWa sndOp) {
		super(fstOp.getAlphabetSize());
		assert fstOp.getAlphabetSize() == sndOp.getAlphabetSize();
		this.mFstOperand = fstOp;
		this.mSndOperand = sndOp;
		computeInitialStates();
	}
	
	
	protected StateWaIntersection addState(int fst, int snd, TrackNumber track) {
		StateWaIntersection state = new StateWaIntersection(this, 0, fst, snd, track);
		if(mState2Int.containsKey(state)) {
			return (StateWaIntersection) getState(mState2Int.get(state));
		}
		// add new state
		StateWaIntersection newState = new StateWaIntersection(this, getStateSize(), fst, snd, track);
		int id = this.addState(newState);
		mState2Int.put(newState, id);
		// whether it is accepting state
		final boolean isFinal = mFstOperand.isFinal(fst) && (track == TrackNumber.TRACK_ONE);
		if(isFinal) setFinal(id);
		return newState;
	}

	private void computeInitialStates() {
		IntSet fstInits = mFstOperand.getInitialStates();
		IntSet sndInits = mSndOperand.getInitialStates();
		
		IntIterator fstIter = fstInits.iterator();
		while(fstIter.hasNext()) {
			int fst = fstIter.next();
			IntIterator sndIter = sndInits.iterator();
			while(sndIter.hasNext()) {
				int snd = sndIter.next();
				StateWaIntersection state = addState(fst, snd, TrackNumber.TRACK_ONE);		
				this.setInitial(state);
			}
		}
	}

	@Override
	public IBuchiWa getFirstOperand() {
		return mFstOperand;
	}

	@Override
	public IBuchiWa getSecondOperand() {
		return mSndOperand;
	}

	@Override
	public IBuchiWa getResult() {
		return this;
	}

}
