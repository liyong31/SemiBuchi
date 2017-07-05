package operation.inclusion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import complement.StateNCSB;

public class Antichain {
	
	private Map<Integer, List<StateNCSB>> mPairMap;
	
	public Antichain() {
		mPairMap = new HashMap<>();
	}
	
	/**
	 * return true if @param snd has been added successfully
	 * */
	public boolean addPair(InclusionPairNCSB pair) {
		return addPair(pair.getFstElement(), pair.getSndElement());
	}
	
	public boolean addPair(int fst, StateNCSB snd) {
		
		List<StateNCSB> sndElem = mPairMap.get(fst);
		
		if(sndElem == null) {
			sndElem = new ArrayList<>();
		}
		List<StateNCSB> copy = new ArrayList<>();
		//avoid to add pairs are covered by other pairs
		for(int i = 0; i < sndElem.size(); i ++) {
			StateNCSB s = sndElem.get(i);
			//pairs covered by the new pair
			//will not be kept in copy
			if(s.coveredBy(snd)) { 
				continue;
			}else if(snd.coveredBy(s)) {
				// no need to add it
				return false;
			}
			copy.add(s);
		}
		
		copy.add(snd); // should add snd
		mPairMap.put(fst, copy);
		return true;
	}
	
	public boolean covers(InclusionPairNCSB pair) {
		List<StateNCSB> sndElem = mPairMap.get(pair.getFstElement());
		if(sndElem == null) return false;
		
		StateNCSB snd = pair.getSndElement();
		for(int i = 0; i < sndElem.size(); i ++) {
			StateNCSB s = sndElem.get(i);
			if(snd.coveredBy(s)) { // no need to add it
				return true;
			}
		}
		
		return false;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Entry<Integer, List<StateNCSB>> entry : mPairMap.entrySet()) {
			sb.append(entry.getKey() + " -> " + entry.getValue() + "\n");
		}
		return sb.toString();
	}

}
