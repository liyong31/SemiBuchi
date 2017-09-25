package operation.exploration.wa;

import java.util.LinkedList;

import automata.wa.IBuchiWa;
import automata.wa.IStateWa;
import main.Options;
import operation.exploration.BuchiExploration;
import util.IntIterator;
import util.IntSet;
import util.UtilIntSet;

/**
 * Explore state space of Buchi Word Automata
 * */
public class BuchiWaExploration extends BuchiExploration<IBuchiWa>{

	public BuchiWaExploration(IBuchiWa operand) {
		super(operand);
	}

	@Override
	protected void explore() {
		if(mExplored) return ;
		mExplored = true;
		
		LinkedList<IStateWa> workList = new LinkedList<>();
		IntSet initialStates = mOperand.getInitialStates();
		
		IntIterator iter = initialStates.iterator();
		while(iter.hasNext()) {
			workList.addFirst(mOperand.getState(iter.next()));
		}
        IntSet visited = UtilIntSet.newIntSet();
        while(! workList.isEmpty()) {
        	IStateWa s = workList.remove();
        	if(visited.get(s.getId())) continue;
        	visited.set(s.getId());
        	if(Options.verbose) System.out.println("s"+ s.getId() + ": " + s.toString());
        	for(int i = 0; i < mOperand.getAlphabetSize(); i ++) {
        		IntSet succs = s.getSuccessors(i);
        		iter = succs.iterator();
        		while(iter.hasNext()) {
        			int n = iter.next();
        			if(Options.verbose) System.out.println(" s"+ s.getId() + ": " + s.toString() + "- L" + i + " -> s" + n + ": " + mOperand.getState(n));
        			if(! visited.get(n)) {
        				workList.addFirst(mOperand.getState(n));
        			}
        		}
        	}
        }
	}

	@Override
	public String getOperationName() {
		return "WaExploration";
	}

}

