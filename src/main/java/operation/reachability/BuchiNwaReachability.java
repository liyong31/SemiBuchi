package operation.reachability;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import automata.IBuchiNwa;
import automata.IStateNwa;
import complement.BuchiNwaComplement;
import complement.DoubleDecker;

import main.Options;
import util.IntIterator;
import util.IntSet;

import util.parser.nwa.ats.ATSFileParser4Nwa;

/**
 * explore the state space of the input Buchi nested word automaton
 * */
public class BuchiNwaReachability implements IBuchiNwaReachability {
	
	private final IBuchiNwa mOperand;
	private boolean mExplored = false;
	private final Set<ExploreTask> mExploreTasks;
	private final LinkedList<ExploreTask> mWorkList;
	public final DoubleDecker EMPTY_DOUBLEDECKER = new DoubleDecker(-1, -1);
	
	
	public BuchiNwaReachability(IBuchiNwa operand) {
		mOperand = operand;
		mExploreTasks = new HashSet<>();
		mWorkList =  new LinkedList<>(); 
	}
	
	private void initWorkList() {
		// get initial double deckers 
		IntSet initialStates = mOperand.getInitialStates();
		IntIterator iter = initialStates.iterator();
		while(iter.hasNext()) {
			DoubleDecker doubleDecker = new DoubleDecker(
					DoubleDecker.EMPTY_DOWN_STATE
					, iter.next());
			addTaskInWorkList(EMPTY_DOUBLEDECKER, doubleDecker);
		}
	}
	
	private void addTaskInWorkList(DoubleDecker pred, DoubleDecker curr) {
		ExploreTask task = new ExploreTask(pred, curr);
		if(! mExploreTasks.contains(task)) {
			mWorkList.addFirst(task);
			mExploreTasks.add(task);
		}
	}
	
	@Override
	public void explore() {
		
		if(mExplored) return ;
		
		mExplored = true;
		initWorkList();
		Set<DoubleDecker> visitedDoubleDeckers = new HashSet<>();
        while(! mWorkList.isEmpty()) {
        	ExploreTask task = mWorkList.remove();
        	if(Options.verbose) System.out.println("visiting " + task.toString());
        	IStateNwa s = mOperand.getState(task.getCurrDoubleDecker().getUpState());
        	if(!visitedDoubleDeckers.contains(task.getCurrDoubleDecker())) {
        		exploreCallSuccessors(s, task);
        		exploreInternalSuccessors(s, task);
        		visitedDoubleDeckers.add(task.getCurrDoubleDecker());
        	}
        	exploreReturnSuccessors(s, task);
        }        
	}
	
	private void exploreCallSuccessors(IStateNwa s, ExploreTask task) {
    	IntSet calls = mOperand.getAlphabetCall();
    	IntIterator iterCallLetter = calls.iterator();
    	while(iterCallLetter.hasNext()) {
    		int callLetter = iterCallLetter.next();
    		IntSet succs = s.getSuccessorsCall(callLetter);
    		IntIterator iterSuccs = succs.iterator();
    		while(iterSuccs.hasNext()) {
    			int succ = iterSuccs.next();
    			if(Options.verbose) System.out.println(" s"+ s.getId() + ": " + s.toString() + "- " + callLetter + "< -> s" + succ + ": " + mOperand.getState(succ));
    			DoubleDecker succDoubleDecker = new DoubleDecker(s.getId(), succ); // (d, q) - c< -> (q, q') with (q, c<, q') in dr 
    			addTaskInWorkList(task.getCurrDoubleDecker(), succDoubleDecker);
    		}
    	}
	}
	
	private void exploreInternalSuccessors(IStateNwa s, ExploreTask task) {
    	IntSet internals = mOperand.getAlphabetInternal();
    	IntIterator iterInternalLetter = internals.iterator();
    	while(iterInternalLetter.hasNext()) {
    		int internalLetter = iterInternalLetter.next();
    		IntSet succs = s.getSuccessorsInternal(internalLetter);
    		IntIterator iterSuccs = succs.iterator();
    		while(iterSuccs.hasNext()) {
    			int succ = iterSuccs.next();
    			if(Options.verbose) System.out.println(" s"+ s.getId() + ": " + s.toString() + "- L" + internalLetter + " -> s" + succ + ": " + mOperand.getState(succ));
    			DoubleDecker succDoubleDecker = new DoubleDecker(task.getCurrDoubleDecker().getDownState(), succ); // (d, q) - L -> (d, q') with (q, c<, q') in dr 
    			addTaskInWorkList(task.getCurrDoubleDecker(), succDoubleDecker);
    		}
    	}
	}
	
	private void exploreReturnSuccessors(IStateNwa s, ExploreTask task) {
    	IntSet returns = mOperand.getAlphabetReturn();
    	IntIterator iterReturnLetter = returns.iterator();
    	while(iterReturnLetter.hasNext()) {
    		int returnLetter = iterReturnLetter.next();
    		int predHier = task.getCurrDoubleDecker().getDownState();
    		if(predHier == DoubleDecker.EMPTY_DOWN_STATE) return;
    		IntSet succs = s.getSuccessorsReturn(predHier, returnLetter);
    		IntIterator iterSuccs = succs.iterator();
    		while(iterSuccs.hasNext()) {
    			int succ = iterSuccs.next();
    			if(Options.verbose) System.out.println(" s"+ s.getId() + ": " + s.toString() + " s" + predHier + ": " + mOperand.getState(predHier) + "- >" + returnLetter + " -> s" + succ + ": " + mOperand.getState(succ));
    			DoubleDecker succDoubleDecker = new DoubleDecker(task.getPredDoubleDecker().getDownState(), succ); // we have  and (d', q) - (d, d') - >r -> (d, q') with (q, d', >r , q') in dr 
    			addTaskInWorkList(task.getCurrDoubleDecker(), succDoubleDecker);
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
	
	
	/**
	 * Unique explore task 
	 * */
	private class ExploreTask {
		private final DoubleDecker mPredDoubleDecker;
		private final DoubleDecker mCurrDoubleDecker;
		
		public ExploreTask(DoubleDecker pred, DoubleDecker curr) {
			this.mPredDoubleDecker = pred;
			this.mCurrDoubleDecker = curr;
		}
		
		public DoubleDecker getCurrDoubleDecker() {
			return mCurrDoubleDecker;
		}
		
		public DoubleDecker getPredDoubleDecker() {
			return mPredDoubleDecker;
		}
		
		@Override
		public boolean equals(Object other) {
			if(this == other) return true;
			if(! (other instanceof ExploreTask)) {
				return false;
			}
			ExploreTask otherDecker = (ExploreTask)other;
			return this.mPredDoubleDecker.equals(otherDecker.mPredDoubleDecker)
				&& this.mCurrDoubleDecker.equals(otherDecker.mCurrDoubleDecker);
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
	        int result = 1;
	        result = prime * result + this.mPredDoubleDecker.hashCode();
	        result = prime * result + this.mCurrDoubleDecker.hashCode();
	        return result;
		}
		
		@Override
		public String toString() {
			return "[pred:" + this.mPredDoubleDecker + ", curr:" +this.mCurrDoubleDecker + "]"; 
		}
				
	}
	
	public static void main(String[] args) {
		String file = "/home/liyong/workspace-neon/SemiBuchi/test4.ats";

		ATSFileParser4Nwa parser = new ATSFileParser4Nwa();
		parser.parse(file);
		IBuchiNwa buchi = parser.getBuchi(0);
//		buchi.toATS(System.out, parser.getAlphabet());
		Options.setChoice = 3;
		BuchiNwaComplement complement = new BuchiNwaComplement(buchi);
		Options.verbose = false;
		Options.optNCSB = false;
		Options.optBeqC = true;
		
		BuchiNwaReachability reach = new BuchiNwaReachability(complement);
		reach.explore();
		System.out.println("#states: " + complement.getStateSize() + ", #trans: " + complement.getNumTransition());
		complement.toATS(System.out, parser.getAlphabet());
		
		System.out.println("normal exploration ----------------");
//		complement.toDot(System.out, parser.getAlphabet());
		parser = new ATSFileParser4Nwa();
		parser.parse(file);
		buchi = parser.getBuchi(0);
//		buchi.toATS(System.out, parser.getAlphabet());
		complement = new BuchiNwaComplement(buchi);

		complement.explore();
		System.out.println("#states: " + complement.getStateSize() + ", #trans: " + complement.getNumTransition());
		complement.toATS(System.out, parser.getAlphabet());
	}

}
