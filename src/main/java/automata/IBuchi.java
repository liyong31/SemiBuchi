package automata;

import java.util.BitSet;
import java.util.Collection;
import java.util.LinkedList;

public interface IBuchi {
	
	int getAlphabetSize();
	
	int getStateSize();
	
	IState addState();
	
	int addState(IState state);
	
	IState getState(int id);
	
	BitSet getInitialStates();

	BitSet getFinalStates();
	
	
	default public boolean isInitial(IState s) {
		return isInitial(s.getId());
	}
	
	boolean isInitial(int id);
	
	default public boolean isFinal(IState s) {
		return isFinal(s.getId());
	}
	
	boolean isFinal(int id);
	
	default public void setInitial(IState s) {
		setInitial(s.getId());
	}
	
	void setInitial(int id);
	
	default public void setFinal(IState s) {
		setFinal(s.getId());
	}
	
	void setFinal(int id);
	
	default public BitSet getSuccessors(BitSet states, int letter) {
		BitSet result = new BitSet();
		for(int n = states.nextSetBit(0); n >= 0; n = states.nextSetBit(n + 1)) {
			result.or(getSuccessors(n, letter));
		}
		return result;
	}
	
	BitSet getSuccessors(int state, int letter);
	
	Collection<IState> getStates();
	
	default public String toDot() {
		
		StringBuilder sb = new StringBuilder();
		
		// output automata in dot
		sb.append("digraph {\n");
		Collection<IState> states = getStates();
		for(IState state : states) {
			sb.append("  " + state.getId() + " [label=\"" +  state.getId() + "\"");
            if(isFinal(state.getId())) sb.append(", shape = doublecircle");
            else sb.append(", shape = circle");
            sb.append("];\n");
            for (int letter = 0; letter < getAlphabetSize(); letter ++) {
            	BitSet succs = state.getSuccessors(letter);
            	for(int succ = succs.nextSetBit(0); succ >= 0; succ = succs.nextSetBit(succ + 1)) {
            		sb.append("  " + state.getId() + " -> " + succ + " [label=\"" + letter + "\"];\n");
            	}
            }
        }	
        sb.append("  " + states.size() + " [label=\"\", shape = plaintext];\n");
        BitSet initialStates = getInitialStates();
        for(int init = initialStates.nextSetBit(0); init >= 0; init = initialStates.nextSetBit(init + 1)) {
        	sb.append("  " + states.size() + " -> " + init + " [label=\"\"];\n");
        }
        
        sb.append("}\n\n");
		return sb.toString();
	}
	
	// RABIT format
	default public String toBA() {
		
		StringBuilder sb = new StringBuilder();
        BitSet initialStates = getInitialStates();
        if(initialStates.cardinality() > 1) 
        	throw new RuntimeException("BA format does not allow multiple initial states...");
        
        sb.append("[" + initialStates.nextSetBit(0) + "]\n");
		// output automata in BA (RABIT format)
		Collection<IState> states = getStates();
		for(IState state : states) {
            for (int letter = 0; letter < getAlphabetSize(); letter ++) {
            	BitSet succs = state.getSuccessors(letter);
            	for(int succ = succs.nextSetBit(0); succ >= 0; succ = succs.nextSetBit(succ + 1)) {
            		sb.append("a" + letter + ",[" + state.getId() + "]->[" + succ + "]\n");
            	}
            }
        }	
        BitSet finStates = getFinalStates();
        for(int fin = finStates.nextSetBit(0); fin >= 0; fin = finStates.nextSetBit(fin + 1)) {
        	sb.append("[" + fin + "]\n");
        }
        
		return sb.toString();
	}
	
	// a Buchi automaton is semideterministic if all transitions after the accepting states are deterministic
	default boolean isSemiDeterministic() {
		BitSet finIds = getFinalStates();
		LinkedList<IState> walkList = new LinkedList<>();
		
		// add to list
		for(int i = finIds.nextSetBit(0); i >= 0; i = finIds.nextSetBit(i + 1)) {
			walkList.addFirst(getState(i));
		}
		
        BitSet visited = new BitSet();
        while(! walkList.isEmpty()) {
        	IState s = walkList.remove();
        	visited.set(s.getId());
        	for(int i = 0; i < getAlphabetSize(); i ++) {
        		BitSet succs = s.getSuccessors(i);
        		if(succs.cardinality() > 1) return false;
        		if(succs.isEmpty()) continue;
        		int succ = succs.nextSetBit(0);
    			if(! visited.get(succ)) {
    				walkList.addFirst(getState(succ));
    			}
        	}
        }
        
        return true;
	}
	
	default boolean isDeterministic(int state) {
		LinkedList<IState> walkList = new LinkedList<>();
		
		walkList.addFirst(getState(state));
		
        BitSet visited = new BitSet();
        while(! walkList.isEmpty()) {
        	IState s = walkList.remove();
        	visited.set(s.getId());
        	for(int i = 0; i < getAlphabetSize(); i ++) {
        		BitSet succs = s.getSuccessors(i);
        		if(succs.cardinality() > 1) return false;
        		if(succs.isEmpty()) continue;
        		int succ = succs.nextSetBit(0);
    			if(! visited.get(succ)) {
    				walkList.addFirst(getState(succ));
    			}
        	}
        }
        
        return true;
	}


}
