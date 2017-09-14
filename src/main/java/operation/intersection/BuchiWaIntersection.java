package operation.intersection;

import automata.BuchiWa;
import automata.IBuchiWa;
import automata.StateWa;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import util.IntIterator;
import util.IntSet;
import util.UtilIntSet;
/**
 * Compute Intersection of two Buchi automta and the result is Buchi automaton
 * */
public class BuchiWaIntersection extends BuchiWa implements IBuchiWaIntersection {

	private final IBuchiWa mFstOperand;
	private final IBuchiWa mSndOperand;
		
	private final TObjectIntMap<ProductState> mState2Int = new TObjectIntHashMap<>();
	
	public BuchiWaIntersection(IBuchiWa fstOp, IBuchiWa sndOp) {
		super(fstOp.getAlphabetSize());
		assert fstOp.getAlphabetSize() == sndOp.getAlphabetSize();
		this.mFstOperand = fstOp;
		this.mSndOperand = sndOp;
		computeInitialStates();
	}
	
	
	protected ProductState addState(int fst, int snd, TrackNumber track) {
		ProductState state = new ProductState(this, 0, fst, snd, track);
		if(mState2Int.containsKey(state)) {
			return (ProductState) getState(mState2Int.get(state));
		}
		// add new state
		ProductState newState = new ProductState(this, getStateSize(), fst, snd, track);
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
				ProductState state = addState(fst, snd, TrackNumber.TRACK_ONE);		
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
	
	// ------------------------------------ 
	class ProductState extends StateWa implements IProductState {

		private final int mFstState;
		private final int mSndState;
		private final TrackNumber mTrack;
		private TrackNumber mSuccTrack = null;
		
		public ProductState(BuchiWaIntersection buchi
				, int id, int fstState, int sndState, TrackNumber track) {
			super(id);
			this.mFstState = fstState;
			this.mSndState = sndState;
			this.mTrack = track;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(this == obj) return true;
			if(!(obj instanceof ProductState)) {
				return false;
			}
			ProductState other = (ProductState)obj;
			return this.contentEq(other);
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
				hashCode = mFstState * mFstOperand.getStateSize() + mSndState;
				hashCode += mTrack == TrackNumber.TRACK_ONE ? 1 : 2;
			}
			return hashCode;
		}
		
		public int getFstState() {
			return mFstState;
		}
		
		public int getSndState() {
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
			IntSet fstSuccs = mFstOperand.getState(mFstState).getSuccessors(letter);
			IntSet sndSuccs = mSndOperand.getState(mSndState).getSuccessors(letter);
			final IntSet succs = UtilIntSet.newIntSet();
			for(final Integer fstSucc : fstSuccs.iterable()) {
				for(final Integer sndSucc : sndSuccs.iterable()) {
					TrackNumber succTrack = getSuccStateTrack();
					// pair (X, Y)
					ProductState succ = addState(fstSucc, sndSucc, succTrack);                
					this.addSuccessor(letter, succ.getId());
					succs.set(succ.getId());
				}
			}

			return succs;
		}

		@Override
		public TrackNumber getTrackNumber() {
			return mTrack;
		}

		@Override
		public TrackNumber getSuccStateTrack() {
			if(mSuccTrack == null) {
				mSuccTrack = this.getSuccStateTrack(
                               mFstOperand.isFinal(mFstState)
                               , mSndOperand.isFinal(mSndState));
			}
			return mSuccTrack;
		}

	}

}
