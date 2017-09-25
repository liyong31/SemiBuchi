package complement.wa;

import automata.wa.IBuchiWa;
import automata.wa.IStateWa;

public interface IStateWaComplement extends IStateWa {
	
	IBuchiWa getOperand();
	
	IBuchiWa getComplement();
}
