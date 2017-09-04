package automata;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import util.IntSet;
import util.UtilIntSet;


//TODO deal with automata with large alphabet
public class StateWa implements IStateWa, Comparable<StateWa> {

	private final int mId;
	private final Map<Integer, IntSet> mSuccessors;
	public StateWa(int id) {
		this.mId = id;
		this.mSuccessors = new HashMap<>();
	}
	
	@Override
	public int getId() {
		return mId;
	}

	@Override
	public void addSuccessor(int letter, int state) {
		IntSet succs = mSuccessors.get(letter);
		if(succs == null) {
			succs =  UtilIntSet.newIntSet();
		}
		succs.set(state);
		mSuccessors.put(letter, succs);
	}

	@Override
	public IntSet getSuccessors(int letter) {
		IntSet succs = mSuccessors.get(letter);
		if(succs == null) { // transition function may not be complete
			return UtilIntSet.newIntSet();
		}
		return succs.clone();
	}

	@Override
	public Set<Integer> getEnabledLetters() {
		return Collections.unmodifiableSet(mSuccessors.keySet());
	}

	@Override
	public int compareTo(StateWa other) {
		return mId - other.mId;
	}
	
	@Override
	public boolean equals(Object other) {
		if(this == other) return true;
		if(!(other instanceof StateWa)) {
			return false;
		}
		StateWa otherState = (StateWa)other;
		return otherState.mId == this.mId;
	}
	
	@Override
	public int hashCode() {
		return mId;
	}
	
	@Override
	public String toString() {
		return "s" + mId;
	}

}
