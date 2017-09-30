package operation.difference.wa;

import automata.wa.IBuchiWa;

import operation.exploration.wa.AsccWaExploration;

public class BuchiWaDifferenceAscc extends BuchiWaDifference {
	
	public BuchiWaDifferenceAscc(IBuchiWa fstOp, IBuchiWa sndOp) {
		super(fstOp, sndOp);
	}
	
	@Override
	protected void initializeExplorer() {
	    mExplorer = new AsccWaExploration(mDifference);
	}

	@Override
	public String getOperationName() {
		return "DifferenceAscc";
	}
}
