package operation.universality;

import java.util.BitSet;
import java.util.List;

import automata.IBuchi;
import automata.IBuchiWa;
import automata.IState;
import automata.IStateWa;
import operation.emptiness.IBuchiWaIsEmpty;
import util.IPair;
import util.IntArray;
import util.IntIterator;
import util.IntSet;
import util.MarkedIntStack;
import util.Timer;

public class BuchiUniversalityTarjanAntichain extends BuchiUniversality {

	public BuchiUniversalityTarjanAntichain(IBuchiWa buchi) {
		super(buchi);
	}
	
	// ------------- detection
	
	private class Tarjan implements IBuchiWaIsEmpty{
		private int mDepth;
		private final IntArray mStateStack;
		private final MarkedIntStack mFinalStack;
		private final IntArray mStateMap;
		private final BitSet mTable;
		private final long TIME_LIMIT;
		private final Timer mTimer;
		private Boolean mIsEmpty = true;
		
		Tarjan(int timeLimit) {
			this.TIME_LIMIT = timeLimit;
			this.mFinalStack = new MarkedIntStack();
			this.mStateStack = new IntArray();
			this.mStateMap = new IntArray();
			this.mTable = new BitSet();
			this.mTimer = new Timer();
			mTimer.start();
			explore();
		}

		private void explore() {
			// TODO Auto-generated method stub
			mDepth = 0;
			IntIterator iter = mBuchiComplement.getInitialStates().iterator();
			while(iter.hasNext()) {
			    int n = iter.next();
					
				if(!mTable.get(n) && !terminate()){
					dfs(n, false);
					if(mIsEmpty == null || mIsEmpty.booleanValue() == false) return;
				}
			}
		}
		
		private boolean terminate() {
			if(mTimer.tick() > TIME_LIMIT) 
				return true;
			return false;
		}

		private void dfs(int n, boolean acc) {
			
			if(terminate()) {
				mIsEmpty = null;
				return ;
			}
			// TODO Auto-generated method stub
			mStateStack.set(mDepth, n);
			
			if(mBuchiComplement.isFinal(n)) {
				if(acc) {
					mIsEmpty = false;
				}
				mFinalStack.push(mDepth);
			}
			++ mDepth;
			
			mTable.set(n);
			
			IStateWa state = mBuchiComplement.getState(n);
			//TODO only get enabled letters
			for(int letter = 0; letter < mBuchiComplement.getAlphabetSize(); letter ++) {
				IntSet succs = state.getSuccessors(letter);

			}
			
			mStateMap.clear(n);
			mStateStack.pop();
			-- mDepth;
			
			if(mFinalStack.peek() == mDepth) {
				mFinalStack.pop();
			}
		}

		@Override
		public IBuchiWa getOperand() {
			return mBuchiComplement;
		}

		@Override
		public Boolean getResult() {
			return mIsEmpty;
		}

		@Override
		public IPair<List<Integer>, List<Integer>> getAcceptingWord() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getOperationName() {
			return "EmptyTarjanAntichain";
		}
				
		
	}

	@Override
	protected void initializeEmptinessChecker() {
		this.mEmptinessChecker = new Tarjan(10 * 1000);
	}

	@Override
	public String getOperationName() {
		return "UniversalityTarjanAntichain";
	}
}
