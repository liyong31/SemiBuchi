package operation.exploration.wa;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import automata.AccGenBuchi;
import automata.wa.GeneralizedBuchiWa;
import automata.wa.IBuchiWa;
import automata.wa.IStateWa;
import complement.wa.BuchiWaComplement;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import util.IntSet;
import util.MarkedIntStack;


import util.UtilIntSet;

public class AntichainDifferenceExploration {
    
    private final GeneralizedBuchiWa mDifference;
    private final IBuchiWa mFstOperand;
    private final BuchiWaComplement mSndComplement;
    private final AccGenBuchi mAcceptance;
    private AntichainASCCExploration mAntichainExploration;
    
    public AntichainDifferenceExploration(IBuchiWa fstOp, BuchiWaComplement sndComplement) {
        mFstOperand = fstOp;
        mSndComplement = sndComplement;
        mDifference = new GeneralizedBuchiWa(fstOp.getAlphabetSize());
        mAcceptance = mDifference.getAcceptance();
        computeInitialStates();
    }
    
    private void computeInitialStates() {
        for(final int fst : mFstOperand.getInitialStates().iterable()) {
            for(final int snd : mSndComplement.getInitialStates().iterable()) {
                DifferencePair pair = new DifferencePair(mSndComplement, fst, snd);
                IStateWa state = getOrAddState(pair);
                mDifference.setInitial(state);
            }
        }
    }

    public IBuchiWa getDifference() {
        if(mAntichainExploration == null) {
            mAntichainExploration = new AntichainASCCExploration();
        }
        return mDifference;
    }
    
    public Boolean isEmpty() {
        if(mAntichainExploration == null) {
            mAntichainExploration = new AntichainASCCExploration();
        }
        return mAntichainExploration.mIsEmpty;
    }
    
    protected final Map<DifferencePair, IStateWa> mPairStateMap = new HashMap<>();
    protected final List<DifferencePair> mPairArray = new ArrayList<>();
    
    protected IStateWa getOrAddState(DifferencePair pair) {
        IStateWa state = mPairStateMap.get(pair);
        if(state == null) {
            state = mDifference.addState();
            mPairStateMap.put(pair, state);
            mPairArray.add(pair);
            computeAcceptance(state.getId(), pair);
        }
        return state;
    }
    
    private void computeAcceptance(int state, DifferencePair pair) {
        // acceptance condition
        IntSet labels = mFstOperand.getAcceptance().getLabels(pair.getFirstState());
        for(final int label : labels.iterable()) {
            mAcceptance.setLabel(state, label);
        }
        if(mSndComplement.isFinal(pair.getSecondState())) {
            mAcceptance.setLabel(state, mFstOperand.getAcceptance().getAccSize());
        }
    }
    
    private boolean isAccepting(IntSet labels) {
        return labels.cardinality() == mFstOperand.getAcceptance().getAccSize() + 1;
    }
    
    // generalized Buchi exploration
    
    private class StackPair {
        final int mState;
        final IntSet mLabels;
        StackPair(int state, IntSet labels) {
            mState = state;
            mLabels = labels;
        }
        
        int getFirstState() {
            return mState;
        }
        
        IntSet getLabels() {
            return mLabels;
        }
    }
    
    private class AntichainASCCExploration {
        
        private int mIndex=0;
        private final Stack<StackPair> mRootsStack ;
        private final TIntIntMap mDfsNum;
        private final MarkedIntStack mActiveStack ;
        private final IntSet mCurrent;
        
        private Boolean mIsEmpty = null;
                
        private final Antichain mAntichain;
        
        public AntichainASCCExploration() {
            this.mRootsStack = new Stack<>();
            this.mActiveStack = new MarkedIntStack();
            this.mDfsNum = new TIntIntHashMap();
            this.mCurrent = UtilIntSet.newIntSet();
            this.mAntichain = new Antichain();
            
            for(int init : mDifference.getInitialStates().iterable()) {
                if(! mDfsNum.containsKey(init)){
                    dfs(init);
                }
            }
            
            if(mIsEmpty == null) {
                mIsEmpty = true;
            }
        }
        
        // make use of tarjan algorithm, only for Buchi and Buchi
        void dfs(int s) {
            
            mIndex++;
            mDfsNum.put(s, mIndex);
            mActiveStack.push(s);
            mCurrent.set(s);
            
            DifferencePair pair = mPairArray.get(s); //v must in mPairArray
            // get state labels
            IntSet labels = UtilIntSet.newIntSet();
            labels.or(mFstOperand.getAcceptance().getLabels(pair.getFirstState()));
            if(mSndComplement.isFinal(pair.getSecondState())) {
                labels.set(mFstOperand.getAcceptance().getAccSize());
            }
            mRootsStack.push(new StackPair(s, labels));
            
            for(int letter = 0; letter < mFstOperand.getAlphabetSize(); letter ++) {
                IStateWa fstState = mFstOperand.getState(pair.getFirstState());
                for(int fstSucc : fstState.getSuccessors(letter).iterable()) {
                    IStateWa sndState = mSndComplement.getState(pair.getSecondState());
                    for(int sndSucc : sndState.getSuccessors(letter).iterable()) {            
                        // pair (X, Y)
                        DifferencePair pairSucc = new DifferencePair(mSndComplement, fstSucc, sndSucc);
                        IStateWa succState = getOrAddState(pairSucc);
                        // update mDifference successors
                        mPairStateMap.get(pair).addSuccessor(letter, succState.getId());
                        //OPT1, if we visited a state which covers this state
                        if(mAntichain.covers(pairSucc)) {
                            continue;
                        }
                        
                        if(! mDfsNum.containsKey(succState.getId())){
                            dfs(succState.getId());
                        }else if(mCurrent.get(succState.getId())){
                            IntSet B = UtilIntSet.newIntSet();
                            int u;
                            do {
                                StackPair p = mRootsStack.pop();
                                B.or(p.getLabels());
                                u = p.getFirstState();
                                if(isAccepting(B)) {
                                    mIsEmpty = false;
                                }
                            }while(mDfsNum.get(u) > mDfsNum.get(succState.getId()));
                            mRootsStack.push(new StackPair(u, B));
                        }
                    }
                }
            }

            if(mRootsStack.peek().getFirstState() == s){
                
                mRootsStack.pop();
                int u;
                do {
                    if(mActiveStack.empty()) break;
                    u = mActiveStack.pop();
                    mCurrent.clear(u);
                    //cache all nodes which has empty language
                    mAntichain.addPair(mPairArray.get(u));
                }while(u != s); 
                
                // OPT2, backjump to depth i
                for(int i = 0; i < mActiveStack.size(); i ++) {
                    int t = mActiveStack.get(i);
                    DifferencePair p = mPairArray.get(t);
                    if(t != s && p.coveredBy(pair)) {
                        int e;
                        do {
                            if(mActiveStack.empty()) break;
                            e = mActiveStack.pop();
                            mCurrent.clear(e);
                            //cache all nodes which has empty language
                            mAntichain.addPair(mPairArray.get(e));
                        }while(t != e);
                    }
                }
                
            }
        }
        
    }

    
}
