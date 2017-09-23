package operation.difference;

import java.util.List;

import automata.IBuchiWa;
import automata.IStateWa;
import complement.BuchiWaComplement;
import complement.IBuchiWaComplement;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import main.Options;
import operation.intersection.GeneralizedBuchiIntersection;
import util.IPair;

import util.IntSet;
import util.MarkedIntStack;
import util.UtilIntSet;

public class BuchiWaDifference implements IBuchiWaDifference {
	
	private final IBuchiWa mFstOperand;
	private final IBuchiWa mSndOperand;
	private final BuchiWaComplement mSndComplement;
	private final IBuchiWa mDifference;
	private TarjanExploration mTarjan; 
	
	public BuchiWaDifference(IBuchiWa fstOp, IBuchiWa sndOp) {
		mFstOperand = fstOp;
		mSndOperand = sndOp;
		assert fstOp.getAlphabetSize() == sndOp.getAlphabetSize();
		mSndComplement = new BuchiWaComplement(sndOp);
		mDifference = new GeneralizedBuchiIntersection(fstOp, mSndComplement);
	}

	@Override
	public IBuchiWaComplement getSecondBuchiComplement() {
		return mSndComplement;
	}
	
	private void initializeTarjan() {
		mTarjan = new TarjanExploration();
	}

	@Override
	public IBuchiWa getResult() {
		if(mTarjan == null) 
			initializeTarjan();
		return mDifference;
	}

	@Override
	public Boolean isIncluded() {
		if(mTarjan == null) 
			initializeTarjan();
		return mTarjan.mIsEmpty;
	}

	@Override
	public IPair<List<Integer>, List<Integer>> getCounterexampleWord() {
		return null;
	}

	@Override
	public IPair<List<IStateWa>, List<IStateWa>> getCounterexampleRun() {
		return null;
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
	public String getOperationName() {
		return "DifferenceTarjan";
	}

	
	class TarjanExploration {
		private int mIndex;
		private final MarkedIntStack mStateStack;
		private final TIntIntMap mIndexMap ;
		private final TIntIntMap mLowlinkMap;
		private TIntList mSCC;
		private Boolean mIsEmpty;
		
		TarjanExploration() {

			mStateStack = new MarkedIntStack(false);
			mIndexMap = new TIntIntHashMap();
			mLowlinkMap = new TIntIntHashMap();
			
			mIsEmpty = true;
			tarjan();
		}

		private void tarjan() {
			mIndex = 0;
			for (final Integer init : mDifference.getInitialStates().iterable()) {
				if (!mIndexMap.containsKey(init)) {
					strongConnect(init);
				}
			}
		}

		private void strongConnect(int v) {
			
			mStateStack.push(v);
			mIndexMap.put(v, mIndex);
			mLowlinkMap.put(v, mIndex);
			
			++ mIndex;	
			
			IStateWa state = mDifference.getState(v);
			for(int letter = 0; letter < mDifference.getAlphabetSize(); letter ++) {
				IntSet succs = state.getSuccessors(letter);
				for(final Integer succ : succs.iterable()) {
					if(! mIndexMap.containsKey(succ)) {
						strongConnect(succ); // did not visit succ before
	                    mLowlinkMap.put(v, Math.min(mLowlinkMap.get(v), mLowlinkMap.get(succ)));					
					}else if(mStateStack.contains(succ)) {
					    mLowlinkMap.put(v, Math.min(mLowlinkMap.get(v), mIndexMap.get(succ)));					
					}
				}
			}
			
			// found one strongly connected component
			if(mLowlinkMap.get(v) == mIndexMap.get(v)){
				
				IntSet sccSet = UtilIntSet.newIntSet();
				TIntList sccList = new TIntArrayList();
				
				while(! mStateStack.empty()){
					int t = mStateStack.pop();
					sccSet.set(t);
					sccList.add(t);
					if(t == v)
						break;
				}

				boolean hasAcc = mDifference.getAcceptance().isAccepted(sccSet);
				if(Options.verbose) System.out.println("hasAcc: " + hasAcc + "," + sccSet);
				
				if(sccSet.cardinality() == 1 // only has a single state
						&& hasAcc            // it is an accepting states
						) {
					boolean hasSelfLoop = false;
					for(Integer letter : state.getEnabledLetters()) {
						if(state.getSuccessors(letter).get(v)) hasSelfLoop = true;
					}
					if(!hasSelfLoop) hasAcc = false;
				}
								
				if(hasAcc) {
					mIsEmpty = false;
					if(mSCC == null || mSCC.size() > sccList.size()) {
						mSCC = sccList;
					}
					if(Options.verbose) {
						System.out.println("Loop: " + mSCC.toString());
					}
				}
			}
		}
	}



}
