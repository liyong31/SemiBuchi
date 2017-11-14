package complement.wa;

import java.util.Stack;

import automata.wa.BuchiWa;
import automata.wa.IStateWa;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import util.IntSet;
import util.UtilIntSet;

public class OndraComplementExplore { 

    Stack<AsccElem> mSCCs;
    Stack<Integer> mAct;
    TIntIntMap mDfsNum;
    int mCnt;
    Antichain mEmp;
    IntSet mQPrime;
    BuchiWaComplement mOperand;
    Boolean mIsEmpty;
    
    int numOfStates = 0;
    int numOfTrans = 0;
    
    public OndraComplementExplore(BuchiWaComplement operand) {
        mOperand = operand;
        
        mSCCs = new Stack<>();
        mAct = new Stack<>();
        mCnt = 0;
        mEmp = new Antichain();
        mQPrime = UtilIntSet.newIntSet();
        mDfsNum = new TIntIntHashMap();
        
        boolean is_nemp = false;
        
        for(final int init : mOperand.getInitialStates().iterable()) {
            if(!mDfsNum.containsKey(init)) {
                boolean result = construct(init);
                is_nemp = result || is_nemp;
            }
        }

        mIsEmpty= !is_nemp;
//        new Explore(mOperand);
//        for(int s : mEmp) {
//            assert !mQPrime.get(s) : "Wrong state in mQPrime";
//            // check whether this state can reach any accepting loop
//            IGba gba = copyGba(mOperandGBA);
//            gba.setInitial(s);
//            Tarjan tarjan = new Tarjan(gba);
//            assert tarjan.isEmpty() : "not empty language";
//        }
        
    }
    
    class AsccElem {
        int mState;
        boolean mIsFinal;
        AsccElem(int state, boolean isFinal) {
            mState = state;
            mIsFinal = isFinal;
        }
        
        public String toString() {
            return "(" + mState + "," + mIsFinal + ")";
        }
    }
    
    private BuchiWa copyGba(BuchiWa operand) {
        BuchiWa copy = new BuchiWa(operand.getAlphabetSize());
        for(int i = 0; i < operand.getStateSize(); i ++) {
            copy.addState();
        }
        // copy states
        for(int i = 0; i < operand.getStateSize(); i ++) {
            IStateWa state = operand.getState(i);
            IStateWa copyState = copy.getState(i);
            for(int letter = 0; letter < operand.getAlphabetSize(); letter ++) {
                for(final int t : state.getSuccessors(letter).iterable()) {
                    copyState.addSuccessor(letter, t);
                }
            }
            if(operand.isFinal(i)) {
                copy.setFinal(i);
            }
        }
        return copy;
    }
    
    private StateWaNCSB getStateWaNCSB(int id) {
        return (StateWaNCSB) mOperand.getState(id);
    }
    
    private boolean construct(int s) {
        IStateWa state = mOperand.getState(s);
        ++ mCnt;
        mDfsNum.put(s, mCnt);
        mSCCs.push(new AsccElem(s, mOperand.isFinal(s)));
        mAct.push(s);
        boolean is_nemp = false;
        for(int letter = 0; letter < mOperand.getAlphabetSize(); letter ++) {
            for(final int t : state.getSuccessors(letter).iterable()) {
                if(mQPrime.get(t)) {
                    is_nemp = true;
                }else if(mEmp.covers(getStateWaNCSB(t))) {
                    continue;
                }else if(! mAct.contains(t)) {
                    boolean result = construct(t);
                    is_nemp = result || is_nemp;
                }else {
                    boolean B = false;
                    int u;
                    do {
                        AsccElem p = mSCCs.pop();
                        u = p.mState;
                        B = B || p.mIsFinal;
                        if(B) {
                            is_nemp = true;
                        }
                    }while(mDfsNum.get(u) > mDfsNum.get(t));
                    mSCCs.push(new AsccElem(u, B));
                }
            }
        }
        
        if(mSCCs.peek().mState == s) {
            mSCCs.pop();
            int u = 0;
            do {
                assert !mAct.isEmpty() : "Act empty";
                u = mAct.pop();
                if(is_nemp) {
                    mQPrime.set(u);
                }else {
                    mEmp.add(getStateWaNCSB(u));
                }
            }while(u != s);
        }
        
        return is_nemp;
    }

}
