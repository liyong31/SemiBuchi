package automata.wa;

import java.io.PrintStream;
import java.util.List;
import java.util.Set;

import automata.IState;
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
        	for(Integer succ : this.getSuccessors(letter).iterable()) {
        		printer.print(alphabet.get(letter) + ",[" + this.getId() + "]->[" + succ + "]\n");
        	}
		}
	}

	@Override
	default void toDot(PrintStream printer, List<String> alphabet) {
		Set<Integer> enabledLetters = this.getEnabledLetters();
		for(Integer letter : enabledLetters) {
    		for(Integer succ : this.getSuccessors(letter).iterable()) {
    			printer.print("  " + this.getId() + " -> " + succ + " [label=\"" + alphabet.get(letter) + "\"];\n");
    		}
        }
	}
}
