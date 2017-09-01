package operation.inclusion;

import java.util.List;

import automata.IBuchiWa;
import automata.IStateWa;
import complement.IBuchiComplement;
import util.IPair;

public interface IBuchiInclusion {
	
	IBuchiWa getFstBuchi();
	IBuchiWa getSndBuchi();
	IBuchiComplement getSndBuchiComplement();
	IBuchiWa getBuchiDifference();
	
	Boolean isIncluded();
	
    IPair<List<Integer>, List<Integer>> getCounterexampleWord();
    IPair<List<IStateWa>, List<IStateWa>> getCounterexampleRun();
    
    String getName();

}
