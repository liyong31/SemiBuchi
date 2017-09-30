package operation.difference.wa;

import automata.wa.IBuchiWa;

import main.Options;
import operation.exploration.wa.TarjanWaExploration;
import operation.intersection.wa.BuchiWaIntersection;
import operation.intersection.wa.GeneralizedBuchiIntersection;


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
