package operation.intersection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import automata.Acc;
import automata.BuchiWa;
import automata.IBuchiWa;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import util.IntSet;
import util.UtilIntSet;

/**
 * Compute Intersection of two Buchi automta and the result is generalized Buchi automaton
 * */
public class GeneralizedBuchiIntersection extends BuchiWa implements IBuchiWaIntersection {

	private final IBuchiWa mFstOperand;
	private final IBuchiWa mSndOperand;
	
	private final TObjectIntMap<GeneralizedState> mState2Int = new TObjectIntHashMap<>();
		
	public GeneralizedBuchiIntersection(IBuchiWa fstOp, IBuchiWa sndOp) {
		super(fstOp.getAlphabetSize());
		this.mFstOperand = fstOp;
		this.mSndOperand = sndOp;
		computeInitialStates();
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
	
	// take care of the acceptance
	protected GeneralizedState addState(int left, int right) {
		GeneralizedState state = new GeneralizedState(this, 0);
		state.setPairs(left, right);		
		if(mState2Int.containsKey(state)) {
			return (GeneralizedState) getState(mState2Int.get(state));
		}else {
			GeneralizedState newState = new GeneralizedState(this, getStateSize());
			newState.setPairs(left, right);
			int id = this.addState(newState);
			mState2Int.put(newState, id);
			getAcceptance().setAcc(newState);
			return newState;
		}
	}

	private void computeInitialStates() {
		// first compute initial states
		IntSet fstInits = mFstOperand.getInitialStates();
		IntSet sndInits = mSndOperand.getInitialStates();
		for(final Integer fst : fstInits.iterable()) {
			for(final Integer snd : sndInits.iterable()) {
				GeneralizedState state = addState(fst, snd);				
				this.setInitial(state);
			}
		}
	}
	
	@Override
	public AccBuchiIntersection getAcceptance() {
		if(acc == null) {
			acc = new AccBuchiIntersection();
		}
		return (AccBuchiIntersection)acc;
	}
	
	private class AccBuchiIntersection implements Acc {

		private final List<IntSet> accs;
		public AccBuchiIntersection() {
			accs = new ArrayList<>();
			for(int i = 0; i < mFstOperand.getAcceptance().getAccs().size(); i ++) {
				accs.add(UtilIntSet.newIntSet());
			}
			
			for(int i = 0; i < mSndOperand.getAcceptance().getAccs().size(); i ++) {
				accs.add(UtilIntSet.newIntSet());
			}
		}
		
		protected void setAcc(GeneralizedState state) {
			int i = 0;
			for(IntSet set : mFstOperand.getAcceptance().getAccs()) {
				if(set.get(state.getLeft())) {
					accs.get(i).set(state.getId());
				}
				i ++;
			}
			
			for(IntSet set : mSndOperand.getAcceptance().getAccs()) {
				if(set.get(state.getRight())) {
					accs.get(i).set(state.getId());
				}
				i ++;
			}
		}
		
		@Override
		public boolean isAccepted(IntSet set) {
			for(IntSet acc : accs) {
				if(!acc.overlap(set)) return false;
			}
			return true;
		}

		@Override
		public List<IntSet> getAccs() {
			return Collections.unmodifiableList(accs);
		}
	}

	@Override
	public String getOperationName() {
		return "GenWaIntersection";
	}

}
