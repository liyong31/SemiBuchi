package operation.universality;


import java.util.List;

import automata.IBuchiWa;
import complement.BuchiWaComplement;
import operation.emptiness.BuchiIsEmptyASCC;
import operation.emptiness.IBuchiWaIsEmpty;
import util.IPair;

public abstract class BuchiUniversality implements IBuchiWaUniversality {
	
	protected final IBuchiWa mBuchi;
	protected final BuchiWaComplement mBuchiComplement;
	protected IBuchiWaIsEmpty mEmptinessChecker;
	
	public BuchiUniversality(IBuchiWa buchi) {
		this.mBuchi = buchi;
		this.mBuchiComplement = new BuchiWaComplement(buchi);
		initializeEmptinessChecker();
	}

	@Override
	public IBuchiWa getOperand() {
		return mBuchi;
	}
	
	protected abstract void initializeEmptinessChecker();
	
	@Override
	public Boolean getResult() {
		assert mEmptinessChecker != null;
		return mEmptinessChecker.getResult();
	}

	@Override
	public IPair<List<Integer>, List<Integer>> getCounterexampleWord() {
		// TODO Auto-generated method stub
		return null;
	}

}
