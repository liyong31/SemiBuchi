package automata;

import java.io.PrintStream;
import java.util.List;
import java.util.Set;

import util.IntIterator;
import util.IntSet;

/**
 * State interface of word automata
 * */
public interface IStateWa extends IState {
		
	void addSuccessor(int letter, int state);
	
	IntSet getSuccessors(int letter);
	
	Set<Integer> getEnabledLetters();

	default void toBA(PrintStream printer, List<String> alphabet) {
		Set<Integer> enabledLetters = this.getEnabledLetters();
		for(Integer letter : enabledLetters) {
         	IntSet succs = this.getSuccessors(letter);
        	IntIterator iter = succs.iterator();
        	while(iter.hasNext()) {
        		int succ = iter.next();
        		printer.print(alphabet.get(letter) + ",[" + this.getId() + "]->[" + succ + "]\n");
        	}
		}
	}

	@Override
	default void toDot(PrintStream printer, List<String> alphabet) {
		Set<Integer> enabledLetters = this.getEnabledLetters();
		for(Integer letter : enabledLetters) {
        	IntSet succs = this.getSuccessors(letter);
    		IntIterator iter = succs.iterator();
    		while(iter.hasNext()) {
    			int succ = iter.next();
    			printer.print("  " + this.getId() + " -> " + succ + " [label=\"" + alphabet.get(letter) + "\"];\n");
    		}
        }
	}
}
