package main;

import operation.inclusion.BuchiInclusionRABIT;
import operation.inclusion.IBuchiInclusion;
import util.Timer;
import util.UtilIntSet;

public class TaskInclusion extends GenericTask {
	
	
//	/**
//	 *  some runtime data
//	 *  */
//	private int mNumPairsRejectedByAntichain; // number of pairs covered by some current pair
//	
//	private int mNumPairsDeletedInAntichain;  // number of pairs deleted since it is covered by a new pair
//	
//	private int mNumPairsIgnoredByAntichain; // number of pairs ignored by some current pair
//	
//	private int mNumPairsInAntichain;         // number of pairs left in Antichain
//	
//	private int mNumTransUsedInSndBuchi;      // number of transition used in the second Buchi (no duplicate)
//	
//	
	private IBuchiInclusion mInclusionChecker;
	
	public TaskInclusion(String fileName) {
		this.mFileName = fileName;
	}
	
	@Override
	public void runTask() {
		Boolean result = null;
		Timer timer = new Timer();
		timer.start();
		result = mInclusionChecker.isIncluded();
		timer.stop();
		mRunTime = timer.getTimeElapsed();
		if(result == null) {
			mResultValue = ResultValue.NULL;
		}else if(result.booleanValue()) {
			mResultValue = ResultValue.TRUE;
		}else {
			mResultValue = ResultValue.FALSE;
		}
		// first get the used transition in mSndOperation
//		if(mChecker.getSndBuchiComplement() != null)
//			mNumTransUsedInSndBuchi = mChecker.getSndBuchiComplement().getNumUsedOpTransition();
		// get sizes
		mLHSStateNum = mInclusionChecker.getFstBuchi().getStateSize();
		mRHSStateNum = mInclusionChecker.getSndBuchi().getStateSize();
		mLHSTransNum = mInclusionChecker.getFstBuchi().getNumTransition();
		mRHSTransNum = mInclusionChecker.getSndBuchi().getNumTransition();
		mAlphabetSize = mInclusionChecker.getFstBuchi().getAlphabetSize();
		mResultStateSize = mInclusionChecker.getBuchiDifference().getStateSize();
//		mIsLHSSemiDet = mChecker.getFstBuchi().isSemiDeterministic();
//		mIsRHSSemiDet = mChecker.getSndBuchi().isSemiDeterministic();
	}
	
	public void setOperation(IBuchiInclusion checker) {
		this.mInclusionChecker = checker;
		this.mOperationName = checker.getName();
		if(!(checker instanceof BuchiInclusionRABIT)) {
			this.mOperationName += 
				"+" + UtilIntSet.getSetType() + (Options.lazyS ? "+lazyS" : "")
                    + (Options.lazyB ? "+lazyB" : "")
                    + (Options.useGBA ? "+GBA" : "+BA");
		}
	}
	
	public IBuchiInclusion getOperation() {
		return mInclusionChecker;
	}
	
//	@Override
//	public void increaseRejPairByAntichain() {
//		mNumPairsRejectedByAntichain ++;
//	}
//	
//	@Override
//	public void increaseDelPairInAntichain() {
//		mNumPairsDeletedInAntichain ++;
//	}
//	
//	@Override
//	public void increaseIngPairByAntichain() {
//		mNumPairsIgnoredByAntichain ++;
//	}
//	
//	@Override
//	public void setNumPairInAntichain(int num) {
//		mNumPairsInAntichain = num;
//	}

	
//	public void useTransition() {
//		mNumTransUsedInSndBuchi
//	}
	
}
