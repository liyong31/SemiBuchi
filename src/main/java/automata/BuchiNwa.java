package automata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import util.IntSet;
import util.UtilIntSet;

public class BuchiNwa implements IBuchiNwa {
	
	private final IntSet mInitialStates;
	private final IntSet mFinalStates;
	private final List<IStateNwa> mStates;
	private final IntSet mCallAlphabet;
	private final IntSet mInternalAlphabet;
	private final IntSet mReturnAlphabet;
	
	
	public BuchiNwa(IntSet aCall, IntSet aInternal, IntSet aReturn) {
		this.mInitialStates = UtilIntSet.newIntSet();
		this.mFinalStates = UtilIntSet.newIntSet();
		this.mStates = new ArrayList<>();
		this.mCallAlphabet = aCall.clone();
		this.mInternalAlphabet = aInternal.clone();
		this.mReturnAlphabet = aReturn.clone();
	}

	@Override
	public Acc getAcceptance() {
		return new AccBuchi(mFinalStates);
	}

	@Override
	public IntSet getAlphabetInternal() {
		return mInternalAlphabet;
	}

	@Override
	public IntSet getAlphabetCall() {
		return mCallAlphabet;
	}

	@Override
	public IntSet getAlphabetReturn() {
		return mReturnAlphabet;
	}

	@Override
	public int getStateSize() {
		return mStates.size();
	}

	@Override
	public IStateNwa addState() {
		int id = mStates.size();
		IStateNwa state = makeState(id);
		mStates.add(state);
		assert state == mStates.get(id);
		return state;
	}

	@Override
	public int addState(IStateNwa state) {
		int id = mStates.size();
		mStates.add(state);
		return id;
	}

	@Override
	public IStateNwa getState(int id) {
		if(id < mStates.size()) {
			return mStates.get(id);
		}
		return null;
	}

	@Override
	public IntSet getInitialStates() {
		return mInitialStates;
	}

	@Override
	public IntSet getFinalStates() {
		return mFinalStates;
	}

	@Override
	public boolean isInitial(int id) {
		return mInitialStates.get(id);
	}

	@Override
	public boolean isFinal(int id) {
		return mFinalStates.get(id);
	}

	@Override
	public void setInitial(int id) {
		mInitialStates.set(id);
	}

	@Override
	public void setFinal(int id) {
		mFinalStates.set(id);
	}

	@Override
	public IntSet getSuccessorsInternal(int state, int letter) {
		assert state < mStates.size();
		return getState(state).getSuccessorsInternal(letter);
	}

	@Override
	public IntSet getSuccessorsCall(int state, int letter) {
		assert state < mStates.size();
		return getState(state).getSuccessorsCall(letter);
	}

	@Override
	public IntSet getSuccessorsReturn(int state, int pred, int letter) {
		assert state < mStates.size();
		return getState(state).getSuccessorsReturn(pred, letter);
	}

	@Override
	public Collection<IStateNwa> getStates() {
		return Collections.unmodifiableList(mStates);
	}

	@Override
	public IStateNwa makeState(int id) {
		return new StateNwa(this, id);
	}

}
