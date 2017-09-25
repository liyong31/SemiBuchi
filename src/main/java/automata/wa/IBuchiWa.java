package automata.wa;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import automata.IBuchi;
import util.IntIterator;
import util.IntSet;
import util.UtilIntSet;
/**
 * Buchi word automata
 * */
public interface IBuchiWa extends IBuchi<IStateWa> {
	
	default public IntSet getSuccessors(IntSet states, int letter) {
		IntSet result = UtilIntSet.newIntSet();
		IntIterator iter = states.iterator();
		while(iter.hasNext()) {
			int n = iter.next();
			result.or(getSuccessors(n, letter));
		}
		return result;
	}
	
	IntSet getSuccessors(int state, int letter);
		
	void makeComplete();
	
	// use this function if automtaton is too large 
	default public void toBA(PrintStream out, List<String> alphabet) {
        IntSet initialStates = getInitialStates();
        if(initialStates.cardinality() > 1) 
        	throw new RuntimeException("BA format does not allow multiple initial states...");
        IntIterator iter = initialStates.iterator();
        out.print("[" + iter.next() + "]\n");
		// output automata in BA (RABIT format)
		Collection<IStateWa> states = getStates();
		for(IStateWa state : states) {
            state.toBA(out, alphabet);
        }	
        IntSet finStates = getFinalStates();
        iter = finStates.iterator();
        while(iter.hasNext()) {
        	out.print("[" + iter.next() + "]\n");
        }
	}
	
	default public String toBA() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
        	List<String> alphabet = new ArrayList<>();
        	for(int i = 0; i < getAlphabetSize(); i ++) {
        		alphabet.add(i + "");
        	}
            toBA(new PrintStream(out), alphabet);
            return out.toString();
        } catch (Exception e) {
            return "ERROR";
        }
	}
	
	default int getNumTransition() {
		int num = 0;
		for(IStateWa s : getStates()) {
			for(Integer letter : s.getEnabledLetters()) {
				num += s.getSuccessors(letter).cardinality();
			}
		}
		return num;
	}
	
	default public void toATS(PrintStream out, List<String> alphabet) {
		final String PRE_BLANK = "   "; 
		final String ITEM_BLANK = " ";
		final String LINE_END = "},";
		final String BLOCK_END = "\n" + PRE_BLANK + "}";
		final String TRANS_PRE_BLANK = PRE_BLANK + "   "; 
		out.println("FiniteAutomaton result = (");
		
        
        out.print(PRE_BLANK + "alphabet = {");
        for(int i = 0; i < this.getAlphabetSize(); i ++) {
        	out.print(alphabet.get(i) + ITEM_BLANK);
        }
        out.println(LINE_END);
        
        // states
		Collection<IStateWa> states = getStates();
		out.print(PRE_BLANK + "states = {");
		for(IStateWa state : states) {
			out.print("s" + state.getId() + ITEM_BLANK);
        }	
        out.println(LINE_END);
        // initial states
        IntSet initialStates = getInitialStates();
        IntIterator iter = initialStates.iterator();
        out.print(PRE_BLANK + "initialStates = {");
        while(iter.hasNext()) {
        	int id = iter.next();
        	out.print("s" + id + ITEM_BLANK);
        }
        out.println(LINE_END);
        
        // final states
        IntSet finalStates = getFinalStates();
        iter = finalStates.iterator();
        out.print(PRE_BLANK + "finalStates = {");
        while(iter.hasNext()) {
        	int id = iter.next();
        	out.print("s" + id + ITEM_BLANK);
        }
        out.println(LINE_END);
        
        // call transitions
        out.print(PRE_BLANK + "transitions = {");
		for(IStateWa state : states) {
			Set<Integer> letters = state.getEnabledLetters();
			for(Integer letter : letters) {
				IntSet succs = state.getSuccessors(letter);
            	IntIterator iterInner = succs.iterator();
            	while(iterInner.hasNext()) {
            		int succ = iterInner.next();
            		out.print("\n" + TRANS_PRE_BLANK + "(s" + state.getId() + " " + alphabet.get(letter) + " s" + succ + ")" );
            	}
            }
        }
		out.println(BLOCK_END);
       		
		out.println(");");
	}
	
	// a Buchi automaton is semideterministic if all transitions after the accepting states are deterministic
	default boolean isSemiDeterministic() {
		IntSet finIds = getFinalStates();
		LinkedList<IStateWa> walkList = new LinkedList<>();
		
		// add to list
		IntIterator iter = finIds.iterator();
		while(iter.hasNext()) {
			walkList.addFirst(getState(iter.next()));
		}
		
        IntSet visited = UtilIntSet.newIntSet();
        while(! walkList.isEmpty()) {
        	IStateWa s = walkList.remove();
        	if(visited.get(s.getId())) continue;
        	visited.set(s.getId());
        	for(int i = 0; i < getAlphabetSize(); i ++) {
        		IntSet succs = s.getSuccessors(i);
        		if(succs.isEmpty()) continue;
        		if(succs.cardinality() > 1) return false;

        		iter = succs.iterator();
        		int succ = iter.next();
    			if(! visited.get(succ)) {
    				walkList.addFirst(getState(succ));
    			}
        	}
        }
        
        return true;
	}
	
	default boolean isDeterministic(int state) {
		LinkedList<IStateWa> walkList = new LinkedList<>();
		
		walkList.addFirst(getState(state));
		
        IntSet visited = UtilIntSet.newIntSet();
        while(! walkList.isEmpty()) {
        	IStateWa s = walkList.remove();
        	if(visited.get(s.getId())) continue;
        	visited.set(s.getId());
        	for(int i = 0; i < getAlphabetSize(); i ++) {
        		IntSet succs = s.getSuccessors(i);
        		if(succs.cardinality() > 1) return false;
        		if(succs.isEmpty()) continue;
        		IntIterator iter = succs.iterator();
        		int succ = iter.next();
    			if(! visited.get(succ)) {
    				walkList.addFirst(getState(succ));
    			}
        	}
        }
        
        return true;
	}


}
