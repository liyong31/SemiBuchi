package automata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.IntSet;
import util.UtilIntSet;

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

	@Override
	public IntSet getLabels(int state) {
		IntSet labels = UtilIntSet.newIntSet();
		if(mFinalStates.get(state)) {
			labels.set(0);
		}
		return labels;
	}

}
