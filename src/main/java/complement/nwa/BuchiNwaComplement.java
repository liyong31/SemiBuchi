package complement.nwa;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import automata.nwa.BuchiNwa;
import automata.nwa.IBuchiNwa;
import automata.nwa.IStateNwa;
import complement.NCSB;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import main.Options;
import util.IntIterator;
import util.IntSet;
import util.UtilIntSet;

/**
 * Only valid for semi-deterministic Buchi nesed word automata
 */
public class BuchiNwaComplement extends BuchiNwa implements IBuchiNwaComplement {

    private final IBuchiNwa mOperand;
    private final TObjectIntMap<DoubleDecker> mDeckerMap;
    private final List<DoubleDecker> mDeckerList;

    private final TObjectIntMap<StateNwaNCSB> mStateIndices = new TObjectIntHashMap<>();
    private final IntSet mFinalDeckers;

    public BuchiNwaComplement(IBuchiNwa buchi) {
        super(buchi.getAlphabetCall(), buchi.getAlphabetInternal(), buchi.getAlphabetReturn());
        this.mOperand = buchi;
        this.mDeckerMap = new TObjectIntHashMap<>();
        this.mDeckerList = new ArrayList<>();
        this.mFinalDeckers = UtilIntSet.newIntSet();
        computeInitialStates();
    }

    private void computeInitialStates() {
        IntSet upC = mOperand.getInitialStates().clone();
        upC.and(mOperand.getFinalStates()); // goto C
        IntSet upN = mOperand.getInitialStates().clone();
        upN.andNot(upC);
        IntSet N = generateDeckers(DoubleDecker.EMPTY_DOWN_STATE, upN);
        IntSet C = generateDeckers(DoubleDecker.EMPTY_DOWN_STATE, upC);
        NCSB ncsb = new NCSB(N, C, UtilIntSet.newIntSet(), C);
        StateNwaNCSB state = new StateNwaNCSB(this, 0, ncsb);
        if (C.isEmpty())
            this.setFinal(0);
        this.setInitial(0);
        int id = this.addState(state);
        mStateIndices.put(state, id);
    }

    protected StateNwaNCSB addState(NCSB ncsb) {

        StateNwaNCSB state = new StateNwaNCSB(this, 0, ncsb);

        if (mStateIndices.containsKey(state)) {
            return (StateNwaNCSB) getState(mStateIndices.get(state));
        } else {
            int index = getStateSize();
            StateNwaNCSB newState = new StateNwaNCSB(this, index, ncsb);
            int id = this.addState(newState);
            mStateIndices.put(newState, id);
            if (ncsb.getBSet().isEmpty())
                setFinal(index);
            return newState;
        }
    }

    @Override
    public IBuchiNwa getOperand() {
        return mOperand;
    }

    @Override
    public DoubleDecker getDoubleDecker(int id) {
        assert id < mDeckerList.size();
        return mDeckerList.get(id);
    }

    @Override
    public int getDoubleDeckerId(DoubleDecker decker) {
        if (mDeckerMap.containsKey(decker)) {
            return mDeckerMap.get(decker);
        }
        int id = mDeckerList.size();
        mDeckerList.add(decker);
        mDeckerMap.put(decker, id);
        if (mOperand.isFinal(decker.getUpState())) {
            mFinalDeckers.set(id);
        }
        return id;
    }

    protected IntSet getFinalDeckers() {
        return mFinalDeckers;
    }

    protected IntSet generateDeckers(int downState, IntSet upStates) {
        IntSet result = UtilIntSet.newIntSet();
        for (final int upState : upStates.iterable()) {
            result.set(getDoubleDeckerId(new DoubleDecker(downState, upState)));
        }
        return result;
    }

    protected int getUpState(int decker) {
        return getDoubleDecker(decker).getUpState();
    }

    protected int getDownState(int decker) {
        return getDoubleDecker(decker).getDownState();
    }

    // ------------------- following is not needed
    private boolean mExplored = false;

    // TODO: removed
    public void explore() {

        if (mExplored)
            return;

        mExplored = true;

        LinkedList<IStateNwa> walkList = new LinkedList<>();
        IntSet initialStates = getInitialStates();

        IntIterator iter = initialStates.iterator();
        while (iter.hasNext()) {
            walkList.addFirst(getState(iter.next()));
        }

        BitSet visited = new BitSet();

        Set<Integer> callPreds = new HashSet<>();

        while (!walkList.isEmpty()) {
            IStateNwa s = walkList.remove();
            if (visited.get(s.getId()))
                continue;
            visited.set(s.getId());
            if (Options.verbose)
                System.out.println("s" + s.getId() + ": " + s.toString());
            // call alphabets
            IntSet calls = mOperand.getAlphabetCall();
            IntIterator iterLetter = calls.iterator();
            while (iterLetter.hasNext()) {
                int letter = iterLetter.next();
                IntSet succs = s.getSuccessorsCall(letter);
                if (!succs.isEmpty())
                    callPreds.add(s.getId());
                exploreSuccessors(s, walkList, succs, visited, letter);
            }

            iterLetter = mOperand.getAlphabetInternal().iterator();
            while (iterLetter.hasNext()) {
                int letter = iterLetter.next();
                if (s.getId() == 6 && letter == 4) {
                    System.out.println("HAHA");
                }
                IntSet succs = s.getSuccessorsInternal(letter);
                exploreSuccessors(s, walkList, succs, visited, letter);
            }

            iterLetter = mOperand.getAlphabetReturn().iterator();

            while (iterLetter.hasNext()) {
                // System.out.println(callPreds);
                int letter = iterLetter.next();
                //
                int size = getStateSize();
                for (int i = 0; i < size; i++) {
                    for (Integer hier : callPreds) {
                        IStateNwa state = getState(i);
                        if (Options.verbose)
                            System.out.println("current: " + state.toString() + "  hier: " + hier + " :"
                                    + getState(hier).toString());
                        IntSet succs = state.getSuccessorsReturn(hier, letter);
                        exploreSuccessors(state, walkList, succs, visited, letter);
                    }
                }

            }
        }

        // System.out.println("" + getStates());
    }

    private void exploreSuccessors(IStateNwa s, LinkedList<IStateNwa> walkList, IntSet succs, BitSet visited,
            int letter) {
        IntIterator iter = succs.iterator();
        while (iter.hasNext()) {
            int n = iter.next();
            // if(Options.verbose) System.out.println("size of deckers: " +
            // mDeckerList.size() + " " + mDeckerList.toString());
            if (Options.verbose)
                System.out.println(
                        " s" + s.getId() + ": " + s.toString() + "- L" + letter + " -> s" + n + ": " + getState(n));
            if (!visited.get(n)) {
                walkList.addFirst(getState(n));
            }
        }
    }

}
