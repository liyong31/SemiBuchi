package operation.inclusion.wa;

import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import automata.wa.IBuchiWa;
import automata.wa.IStateWa;
import complement.wa.StateWaNCSB;
import main.Options;
import util.IPair;
import util.IntIterator;
import util.IntSet;
import util.Timer;

/**
 * only valid for automata pair whose second element is an SDBA
 * make use of NCSB complementation
 * **/
public class BuchiInclusionComplement extends BuchiInclusion {
		
	public BuchiInclusionComplement(IBuchiWa fstOp, IBuchiWa sndOp) {
		super(fstOp, sndOp);
	}
		
	/**
	 * try to compute the product of mFstOperand and mSndComplement
	 * by constructing the complement of mSndOperand
	 * */
	
	public Boolean isIncluded() {
		Tarjan scc = new Tarjan();
//		System.out.println(mResult.toString());
//		System.out.println(mFstFinalStates + ", " + mSndFinalStates);
//		System.out.println("acc:" + scc.getAcceptedSCC());
//		System.out.println(scc.getPrefix() + ", (" + scc.getLoop() + ")");
		return scc.mIsEmpty;
	}

	@Override
	public IPair<List<Integer>, List<Integer>> getCounterexampleWord() {
		return null;
	}

	@Override
	public IPair<List<IStateWa>, List<IStateWa>> getCounterexampleRun() {
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
		
		private final BitSet scc = new BitSet();
		
		private Boolean mIsEmpty = true;
		
		private final Timer mTimer;
		
		public Tarjan() {
			this.mTimer = new Timer();
			
			mTimer.start();
			IntIterator iter = mResult.getInitialStates().iterator();
			while(iter.hasNext()) {
				int n = iter.next();
				if(! mIndexMap.containsKey(n)){
					tarjan(n);
				}
			}
		}

		
		private final LinkedList<Integer> mPrefix = new LinkedList<>();
		private final LinkedList<Integer> mLoop = new LinkedList<>();
//		
		
		// make use of tarjan algorithm
		void tarjan(int v) {
			
			mIndexMap.put(v, mIndex);
			mLowlinkMap.put(v, mIndex);
			mIndex++;
			mStack.push(v);
			InclusionPairNCSB pair = mPairNCSBArray.get(v); //v must in mPairArray
			//TODO only get enabled letters
			for(int letter = 0; letter < mFstOperand.getAlphabetSize(); letter ++) {
				// X states from first BA 
				IntSet fstSuccs = mFstOperand.getState(pair.getFstElement()).getSuccessors(letter);
				if(fstSuccs.isEmpty()) continue;
				// Y states from second BA
				IntSet sndSuccs = pair.getSndElement().getSuccessors(letter);
				IntIterator fstIter = fstSuccs.iterator();
				while(fstIter.hasNext()) {
					int fstSucc = fstIter.next();
					IntIterator sndIter = sndSuccs.iterator();
					while(sndIter.hasNext()) {
						int sndSucc = sndIter.next();
						// pair (X, Y)
						StateWaNCSB yState = (StateWaNCSB) mSndComplement.getState(sndSucc);
						InclusionPairNCSB pairSucc = new InclusionPairNCSB(fstSucc, yState);
						IStateWa stateSucc = getOrAddState(pairSucc);
						mPairStateMap.get(pair).addSuccessor(letter, stateSucc.getId());
						if(! mIndexMap.containsKey(stateSucc.getId())){
							tarjan(stateSucc.getId());
							if(mIsEmpty == null || !mIsEmpty.booleanValue()) 
								return;
							mLowlinkMap.put(v,	Math.min(mLowlinkMap.get(v), mLowlinkMap.get(stateSucc.getId())));
						}else if(mStack.contains(stateSucc.getId())){
							mLowlinkMap.put(v,	Math.min(mLowlinkMap.get(v), mIndexMap.get(stateSucc.getId())));
						}
					}
				}
			}

			if(mLowlinkMap.get(v).intValue() == mIndexMap.get(v).intValue()){
				boolean hasFstAcc = false, hasSndAcc = false;
				// In order to get the accepting run, we have to use list in the future
				scc.clear();
				mLoop.clear();
				while(! mStack.empty()){
					Integer t = mStack.pop();
					scc.set(t);
					mLoop.addFirst(t);
					InclusionPairNCSB sp = mPairNCSBArray.get(t);
					if(mFstOperand.isFinal(sp.getFstElement())) hasFstAcc = true;
					if(mSndComplement.isFinal(sp.getSndElement())) hasSndAcc = true;
					if(t.intValue() == v)
						break;
				}
				if(Options.verbose) System.out.println("hasAcc: " + (hasFstAcc && hasSndAcc) + "," + scc);
				mIsEmpty = ! (hasFstAcc && hasSndAcc);
				if(scc.cardinality() == 1 // only has a single state
						&& ! mIsEmpty     // it is an accepting states
						) {
					IStateWa s = mResult.getState(v);
					boolean hasSelfLoop = false;
					for(Integer letter : s.getEnabledLetters()) {
						if(s.getSuccessors(letter).get(v)) hasSelfLoop = true;
					}
					if(!hasSelfLoop) mIsEmpty = true;
				}
								
				if(!mIsEmpty) {
					mPrefix.addFirst(v);
					while(! mStack.empty()){
						Integer t = mStack.pop();
						mPrefix.addFirst(t);
					}
				}
				
//				if(! mIsEmpty) {
//					System.out.println("" + mPrefix);
//					System.out.println("" + mLoop);
//				}
			}
		}
		
	}


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "NCSB+Tarjan";
	}




}
