package operation.difference.wa;

import automata.wa.IBuchiWa;

import main.Options;
import operation.exploration.wa.TarjanWaExploration;
import operation.intersection.wa.BuchiWaIntersection;
import operation.intersection.wa.GeneralizedBuchiIntersection;


public class BuchiWaDifference extends AbstractBuchiWaDifference {
	
	private final IBuchiWa mDifference;
	private TarjanWaExploration mTarjan; 
	
	public BuchiWaDifference(IBuchiWa fstOp, IBuchiWa sndOp) {
		super(fstOp, sndOp);
		if(Options.useGBA) {
			mDifference = new GeneralizedBuchiIntersection(fstOp, mSndComplement);
		}else {
			mDifference = new BuchiWaIntersection(fstOp, mSndComplement);
		}
	}
	
	private void initializeTarjan() {
		mTarjan = new TarjanWaExploration(mDifference);
	}

	@Override
	public IBuchiWa getResult() {
		if(mTarjan == null) 
			initializeTarjan();
		return mDifference;
	}

	@Override
	public Boolean isIncluded() {
		if(mTarjan == null) 
			initializeTarjan();
		return mTarjan.isEmpty();
	}

	@Override
	public String getOperationName() {
		return "DifferenceTarjan";
	}
}
