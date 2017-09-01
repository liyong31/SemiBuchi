package automata;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

import util.IntSet;

public interface IBuchi<S extends IState> {
	
	Acc getAcceptance();
	
	int getStateSize();
	
	S addState();
	
	S makeState(int id);
	
	int addState(S state);
	
	S getState(int id);
	
	IntSet getInitialStates();

	IntSet getFinalStates();
	
	
	default public boolean isInitial(S s) {
		return isInitial(s.getId());
	}
	
	boolean isInitial(int id);
	
	default public boolean isFinal(S s) {
		return isFinal(s.getId());
	}
	
	boolean isFinal(int id);
	
	default public void setInitial(S s) {
		setInitial(s.getId());
	}
	
	void setInitial(int id);
	
	default public void setFinal(S s) {
		setFinal(s.getId());
	}
	
	void setFinal(int id);
	
	Collection<S> getStates();
	
	void makeComplete();
		
	int getNumTransition();
	// printer
	
	String toDot();
	
	String toBA();
	
	void toATS(PrintStream out, List<String> alphabet);
	
	// a Buchi automaton is semideterministic 
	// if all transitions after the accepting states are deterministic
	boolean isSemiDeterministic();
	
	boolean isDeterministic(int state);

}
