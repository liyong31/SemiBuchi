package operation.universality;

import java.util.List;

import operation.IBuchiWaUnaryOperation;
import util.IPair;

// check whether given Buchi accepts \sigma^\omega

public interface IBuchiWaUniversality extends IBuchiWaUnaryOperation<Boolean>{
	IPair<List<Integer>, List<Integer>> getCounterexampleWord();
}
