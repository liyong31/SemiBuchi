package complement;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

import automata.BuchiWa;
import automata.IBuchiWa;
import automata.IStateWa;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import main.Options;
import util.IntIterator;
import util.IntSet;
import util.UtilIntSet;

/**
 * only valid for semi-deterministic Buchi word automata
 * */
public class BuchiWaComplement extends BuchiWa implements IBuchiComplement {

	private final IBuchiWa mOperand;
	
	private final List<IntSet> mOpTransUsed;
	
	public BuchiWaComplement(IBuchiWa buchi) {
		super(buchi.getAlphabetSize());
		// TODO Auto-generated constructor stub
		this.mOperand = buchi;
		this.mOpTransUsed = new ArrayList<>();
		for(int i = 0; i < mOperand.getAlphabetSize(); i ++) {
			this.mOpTransUsed.add(UtilIntSet.newIntSet());
		}
		computeInitialStates();
	}
	
	private final TObjectIntMap<StateWaNCSB> mState2Int = new TObjectIntHashMap<>();

	private void computeInitialStates() {
		StateWaNCSB state = new StateWaNCSB(0, this);
		IntSet C = mOperand.getInitialStates().clone();
		C.and(mOperand.getFinalStates()); // goto C
		IntSet N = mOperand.getInitialStates().clone();
		N.andNot(C);
		NCSB ncsb = new NCSB(N, C, UtilIntSet.newIntSet(), C);
		state.setNCSB(ncsb);
		if(C.isEmpty()) this.setFinal(0);
		this.setInitial(0);
		int id = this.addState(state);
		mState2Int.put(state, id);
	}
	

	protected StateWaNCSB addState(NCSB ncsb) {
		
		StateWaNCSB state = new StateWaNCSB(0, this);
		state.setNCSB(ncsb);
		
		if(mState2Int.containsKey(state)) {
			return (StateWaNCSB) getState(mState2Int.get(state));
		}else {
			int index = getStateSize();
			StateWaNCSB newState = new StateWaNCSB(index, this);
			newState.setNCSB(ncsb);
			int id = this.addState(newState);
			mState2Int.put(newState, id);
			
			if(ncsb.getBSet().isEmpty()) setFinal(index);
			
			return newState;
		}
	}

	@Override
	public IBuchiWa getOperand() {
		return mOperand;
	}
	
	
	private boolean mExplored = false;
	
	public void explore() {
		
		if(mExplored) return ;
		
		mExplored = true;
		
		LinkedList<IStateWa> walkList = new LinkedList<>();
		IntSet initialStates = getInitialStates();
		
		IntIterator iter = initialStates.iterator();
		while(iter.hasNext()) {
			walkList.addFirst(getState(iter.next()));
		}

		
        BitSet visited = new BitSet();
        
        while(! walkList.isEmpty()) {
        	IStateWa s = walkList.remove();
        	if(visited.get(s.getId())) continue;
        	visited.set(s.getId());
        	if(Options.verbose) System.out.println("s"+ s.getId() + ": " + s.toString());
        	
        	for(int i = 0; i < mOperand.getAlphabetSize(); i ++) {
        		IntSet succs = s.getSuccessors(i);
        		iter = succs.iterator();
        		while(iter.hasNext()) {
        			int n = iter.next();
        			if(Options.verbose) System.out.println(" s"+ s.getId() + ": " + s.toString() + "- L" + i + " -> s" + n + ": " + getState(n));
        			if(! visited.get(n)) {
        				walkList.addFirst(getState(n));
        			}
        		}
        	}
        }
	}


	@Override
	public void useOpTransition(int letter, IntSet states) {
		this.mOpTransUsed.get(letter).or(states);
	}


	@Override
	public int getNumUsedOpTransition() {
		// TODO Auto-generated method stub
		int num = 0;
		for(int i = 0; i < mOpTransUsed.size(); i ++) {
			IntSet sources = mOpTransUsed.get(i);
			IntIterator iter = sources.iterator();
			while(iter.hasNext()) {
				num += mOperand.getState(iter.next()).getSuccessors(i).cardinality();
			}
		}
		return num;
	}
	
	
}
