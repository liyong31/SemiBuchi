package operation.emptiness;

import java.util.LinkedList;
import java.util.PriorityQueue;

import automata.wa.IBuchiWa;
import automata.wa.IStateWa;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import util.IntSet;
import util.UtilIntSet;

class RunConstructor {
    
    private final IBuchiWa mBuchi;
    private final IntSet mSources;
    private final IntSet mTargets;
    private int mGoal;
    private LinkedList<Integer> mWord;
    private LinkedList<Integer> mRun;
    private final TIntObjectMap<SuccessorInfo> mSuccInfo;
    private final boolean mGoalIsFinal;
    
    RunConstructor(IBuchiWa buchi, IntSet source
                  , IntSet target, boolean goalIsFinal) {
        mBuchi = buchi;
        mSources = source;
        mTargets = target;
        mSuccInfo = new TIntObjectHashMap<>();
        mGoalIsFinal = goalIsFinal;
    }
    
    LinkedList<Integer> getWord() {
        if(mWord == null) {
            search();
            construct();
        }
        return mWord;
    }
    
    LinkedList<Integer> getRun() {
        if(mRun == null) {
            search();
            construct();
        }
        return mRun;
    }
    
    int getGoalState() {
        return mGoal;
    }

    private void construct() {
        // construct the run and word
        SuccessorInfo currInfo = getSuccessorInfo(mGoal);
        mWord = new LinkedList<>();
        mRun = new LinkedList<>();
        while(! mSources.get(currInfo.mState)) {
            mRun.addFirst(currInfo.mState);
            mWord.addFirst(currInfo.mLetter);
            currInfo = getSuccessorInfo(currInfo.mPreState);
        }
        mRun.addFirst(currInfo.mState);
    }
    
    private SuccessorInfo getSuccessorInfo(int state) {
        if(mSuccInfo.containsKey(state)) {
            return mSuccInfo.get(state);
        }
        SuccessorInfo succInfo = new SuccessorInfo(state);
        mSuccInfo.put(state, succInfo);
        return succInfo;
    }

    private void search() {
        PriorityQueue<SuccessorInfo> workList = new PriorityQueue<>();
        // input source states
        for(int state : mSources.iterable()) {
            SuccessorInfo succInfo = getSuccessorInfo(state);
            workList.add(succInfo);
            succInfo.mDistance = 0;
        }
        
        IntSet visited = UtilIntSet.newIntSet();
        while(! workList.isEmpty()) {
            SuccessorInfo currInfo = workList.remove(); 
            if(visited.get(currInfo.mState)) {
                continue;
            }
            if(isGoalState(currInfo.mState)) {
                mGoal = currInfo.mState;
                break;
            }
            if(currInfo.unreachable()) {
                assert false : "Unreachable state";
            }
            // update distance of successors
            IStateWa state = mBuchi.getState(currInfo.mState);
            for(final int letter : state.getEnabledLetters()) {
                for(int succ : state.getSuccessors(letter).iterable()) {
                    SuccessorInfo succInfo = getSuccessorInfo(succ);
                    int distance = currInfo.mDistance + 1;
                    if(!visited.get(succ) && succInfo.mDistance > distance) {
                        // update distance
                        succInfo.mLetter = letter;
                        succInfo.mDistance = distance;
                        succInfo.mPreState = state.getId();
                        workList.remove(succInfo);
                        workList.add(succInfo);
                    }
                }
            }
        }
        
    }
    
    private boolean isGoalState(int state) {
        if(! mTargets.get(state))
            return false;
        if(mGoalIsFinal) {
            return mBuchi.isFinal(state);
        }
        return true;
    }
}
