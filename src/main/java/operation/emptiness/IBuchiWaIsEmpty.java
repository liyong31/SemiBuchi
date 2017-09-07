package operation.emptiness;

import java.util.List;

import operation.IBuchiWaUnaryOperation;
import util.IPair;

public interface IBuchiWaIsEmpty extends IBuchiWaUnaryOperation<Boolean> {	
	IPair<List<Integer>, List<Integer>> getAcceptingWord();
}
