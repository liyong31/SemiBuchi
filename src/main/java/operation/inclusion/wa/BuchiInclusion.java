package operation.inclusion.wa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import automata.wa.BuchiWa;
import automata.wa.IBuchiWa;
import automata.wa.IStateWa;
import complement.wa.BuchiWaComplement;
import complement.wa.IBuchiWaComplement;
import complement.wa.StateWaNCSB;
import util.IntSet;
import util.UtilIntSet;


public abstract class BuchiInclusion implements IBuchiInclusion{
	
	protected final IBuchiWa mFstOperand;
	protected final IBuchiWa mSndOperand;
	protected final BuchiWaComplement mSndComplement;
	
	protected final IBuchiWa mResult;
	// use antichain to accelerate inclusion check
	
	protected BuchiInclusion(IBuchiWa fstOp, IBuchiWa sndOp) {
		this.mFstOperand = fstOp;
		this.mSndOperand = sndOp;
		this.mSndComplement = new BuchiWaComplement(sndOp);
		this.mResult = new BuchiWa(fstOp.getAlphabetSize());
		computeInitalStates();
	}
	
	private void computeInitalStates() {
		for(final Integer fst : mFstOperand.getInitialStates().iterable()) {
		    for(final Integer snd : mSndComplement.getInitialStates().iterable()) {
				StateWaNCSB sndState = (StateWaNCSB)mSndComplement.getState(snd);
				InclusionPairNCSB pair = new InclusionPairNCSB(fst, sndState);
				IStateWa state = getOrAddState(pair);
				mResult.setInitial(state);
			}
		}
	}

	protected final Map<InclusionPairNCSB, IStateWa> mPairStateMap = new HashMap<>();
	protected final IntSet mFstFinalStates = UtilIntSet.newIntSet();
	protected final IntSet mSndFinalStates = UtilIntSet.newIntSet();
	protected final List<InclusionPairNCSB> mPairNCSBArray = new ArrayList<>();
	
	protected IStateWa getOrAddState(InclusionPairNCSB pair) {
		IStateWa state = mPairStateMap.get(pair);
		if(state == null) {
			state = mResult.addState();
			mPairNCSBArray.add(pair);
			mPairStateMap.put(pair, state);
			if(mFstOperand.isFinal(pair.getFstElement())) mFstFinalStates.set(state.getId());
			if(mSndComplement.isFinal(pair.getSndElement())) mSndFinalStates.set(state.getId());
		}
		return state;
	}
	
//	protected abstract boolean isValidPair(InclusionPairNCSB pair);
	
	
	@Override
	public IBuchiWa getFstBuchi() {
		return mFstOperand;
	}
	
	@Override
	public IBuchiWa getSndBuchi() {
		return mSndOperand;
	}

	@Override
	public IBuchiWaComplement getSndBuchiComplement() {
		return mSndComplement;
	}
	
	@Override
	public IBuchiWa getBuchiDifference() {
		return mResult;
	}

}
