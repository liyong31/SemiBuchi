package complement.nwa;

import java.util.ArrayList;
import java.util.List;

import automata.nwa.IBuchiNwa;
import automata.nwa.StateNwa;
import complement.NCSB;
import complement.SuccessorGenerator;
import complement.SuccessorResult;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import main.Options;
import util.IntIterator;
import util.IntSet;

import util.UtilIntSet;

public class StateNwaNCSB extends StateNwa implements IStateNwaComplement {

    private final BuchiNwaComplement mComplement;
    private final IBuchiNwa mOperand;
    private final NCSB mNCSB;

    public StateNwaNCSB(BuchiNwaComplement complement, int id, NCSB ncsb) {
        super(complement, id);
        this.mComplement = complement;
        this.mOperand = complement.getOperand();
        this.mNCSB = ncsb;
    }

    public NCSB getNCSB() {
        return mNCSB;
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
        if (this == obj)
            return true;
        if (!(obj instanceof StateNwaNCSB)) {
            return false;
        }
        StateNwaNCSB state = (StateNwaNCSB) obj;
        return this.mNCSB.equals(state.mNCSB);
    }

    // private IntSet visitedLetters = UtilIntSet.newIntSet();

    /**
     * compute the successor deckers for internal/call transition
     */
    private SuccessorResult computeSuccDoubleDeckers_CallOrInternal(IntSet predDoubleDeckers, int letter,
            boolean testTrans) {
        IntIterator iter = predDoubleDeckers.iterator();
        SuccessorResult resultSucc = new SuccessorResult();
        while (iter.hasNext()) {
            int doubleDecker = iter.next();
            int downState = mComplement.getDownState(doubleDecker);
            int upState = mComplement.getUpState(doubleDecker);
            IntSet upStateSuccs = null;
            IntSet succDeckers = null;

            boolean isInternalLetter = mComplement.getAlphabetInternal().get(letter);
            // generate all deckers (down, succ)
            if (isInternalLetter) {
                // internal (x, y) - l -> (x, d)
                upStateSuccs = mOperand.getSuccessorsInternal(upState, letter);
                succDeckers = mComplement.generateDeckers(downState, upStateSuccs);
            } else {
                // call (x, y) - l -> (y, d)
                upStateSuccs = mOperand.getSuccessorsCall(upState, letter);
                succDeckers = mComplement.generateDeckers(upState, upStateSuccs);
            }

            if (testTrans && noTransitionAssertion_MinusF(upState, upStateSuccs)) {
                resultSucc.hasSuccessor = false;
                return resultSucc;
            }

            resultSucc.mSuccs.or(succDeckers);
            if (testTrans) {
                if (mOperand.isFinal(upState)) {
                    resultSucc.mInterFSuccs.or(succDeckers);
                } else {
                    resultSucc.mMinusFSuccs.or(succDeckers);
                }
            }
        }
        return resultSucc;
    }

    private IntSet computeSuccessors(NCSB succNCSB, IntSet minusFSuccs, IntSet interFSuccs, int hier, int letter) {
        // d_a(S) /\ F and d_a(S) /\ must-in states should be empty
        if (succNCSB.getSSet().overlap(mComplement.getFinalDeckers()) || minusFSuccs.overlap(succNCSB.getSSet()))
            return UtilIntSet.newIntSet();

        SuccessorGenerator generator = new SuccessorGenerator(mNCSB.getBSet().isEmpty(), succNCSB, minusFSuccs,
                interFSuccs, mComplement.getFinalDeckers());
        IntSet succs = UtilIntSet.newIntSet();
        while (generator.hasNext()) {
            NCSB ncsb = generator.next();
            if (ncsb == null)
                continue;
            StateNwaNCSB succ = mComplement.addState(ncsb);
            if (mComplement.getAlphabetInternal().get(letter)) {
                super.addSuccessorInternal(letter, succ.getId());
            } else if (mComplement.getAlphabetCall().get(letter)) {
                super.addSuccessorCall(letter, succ.getId());
            } else {
                super.addSuccessorReturn(hier, letter, succ.getId());
            }
            succs.set(succ.getId());
        }

        return succs;
    }

    private IntSet computeSuccCallOrInternal(int letter) {

        IntSet minusFSuccs = UtilIntSet.newIntSet();
        IntSet interFSuccs = UtilIntSet.newIntSet();

        // Compute the successors of B
        SuccessorResult succResult = computeSuccDoubleDeckers_CallOrInternal(mNCSB.getBSet(), letter, true);
        if (!succResult.hasSuccessor)
            return UtilIntSet.newIntSet();
        IntSet BSuccs = succResult.mSuccs;
        minusFSuccs.or(succResult.mMinusFSuccs);
        interFSuccs.or(succResult.mInterFSuccs);

        // First compute the successors of C
        IntSet CMinusB = mNCSB.copyCSet();
        CMinusB.andNot(mNCSB.getBSet()); // C\B
        succResult = computeSuccDoubleDeckers_CallOrInternal(CMinusB, letter, !Options.lazyS);
        if (!succResult.hasSuccessor)
            return UtilIntSet.newIntSet();
        IntSet CSuccs = succResult.mSuccs;
        CSuccs.or(BSuccs);
        minusFSuccs.or(succResult.mMinusFSuccs);
        interFSuccs.or(succResult.mInterFSuccs);

        // Compute the successors of N
        succResult = computeSuccDoubleDeckers_CallOrInternal(mNCSB.getNSet(), letter, false);
        IntSet NSuccs = succResult.mSuccs;

        // Compute the successors of S
        succResult = computeSuccDoubleDeckers_CallOrInternal(mNCSB.getSSet(), letter, false);
        IntSet SSuccs = succResult.mSuccs;

        return computeSuccessors(new NCSB(NSuccs, CSuccs, SSuccs, BSuccs), minusFSuccs, interFSuccs, -1, letter);
    }

    @Override
    public IntSet getSuccessorsInternal(int letter) {
        assert mComplement.getAlphabetInternal().get(letter);
        if (super.getEnabledLettersInternal().contains(letter)) {
            return super.getSuccessorsInternal(letter);
        }
        return computeSuccCallOrInternal(letter);
    }

    /**
     * If q in C\F or (B\F), then tr(q, a) should not be not empty
     */
    private boolean noTransitionAssertion_MinusF(int upState, IntSet succs) {
        return !mOperand.isFinal(upState) && succs.isEmpty();
    }

    @Override
    public IntSet getSuccessorsCall(int letter) {
        assert mComplement.getAlphabetCall().get(letter);
        if (super.getEnabledLettersCall().contains(letter)) {
            return super.getSuccessorsCall(letter);
        }
        return computeSuccCallOrInternal(letter);
    }

    private IntSet computeSuccReturn(int hier, int letter) {

        StateNwaNCSB hierState = (StateNwaNCSB) mComplement.getState(hier);
        NCSB hierNCSB = hierState.getNCSB();

        IntSet minusFSuccs = UtilIntSet.newIntSet();
        IntSet interFSuccs = UtilIntSet.newIntSet();
        // Compute the successors of B
        TIntObjectMap<List<Integer>> hierDoubleDeckers = doubleDeckerSetToMap(hierNCSB);
        SuccessorResult succResult = computeSuccDoubleDeckers_Return(mNCSB.getBSet(), hierDoubleDeckers, letter, true);
        if (!succResult.hasSuccessor)
            return UtilIntSet.newIntSet();
        IntSet BSuccs = succResult.mSuccs;
        minusFSuccs.or(succResult.mMinusFSuccs);
        interFSuccs.or(succResult.mInterFSuccs);

        // First compute the successors of C
        IntSet CMinusB = mNCSB.getCSet().clone();
        CMinusB.andNot(mNCSB.getBSet()); // C\B

        succResult = computeSuccDoubleDeckers_Return(CMinusB, hierDoubleDeckers, letter, !Options.lazyS);
        if (!succResult.hasSuccessor)
            return UtilIntSet.newIntSet();
        IntSet CSuccs = succResult.mSuccs;
        CSuccs.or(BSuccs); // add successors of B
        minusFSuccs.or(succResult.mMinusFSuccs);
        interFSuccs.or(succResult.mInterFSuccs);

        // Compute the successors of N
        succResult = computeSuccDoubleDeckers_Return(mNCSB.getNSet(), hierDoubleDeckers, letter, false);
        if (!succResult.hasSuccessor)
            return UtilIntSet.newIntSet();
        IntSet NSuccs = succResult.mSuccs;

        // Compute the successors of S
        succResult = computeSuccDoubleDeckers_Return(mNCSB.getSSet(), hierDoubleDeckers, letter, false);
        if (!succResult.hasSuccessor)
            return UtilIntSet.newIntSet();
        IntSet SSuccs = succResult.mSuccs;

        return computeSuccessors(new NCSB(NSuccs, CSuccs, SSuccs, BSuccs), minusFSuccs, interFSuccs, hier, letter);
    }

    @Override
    public IntSet getSuccessorsReturn(int hier, int letter) {
        assert mComplement.getAlphabetReturn().get(letter);
        if (super.getEnabledLettersReturn().contains(letter) 
         && super.getEnabledHiersReturn(letter).contains(hier)) {
            return super.getSuccessorsReturn(hier, letter);
        }
        return computeSuccReturn(hier, letter);
    }

    private SuccessorResult computeSuccDoubleDeckers_Return(IntSet predDoubleDecker,
            TIntObjectMap<List<Integer>> predHierDoubleDeckerMap, int letter, boolean testTransition) {
        SuccessorResult succResult = new SuccessorResult();
        for (final int doubleDecker : predDoubleDecker.iterable()) {
            final int downState = mComplement.getDownState(doubleDecker);
            final int upState = mComplement.getUpState(doubleDecker);
            // predHier should contain all downState as its upState
            if (!predHierDoubleDeckerMap.containsKey(downState)) {
                succResult.hasSuccessor = false;
                return succResult;
            }
            // compute successors of return
            IntSet upStateSuccs = mOperand.getSuccessorsReturn(upState, downState, letter);
            if (testTransition && noTransitionAssertion_MinusF(upState, upStateSuccs)) {
                succResult.hasSuccessor = false;
                return succResult;
            }

            List<Integer> downHiers = predHierDoubleDeckerMap.get(downState);
            // put (upHier, succ)
            for (final Integer downHier : downHiers) {
                IntSet succDeckers = mComplement.generateDeckers(downHier, upStateSuccs);
                succResult.mSuccs.or(succDeckers);
                if (testTransition) {
                    if (mOperand.isFinal(upState)) {
                        succResult.mInterFSuccs.or(succDeckers);
                    } else {
                        succResult.mMinusFSuccs.or(succDeckers);
                    }
                }
            }
        }
        return succResult;
    }

    private TIntObjectMap<List<Integer>> doubleDeckerSetToMap(NCSB hierNCSB) {
        IntSet ncsb = hierNCSB.copyNSet();
        ncsb.or(hierNCSB.getCSet());
        ncsb.or(hierNCSB.getSSet());
        return doubleDeckerSetToMap(ncsb, false);
    }

    private TIntObjectMap<List<Integer>> doubleDeckerSetToMap(IntSet doubleDeckerSet
            , boolean keyIsDownState) {
        TIntObjectMap<List<Integer>> doubleDeckerMap = new TIntObjectHashMap<>();
        for (final int doubleDecker : doubleDeckerSet.iterable()) {
            final int downState = mComplement.getDownState(doubleDecker);
            final int upState = mComplement.getUpState(doubleDecker);
            List<Integer> temp = null;
            final int key = keyIsDownState ? downState : upState;
            final int value = !keyIsDownState ? downState : upState;
            if (doubleDeckerMap.containsKey(key)) {
                temp = doubleDeckerMap.get(key);
            } else {
                temp = new ArrayList<>();
            }
            temp.add(value);
            doubleDeckerMap.put(key, temp);
        }
        return doubleDeckerMap;
    }

    @Override
    public String toString() {

        return "(" + outputSet(mNCSB.getNSet()) + "," + outputSet(mNCSB.getCSet()) + "," + outputSet(mNCSB.getSSet())
                + "," + outputSet(mNCSB.getBSet()) + ")";
    }

    private String outputSet(IntSet set) {
        IntIterator iter = set.iterator();
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        boolean first = true;
        while (iter.hasNext()) {
            if (!first)
                builder.append(",");
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

}
