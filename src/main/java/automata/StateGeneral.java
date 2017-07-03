package automata;

import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
//TODO deal with automata with large alphabet
public class StateGeneral implements IState, Comparable<StateGeneral> {

	private final int mId;
	private final Map<Integer, BitSet> mSuccessors;
	public StateGeneral(int id) {
		this.mId = id;
		this.mSuccessors = new HashMap<>();
	}
	
	@Override
	public int getId() {
		return mId;
	}

	@Override
	public void addSuccessor(int letter, int state) {
		// TODO Auto-generated method stub
		BitSet succs = mSuccessors.get(letter);
		if(succs == null) {
			succs = new BitSet();
		}
		succs.set(state);
		mSuccessors.put(letter, succs);
	}

	@Override
	public BitSet getSuccessors(int letter) {
		// TODO Auto-generated method stub
		BitSet succs = mSuccessors.get(letter);
		if(succs == null) { // transition function may not be complete
			return new BitSet();
		}
		return (BitSet)succs.clone();
	}

	@Override
	public Set<Integer> getEnabledLetters() {
		// TODO Auto-generated method stub
		return Collections.unmodifiableSet(mSuccessors.keySet());
	}

	@Override
	public int compareTo(StateGeneral other) {
		// TODO Auto-generated method stub
		return mId - other.mId;
	}
	
	public boolean equals(Object other) {
		if(!(other instanceof StateGeneral)) {
			return false;
		}
		StateGeneral otherState = (StateGeneral)other;
		return otherState.mId == this.mId;
	}
	
	public int hashCode() {
		return mId;
	}
	
	public String toString() {
		return "s" + mId;
	}

}
