package operation.universality;

import java.util.List;

import automata.IBuchiWa;
import util.IPair;

// check whether given Buchi accepts \sigma^\omega

public interface IBuchiUniversality {
	
	IBuchiWa getBuchi();
	IBuchiWa getBuchiComplement();
	
	Boolean isUniversal();
	
	IPair<List<Integer>, List<Integer>> getCounterexampleWord();

}
