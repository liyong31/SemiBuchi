package automata;

import java.util.BitSet;
import java.util.Set;

public interface IState {
	
	int getId();
		
	void addSuccessor(int letter, int state);
	
	BitSet getSuccessors(int letter);
	
	Set<Integer> getEnabledLetters();

	// ----- general requirements
	boolean equals(Object otherState);
	
	int hashCode();
	
	String toString();
}
