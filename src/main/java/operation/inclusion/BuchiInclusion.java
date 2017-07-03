package operation.inclusion;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import automata.BuchiGeneral;
import automata.IBuchi;
import automata.IState;
import complement.BuchiComplementSDBA;
import complement.StateNCSB;


public abstract class BuchiInclusion implements IBuchiInclusion{
	
	protected final IBuchi mFstOperand;
	protected final IBuchi mSndOperand;
	protected final BuchiComplementSDBA mSndComplement;
	
	protected final IBuchi mResult;
	// use antichain to accelerate inclusion check
	
	protected BuchiInclusion(IBuchi fstOp, IBuchi sndOp) {
		this.mFstOperand = fstOp;
		this.mSndOperand = sndOp;
		this.mSndComplement = new BuchiComplementSDBA(sndOp);
		this.mResult = new BuchiGeneral(fstOp.getAlphabetSize());
		computeInitalStates();
	}
	
	private void computeInitalStates() {
		// TODO Auto-generated method stub
		for(int fst = mFstOperand.getInitialStates().nextSetBit(0);
				fst >= 0;
				fst = mFstOperand.getInitialStates().nextSetBit(fst + 1)) {
			for(int snd = mSndComplement.getInitialStates().nextSetBit(0);
					snd >= 0;
					snd = mSndComplement.getInitialStates().nextSetBit(snd + 1)) {
				StateNCSB sndState = (StateNCSB)mSndComplement.getState(snd);
				InclusionPairNCSB pair = new InclusionPairNCSB(fst, sndState);
				IState state = getOrAddState(pair);
				mResult.setInitial(state);
			}
		}
	}

	protected final Map<InclusionPairNCSB, IState> mPairStateMap = new HashMap<>();
	protected final BitSet mFstFinalStates = new BitSet();
	protected final BitSet mSndFinalStates = new BitSet();
	protected final List<InclusionPairNCSB> mPairNCSBArray = new ArrayList<>();
	
	protected IState getOrAddState(InclusionPairNCSB pair) {
		IState state = mPairStateMap.get(pair);
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
	public IBuchi getFstBuchi() {
		return mFstOperand;
	}
	
	@Override
	public IBuchi getSndBuchi() {
		return mSndOperand;
	}

	@Override
	public IBuchi getSndBuchiComplement() {
		return mSndComplement;
	}
	
	@Override
	public IBuchi getBuchiDifference() {
		return mResult;
	}

}
