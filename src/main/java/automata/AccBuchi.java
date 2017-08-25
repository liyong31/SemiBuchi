package automata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.IntSet;

public class AccBuchi implements Acc {

	private final IntSet mFinalStates;
	private final List<IntSet> mAccList;
	
	public AccBuchi(IntSet finalStates) {
		this.mFinalStates = finalStates;
		this.mAccList = new ArrayList<>();
		mAccList.add(mFinalStates);
	}
	
	@Override
	public boolean isAccepted(IntSet set) {
		return mFinalStates.overlap(set);
	}

	@Override
	public List<IntSet> getAccs() {
		return Collections.unmodifiableList(mAccList);
	}

}
