package complement;


import automata.IBuchiWa;
import util.IntSet;

public interface IBuchiWaComplement extends IBuchiWa {
	IBuchiWa getOperand();
	void useOpTransition(int letter, IntSet states);
	int getNumUsedOpTransition();
}
