package operation.universality;

import java.util.BitSet;
import java.util.List;

import automata.IBuchi;
import automata.IState;
import util.IPair;
import util.IntArray;
import util.IntStack;
import util.Timer;

public class BuchiUniversalityTarjanAntichain extends BuchiUniversality {

	public BuchiUniversalityTarjanAntichain(IBuchi buchi) {
		super(buchi);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Boolean isUniversal() {
		// TODO Auto-generated method stub
		Tarjan tarjan = new Tarjan(10 * 1000);
		return tarjan.mIsEmpty;
	}

	@Override
	public IPair<List<Integer>, List<Integer>> getCounterexampleWord() {
		// TODO Auto-generated method stub
		return null;
	}
	
	// ------------- detection
	
	private class Tarjan {
		private int mDepth;
		private final IntArray mStateStack;
		private final IntStack mFinalStack;
		private final IntArray mStateMap;
		private final BitSet mTable;
		private final long TIME_LIMIT;
		private final Timer mTimer;
		private Boolean mIsEmpty = true;
		
		Tarjan(int timeLimit) {
			this.TIME_LIMIT = timeLimit;
			this.mFinalStack = new IntStack();
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
			for(int n = mBuchiComplement.getInitialStates().nextSetBit(0);
					n >= 0;
					n = mBuchiComplement.getInitialStates().nextSetBit(n + 1)) {
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
			
			IState state = mBuchiComplement.getState(n);
			//TODO only get enabled letters
			for(int letter = 0; letter < mBuchiComplement.getAlphabetSize(); letter ++) {
				BitSet succs = state.getSuccessors(letter);
				for(int succ = succs.nextSetBit(0); succ >= 0; succ = succs.nextSetBit(succ + 1)) {

				}
			}
			
			mStateMap.clear(n);
			mStateStack.pop();
			-- mDepth;
			
			if(mFinalStack.peek() == mDepth) {
				mFinalStack.pop();
			}
		}
				
		
	}

}
