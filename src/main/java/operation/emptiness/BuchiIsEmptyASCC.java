package operation.emptiness;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import automata.IBuchi;
import automata.IState;
import automata.wa.IBuchiWa;
import automata.wa.IStateWa;
import util.IPair;
import util.IntIterator;
import util.IntSet;
import util.MarkedIntStack;
import util.Timer;

// from paper Comparison of Algorithms for Checking Emptiness on B¨uchi Automata
// by Andreas Gaiser and Stefan Schwoon
public class BuchiIsEmptyASCC implements IBuchiWaIsEmpty {
	
	private final IBuchiWa mBuchi;
	private int mDepth;
	private final MarkedIntStack mRootsStack;             // C99 's root stack
	private final MarkedIntStack mActiveStack;            // tarjan's stack
	private final Map<Integer, Integer> mDfsNum;
	private final BitSet mCurrent;
	private final long TIME_LIMIT;
	private final Timer mTimer;
	private Boolean mIsEmpty = true;
	
	public BuchiIsEmptyASCC(IBuchiWa buchi, int timeLimit) {
		
		this.mBuchi = buchi;
		this.TIME_LIMIT = timeLimit;
		this.mRootsStack = new MarkedIntStack();
		this.mActiveStack = new MarkedIntStack();
		this.mDfsNum = new HashMap<>();
		this.mCurrent = new BitSet();
		this.mTimer = new Timer();
		mTimer.start();
		explore();
	}

	private void explore() {
		// TODO Auto-generated method stub
		mDepth = 0;
		IntIterator iter = mBuchi.getInitialStates().iterator();
		while(iter.hasNext()) {
			int n = iter.next();
			if(!mDfsNum.containsKey(n) && !terminate()){
				dfs(n);
				if(mIsEmpty == null || mIsEmpty.booleanValue() == false) return;
			}
		}
	}
	
	private boolean terminate() {
//		if(mTimer.tick() > TIME_LIMIT) 
//			return true;
		return false;
	}

	private void dfs(int n) {
		
		if(terminate()) {
			mIsEmpty = null;
			return ;
		}
		
		++ mDepth;
		mDfsNum.put(n, mDepth);
		mRootsStack.push(n);
		mActiveStack.push(n);
		mCurrent.set(n);
		
		IStateWa state = mBuchi.getState(n);
		for(int letter = 0; letter < mBuchi.getAlphabetSize(); letter ++) {
			for(int succ : state.getSuccessors(letter).iterable()) {
				if(! mDfsNum.containsKey(succ)) {
					dfs(succ);
				}else if(mCurrent.get(succ)) {
					// we have already seen it before, there is a loop
					while(true) {
						//pop element u
						int u = mRootsStack.pop();
						if(mBuchi.isFinal(u)) {
							mIsEmpty = false;
							return;
						}
						
						if(mDfsNum.get(u) <= mDfsNum.get(succ)) {
							mRootsStack.push(u); // push back
							break;
						}
					}
				}
			}
		}
		
		// if current number is done, 
		// then we should remove all 
		// active states in the same scc
		if(mRootsStack.peek() == n) {
			mRootsStack.pop();
			while(true) {
				int u = mActiveStack.pop(); // Tarjan' Stack
				mCurrent.clear(u);
				if(u == n) break;
			}
		}
	}
			

	@Override
	public Boolean getResult() {
		return mIsEmpty;
	}

	@Override
	public IPair<List<Integer>, List<Integer>> getAcceptingWord() {
		return null;
	}

	@Override
	public IBuchiWa getOperand() {
		return mBuchi;
	}

	@Override
	public String getOperationName() {
		return "EmptyASCC";
	}
			

}
