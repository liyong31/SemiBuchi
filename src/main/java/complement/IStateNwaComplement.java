package complement;

import automata.IBuchiNwa;
import automata.IStateNwa;

public interface IStateNwaComplement extends IStateNwa {
	
	IBuchiNwa getOperand();
	
	IBuchiNwa getComplement();
}
