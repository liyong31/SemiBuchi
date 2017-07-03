package operation.inclusion;

import java.util.List;

import automata.IBuchi;
import automata.IState;
import util.IPair;

public interface IBuchiInclusion {
	
	IBuchi getFstBuchi();
	IBuchi getSndBuchi();
	IBuchi getSndBuchiComplement();
	IBuchi getBuchiDifference();
	
	Boolean isIncluded(long timeLimit);
	
    IPair<List<Integer>, List<Integer>> getCounterexampleWord();
    IPair<List<IState>, List<IState>> getCounterexampleRun();
    
    String getName();

}
