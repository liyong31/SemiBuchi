package automata.nwa;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import automata.IBuchi;
import util.IntSet;
import util.UtilIntSet;

/**
 * Buchi nested word automata 
 * */
public interface IBuchiNwa extends IBuchi<IStateNwa> {
	
	IntSet getAlphabetInternal();
	
	IntSet getAlphabetCall();
	
	IntSet getAlphabetReturn();
	
	// get nested alphabet size
	default public int getAlphabetSize() {
		return getAlphabetInternal().cardinality() + getAlphabetCall().cardinality() + getAlphabetReturn().cardinality();
	}
	
	// should use functional programming to following three 
	default public IntSet getSuccessorsInternal(IntSet states, int letter) {
		assert getAlphabetInternal().get(letter);
		IntSet result = UtilIntSet.newIntSet();
		for(final Integer state : states.iterable()) {
			result.or(getSuccessorsInternal(state, letter));
		}
		return result;
	}
	
	default public IntSet getSuccessorsCall(IntSet states, int letter) {
		assert getAlphabetCall().get(letter);
		IntSet result = UtilIntSet.newIntSet();
		for(final Integer state : states.iterable()) {
			result.or(getSuccessorsCall(state, letter));
		}
		return result;
	}
	
	default public IntSet getSuccessorsReturn(IntSet states, int letter) {
		assert getAlphabetReturn().get(letter);
		IntSet result = UtilIntSet.newIntSet();
		for(final Integer state : states.iterable()) {
			Set<Integer> enabledHiers = getState(state).getEnabledHiersReturn(letter);
			for(Integer hier : enabledHiers) {
				result.or(getState(state).getSuccessorsReturn(hier, letter));
			}
		}
		return result;
	}
	
	IntSet getSuccessorsInternal(int state, int letter);

	IntSet getSuccessorsCall(int state, int letter);

	IntSet getSuccessorsReturn(int state, int hier, int letter);	
	
	default public void toATS(PrintStream out, List<String> alphabet) {
		final String PRE_BLANK = "   "; 
		final String ITEM_BLANK = " ";
		final String LINE_END = "},";
		final String BLOCK_END = "\n" + PRE_BLANK + "},";
		final String TRANS_PRE_BLANK = PRE_BLANK + "   "; 
		out.println("NestedWordAutomaton result = (");
        
        out.print(PRE_BLANK + "callAlphabet = {");
        for(final Integer id : getAlphabetCall().iterable()) {
        	out.print(alphabet.get(id) + ITEM_BLANK);
        }
        out.println(LINE_END);
        
        out.print(PRE_BLANK + "internalAlphabet = {");
        for(final Integer id : getAlphabetInternal().iterable()) {
        	out.print(alphabet.get(id) + ITEM_BLANK);
        }
        out.println(LINE_END);
        
        out.print(PRE_BLANK + "returnAlphabet = {");
        for(final Integer id : getAlphabetReturn().iterable()) {
        	out.print(alphabet.get(id) + ITEM_BLANK);
        }
        out.println(LINE_END);
        
        // states
		Collection<IStateNwa> states = getStates();
		out.print(PRE_BLANK + "states = {");
		for(IStateNwa state : states) {
			out.print("s" + state.getId() + ITEM_BLANK);
        }	
        out.println(LINE_END);
        // initial states
        out.print(PRE_BLANK + "initialStates = {");
        for(final Integer id : getInitialStates().iterable()) {
        	out.print("s" + id + ITEM_BLANK);
        }
        out.println(LINE_END);
        
        // final states
        out.print(PRE_BLANK + "finalStates = {");
        for(final Integer id : getFinalStates().iterable()) {
        	out.print("s" + id + ITEM_BLANK);
        }
        out.println(LINE_END);
        
        // call transitions
        out.print(PRE_BLANK + "callTransitions = {");
		for(IStateNwa state : states) {
			for(final Integer letter : state.getEnabledLettersCall()) {
            	for(final Integer succ : state.getSuccessorsCall(letter).iterable()) {
            		out.print("\n" + TRANS_PRE_BLANK + "(s" + state.getId() + " " + alphabet.get(letter) + " s" + succ + ")" );
            	}
            }
        }
		out.println(BLOCK_END);
        
        // internal transitions
        out.print(PRE_BLANK + "internalTransitions = {");
		for(IStateNwa state : states) {
			for(final Integer letter : state.getEnabledLettersInternal()) {
            	for(final Integer succ : state.getSuccessorsInternal(letter).iterable()) {
            		out.print("\n" + TRANS_PRE_BLANK + "(s" + state.getId() + " " + alphabet.get(letter) + " s" + succ + ")" );
            	}
            }
        }
		out.println(BLOCK_END);
		
        // return transitions
        out.print(PRE_BLANK + "returnTransitions = {");
		for(IStateNwa state : states) {
			for(final Integer letter : state.getEnabledLettersReturn()) {
            	Set<Integer> enabledHiers = state.getEnabledHiersReturn(letter);
            	for(Integer hier : enabledHiers) {
            		if(hier < 0) continue;
                	for(Integer succ : state.getSuccessorsReturn(hier, letter).iterable()) {
                		out.print("\n" + TRANS_PRE_BLANK + "(s" + state.getId() + " s" + hier + " " + alphabet.get(letter) + " s" + succ + ")" );
                	}
            	}
            }
        }
		out.println("\n" + PRE_BLANK + "}");
		
		out.println(");");
	}
	
	
	default int getNumTransition() {
		int num = 0;
		for(IStateNwa s : getStates()) {
			// call 
			for(Integer letter : s.getEnabledLettersCall()) {
				num += s.getSuccessorsCall(letter).cardinality();
			}
			// internal 
			for(Integer letter : s.getEnabledLettersInternal()) {
				num += s.getSuccessorsInternal(letter).cardinality();
			}
			// return 
			for(Integer letter : s.getEnabledLettersReturn()) {
				for(Integer hier : s.getEnabledHiersReturn(letter)) {
					num += s.getSuccessorsReturn(hier, letter).cardinality();	
				}
			}
		}
		return num;
	}
	
	@Override
	default public void makeComplete() {
		assert false : "unsupported function in nested word automata" ;
	}

	@Override
	default public boolean isSemiDeterministic() {
		assert false : "unsupported function in nested word automata" ;
		return false;
	}

	@Override
	default public boolean isDeterministic(int state) {
		assert false : "unsupported function in nested word automata" ;
		return false;
	}
	
	

}
