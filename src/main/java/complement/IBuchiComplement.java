package complement;

import automata.IBuchi;

public interface IBuchiComplement extends IBuchi {
	IBuchi getOperand();
}
