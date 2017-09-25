package main;

import complement.wa.BuchiWaComplement;
import util.Timer;
import util.UtilIntSet;

public class TaskComplement extends GenericUnaryTask {
	
	private BuchiWaComplement mComplement;
	
	public TaskComplement(String file) {
		mFileName = file;
	}

	@Override
	public void runTask() {
		mResultValue = ResultValue.EXE_UNKNOWN;
		
		Timer timer = new Timer();
		timer.start();
		mComplement.explore();
		timer.stop();
		mRunTime = timer.getTimeElapsed();
		mResultValue = ResultValue.OK;
		// get sizes
		mOpStateNum = mComplement.getOperand().getStateSize();
		mOpTransNum = mComplement.getOperand().getNumTransition();
		mAlphabetSize = mComplement.getOperand().getAlphabetSize();
		mResultStateSize = mComplement.getStateSize();
	}
	
	public void setOperation(BuchiWaComplement complement) {
		mComplement = complement;
		this.mOperationName = "Complement";
		this.mOperationName += "+" + UtilIntSet.getSetType() + (Options.lazyS ? "+lazyS" : "")
				                                         + (Options.lazyB ? "+lazyB" : "")
				                                         + (Options.useGBA ? "+GBA" : "+BA");
	}

}
