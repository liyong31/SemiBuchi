package operation.reachability;

import java.util.LinkedList;

import automata.IBuchiWa;
import automata.IStateWa;
import main.Options;
import util.IntIterator;
import util.IntSet;
import util.UtilIntSet;

public class BuchiWaReachability extends BuchiReachability<IBuchiWa>{

	public BuchiWaReachability(IBuchiWa operand) {
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

}
