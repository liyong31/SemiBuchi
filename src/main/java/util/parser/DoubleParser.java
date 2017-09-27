package util.parser;

import automata.wa.IBuchiWa;

public interface DoubleParser extends Parser {
	
	IBuchiWa getFstBuchi();
	IBuchiWa getSndBuchi();

}
