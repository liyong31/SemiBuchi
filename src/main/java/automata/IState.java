package automata;

// state interface requirements for (nested) word automata
public interface IState {

	// ----- general requirements
	int getId();

	boolean equals(Object otherState);
	
	int hashCode();
	
	String toString();
}
