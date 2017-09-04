package util;

import java.util.Random;

import automata.BuchiWa;
import automata.IBuchiWa;
import automata.IStateWa;

public class BuchiGenerator {
	
	public static IBuchiWa generate(int stateSize, int alphabetSzie) {
		
    	IBuchiWa result = new BuchiWa(alphabetSzie);

		Random r = new Random(System.currentTimeMillis());
		
		for(int i = 0; i < stateSize; i ++) {
			result.addState();
		}
		
		// add self loops for those transitions
		for(int i = 0; i < stateSize; i ++) {
			IStateWa state = result.getState(i);
			for(int k=0 ; k < alphabetSzie; k++){
				state.addSuccessor(k, i);
			}
		}
		
		result.setInitial(0);
		
		// final states
		int numF = r.nextInt(stateSize-1);
		boolean hasF = false;
		numF = numF > 0 ? numF : 1;
		for(int n = 0; n < numF ; n ++) {
			int f = r.nextInt(numF);
			if(f != 0) {
				result.setFinal(f);
				hasF = true;
			}
		}
		
		if(! hasF) {
			result.setFinal(numF);
		}
		
		int numTrans = r.nextInt(stateSize * alphabetSzie);
		
		// transitions
		for(int k=0 ; k < alphabetSzie; k++){
			for(int n = 0; n < numTrans; n++ ){
				int i=r.nextInt(stateSize);
				int j=r.nextInt(stateSize);
				result.getState(i).addSuccessor(k, j);
			}
		}
		return null;
	}

}
