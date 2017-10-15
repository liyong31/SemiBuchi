package operation.emptiness;

import java.util.LinkedList;
import java.util.List;

import automata.wa.IBuchiWa;
import automata.wa.IStateWa;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import util.IPair;
import util.IntSet;
import util.PairXX;
import util.UtilIntSet;


// only works for Buchi automata
public class LassoConstructor {
    
    private final IBuchiWa mBuchi;
    private final IntSet mSCC;
    private int mGoalState;
    
    private LinkedList<Integer> mStem;
    private LinkedList<Integer> mLoop;
    
    public LassoConstructor(IBuchiWa buchi, IntSet scc) {
        mBuchi = buchi;
        mSCC = scc;
        constructStem();
        constructLoop();
    }
    
    private void constructLoop() {
        // we construct a loop from the goal state
        IStateWa state = mBuchi.getState(mGoalState);
        TIntIntMap letterMap = new TIntIntHashMap();
        IntSet sources = UtilIntSet.newIntSet();
        for(int letter : state.getEnabledLetters()) {
            for(int succ : state.getSuccessors(letter).iterable()) {
                if(mGoalState == succ) {
                    // found a self loop
                    mLoop = new LinkedList<>();
                    mLoop.add(letter);
                    return ;
                }
                letterMap.put(succ, letter);
                sources.set(succ);
            }
        }
        // else we construct a path 
        IntSet target = UtilIntSet.newIntSet();
        target.set(mGoalState);
        RunConstructor rc = new RunConstructor(mBuchi, sources, target, false);
        mLoop = rc.getWord();
        List<Integer> run = rc.getRun();
        assert sources.get(run.get(0));
        mLoop.addFirst(letterMap.get(run.get(0)));
    }


    private void constructStem() {
        RunConstructor rc = new RunConstructor(mBuchi, mBuchi.getInitialStates(), mSCC, true);
        mStem = rc.getWord();
        mGoalState = rc.getGoalState();
    }

    public IPair<List<Integer>, List<Integer>> getAcceptingWord() {
        return new PairXX<>(mStem, mLoop);
    }

}
