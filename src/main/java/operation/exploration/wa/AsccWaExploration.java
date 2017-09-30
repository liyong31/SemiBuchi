package operation.exploration.wa;

import automata.wa.IBuchiWa;
import automata.wa.IStateWa;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import main.Options;
import util.IntSet;
import util.IntStack;
import util.UtilIntSet;


public class AsccWaExploration implements EmptinessExploration{
    
	private final IBuchiWa mBuchi;
	private Ascc mAscc;
	
	public AsccWaExploration(IBuchiWa buchi) {
		mBuchi = buchi;
	}
	
	@Override
	public Boolean isEmpty() {
	    if(mAscc == null) {
	        mAscc = new Ascc();
	    }
		return mAscc.mIsEmpty;
	}

    private class Ascc {
        
        private int mDepth;
        private final IntStack mRootsStack;             // C99 's root stack
        private final IntStack mActiveStack;            // tarjan's stack
        private final TIntIntMap mDfsNum;
        private final IntSet mCurrent;
        
        private Boolean mIsEmpty = null;
                
        public Ascc() {
            
            this.mRootsStack = new IntStack();
            this.mActiveStack = new IntStack();
            this.mDfsNum = new TIntIntHashMap();
            this.mCurrent = UtilIntSet.newIntSet();
            
            for(int n : mBuchi.getInitialStates().iterable()) {
                if(! mDfsNum.containsKey(n)){
                    strongConnect(n);
                }
            }
            
            if(mIsEmpty == null) {
                mIsEmpty = true;
            }
        }
        
        void strongConnect(int n) {
            
            ++ mDepth;
            mDfsNum.put(n, mDepth);
            mRootsStack.push(n);
            mActiveStack.push(n);
            mCurrent.set(n);
            
            IStateWa state = mBuchi.getState(n);
            for(int letter = 0; letter < mBuchi.getAlphabetSize(); letter ++) {
                for(int succ : state.getSuccessors(letter).iterable()) {
                    if(! mDfsNum.containsKey(succ)) {
                        strongConnect(succ);
                    }else if(mCurrent.get(succ)) {
                        // we have already seen it before, there is a loop
                        IntSet scc = UtilIntSet.newIntSet();
                        while(true) {
                            //pop element u
                            int u = mRootsStack.pop();
                            scc.set(u);
                            // found one accepting scc
                            if(mBuchi.getAcceptance().isAccepted(scc)) {
                                mIsEmpty = false;
                                if(Options.verbose) System.out.println("ACC: " + scc);
                            }
                            if(mDfsNum.get(u) <= mDfsNum.get(succ)) {
                                mRootsStack.push(u); // push back
                                break;
                            }
                        }
                    }
                }
            }
            
            // if current number is done, 
            // then we should remove all 
            // active states in the same scc
            if(mRootsStack.peek() == n) {
                mRootsStack.pop();
                while(true) {
                    int u = mActiveStack.pop(); // Tarjan' Stack
                    mCurrent.clear(u);
                    if(u == n) break;
                }
            }
        }
        
    }

}
