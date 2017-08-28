package operation.reachability;

import automata.IBuchiNwa;

public interface IBuchiReachability {
	
	void explore();
	
	IBuchiNwa getOperand();

}
