package operation.difference.wa;

import automata.wa.IBuchiWa;

import main.Options;
import operation.exploration.wa.AsccWaExploration;
import operation.intersection.wa.BuchiWaIntersection;
import operation.intersection.wa.GeneralizedBuchiIntersection;


public class BuchiWaDifferenceAscc extends AbstractBuchiWaDifference {
	
	private AsccWaExploration mAscc; 
	
	public BuchiWaDifferenceAscc(IBuchiWa fstOp, IBuchiWa sndOp) {
		super(fstOp, sndOp);
		if(Options.useGBA) {
			mDifference = new GeneralizedBuchiIntersection(fstOp, mSndComplement);
		}else {
			mDifference = new BuchiWaIntersection(fstOp, mSndComplement);
		}
	}
	
	private void initializeAscc() {
		mAscc = new AsccWaExploration(mDifference);
	}

	@Override
	public Boolean isIncluded() {
		if(mAscc == null) 
		    initializeAscc();
		return mAscc.isEmpty();
	}

	@Override
	public String getOperationName() {
		return "DifferenceAscc";
	}
}
