package main;

import operation.difference.wa.IBuchiWaDifference;
import util.Timer;
import util.UtilIntSet;

public class TaskDifference extends GenericBinaryTask {
	
	private IBuchiWaDifference mDifferenceOprator;
	
	public TaskDifference(String fileName) {
		this.mFileName = fileName;
	}
	
	@Override
	public void runTask() {
		Boolean result = mDifferenceOprator.isIncluded();
		if(result == null) {
			mResultValue = ResultValue.NULL;
		}else if(result.booleanValue()) {
			mResultValue = ResultValue.TRUE;
		}else {
			mResultValue = ResultValue.FALSE;
		}
		// get sizes
		mLHSStateNum = mDifferenceOprator.getFirstOperand().getStateSize();
		mRHSStateNum = mDifferenceOprator.getSecondOperand().getStateSize();
		mLHSTransNum = mDifferenceOprator.getFirstOperand().getNumTransition();
		mRHSTransNum = mDifferenceOprator.getSecondOperand().getNumTransition();
		mAlphabetSize = mDifferenceOprator.getFirstOperand().getAlphabetSize();
		mResultStateSize = mDifferenceOprator.getResult().getStateSize();
//		mIsLHSSemiDet = mChecker.getFstBuchi().isSemiDeterministic();
//		mIsRHSSemiDet = mChecker.getSndBuchi().isSemiDeterministic();
	}
	
	public void setOperation(IBuchiWaDifference checker) {
		this.mDifferenceOprator = checker;
		this.mOperationName = checker.getOperationName();
		this.mOperationName += "+" + UtilIntSet.getSetType() + (Options.lazyS ? "+lazyS" : "")
				                                         + (Options.lazyB ? "+lazyB" : "")
				                                         + (Options.useGBA ? "+GBA" : "+BA");
	}
	
	public IBuchiWaDifference getOperation() {
		return mDifferenceOprator;
	}
	
}
