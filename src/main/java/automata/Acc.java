package automata;

import java.util.List;

import util.IntSet;

public interface Acc {
	
	boolean isAccepted(IntSet set);
	
	List<IntSet> getAccs();
	
	IntSet getLabels(int state);
	
	default int getAccSize() {
	    return getAccs().size();
	}
	
}
