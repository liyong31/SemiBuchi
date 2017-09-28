package automata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.IntSet;
import util.UtilIntSet;

public class AccGenBuchi implements Acc {

    private final List<IntSet> mAccList;
    
    public AccGenBuchi() {
        mAccList = new ArrayList<>();
    }
    
    public AccGenBuchi(IntSet finalStates) {
        mAccList = new ArrayList<>();
        mAccList.add(finalStates);
    }
    
    @Override
    public boolean isAccepted(IntSet set) {
        for(int i = 0; i < mAccList.size(); i ++) {
            if(!set.overlap(mAccList.get(i))) return false;
        }
        return true;
    }

    @Override
    public List<IntSet> getAccs() {
        return Collections.unmodifiableList(mAccList);
    }

    @Override
    public IntSet getLabels(int state) {
        IntSet labels = UtilIntSet.newIntSet();
        for(int i = 0; i < mAccList.size(); i ++) {
            if(mAccList.get(i).get(state)) {
                labels.set(i);
            }
        }
        return labels;
    }
    
    public void setLabel(int state, int label) {
        while(mAccList.size() <= label) {
            mAccList.add(UtilIntSet.newIntSet());
        }
        mAccList.get(label).set(state);
    }
    
}
