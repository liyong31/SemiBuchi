package operation.inclusion.wa;

import java.util.ArrayList;
import java.util.List;

import complement.wa.StateWaNCSB;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;


public class Antichain {
	
	private TIntObjectMap<List<StateWaNCSB>> mPairMap;
	
	public Antichain() {
		mPairMap = new TIntObjectHashMap<>();
	}
	
	/**
	 * return true if @param snd has been added successfully
	 * */
	public boolean addPair(InclusionPairNCSB pair) {
		return addPair(pair.getFstElement(), pair.getSndElement());
	}
	
	public boolean addPair(int fst, StateWaNCSB snd) {
		
		List<StateWaNCSB> sndElem = mPairMap.get(fst);
		
		if(sndElem == null) {
			sndElem = new ArrayList<>();
		}
		List<StateWaNCSB> copy = new ArrayList<>();
		//avoid to add pairs are covered by other pairs
		for(int i = 0; i < sndElem.size(); i ++) {
			StateWaNCSB s = sndElem.get(i);
			//pairs covered by the new pair
			//will not be kept in copy
			if(s.getNCSB().coveredBy(snd.getNCSB())) {
//				mTask.increaseDelPairInAntichain();
				continue;
			}else if(snd.getNCSB().coveredBy(s.getNCSB())) {
				// no need to add it
//				mTask.increaseRejPairByAntichain();
				return false;
			}
			copy.add(s);
		}
		
		copy.add(snd); // should add snd
		mPairMap.put(fst, copy);
		return true;
	}
	
	public boolean covers(InclusionPairNCSB pair) {
		List<StateWaNCSB> sndElem = mPairMap.get(pair.getFstElement());
		if(sndElem == null) return false;
		
		StateWaNCSB snd = pair.getSndElement();
		for(int i = 0; i < sndElem.size(); i ++) {
			StateWaNCSB s = sndElem.get(i);
			if(snd.getNCSB().coveredBy(s.getNCSB())) { // no need to add it
				return true;
			}
		}
		
		return false;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
//		for(Entry<Integer, List<StateNCSB>> entry : mPairMap.entrySet()) {
//			sb.append(entry.getKey() + " -> " + entry.getValue() + "\n");
//		}
		TIntObjectIterator<List<StateWaNCSB>> iter = mPairMap.iterator();
		while(iter.hasNext()) {
			iter.advance();
			sb.append(iter.key() + " -> " + iter.value() + "\n");
		}
		return sb.toString();
	}
	
	public int size() {
		int num = 0;
//		for(Map.Entry<Integer, List<StateNCSB>> entry : mPairMap.entrySet()) {
//			num += entry.getValue().size();
//		}
		TIntObjectIterator<List<StateWaNCSB>> iter = mPairMap.iterator();
		while(iter.hasNext()) {
			iter.advance();
			num += iter.value().size();
		}
		return num;
	}

}
