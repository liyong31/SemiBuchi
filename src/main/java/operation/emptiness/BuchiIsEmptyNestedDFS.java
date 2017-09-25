package operation.emptiness;

import java.util.BitSet;
import java.util.List;

import automata.wa.BuchiWa;
import automata.wa.IBuchiWa;
import automata.wa.IStateWa;
import util.IPair;
import util.IntIterator;
import util.IntSet;
import util.MarkedIntStack;
import util.Timer;

public class BuchiIsEmptyNestedDFS implements IBuchiWaIsEmpty {

	private final IBuchiWa mBuchi;
	private final MarkedIntStack mFstStack;
	private final MarkedIntStack mSndStack;

	private final BitSet mFstTable;
	private final BitSet mSndTable;
	private Boolean mIsEmpty = true;
	private final long TIME_LIMIT;
	private Timer mTimer;

	public BuchiIsEmptyNestedDFS(IBuchiWa buchi, long timeLimit) {
		this.mBuchi = buchi;
		this.TIME_LIMIT = timeLimit;
		this.mFstStack = new MarkedIntStack();
		this.mSndStack = new MarkedIntStack();
		this.mFstTable = new BitSet();
		this.mSndTable = new BitSet();
		this.mTimer = new Timer();
		mTimer.start();
		explore();
	}

	private void explore() {
		// TODO Auto-generated method stub
		IntIterator iter = mBuchi.getInitialStates().iterator();
		while(iter.hasNext()) {
			int n = iter.next();
			if (!mFstTable.get(n) && !terminate()) {
				fstDFS(n);
				if (mIsEmpty == null || mIsEmpty.booleanValue() == false)
					return;
			}
		}

	}

	private boolean terminate() {
		if (mTimer.tick() > TIME_LIMIT)
			return true;
		return false;
	}

	private void fstDFS(int n) {

		if (terminate()) {
			mIsEmpty = null;
			return;
		}
		// TODO Auto-generated method stub
		mFstStack.push(n);
		mFstTable.set(n);

		IStateWa state = mBuchi.getState(n);
		// TODO only get enabled letters
		for (int letter = 0; letter < mBuchi.getAlphabetSize(); letter++) {
			IntSet succs = state.getSuccessors(letter);
			IntIterator iter = succs.iterator();
			while(iter.hasNext()) {
				int succ = iter.next();
				if (!mFstTable.get(succ)) {
					fstDFS(succ);
					if (mIsEmpty == null || mIsEmpty.booleanValue() == false)
						return;
				}
			}
		}

		// if it is final ,check whether it is in a loop
		if (mBuchi.isFinal(n)) {
			sndDFS(n);
		}

		mFstStack.pop();
	}

	private void sndDFS(int n) {

		if (terminate()) {
			mIsEmpty = null;
			return;
		}

		mSndStack.push(n);
		mSndTable.set(n);

		IStateWa state = mBuchi.getState(n);
		// TODO only get enabled letters
		for (int letter = 0; letter < mBuchi.getAlphabetSize(); letter++) {
			IntSet succs = state.getSuccessors(letter);
			IntIterator iter = succs.iterator();
			while(iter.hasNext()) {
				int succ = iter.next();
				// we visited it before
				if (mFstStack.contains(succ)) {
					mIsEmpty = false;
					return;
				} else if (!mSndTable.get(succ)) {
					sndDFS(succ);
					if (mIsEmpty == null || mIsEmpty.booleanValue() == false)
						return;
				}
			}
		}

		mSndStack.pop();

	}

	@Override
	public IPair<List<Integer>, List<Integer>> getAcceptingWord() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) {
		IBuchiWa buchi = new BuchiWa(2);
		IStateWa a = buchi.addState();
		IStateWa b = buchi.addState();
		IStateWa c = buchi.addState();
		IStateWa d = buchi.addState();
		IStateWa e = buchi.addState();
		
		a.addSuccessor(0, b.getId());
		b.addSuccessor(0, c.getId());
		
		c.addSuccessor(0, d.getId());
		d.addSuccessor(1, b.getId());
		
		b.addSuccessor(1, e.getId());
		
		e.addSuccessor(1, d.getId());
		
		buchi.setFinal(e);
		
		buchi.setInitial(a);
		
		System.out.println(buchi.toDot());
		
		BuchiIsEmptyNestedDFS dfs = new BuchiIsEmptyNestedDFS(buchi, 10*1000);
		System.out.println(dfs.getResult());
	}

	@Override
	public IBuchiWa getOperand() {
		// TODO Auto-generated method stub
		return mBuchi;
	}

	@Override
	public Boolean getResult() {
		return mIsEmpty;
	}

	@Override
	public String getOperationName() {
		return "EmptyNestedDFS";
	}
}
