package automata;

import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.IntIterator;
import util.IntSet;
import util.UtilIntSet;

/**
 * State class for Buchi Nested Word Automata
 * */
public class StateNwa implements IStateNwa, Comparable<StateNwa> {
	
	private final IBuchiNwa mBuchi;
	private final int mId;
	
	private final Map<Integer, IntSet> mSuccessorsInternal;
	private final Map<Integer, IntSet> mSuccessorsCall;
	// letter * hier -> succ
	private final Map<Integer, Map<Integer, IntSet>> mSuccessorsReturn; 
	
	public StateNwa(IBuchiNwa buchi, int id) {
		this.mBuchi = buchi;
		this.mId = id;
		this.mSuccessorsCall = new HashMap<>();
		this.mSuccessorsInternal = new HashMap<>();
		this.mSuccessorsReturn = new HashMap<>();
	}

	@Override
	public int getId() {
		return mId;
	}
	
	private void addSuccessors(Map<Integer, IntSet> succMap, int letterOrHier, int state) {
		IntSet succs = succMap.get(letterOrHier);
		if(succs == null) {
			succs =  UtilIntSet.newIntSet();
		}
		succs.set(state);
		succMap.put(letterOrHier, succs);	
	}

	@Override
	public void addSuccessorInternal(int letter, int state) {
		assert mBuchi.getAlphabetInternal().get(letter);
		addSuccessors(mSuccessorsInternal, letter, state);
	}

	@Override
	public void addSuccessorCall(int letter, int state) {
		assert mBuchi.getAlphabetCall().get(letter);
		addSuccessors(mSuccessorsCall, letter, state);		
	}

	@Override
	public void addSuccessorReturn(int hier, int letter, int state) {
		assert mBuchi.getAlphabetReturn().get(letter);
		Map<Integer, IntSet> succMap = mSuccessorsReturn.get(letter);
		if(succMap == null) {
			succMap = new HashMap<>();
		}
		addSuccessors(succMap, hier, state);
		mSuccessorsReturn.put(letter, succMap);
	}

	private IntSet getSuccessors(Map<Integer, IntSet> succMap, int letter) {
		IntSet succs = succMap.get(letter);
		if(succs == null) { // transition function may not be complete
			return UtilIntSet.newIntSet();
		}
		return succs.clone();
	}
	
	@Override
	public IntSet getSuccessorsInternal(int letter) {
		assert mBuchi.getAlphabetInternal().get(letter);
		return getSuccessors(mSuccessorsInternal, letter);
	}

	@Override
	public IntSet getSuccessorsCall(int letter) {
		assert mBuchi.getAlphabetCall().get(letter);
		return getSuccessors(mSuccessorsCall, letter);
	}

	@Override
	public IntSet getSuccessorsReturn(int hier, int letter) {
		assert mBuchi.getAlphabetReturn().get(letter);
		Map<Integer, IntSet> succMap = mSuccessorsReturn.get(letter);
		if(succMap == null) {
			return UtilIntSet.newIntSet();
		}
		return getSuccessors(succMap, hier);
	}

	@Override
	public Set<Integer> getEnabledLettersInternal() {
		return mSuccessorsInternal.keySet();
	}

	@Override
	public Set<Integer> getEnabledLettersCall() {
		return mSuccessorsCall.keySet();
	}

	@Override
	public Set<Integer> getEnabledLettersReturn() {
		return mSuccessorsReturn.keySet();
	}

	@Override
	public Set<Integer> getEnabledHiersReturn(int letter) {
		Map<Integer, IntSet> succMap = mSuccessorsReturn.get(letter);
		if(succMap ==null) {
			return Collections.emptySet();
		}
		return succMap.keySet();
	}
	
	@Override
	public int compareTo(StateNwa other) {
		return mId - other.mId;
	}
	
	@Override
	public boolean equals(Object other) {
		if(this == other) return true;
		if(!(other instanceof StateNwa)) {
			return false;
		}
		
		StateNwa otherState = (StateNwa)other;
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
	

	@Override
	public void toDot(PrintStream printer, List<String> alphabet) {
		Set<Integer> callLetters = this.getEnabledLettersCall();
		for(Integer letter : callLetters) {
        	IntSet succs = this.getSuccessorsCall(letter);
    		transToDot(printer, alphabet, succs, alphabet.get(letter) + "<");
        }
		
		Set<Integer> internalLetters = this.getEnabledLettersInternal();
		for(Integer letter : internalLetters) {
        	IntSet succs = this.getSuccessorsInternal(letter);
    		transToDot(printer, alphabet, succs, alphabet.get(letter).toString());
        }
		
		Set<Integer> returnLetters = this.getEnabledLettersReturn();
		for(Integer letter : returnLetters) {
			Set<Integer> predHiers = this.getEnabledHiersReturn(letter);
			for(Integer predHier : predHiers) {
	        	IntSet succs = this.getSuccessorsReturn(predHier, letter);
	    		transToDot(printer, alphabet, succs, predHier + ",>" + alphabet.get(letter));
			}
        }
	}
	
	private void transToDot(PrintStream printer, List<String> alphabet, IntSet succs, String letter) {
		IntIterator iter = succs.iterator();
		while(iter.hasNext()) {
			int succ = iter.next();
			printer.print("  " + this.getId() + " -> " + succ + " [label=\"" + letter.replaceAll("\"", "") + "\"];\n");
		}
	}

}
