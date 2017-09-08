package automata;

import java.io.PrintStream;
import java.util.List;
import java.util.Set;

import util.IntIterator;
import util.IntSet;

/**
 * State interface of nested word automata
 * */
public interface IStateNwa extends IState {

	// setters
	void addSuccessorInternal(int letter, int state);
	
	void addSuccessorCall(int letter, int state);
	
	void addSuccessorReturn(int hier, int letter, int state);

	// getters
	IntSet getSuccessorsInternal(int letter);
	
	IntSet getSuccessorsCall(int letter);
	
	IntSet getSuccessorsReturn(int hier, int letter);
	
	Set<Integer> getEnabledLettersInternal();
	
	Set<Integer> getEnabledLettersCall();
	
	Set<Integer> getEnabledLettersReturn();
	
	Set<Integer> getEnabledHiersReturn(int letter);

}
