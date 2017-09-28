package operation.exploration.wa;

import java.util.ArrayList;
import java.util.List;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public class Antichain {
    
    private TIntObjectMap<List<DifferencePair>> mPairMap;
    
    public Antichain() {
        mPairMap = new TIntObjectHashMap<>();
    }
    
    public boolean addPair(DifferencePair pair) {
        
        final int fstState = pair.getFirstState();
        List<DifferencePair> sndElem = mPairMap.get(fstState);
        
        if(sndElem == null) {
            sndElem = new ArrayList<>();
        }
        List<DifferencePair> copy = new ArrayList<>();
        //avoid to add pairs are covered by other pairs
        for(int i = 0; i < sndElem.size(); i ++) {
            DifferencePair elem = sndElem.get(i);
            //pairs covered by the new pair
            //will not be kept in copy
            if(elem.coveredBy(pair)) {
                continue;
            }else if(pair.coveredBy(elem)) {
                return false;
            }
            copy.add(elem);
        }
        
        copy.add(pair); // should add snd
        mPairMap.put(fstState, copy);
        return true;
    }
    
    public boolean covers(DifferencePair pair) {
        List<DifferencePair> sndElem = mPairMap.get(pair.getFirstState());
        if(sndElem == null) return false;
        for(int i = 0; i < sndElem.size(); i ++) {
            DifferencePair elem = sndElem.get(i);
            if(pair.coveredBy(elem)) { // no need to add it
                return true;
            }
        }
        return false;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        TIntObjectIterator<List<DifferencePair>> iter = mPairMap.iterator();
        while(iter.hasNext()) {
            iter.advance();
            sb.append(iter.key() + " -> " + iter.value() + "\n");
        }
        return sb.toString();
    }
    
    public int size() {
        int num = 0;
        TIntObjectIterator<List<DifferencePair>> iter = mPairMap.iterator();
        while(iter.hasNext()) {
            iter.advance();
            num += iter.value().size();
        }
        return num;
    }

}
