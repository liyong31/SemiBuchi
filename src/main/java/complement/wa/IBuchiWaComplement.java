package complement.wa;


import automata.wa.IBuchiWa;

public interface IBuchiWaComplement extends IBuchiWa {
	IBuchiWa getOperand();
//	void useOpTransition(int letter, IntSet states);
//	int getNumUsedOpTransition();
}
