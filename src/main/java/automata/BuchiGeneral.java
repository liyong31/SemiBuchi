package automata;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BuchiGeneral implements IBuchi {

	private final BitSet mInitStates;
	
	private final BitSet mFinalStates;
	
	private final List<IState> mStates;
	
	private final int mAlphabetSize;
	
	public BuchiGeneral(int alphabetSize) {
		this.mAlphabetSize = alphabetSize;
		this.mInitStates  = new BitSet();
		this.mFinalStates = new BitSet();
		this.mStates = new ArrayList<>();
	}
	
	@Override
	public int getAlphabetSize() {
		// TODO Auto-generated method stub
		return mAlphabetSize;
	}

	@Override
	public IState addState() {
		int id = mStates.size();
		mStates.add(new StateGeneral(id));
		return mStates.get(id);
	}
	
	/** should keep it safe */
	@Override
	public int addState(IState state) {
		// TODO Auto-generated method stub
		int id = mStates.size();
		mStates.add(state);
		return id;
	}

	@Override
	public IState getState(int id) {
		// TODO Auto-generated method stub
		if(id < mStates.size()) {
			return mStates.get(id);
		}
		return null;
	}

	@Override
	public BitSet getInitialStates() {
		// TODO Auto-generated method stub
		return (BitSet) mInitStates.clone();
	}

	@Override
	public boolean isInitial(int id) {
		// TODO Auto-generated method stub
		return mInitStates.get(id);
	}

	@Override
	public boolean isFinal(int id) {
		// TODO Auto-generated method stub
		return mFinalStates.get(id);
	}

	@Override
	public void setInitial(int id) {
		// TODO Auto-generated method stub
		mInitStates.set(id);
	}

	@Override
	public void setFinal(int id) {
		// TODO Auto-generated method stub
		mFinalStates.set(id);
	}

	@Override
	public Collection<IState> getStates() {
		// TODO Auto-generated method stub
		return Collections.unmodifiableList(mStates);
	}

	@Override
	public BitSet getFinalStates() {
		// TODO Auto-generated method stub
		return (BitSet) mFinalStates.clone();
	}

	@Override
	public int getStateSize() {
		// TODO Auto-generated method stub
		return mStates.size();
	}
	
	public String toString() {
		return toDot();
	}

	@Override
	public BitSet getSuccessors(int state, int letter) {
		// TODO Auto-generated method stub
		return mStates.get(state).getSuccessors(letter);
	}



}
