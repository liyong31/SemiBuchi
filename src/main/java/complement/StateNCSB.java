package complement;

import automata.IBuchi;

import automata.StateGeneral;

import main.Options;
import util.IntIterator;
import util.IntSet;
import util.UtilIntSet;

public class StateNCSB extends StateGeneral implements IStateComplement {


	private NCSB mNCSB;
	
	private final IBuchi mOperand;
	private final BuchiComplementSDBA mComplement;
	
	public StateNCSB(int id, BuchiComplementSDBA complement) {
		super(id);
		this.mComplement = complement;
		this.mOperand = complement.getOperand();
		this.mNCSB = new NCSB();
	}
	
	public void setNCSB(NCSB ncsb) {
		this.mNCSB = ncsb;
	}
	
	public NCSB getNCSB() {
		return  mNCSB;
	}

	@Override
	public IBuchi getOperand() {
		return this.mOperand;
	}

	@Override
	public IBuchi getComplement() {
		return mComplement;
	}	
	
	@Override
	public IntSet getSuccessors(int letter) {
		if(super.getEnabledLetters().contains(letter)) {
			return super.getSuccessors(letter);
		}
		
		// B
		SuccessorResult succResult = collectSuccessors(mNCSB.getBSet(), letter, true);
		if(!succResult.hasSuccessor) return UtilIntSet.newIntSet();
		IntSet BSuccs = succResult.mSuccs;
		IntSet minusFSuccs = succResult.mMinusFSuccs;
		IntSet interFSuccs = succResult.mInterFSuccs;

		// C\B
		IntSet cMinusB = mNCSB.copyCSet();
		cMinusB.andNot(mNCSB.getBSet());
		succResult = collectSuccessors(cMinusB, letter, !Options.optNCSB);
		if(!succResult.hasSuccessor) return UtilIntSet.newIntSet();
		IntSet CSuccs = succResult.mSuccs;
		minusFSuccs.or(succResult.mMinusFSuccs);
		interFSuccs.or(succResult.mInterFSuccs);
		
		// N
		succResult = collectSuccessors(mNCSB.getNSet(), letter, false);
		if(!succResult.hasSuccessor) return UtilIntSet.newIntSet();
		IntSet NSuccs = succResult.mSuccs;

		// S
		succResult = collectSuccessors(mNCSB.getSSet(), letter, false);
		if(!succResult.hasSuccessor) return UtilIntSet.newIntSet();
		IntSet SSuccs = succResult.mSuccs;
		
		// check d(S) and d(C)
		if(SSuccs.overlap(mOperand.getFinalStates())
		|| minusFSuccs.overlap(SSuccs)) {
			return UtilIntSet.newIntSet();
		}
		
		return computeSuccessors(new NCSB(NSuccs, CSuccs, SSuccs, BSuccs), minusFSuccs, interFSuccs, letter);
	}
	

	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof StateNCSB)) {
			return false;
		}
		StateNCSB other = (StateNCSB)obj;
		return  mNCSB.equals(other.mNCSB);
	}
	
	
	public String toString() {
		return mNCSB.toString();
	}
	

	@Override
	public int hashCode() {
		return mNCSB.hashCode();
	}
	// -------------------------------------------------

	/**
	 * If q in C\F or (B\F), then tr(q, a) should not be not empty
	 * */
	private boolean noTransitionAssertion_MinusF(int state, IntSet succs) {
		return !mOperand.isFinal(state) && succs.isEmpty();
	}
	
	private SuccessorResult collectSuccessors(IntSet states, int letter, boolean testTrans) {
		SuccessorResult result = new SuccessorResult();
		IntIterator iter = states.iterator();
		while(iter.hasNext()) {
			int state = iter.next();
			IntSet succs = mOperand.getSuccessors(state, letter);
			if (testTrans && noTransitionAssertion_MinusF(state, succs)) {
				result.hasSuccessor = false;
				return result;
			}
			result.mSuccs.or(succs);
			if(testTrans) {
				if(mOperand.isFinal(state)) {
					result.mInterFSuccs.or(succs);
				}else {
					result.mMinusFSuccs.or(succs);
				}
			}
		}
		return result;
	}
	
	private IntSet computeSuccessors(NCSB succNCSB, IntSet minusFSuccs
			, IntSet interFSuccs, int letter) {
		SuccessorGenerator generator = new SuccessorGenerator(mNCSB.getBSet().isEmpty()
															, succNCSB
															, minusFSuccs
															, interFSuccs
															, mOperand.getFinalStates());
		IntSet succs = UtilIntSet.newIntSet();
		while(generator.hasNext()) {
		    NCSB ncsb = generator.next();
		    if(ncsb == null) continue;
			StateNCSB succ = mComplement.addState(ncsb);
			super.addSuccessor(letter, succ.getId());
			succs.set(succ.getId());
		}

		return succs;
	}

}
