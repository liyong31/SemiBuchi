package util.parser;

import automata.IBuchiWa;

public interface DoubleParser extends Parser {
	
	IBuchiWa getFstBuchi();
	IBuchiWa getSndBuchi();

}
