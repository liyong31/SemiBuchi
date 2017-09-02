package complement;

import automata.IBuchiWa;
import automata.IStateWa;

public interface IStateWaComplement extends IStateWa {
	
	IBuchiWa getOperand();
	
	IBuchiWa getComplement();
}
