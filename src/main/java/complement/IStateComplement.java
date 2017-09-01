package complement;

import automata.IBuchiWa;
import automata.IStateWa;

public interface IStateComplement extends IStateWa {
	
	IBuchiWa getOperand();
	
	IBuchiWa getComplement();
}
