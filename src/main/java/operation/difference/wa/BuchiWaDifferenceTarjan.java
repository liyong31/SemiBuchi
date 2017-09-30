package operation.difference.wa;

import automata.wa.IBuchiWa;

import operation.exploration.wa.TarjanWaExploration;

public class BuchiWaDifferenceTarjan extends BuchiWaDifference {
	
	public BuchiWaDifferenceTarjan(IBuchiWa fstOp, IBuchiWa sndOp) {
		super(fstOp, sndOp);
	}
	
	@Override
	protected void initializeExplorer() {
	    mExplorer = new TarjanWaExploration(mDifference);
	}

	@Override
	public String getOperationName() {
		return "DifferenceTarjan";
	}
}
