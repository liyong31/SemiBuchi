package complement;

import java.util.ArrayList;
import java.util.List;

import automata.IBuchiNwa;
import automata.StateNwa;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import main.Options;
import util.IntIterator;
import util.IntSet;

import util.UtilIntSet;

public class StateNwaNCSB extends StateNwa implements IStateNwaComplement {
	
	private final BuchiNwaComplementSDBA mComplement;
	private final IBuchiNwa mOperand;
	
	private NCSB mNCSB;
	
	public StateNwaNCSB(BuchiNwaComplementSDBA complement, int id) {
		super(complement, id);
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
	public IBuchiNwa getOperand() {
		return mOperand;
	}

	@Override
	public IBuchiNwa getComplement() {
		return mComplement;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof StateNwaNCSB)) {
			return false;
		}
		StateNwaNCSB state = (StateNwaNCSB)obj;
		return  this.mNCSB.equals(state.mNCSB);
	}
	
	private IntSet visitedLetters = UtilIntSet.newIntSet();
	
	/** 
	 * compute the successor deckers for internal/call transition 
	 * */
	private ResultSucc computeSuccDoubleDeckers_CallOrInternal(IntSet predDoubleDeckers, int letter, boolean testTrans) {
		IntIterator iter = predDoubleDeckers.iterator();
		ResultSucc resultSucc = new ResultSucc();
		while (iter.hasNext()) {
			int doubleDecker = iter.next();
			int downState = mComplement.getDownState(doubleDecker);
			int upState = mComplement.getUpState(doubleDecker);
			IntSet upStateSuccs = null;
			IntSet succDeckers = null;
			
			boolean isInternalLetter = mComplement.getAlphabetInternal().get(letter);
			// generate all deckers (down, succ)
			if(isInternalLetter) {
				// internal (x, y) - l -> (x, d) 
				upStateSuccs = mOperand.getSuccessorsInternal(upState, letter);
				succDeckers = mComplement.generateDeckers(downState, upStateSuccs);
			}else {
				// call (x, y) - l -> (y, d)
				upStateSuccs = mOperand.getSuccessorsCall(upState, letter);
				succDeckers = mComplement.generateDeckers(upState, upStateSuccs);
			}
			
			if (testTrans && noTransitionAssertion_MinusF(upState, upStateSuccs)) {
				resultSucc.hasSuccs = false;
				return resultSucc;
			}

			resultSucc.mSuccs.or(succDeckers);
			if(mOperand.isFinal(upState)) {
				if(testTrans) resultSucc.mInterFSuccs.or(succDeckers);
			}else {
				if(testTrans) resultSucc.mMinusFSuccs.or(succDeckers);
			}
		}
		return resultSucc;
	}
	
	private IntSet computeSuccessors(NCSB succNCSB, IntSet minusFSuccs
			, IntSet interFSuccs, int hier, int letter) {
		SuccessorGenerator generator = new SuccessorGenerator(mNCSB
															, succNCSB
															, minusFSuccs
															, interFSuccs
															, mComplement.getFinalDeckers());
		IntSet succs = UtilIntSet.newIntSet();
		while(generator.hasNext()) {
		    NCSB ncsb = generator.next();
		    if(ncsb == null) continue;
			StateNwaNCSB succ = mComplement.addState(ncsb);
			if(mComplement.getAlphabetInternal().get(letter)) {
				super.addSuccessorInternal(letter, succ.getId());
			}else if(mComplement.getAlphabetCall().get(letter)) {
				super.addSuccessorCall(letter, succ.getId());
			}else {
				super.addSuccessorReturn(hier, letter, succ.getId());
			}
			succs.set(succ.getId());
		}

		return succs;
	}
	
	private IntSet computeSuccCallOrInternal(int letter) {
		visitedLetters.set(letter);
		
		IntSet minusFSuccs = UtilIntSet.newIntSet();
		IntSet interFSuccs = UtilIntSet.newIntSet();
		
		// Compute the successors of B
		ResultSucc resultSucc = computeSuccDoubleDeckers_CallOrInternal(mNCSB.getBSet(), letter, true);
		if(!resultSucc.hasSuccs) return UtilIntSet.newIntSet();
		IntSet BSuccs = resultSucc.mSuccs;
		minusFSuccs.or(resultSucc.mMinusFSuccs);
		interFSuccs.or(resultSucc.mInterFSuccs);
		
		// First compute the successors of C
		IntSet CSuccs = UtilIntSet.newIntSet();
		CSuccs.or(BSuccs);
        IntSet CMinusB = mNCSB.getCSet().clone();
        CMinusB.andNot(mNCSB.getBSet()); // C\B
        resultSucc = computeSuccDoubleDeckers_CallOrInternal(CMinusB, letter, !Options.optNCSB);
		if(!resultSucc.hasSuccs) return UtilIntSet.newIntSet();
		CSuccs.or( resultSucc.mSuccs);
		minusFSuccs.or(resultSucc.mMinusFSuccs);
		interFSuccs.or(resultSucc.mInterFSuccs);
		
		// Compute the successors of N
		resultSucc = computeSuccDoubleDeckers_CallOrInternal(mNCSB.getNSet(), letter, false);
		IntSet NSuccs = resultSucc.mSuccs;
		
		// Compute the successors of S
		resultSucc = computeSuccDoubleDeckers_CallOrInternal(mNCSB.getSSet(), letter, false);
		IntSet SSuccs = resultSucc.mSuccs;
		
        // d_a(S) /\ F should be empty
		if(SSuccs.overlap(mComplement.getFinalDeckers())) return UtilIntSet.newIntSet();
		
        return computeSuccessors(new NCSB(NSuccs, CSuccs, SSuccs, BSuccs), minusFSuccs, interFSuccs, -1, letter);
	}
		
	@Override
	public IntSet getSuccessorsInternal(int letter) {
		assert mComplement.getAlphabetInternal().get(letter);
		return computeSuccCallOrInternal(letter);
	}
	
	/**
	 * If q in C\F or (B\F), then tr(q, a) should not be not empty
	 * */
	private boolean noTransitionAssertion_MinusF(int upState, IntSet succs) {
		return !mOperand.isFinal(upState) && succs.isEmpty();
	}

	@Override
	public IntSet getSuccessorsCall(int letter) {
		assert mComplement.getAlphabetCall().get(letter);
		return computeSuccCallOrInternal(letter);
	}
	
	private IntSet computeSuccReturn(int hier, int letter) {
		visitedLetters.set(letter);
		
		StateNwaNCSB hierState = (StateNwaNCSB) mComplement.getState(hier);
		NCSB hierNCSB = hierState.getNCSB();
		
		IntSet minusFSuccs = UtilIntSet.newIntSet();
		IntSet interFSuccs = UtilIntSet.newIntSet();
		// Compute the successors of B
//		ResultSucc resultSucc = computeSuccDoubleDeckers_Return(mNCSB.getBSet(), hierNCSB.getBSet(), letter, true);
		TIntObjectMap<List<Integer>> hierDoubleDeckers = doubleDeckerSetToMap(hierNCSB);
		ResultSucc resultSucc = computeSuccDoubleDeckers_Return(mNCSB.getBSet(), hierDoubleDeckers, letter, true);
		if(! resultSucc.hasSuccs) return UtilIntSet.newIntSet();
		IntSet BSuccs = resultSucc.mSuccs;
		minusFSuccs.or(resultSucc.mMinusFSuccs);
		interFSuccs.or(resultSucc.mInterFSuccs);
		
		// First compute the successors of C
		IntSet CSuccs = UtilIntSet.newIntSet();
		CSuccs.or(BSuccs);
        IntSet CMinusB = mNCSB.getCSet().clone();
        CMinusB.andNot(mNCSB.getBSet()); // C\B
        
//        IntSet hierCMinusB = hierNCSB.getCSet().clone();
//        hierCMinusB.andNot(hierNCSB.getBSet());
		resultSucc = computeSuccDoubleDeckers_Return(CMinusB, hierDoubleDeckers, letter, !Options.optNCSB);
//        resultSucc = computeSuccDoubleDeckers_Return(CMinusB, hierCMinusB, letter, true);
        if(! resultSucc.hasSuccs) return UtilIntSet.newIntSet();
		CSuccs.or(resultSucc.mSuccs);
		minusFSuccs.or(resultSucc.mMinusFSuccs);
		interFSuccs.or(resultSucc.mInterFSuccs);
		
		// Compute the successors of N
//		resultSucc = computeSuccDoubleDeckers_Return(mNCSB.getNSet(), hierNCSB.getNSet(), letter, false);
		resultSucc = computeSuccDoubleDeckers_Return(mNCSB.getNSet(), hierDoubleDeckers, letter, false);
		if(! resultSucc.hasSuccs) return UtilIntSet.newIntSet();
		IntSet NSuccs = resultSucc.mSuccs;
		
		// Compute the successors of S
//		resultSucc = computeSuccDoubleDeckers_Return(mNCSB.getSSet(), hierNCSB.getSSet(), letter, false);
		resultSucc = computeSuccDoubleDeckers_Return(mNCSB.getSSet(), hierDoubleDeckers, letter, false);
		if(! resultSucc.hasSuccs) return UtilIntSet.newIntSet();
		IntSet SSuccs = resultSucc.mSuccs;
		
        // d_a(S) /\ F should be empty
		if(SSuccs.overlap(mComplement.getFinalDeckers())) return UtilIntSet.newIntSet();
		
        return computeSuccessors(new NCSB(NSuccs, CSuccs, SSuccs, BSuccs), minusFSuccs, interFSuccs, hier, letter);
	}

	@Override
	public IntSet getSuccessorsReturn(int hier, int letter) {
		assert mComplement.getAlphabetReturn().get(letter);
		return computeSuccReturn(hier, letter);
	}
	
	
	/**
	 * compute the return double decker id for return transitions
	 * */
//	private ResultSucc computeSuccDoubleDeckers_Return(IntSet predDoubleDecker, IntSet predHier, int letter, boolean testTransition) {
//		TIntObjectMap<List<Integer>> predHierDoubleDeckerMap = doubleDeckerSetToMap(predHier, false);
//		ResultSucc resultSucc = new ResultSucc();
//		IntIterator iterDoubleDecker = predDoubleDecker.iterator();
//		while(iterDoubleDecker.hasNext()) {
//			int doubleDecker = iterDoubleDecker.next();
//			int downState = mComplement.getDownState(doubleDecker);
//			int upState = mComplement.getUpState(doubleDecker);
//			// predHier should contain all downState as its upState
//			if(! predHierDoubleDeckerMap.containsKey(downState)) {
//				resultSucc.hasSuccs = false;
//				return resultSucc;
//			}
//			// compute successors of return
//			IntSet upStateSuccs = mOperand.getSuccessorsReturn(upState, downState, letter);
//			if (testTransition && noTransitionAssertion_MinusF(upState, upStateSuccs)) {
//				resultSucc.hasSuccs = false;
//				return resultSucc;
//			}
//			
//			List<Integer> downHiers = predHierDoubleDeckerMap.get(downState);
//			// put (upHier, succ)
//			for(Integer downHier : downHiers) {
//				IntSet succDeckers = mComplement.generateDeckers(downHier, upStateSuccs);
//				resultSucc.mSuccs.or(succDeckers);
//				if(mOperand.isFinal(upState)) {
//					if(testTransition) resultSucc.mInterFSuccs.or(succDeckers);
//				}else {
//					if(testTransition) resultSucc.mMinusFSuccs.or(succDeckers);
//				}
//			}
//		}
//		return resultSucc;
//	}
	private ResultSucc computeSuccDoubleDeckers_Return(IntSet predDoubleDecker, TIntObjectMap<List<Integer>> predHierDoubleDeckerMap, int letter, boolean testTransition) {
		ResultSucc resultSucc = new ResultSucc();
		IntIterator iterDoubleDecker = predDoubleDecker.iterator();
		while(iterDoubleDecker.hasNext()) {
			int doubleDecker = iterDoubleDecker.next();
			int downState = mComplement.getDownState(doubleDecker);
			int upState = mComplement.getUpState(doubleDecker);
			// predHier should contain all downState as its upState
			if(! predHierDoubleDeckerMap.containsKey(downState)) {
				resultSucc.hasSuccs = false;
				return resultSucc;
			}
			// compute successors of return
			IntSet upStateSuccs = mOperand.getSuccessorsReturn(upState, downState, letter);
			if (testTransition && noTransitionAssertion_MinusF(upState, upStateSuccs)) {
				resultSucc.hasSuccs = false;
				return resultSucc;
			}
			
			List<Integer> downHiers = predHierDoubleDeckerMap.get(downState);
			// put (upHier, succ)
			for(Integer downHier : downHiers) {
				IntSet succDeckers = mComplement.generateDeckers(downHier, upStateSuccs);
				resultSucc.mSuccs.or(succDeckers);
				if(mOperand.isFinal(upState)) {
					if(testTransition) resultSucc.mInterFSuccs.or(succDeckers);
				}else {
					if(testTransition) resultSucc.mMinusFSuccs.or(succDeckers);
				}
			}
		}
		return resultSucc;
	}
	
	private TIntObjectMap<List<Integer>> doubleDeckerSetToMap(NCSB hierNCSB) {
		IntSet ncsb = hierNCSB.copyNSet();
		ncsb.or(hierNCSB.getCSet());
		ncsb.or(hierNCSB.getSSet());
		return doubleDeckerSetToMap(ncsb, false);
	}
	
	private TIntObjectMap<List<Integer>> doubleDeckerSetToMap(IntSet doubleDeckerSet, boolean keyIsDownState) {
		TIntObjectMap<List<Integer>> doubleDeckerMap = new TIntObjectHashMap<>();
		IntIterator iterDoubleDecker = doubleDeckerSet.iterator();
		while(iterDoubleDecker.hasNext()) {
			int doubleDecker = iterDoubleDecker.next();
			int downState = mComplement.getDownState(doubleDecker);
			int upState   = mComplement.getUpState(doubleDecker);
			List<Integer> temp = null;
			int key   = keyIsDownState  ? downState : upState;
			int value = !keyIsDownState ? downState : upState;
			if(doubleDeckerMap.containsKey(key)) {
				temp = doubleDeckerMap.get(key);
			}else {
				temp = new ArrayList<>();
			}
			temp.add(value);
			doubleDeckerMap.put(key, temp);
		}
		return doubleDeckerMap;
	}
	
	@Override
	public String toString() {

		return "(" + outputSet(mNCSB.getNSet()) + ","
				   + outputSet(mNCSB.getCSet()) + ","
				   + outputSet(mNCSB.getSSet()) + ","
				   + outputSet(mNCSB.getBSet()) + ")";
	}
	
	private String outputSet(IntSet set) {
		IntIterator iter = set.iterator();
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		boolean first = true;
		while(iter.hasNext()) {
			if(!first) builder.append(",");
			first = false;
			builder.append(mComplement.getDoubleDecker(iter.next()).toString());
		}
		builder.append("}");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return mNCSB.hashCode();
	}
	
	private class ResultSucc {
		IntSet mSuccs = UtilIntSet.newIntSet();
		IntSet mMinusFSuccs = UtilIntSet.newIntSet();
		IntSet mInterFSuccs = UtilIntSet.newIntSet();
		boolean hasSuccs = true;
		
		@Override
		public String toString() {
			return "[" + mSuccs.toString() + ":" + mMinusFSuccs.toString() + ":"
					   + mInterFSuccs.toString() + ":" + hasSuccs + "]";
		}
	}

}
