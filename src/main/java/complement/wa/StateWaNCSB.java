package complement.wa;

import automata.wa.IBuchiWa;
import automata.wa.StateWa;
import complement.NCSB;
import complement.SuccessorGenerator;
import complement.SuccessorResult;
import main.Options;

import util.IntSet;
import util.UtilIntSet;

public class StateWaNCSB extends StateWa implements IStateWaComplement {


	private final NCSB mNCSB;
	
	private final IBuchiWa mOperand;
	private final BuchiWaComplement mComplement;
	
	public StateWaNCSB(BuchiWaComplement complement, int id, NCSB ncsb) {
		super(id);
		this.mComplement = complement;
		this.mOperand = complement.getOperand();
		this.mNCSB = ncsb;
	}
	
	public NCSB getNCSB() {
		return  mNCSB;
	}

	@Override
	public IBuchiWa getOperand() {
		return this.mOperand;
	}

	@Override
	public IBuchiWa getComplement() {
		return mComplement;
	}	
	
	private IntSet mVisitedLetters = UtilIntSet.newIntSet();
	@Override
	public IntSet getSuccessors(int letter) {
		if(mVisitedLetters.get(letter)) {
			return super.getSuccessors(letter);
		}
		mVisitedLetters.set(letter);
		// B
		SuccessorResult succResult = collectSuccessors(mNCSB.getBSet(), letter, true);
		if(!succResult.hasSuccessor) return UtilIntSet.newIntSet();
		IntSet BSuccs = succResult.mSuccs;
		IntSet minusFSuccs = succResult.mMinusFSuccs;
		IntSet interFSuccs = succResult.mInterFSuccs;

		// C\B
		IntSet cMinusB = mNCSB.copyCSet();
		cMinusB.andNot(mNCSB.getBSet());
		succResult = collectSuccessors(cMinusB, letter, !Options.lazyS);
		if(!succResult.hasSuccessor) return UtilIntSet.newIntSet();
		IntSet CSuccs = succResult.mSuccs;
		CSuccs.or(BSuccs);
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
		
		return computeSuccessors(new NCSB(NSuccs, CSuccs, SSuccs, BSuccs), minusFSuccs, interFSuccs, letter);
	}
	

	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof StateWaNCSB)) {
			return false;
		}
		StateWaNCSB other = (StateWaNCSB)obj;
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
		for(final int state : states.iterable()) {
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
		// check d(S) and d(C)
		if(succNCSB.getSSet().overlap(mOperand.getFinalStates())
		|| minusFSuccs.overlap(succNCSB.getSSet())) {
			return UtilIntSet.newIntSet();
		}
		SuccessorGenerator generator = new SuccessorGenerator(mNCSB.getBSet().isEmpty()
															, succNCSB
															, minusFSuccs
															, interFSuccs
															, mOperand.getFinalStates());
		IntSet succs = UtilIntSet.newIntSet();
		while(generator.hasNext()) {
		    NCSB ncsb = generator.next();
		    if(ncsb == null) continue;
			StateWaNCSB succ = mComplement.addState(ncsb);
			super.addSuccessor(letter, succ.getId());
			succs.set(succ.getId());
		}

		return succs;
	}

}
