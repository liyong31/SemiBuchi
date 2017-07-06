package operation.inclusion;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import automata.BuchiGeneral;
import automata.IState;
import automata.IBuchi;
import complement.BuchiComplementSDBA;
import complement.StateNCSB;
import util.IPair;

/**
 * only valid for automata pair whose second element is an SDBA
 * make use of Antichain
 * **/
@Deprecated
public class BuchiInclusionAntichain implements IBuchiInclusion {
	
	private final IBuchi mFstOperand;
	private final IBuchi mSndOperand;
	private final BuchiComplementSDBA mSndComplement;
	
	private final IBuchi mResult;
	// use antichain to accelerate inclusion check
	private final Antichain mAntichain;
	
	public BuchiInclusionAntichain(IBuchi fstOp, IBuchi sndOp) {
		this.mFstOperand = fstOp;
		this.mSndOperand = sndOp;
		this.mSndComplement = new BuchiComplementSDBA(sndOp);
		this.mResult = new BuchiGeneral(fstOp.getAlphabetSize());
		this.mAntichain = new Antichain(null);
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
				if(mAntichain.addPair(fst, sndState)) {
					InclusionPairNCSB pair = new InclusionPairNCSB(fst, sndState);
					IState state = getOrAddState(pair);
					mResult.setInitial(state);
				}
			}
		}
	}

	private final Map<InclusionPairNCSB, IState> mPairStateMap = new HashMap<>();
	private final BitSet mFstFinalStates = new BitSet();
	private final BitSet mSndFinalStates = new BitSet();
	private final List<InclusionPairNCSB> mPairNCSBArray = new ArrayList<>();
	
	private IState getOrAddState(InclusionPairNCSB pair) {
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
		
	/**
	 * try to compute the product of mFstOperand and mSndComplement
	 * and avoid exploring unnecessary states as much as possible
	 * */
	
	public Boolean isIncluded() {
		Tarjan scc = new Tarjan();
		System.out.println(mAntichain.toString() );
		System.out.println(mResult.toString());
		System.out.println(mFstFinalStates + ", " + mSndFinalStates);
		return scc.isEmpty();
	}
	

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

	@Override
	public IPair<List<Integer>, List<Integer>> getCounterexampleWord() {
		return null;
	}

	@Override
	public IPair<List<IState>, List<IState>> getCounterexampleRun() {
		return null;
	}
	
	// ---------------------------- part for SCC decomposition -------------
	
	/**
	 * SCC Decomposition
	 * */
	private class Tarjan {
		
		private int mIndex=0;
		private final Stack<Integer> mStack = new Stack<Integer>();
		private final Map<Integer,Integer> mIndexMap = new HashMap<Integer,Integer>();
		private final Map<Integer,Integer> mLowlinkMap = new HashMap<Integer,Integer>();
		
		private boolean empty = true;
		
		public Tarjan() {

		}
		
		public boolean isEmpty() {
			return empty;
		}
		
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
