package operation.difference.wa;

import java.util.List;

import automata.wa.IBuchiWa;
import automata.wa.IStateWa;
import complement.wa.IBuchiWaComplement;
import operation.IBinaryOperation;
import util.IPair;

/**
 * input two Buchi automata, namely A and B, and check whether the language of A
 * is contained in B, we also need the language difference D, i.e.,
 *          L(D) = L(A) \ L(B)
 **/
public interface IBuchiWaDifference extends IBinaryOperation<IBuchiWa, IBuchiWa>{
	
	IBuchiWaComplement getSecondBuchiComplement();
	
	Boolean isIncluded();
	
    IPair<List<Integer>, List<Integer>> getCounterexampleWord();
    IPair<List<IStateWa>, List<IStateWa>> getCounterexampleRun();

}
