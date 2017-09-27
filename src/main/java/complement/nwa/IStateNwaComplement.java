package complement.nwa;

import automata.nwa.IBuchiNwa;
import automata.nwa.IStateNwa;

public interface IStateNwaComplement extends IStateNwa {
	
	IBuchiNwa getOperand();
	
	IBuchiNwa getComplement();
}
