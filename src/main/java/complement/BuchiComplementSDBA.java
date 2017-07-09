package complement;

import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import automata.BuchiGeneral;
import automata.IBuchi;
import automata.IState;

/**
 * only valid for semi-deterministic Buchi automaton
 * */
public class BuchiComplementSDBA extends BuchiGeneral implements IBuchiComplement {

	private final IBuchi mOperand;
	
	public BuchiComplementSDBA(IBuchi buchi) {
		super(buchi.getAlphabetSize());
		// TODO Auto-generated constructor stub
		this.mOperand = buchi;
		computeInitialStates();
	}
	
	private final Map<StateNCSB, Integer> mState2Int = new HashMap<>();

	private void computeInitialStates() {
		// TODO Auto-generated method stub
		StateNCSB state = new StateNCSB(0, this);
		// TODO get also the initial states where initial state is also final state
		BitSet csets = (BitSet) mOperand.getInitialStates().clone();
		csets.and(getFinalStates()); // goto C
		state.setSets(mOperand.getInitialStates(), csets, new BitSet(), csets);
		if(csets.isEmpty()) this.setFinal(0);
		this.setInitial(0);
		int id = this.addState(state);
		mState2Int.put(state, id);
	}
	

	public StateNCSB addState(BitSet N, BitSet C, BitSet S, BitSet B) {
		
		StateNCSB state = new StateNCSB(0, this);
		state.setSets(N, C, S, B);
		Integer index = mState2Int.get(state);
		
		if(index != null) {
			return (StateNCSB) getState(index);
		}
	
		index = getStateSize();
		StateNCSB newState = new StateNCSB(index, this);
		newState.setSets(N, C, S, B);
		int id = this.addState(newState);
		mState2Int.put(state, id);
		
		if(B.isEmpty()) setFinal(index);
		
		return newState;
	}

	@Override
	public IBuchi getOperand() {
		// TODO Auto-generated method stub
		return mOperand;
	}
	
	
	private boolean mExplored = false;
	
	public void explore() {
		
		if(mExplored) return ;
		
		mExplored = true;
		
		LinkedList<IState> walkList = new LinkedList<>();
		BitSet initialStates = getInitialStates();
		
		for(int i = initialStates.nextSetBit(0); i >= 0; i = initialStates.nextSetBit(i + 1)) {
			walkList.addFirst(getState(i));
		}
		
        BitSet visited = new BitSet();
        
        while(! walkList.isEmpty()) {
        	IState s = walkList.remove();
        	visited.set(s.getId());
        	for(int i = 0; i < mOperand.getAlphabetSize(); i ++) {
        		BitSet succs = s.getSuccessors(i);
        		for(int n = succs.nextSetBit(0); n >= 0; n = succs.nextSetBit(n + 1)) {
        			System.out.println("s"+ s.getId() + ": " + s.toString() + "- L" + i + " -> s" + n + ": " + getState(n));
        			if(! visited.get(n)) {
        				walkList.addFirst(getState(n));
        			}
        		}
        	}
        }
	}
	
	
	public static void main(String[] args) {
		
		IBuchi buchi = new BuchiGeneral(2);
		IState aState = buchi.addState();
		IState bState = buchi.addState();
		
		aState.addSuccessor(0, bState.getId());
		aState.addSuccessor(1, aState.getId());
//		aState.addSuccessor(1, aState.getId());
		
//		aState.addSuccessor(1, bState.getId());
		bState.addSuccessor(0, bState.getId());
//		bState.addSuccessor(0, aState.getId());
		bState.addSuccessor(1, aState.getId());
		
		buchi.setFinal(bState);
		buchi.setInitial(aState);
		
		System.out.println(buchi.toString());
		System.out.println(buchi.isSemiDeterministic());
		
		System.out.println("----------- complement buchi ---------");
		BuchiComplementSDBA complement = new BuchiComplementSDBA(buchi);
		
		complement.explore();
		System.out.println(complement.toString());
		System.out.println(complement.isSemiDeterministic());

	}
	
	
}
