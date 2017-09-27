package operation.difference.wa;

import java.util.List;

import automata.wa.IBuchiWa;
import automata.wa.IStateWa;
import complement.wa.BuchiWaComplement;
import complement.wa.IBuchiWaComplement;
import main.Options;
import operation.exploration.wa.TarjanWaExploration;
import operation.intersection.wa.BuchiWaIntersection;
import operation.intersection.wa.GeneralizedBuchiIntersection;
import util.IPair;


public class BuchiWaDifference implements IBuchiWaDifference {
	
	private final IBuchiWa mFstOperand;
	private final IBuchiWa mSndOperand;
	private final BuchiWaComplement mSndComplement;
	private final IBuchiWa mDifference;
	private TarjanWaExploration mTarjan; 
	
	public BuchiWaDifference(IBuchiWa fstOp, IBuchiWa sndOp) {
		mFstOperand = fstOp;
		mSndOperand = sndOp;
		assert fstOp.getAlphabetSize() == sndOp.getAlphabetSize();
		mSndComplement = new BuchiWaComplement(sndOp);
		if(Options.useGBA) {
			mDifference = new GeneralizedBuchiIntersection(fstOp, mSndComplement);
		}else {
			mDifference = new BuchiWaIntersection(fstOp, mSndComplement);
		}
	}

	@Override
	public IBuchiWaComplement getSecondBuchiComplement() {
		return mSndComplement;
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
	public IPair<List<Integer>, List<Integer>> getCounterexampleWord() {
		return null;
	}

	@Override
	public IPair<List<IStateWa>, List<IStateWa>> getCounterexampleRun() {
		return null;
	}

	@Override
	public IBuchiWa getFirstOperand() {
		return mFstOperand;
	}

	@Override
	public IBuchiWa getSecondOperand() {
		return mSndOperand;
	}

	@Override
	public String getOperationName() {
		return "DifferenceTarjan";
	}
}