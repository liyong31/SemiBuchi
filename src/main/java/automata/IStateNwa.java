package automata;

import java.util.Set;

import util.IntSet;

/**
 * State interface of nested word automata
 * */
public interface IStateNwa {
	
	int getId();

	// setters
	void addSuccessorInternal(int letter, int state);
	
	void addSuccessorCall(int letter, int state);
	
	void addSuccessorReturn(int letter, int state);

	// getters
	IntSet getSuccessorsInternal(int letter);
	
	IntSet getSuccessorsCall(int letter);
	
	IntSet getSuccessorsReturn(int letter);
	
	Set<Integer> getEnabledLettersInternal();
	
	Set<Integer> getEnabledLettersCall();
	
	Set<Integer> getEnabledLettersReturn();

	// ----- general requirements
	boolean equals(Object otherState);
	
	int hashCode();
	
	String toString();

}
