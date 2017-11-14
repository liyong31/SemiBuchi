package complement.wa;

import java.util.ArrayList;
import java.util.List;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public class Antichain {
    private List<StateWaNCSB> antichain;

    
    public Antichain() {
        this.antichain = new ArrayList<>();
    }

    public boolean covers(StateWaNCSB state) {        
        for(StateWaNCSB elem : antichain) {
            if(state.getNCSB().coveredBy(elem.getNCSB())) {
                return true;
            }
        }
        return false;
    }

    public boolean add(StateWaNCSB state) {
        List<StateWaNCSB> copy = new ArrayList<>();
        //avoid to add pairs are covered by other pairs
        for(StateWaNCSB elem : antichain) {
            //pairs covered by the new pair
            //will not be kept in copy
            if(elem.getNCSB().coveredBy(state.getNCSB())) {
                continue;
            }else if(state.getNCSB().coveredBy(elem.getNCSB())) {
                return false;
            }
            copy.add(elem);
        }
        copy.add(state); // should add snd
        antichain = copy;
        return true;
    }
    
    
    public String toString() {
        return antichain.toString();
    }
    
    public int size() {
        return antichain.size();
    }

}
