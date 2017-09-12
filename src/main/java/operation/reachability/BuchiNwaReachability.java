package operation.reachability;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import automata.IBuchiNwa;
import automata.IStateNwa;
import main.Options;
import util.IntIterator;
import util.IntSet;
import util.UtilIntSet;

/**
 * explore the state space of the input Buchi nested word automaton
 * */
public class BuchiNwaReachability implements IBuchiNwaReachability {
	
	private final IBuchiNwa mOperand;
	private boolean mExplored = false;
	
	public BuchiNwaReachability(IBuchiNwa operand) {
		mOperand = operand;
	}
	
	private LinkedList<IStateNwa> init() {
		LinkedList<IStateNwa> walkList = new LinkedList<>();
		// get initial states
		IntSet initialStates = mOperand.getInitialStates();
		
		IntIterator iter = initialStates.iterator();
		while(iter.hasNext()) {
			walkList.addFirst(mOperand.getState(iter.next()));
		}
		return walkList;
	}
	
	@Override
	public void explore() {
		
		if(mExplored) return ;
		
		mExplored = true;
		
		LinkedList<IStateNwa> walkList = init();
		
        IntSet visited = UtilIntSet.newIntSet();
        
        Set<Integer> callPreds = new HashSet<>();
        
        while(! walkList.isEmpty()) {
        	IStateNwa s = walkList.remove();
        	if(visited.get(s.getId())) continue;
        	visited.set(s.getId());
        	if(Options.verbose) System.out.println("s"+ s.getId() + ": " + s.toString());
        	// call alphabets
        	IntSet calls = mOperand.getAlphabetCall();
        	IntIterator iterLetter = calls.iterator();
        	while(iterLetter.hasNext()) {
        		int letter = iterLetter.next();
        		IntSet succs = s.getSuccessorsCall(letter);
        		if(! succs.isEmpty()) callPreds.add(s.getId());
        		exploreSuccessors(s, walkList, succs, visited, letter);
        	}
        	
        	iterLetter = mOperand.getAlphabetInternal().iterator();
        	while(iterLetter.hasNext()) {
        		int letter = iterLetter.next();
        		if(s.getId() == 6 && letter == 4) {
        			System.out.println("HAHA");
        		}
        		IntSet succs = s.getSuccessorsInternal(letter);
        		exploreSuccessors(s, walkList, succs, visited, letter);
        	}
        	
           	iterLetter = mOperand.getAlphabetReturn().iterator();

        	while(iterLetter.hasNext()) {
//        		System.out.println(callPreds);
        		int letter = iterLetter.next();
//        		
        		int size = mOperand.getStateSize();
        		for(int i = 0; i < size; i ++) {
            		for(Integer hier : callPreds) {
            			IStateNwa state = mOperand.getState(i);
            			if(Options.verbose) System.out.println("current: " +  state.toString() +  "  hier: " + hier + " :" + mOperand.getState(hier).toString());
                		IntSet succs = state.getSuccessorsReturn(hier, letter);
                		exploreSuccessors(state, walkList, succs, visited, letter);
            		}
        		}

        	}
        }
        
//        System.out.println("" + getStates());
	}
	
	private void exploreSuccessors(IStateNwa s, LinkedList<IStateNwa> walkList
			, IntSet succs, IntSet visited, int letter) {
		IntIterator iter = succs.iterator();
		while(iter.hasNext()) {
			int n = iter.next();
//			if(Options.verbose) System.out.println("size of deckers: " + mDeckerList.size() + " " + mDeckerList.toString());
			if(Options.verbose) System.out.println(" s"+ s.getId() + ": " + s.toString() + "- L" + letter + " -> s" + n + ": " + mOperand.getState(n));
			if(! visited.get(n)) {
				walkList.addFirst(mOperand.getState(n));
			}
		}
	}

	@Override
	public IBuchiNwa getOperand() {
		return mOperand;
	}

	@Override
	public IBuchiNwa getResult() {
		if(! mExplored) explore();
		return mOperand;
	}

}
