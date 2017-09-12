package operation.reachability;

import automata.IBuchiNwa;
import operation.IUnaryOperation;

public interface IBuchiNwaReachability extends IUnaryOperation<IBuchiNwa, IBuchiNwa> {
	
	void explore();

}
