package automata;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import util.IntSet;
import util.UtilIntSet;

public class StateNwa implements IStateNwa {
	
	private final IBuchiNwa mBuchi;
	private final int mId;
	
	private final Map<Integer, IntSet> mSuccessorsInternal;
	private final Map<Integer, IntSet> mSuccessorsCall;
	private final Map<Integer, IntSet> mSuccessorsReturn;
	
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
	
	private void addSuccessors(Map<Integer, IntSet> succMap, int letter, int state) {
		IntSet succs = succMap.get(letter);
		if(succs == null) {
			succs =  UtilIntSet.newIntSet();
		}
		succs.set(state);
		succMap.put(letter, succs);	
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
	public void addSuccessorReturn(int letter, int state) {
		assert mBuchi.getAlphabetReturn().get(letter);
		addSuccessors(mSuccessorsReturn, letter, state);
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
	public IntSet getSuccessorsReturn(int letter) {
		assert mBuchi.getAlphabetReturn().get(letter);
		return getSuccessors(mSuccessorsReturn, letter);
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

}
