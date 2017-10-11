package automata.wa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import automata.Acc;
import automata.AccBuchi;
import util.IntSet;
import util.UtilIntSet;

public class BuchiWa implements IBuchiWa {

	private final IntSet mInitStates;
	
	private final IntSet mFinalStates;
	
	private final List<IStateWa> mStates;
	
	private final int mAlphabetSize;
		
	public BuchiWa(int alphabetSize) {
		this.mAlphabetSize = alphabetSize;
		this.mInitStates  = UtilIntSet.newIntSet();
		this.mFinalStates = UtilIntSet.newIntSet();
		this.mStates = new ArrayList<>();
	}
	
	@Override
	public int getAlphabetSize() {
		return mAlphabetSize;
	}

	@Override
	public IStateWa addState() {
		int id = mStates.size();
		mStates.add(makeState(id));
		return mStates.get(id);
	}
	
	@Override
	public IStateWa makeState(int id) {
		return new StateWa(id);
	}
	
	/** should keep it safe */
	@Override
	public int addState(IStateWa state) {
		int id = mStates.size();
		mStates.add(state);
		return id;
	}

	@Override
	public IStateWa getState(int id) {
		assert id < mStates.size();
		if(id < mStates.size()) {
			return mStates.get(id);
		}
		return null;
	}

	@Override
	public IntSet getInitialStates() {
		return mInitStates;
	}

	@Override
	public boolean isInitial(int id) {
		return mInitStates.get(id);
	}

	@Override
	public boolean isFinal(int id) {
		return mFinalStates.get(id);
	}

	@Override
	public void setInitial(int id) {
		mInitStates.set(id);
	}

	@Override
	public void setFinal(int id) {
		mFinalStates.set(id);
	}

	@Override
	public Collection<IStateWa> getStates() {
		return Collections.unmodifiableList(mStates);
	}

	@Override
	public IntSet getFinalStates() {
		return mFinalStates;
	}

	@Override
	public int getStateSize() {
		return mStates.size();
	}
	
	public String toString() {
		return toDot();
	}

	@Override
	public IntSet getSuccessors(int state, int letter) {
		return mStates.get(state).getSuccessors(letter);
	}

	protected Acc acc;
	
	@Override
	public Acc getAcceptance() {
		if(acc == null) {
			acc = new AccBuchi(mFinalStates);
		}
		return acc;
	}

	@Override
	public void makeComplete() {
		IStateWa deadState = addState();
		for(final IStateWa state : mStates) {
            for (int letter = 0; letter < getAlphabetSize(); letter ++) {
            	IntSet succs = state.getSuccessors(letter);
            	if(succs.cardinality() == 0) {
            		state.addSuccessor(letter, deadState.getId());
            	}
            }
        }
	}

}
