package automata;

import java.util.Set;

import util.IntSet;

/**
 * State interface of word automata
 * */
public interface IStateWa extends IState {
		
	void addSuccessor(int letter, int state);
	
	IntSet getSuccessors(int letter);
	
	Set<Integer> getEnabledLetters();
}
