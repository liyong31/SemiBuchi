package operation.emptiness;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import automata.BuchiGeneral;
import automata.IBuchi;
import automata.IState;
import util.IPair;
import util.IntArray;
import util.IntStack;
import util.Timer;

//TODO check correctness, currently not correct
@Deprecated
public class BuchiIsEmptyTarjanVariant implements BuchiIsEmpty {
	
	private final IBuchi mBuchi;
	private int mDepth;
	private final IntArray mStateStack;
	private final IntStack mFinalStack;
	private final IntArray mStateMap;
	private final BitSet mTable;
	private final long TIME_LIMIT;
	private final Timer mTimer;
	private Boolean mIsEmpty = true;
	
	public BuchiIsEmptyTarjanVariant(IBuchi buchi, int timeLimit) {
		
		this.mBuchi = buchi;
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
		for(int n = mBuchi.getInitialStates().nextSetBit(0);
				n >= 0;
				n = mBuchi.getInitialStates().nextSetBit(n + 1)) {
			if(!mTable.get(n) && !terminate()){
				dfs(n);
				if(mIsEmpty == null || mIsEmpty.booleanValue() == false) return;
			}
		}
	}
	
	private boolean terminate() {
		if(mTimer.tick() > TIME_LIMIT) 
			return true;
		return false;
	}

	private void dfs(int n) {
		
		if(terminate()) {
			mIsEmpty = null;
			return ;
		}
		// TODO Auto-generated method stub
		mStateStack.set(mDepth, n);
		mStateMap.set(n, mDepth);
		
		if(mBuchi.isFinal(n)) mFinalStack.push(mDepth);
		++ mDepth;
		
		mTable.set(n);
		
		IState state = mBuchi.getState(n);
		//TODO only get enabled letters
		for(int letter = 0; letter < mBuchi.getAlphabetSize(); letter ++) {
			BitSet succs = state.getSuccessors(letter);
			for(int succ = succs.nextSetBit(0); succ >= 0; succ = succs.nextSetBit(succ + 1)) {
				if(! mTable.get(succ)) {
					dfs(succ);
					if(mIsEmpty == null || mIsEmpty.booleanValue() == false) return;
				}else if(mStateMap.isDefined(succ) && !mFinalStack.isEmpty() && mStateMap.get(succ) <= mFinalStack.peek()) {
					mIsEmpty = false;
					return ;
				}
			}
		}
		
		mStateMap.clear(n);
		mStateStack.pop();
		-- mDepth;
		
		if(!mFinalStack.isEmpty() && mFinalStack.peek() == mDepth) {
			mFinalStack.pop();
		}
	}
			

	@Override
	public Boolean isEmpty() {
		// TODO Auto-generated method stub
		return mIsEmpty;
	}

	@Override
	public IPair<List<Integer>, List<Integer>> getAcceptingWord() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) {
		IBuchi buchi = new BuchiGeneral(2);
		IState a = buchi.addState();
		IState b = buchi.addState();
		IState c = buchi.addState();
		IState d = buchi.addState();
		IState e = buchi.addState();
		
		a.addSuccessor(0, b.getId());
		b.addSuccessor(0, c.getId());
		
		c.addSuccessor(0, d.getId());
		d.addSuccessor(1, b.getId());
		
		b.addSuccessor(1, e.getId());
		
		e.addSuccessor(1, d.getId());
		
		buchi.setFinal(e);
		
		buchi.setInitial(a);
		
		System.out.println(buchi.toDot());
		
		BuchiIsEmptyTarjanVariant dfs = new BuchiIsEmptyTarjanVariant(buchi, 10*1000);
		System.out.println(dfs.isEmpty());
	}
			

}
