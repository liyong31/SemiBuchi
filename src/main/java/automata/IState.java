package automata;

import java.io.PrintStream;
import java.util.List;

// state interface requirements for (nested) word automata
public interface IState {

	// ----- general requirements
	int getId();

	boolean equals(Object otherState);
	
	int hashCode();
	
	String toString();
	
	void toDot(PrintStream printer, List<String> alphabet);
}
