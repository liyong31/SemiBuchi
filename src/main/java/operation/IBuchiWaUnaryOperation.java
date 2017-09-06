package operation;

import automata.IBuchiWa;

public interface IBuchiWaUnaryOperation {
	
	IBuchiWa getFstOperand();
	
	IBuchiWa getResult();

}
