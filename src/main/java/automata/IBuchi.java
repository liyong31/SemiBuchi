package automata;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import util.IntIterator;
import util.IntSet;

/**
 * (generalized) Buchi automata
 * */
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
	
	int getAlphabetSize();
		
	int getNumTransition();
	// printer
	
	default public String toDot() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
        	List<String> alphabet = new ArrayList<>();
        	for(int i = 0; i < getAlphabetSize(); i ++) {
        		alphabet.add(i + "");
        	}
            toDot(new PrintStream(out), alphabet);
            return out.toString();
        } catch (Exception e) {
            return "ERROR";
        }
	}
	
	default void toDot(PrintStream out, List<String> alphabet) {
		
		// output automata in dot
		out.print("digraph {\n");
		Collection<S> states = getStates();
		for(S state : states) {
			IntSet labels = getAcceptance().getLabels(state.getId());
//			out.print("  " + state.getId() + " [label=\"" +  state.getId() + "\"");
			out.print("  " + state.getId());
            if(isFinal(state.getId())) out.print(" [label=\"" +  state.getId() + "\"" + ", shape = doublecircle");
            else if(! labels.isEmpty()) {
            	out.print(" [label=\"" +  state.getId() + " " +  labels + "\"" + ", shape = box");
            }else out.print(", shape = circle");
            
            out.print("];\n");
            state.toDot(out, alphabet);
        }	
		out.print("  " + states.size() + " [label=\"\", shape = plaintext];\n");
        for(final Integer init : getInitialStates().iterable()) {
        	out.print("  " + states.size() + " -> " + init + " [label=\"\"];\n");
        }
        
        out.print("}\n\n");

	}
	
	void toATS(PrintStream out, List<String> alphabet);
	
	// a Buchi automaton is semideterministic 
	// if all transitions after the accepting states are deterministic
	boolean isSemiDeterministic();
	
	boolean isDeterministic(int state);

}
