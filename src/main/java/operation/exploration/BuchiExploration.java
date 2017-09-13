package operation.exploration;

import operation.IUnaryOperation;

/**
 * This class is used to explore the state space of a given Buchi (nested) word automaton
 *  */
public abstract class BuchiExploration<T> implements IUnaryOperation<T, T>{
	
	protected final T mOperand;
	protected boolean mExplored;
	
	public BuchiExploration(T operand) {
		mOperand = operand;
		mExplored = false;
	}
	
	protected abstract void explore();
	
	@Override
	public T getOperand() {
		return mOperand;
	}

	@Override
	public T getResult() {
		if(! mExplored) explore();
		return mOperand;
	}
	
}