package complement;

import java.util.BitSet;

import automata.IBuchi;

public interface IBuchiComplement extends IBuchi {
	IBuchi getOperand();
	void useOpTransition(int letter, BitSet states);
	int getNumUsedOpTransition();
}
