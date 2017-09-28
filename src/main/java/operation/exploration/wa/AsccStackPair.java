package operation.exploration.wa;

import util.IntSet;

class AsccStackPair {
    
    final int mState;
    final IntSet mLabels;
    AsccStackPair(int state, IntSet labels) {
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
