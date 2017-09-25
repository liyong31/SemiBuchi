package operation.inclusion.wa;

import java.util.List;

import automata.wa.IBuchiWa;
import automata.wa.IStateWa;
import complement.wa.IBuchiWaComplement;
import util.IPair;

public interface IBuchiInclusion {
	
	IBuchiWa getFstBuchi();
	IBuchiWa getSndBuchi();
	IBuchiWaComplement getSndBuchiComplement();
	IBuchiWa getBuchiDifference();
	
	Boolean isIncluded();
	
    IPair<List<Integer>, List<Integer>> getCounterexampleWord();
    IPair<List<IStateWa>, List<IStateWa>> getCounterexampleRun();
    
    String getName();

}
