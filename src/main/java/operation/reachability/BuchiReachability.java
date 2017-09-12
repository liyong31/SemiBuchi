package operation.reachability;

import operation.IUnaryOperation;

public abstract class BuchiReachability<T> implements IUnaryOperation<T, T>{
	
	protected final T mOperand;
	protected boolean mExplored;
	
	public BuchiReachability(T operand) {
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
