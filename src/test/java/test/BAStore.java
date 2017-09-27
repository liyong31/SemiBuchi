package test;

import automata.wa.BuchiWa;
import automata.wa.IStateWa;

public class BAStore {
	
	public static BuchiWa getA() {
		
		BuchiWa buchi = new BuchiWa(2);
		IStateWa aState = buchi.addState();
		IStateWa bState = buchi.addState();
		
		aState.addSuccessor(0, aState.getId());	
		aState.addSuccessor(0, bState.getId());		

		bState.addSuccessor(0, bState.getId());
//		bState.addSuccessor(0, aState.getId());
		bState.addSuccessor(1, aState.getId());
		bState.addSuccessor(0, aState.getId());
		
		buchi.setFinal(bState);
		buchi.setInitial(aState);
		
		return buchi;
	}
	
	public static BuchiWa getB() {
		BuchiWa buchi = new BuchiWa(2);
		IStateWa aState = buchi.addState();
		IStateWa bState = buchi.addState();
		
		aState.addSuccessor(0, bState.getId());		

		bState.addSuccessor(0, bState.getId());
		bState.addSuccessor(1, aState.getId());
		
		buchi.setFinal(bState);
		buchi.setInitial(aState);
		
		return buchi;
	}
	
	public static BuchiWa getC() {
		BuchiWa buchi = new BuchiWa(2);
		IStateWa aState = buchi.addState();
		IStateWa bState = buchi.addState();
		
		aState.addSuccessor(0, bState.getId());		

		bState.addSuccessor(1, aState.getId());
		
		buchi.setFinal(bState);
		buchi.setInitial(aState);
		return buchi;
	}
	
	// full set
	public static BuchiWa getD() {
		BuchiWa buchi = new BuchiWa(1);
		IStateWa aState = buchi.addState();
		
		aState.addSuccessor(0, aState.getId());		
		
		buchi.setFinal(aState);
		buchi.setInitial(aState);
		return buchi;
	}
	
	// empty
	public static BuchiWa getE() {
		BuchiWa buchi = new BuchiWa(1);
		IStateWa aState = buchi.addState();
		
		aState.addSuccessor(0, aState.getId());	
		
		buchi.setInitial(aState);
		return buchi;
	}

}
